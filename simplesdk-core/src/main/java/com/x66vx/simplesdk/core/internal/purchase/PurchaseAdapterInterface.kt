package com.x66vx.simplesdk.core.internal.purchase

import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError

interface PurchaseAdapterInterface {
    fun initialize(callback: ((SDKError?) -> Unit))
    fun isInitialized(): Boolean
    fun purchase(callback: ((PurchaseData?, SDKError?) -> Unit))
}