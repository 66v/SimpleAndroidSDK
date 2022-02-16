package com.x66vx.simplesdk.auth.adapter.facebook

import android.app.Activity
import android.content.Intent
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.auth.AuthAdapterInterface

class AuthFacebook : AuthAdapterInterface {
    override fun initialize(activity: Activity,
                            callback: (SDKError?) -> Unit) {
        callback.invoke(SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Not implemented yet"))
    }

    override fun isInitialized(): Boolean {
        return false
    }

    override fun login(activity: Activity,
                       callback: (AuthData?, SDKError?) -> Unit) {
        callback.invoke(null, SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Not implemented yet"))
    }

    override fun logout(activity: Activity?, callback: (SDKError?) -> Unit) {
        callback.invoke(SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Not implemented yet"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }
}