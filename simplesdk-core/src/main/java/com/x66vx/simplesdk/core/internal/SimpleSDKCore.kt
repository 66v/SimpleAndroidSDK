package com.x66vx.simplesdk.core.internal

import android.app.Activity
import android.content.Intent
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.auth.AuthCore
import com.x66vx.simplesdk.core.internal.purchase.PurchaseCore
import com.x66vx.simplesdk.core.internal.push.PushCore
import com.x66vx.simplesdk.isSuccess

class SimpleSDKCore {
    private val authCore: AuthCore = AuthCore()
    private val purchaseCore: PurchaseCore = PurchaseCore()
    private val pushCore: PushCore = PushCore()

    private var onActivityResultListeners = ArrayList<OnActivityResultListener>().toMutableList()
    private var lastLoginType: String? = null

    fun destroy() {
        resetListeners()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        authCore.onActivityResult(requestCode, resultCode, data)
    }

    fun initialize(callback: ((SDKError?) -> Unit)) {
        initListeners()
        callback.invoke(null)
    }

    fun login(activity: Activity?,
              authType: String?,
              callback: ((AuthData?, SDKError?) -> Unit)) {
        if (activity == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'activity' is null"))
            return
        }
        if (authType == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'authType' is null"))
            return
        }

        authCore.login(activity, authType) { authData, sdkError ->
            if (sdkError.isSuccess()) {
                lastLoginType = authType
            }
            callback.invoke(authData, sdkError)
        }
    }

    fun logout(activity: Activity?,
               authType: String?,
               callback: (SDKError?) -> Unit) {
        val type = if (authType.isNullOrEmpty()) {
            lastLoginType
        } else {
            authType
        }
        if (type.isNullOrEmpty()) {
            callback.invoke(null)
            return
        }
        authCore.logout(activity, type) {
            callback.invoke(it)
        }
    }

    fun purchase(activity: Activity?,
                 storeType: String?,
                 callback: ((PurchaseData?, SDKError?) -> Unit)) {
        if (activity == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'activity' is null"))
            return
        }
        if (storeType == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'storeType' is null"))
            return
        }

        purchaseCore.purchase(activity, storeType, callback)
    }

    private fun initListeners() {
        onActivityResultListeners.add(authCore)
    }

    private fun resetListeners() {
        onActivityResultListeners.clear()
    }
}

fun SimpleSDKCore?.login(activity: Activity?,
                         authType: String?,
                         callback: ((AuthData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.NOT_INITIALIZED))
        return
    }

    this.login(activity, authType, callback)
}

fun SimpleSDKCore?.logout(activity: Activity?,
                          authType: String? = null,
                          callback: (SDKError?) -> Unit) {
    if (this == null) {
        callback.invoke(SDKError.create(SDKError.NOT_INITIALIZED))
        return
    }

    this.logout(activity, authType, callback)
}

fun SimpleSDKCore?.purchase(activity: Activity?,
                            storeType: String?,
                            callback: ((PurchaseData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.NOT_INITIALIZED))
        return
    }

    this.purchase(activity, storeType, callback)
}
