package com.x66vx.simplesdk.auth.adapter.google

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.auth.AuthAdapterInterface
import com.x66vx.simplesdk.core.internal.util.getMetaData
import com.x66vx.simplesdk.log.LogWrapper

private const val TAG = "AuthGoogle"
private const val META_DATA_KEY_GOOGLE_SERVER_CLIENT_ID = "com.x66vx.simplesdk.google.serverclientid"
private const val REQUEST_CODE_GOOGLE_ONE_TAP_LOGIN = 5001

class AuthGoogle : AuthAdapterInterface {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private var callbckCache: ((AuthData?, SDKError?) -> Unit)? = null

    override fun initialize(activity: Activity,
                            callback: (SDKError?) -> Unit) {
        val serverClientId: String? = getMetaData(activity, META_DATA_KEY_GOOGLE_SERVER_CLIENT_ID)
        if (serverClientId.isNullOrEmpty()) {
            callback.invoke(SDKError.create(SDKError.INVALID_PARAMETER, "'serverClientId' is not exist."))
            return
        }

        oneTapClient = Identity.getSignInClient(activity)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(serverClientId)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        callback.invoke(null)
    }

    override fun isInitialized(): Boolean {
        return false
    }

    override fun login(activity: Activity,
                       callback: (AuthData?, SDKError?) -> Unit) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result ->
                try {
                    callbckCache = callback
                    startIntentSenderForResult(
                        activity, result.pendingIntent.intentSender, REQUEST_CODE_GOOGLE_ONE_TAP_LOGIN,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    LogWrapper.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    callback.invoke(null, SDKError.create(SDKError.AUTH_EXTERNAL_LIBRARY_ERROR, e.message, e))
                }
            }
            .addOnFailureListener(activity) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                LogWrapper.d(TAG, e.localizedMessage.toString())
                callback.invoke(null, SDKError.create(SDKError.AUTH_EXTERNAL_LIBRARY_ERROR, e.message, e))
            }
    }

    override fun logout(activity: Activity?, callback: (SDKError?) -> Unit) {
        oneTapClient.signOut()
        callback.invoke(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_GOOGLE_ONE_TAP_LOGIN -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    LogWrapper.d("AuthGoogle", "idToken: $idToken\nusername: $username\npassword: $password")
                    val authData = AuthData(credential.id, credential.googleIdToken)
                    callbckCache?.invoke(authData, null)
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            LogWrapper.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            LogWrapper.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            LogWrapper.d(TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                    callbckCache?.invoke(null, SDKError.create(SDKError.AUTH_EXTERNAL_LIBRARY_ERROR, e.message, e))
                }
                callbckCache = null
            }
        }
    }
}