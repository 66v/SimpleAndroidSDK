package com.x66vx.simplesdk.core.internal

import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.auth.AuthCore
import com.x66vx.simplesdk.core.internal.auth.login
import com.x66vx.simplesdk.core.internal.purchase.PurchaseCore
import com.x66vx.simplesdk.core.internal.purchase.purchase
import com.x66vx.simplesdk.core.internal.push.PushCore

class SimpleSDKCore {
    private val authCore: AuthCore? = AuthCore.create()
    private val purchaseCore: PurchaseCore? = PurchaseCore.create()
    private val pushCore: PushCore? = PushCore.create()

    fun destroy() {
    }

    fun initialize(callback: ((SDKError?) -> Unit)) {
        callback.invoke(null)
    }

    fun login(authType: String?,
              callback: ((AuthData?, SDKError?) -> Unit)) {
        if (authType == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'authType' is null"))
            return
        }

        authCore.login(authType, callback)
    }

    fun purchase(storeType: String?,
                 callback: ((PurchaseData?, SDKError?) -> Unit)) {
        if (storeType == null) {
            callback.invoke(null, SDKError.create(SDKError.INVALID_PARAMETER, "'storeType' is null"))
            return
        }

        purchaseCore.purchase(storeType, callback)
    }
}

fun SimpleSDKCore?.login(authType: String?,
                         callback: ((AuthData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.NOT_INITIALIZED))
        return
    }

    this.login(authType, callback)
}

fun SimpleSDKCore?.purchase(storeType: String?,
                            callback: ((PurchaseData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.NOT_INITIALIZED))
        return
    }

    this.purchase(storeType, callback)
}
