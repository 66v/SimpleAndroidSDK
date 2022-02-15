package com.x66vx.simplesdk.purchase.adapter.google

import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.purchase.PurchaseAdapterInterface

class PurchaseGoogle : PurchaseAdapterInterface {
    override fun initialize(callback: (SDKError?) -> Unit) {
        callback.invoke(SDKError.create())
    }

    override fun isInitialized(): Boolean {
        return false
    }

    override fun purchase(callback: (PurchaseData?, SDKError?) -> Unit) {
        callback.invoke(null, SDKError.create())
    }
}