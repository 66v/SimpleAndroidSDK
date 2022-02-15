package com.x66vx.simplesdk

import com.x66vx.simplesdk.core.internal.SimpleSDKCore
import com.x66vx.simplesdk.core.internal.login
import com.x66vx.simplesdk.core.internal.purchase
import com.x66vx.simplesdk.internal.initializeCore

/**
 * User Interface Entry Point
 */
class SimpleSDK {
    companion object {
        var core: SimpleSDKCore? = null

        @JvmStatic
        fun initialize(callback: VoidCallback?) {
            initializeCore(core) { newCore, error ->
                core = newCore
                callback?.onResult(error)
            }
        }

        @JvmStatic
        fun isSuccess(error: SDKError?) = error.isSuccess()

        @JvmStatic
        fun isFailed(error: SDKError?) = error.isFailed()
    }

    class Auth {
        companion object {
            @JvmStatic
            fun login(authType: String?,
                      callback: DataCallback<AuthData>?) {
                core.login(authType) { authData, sdkError ->
                    callback?.onResult(authData, sdkError)
                }
            }
        }
    }

    class Purchase {
        companion object {
            @JvmStatic
            fun purchase(storeType: String?,
                         callback: DataCallback<PurchaseData>?) {
                core.purchase(storeType) { purchaseData, sdkError ->
                    callback?.onResult(purchaseData, sdkError)
                }
            }
        }
    }

    class Push {
    }
}