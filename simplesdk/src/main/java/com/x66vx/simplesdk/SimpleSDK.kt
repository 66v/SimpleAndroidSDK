package com.x66vx.simplesdk

import android.app.Activity
import android.content.Intent
import com.x66vx.simplesdk.core.internal.SimpleSDKCore
import com.x66vx.simplesdk.core.internal.login
import com.x66vx.simplesdk.core.internal.logout
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
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            core?.onActivityResult(requestCode, resultCode, data)
        }

        @JvmStatic
        fun isSuccess(error: SDKError?) = error.isSuccess()

        @JvmStatic
        fun isFailed(error: SDKError?) = error.isFailed()
    }

    class Auth {
        companion object {
            @JvmStatic
            fun login(activity: Activity?,
                      authType: String?,
                      callback: DataCallback<AuthData>?) {
                core.login(activity, authType) { authData, sdkError ->
                    callback?.onResult(authData, sdkError)
                }
            }

            @JvmStatic
            fun logout(activity: Activity?,
                       callback: VoidCallback?) {
                core.logout(activity) { sdkError ->
                    callback?.onResult(sdkError)
                }
            }

            @JvmStatic
            fun logout(activity: Activity?,
                       authType: String?,
                       callback: VoidCallback?) {
                core.logout(activity, authType) { sdkError ->
                    callback?.onResult(sdkError)
                }
            }
        }
    }

    class Purchase {
        companion object {
            @JvmStatic
            fun purchase(activity: Activity?,
                         storeType: String?,
                         callback: DataCallback<PurchaseData>?) {
                core.purchase(activity, storeType) { purchaseData, sdkError ->
                    callback?.onResult(purchaseData, sdkError)
                }
            }
        }
    }

    class Push {
    }
}