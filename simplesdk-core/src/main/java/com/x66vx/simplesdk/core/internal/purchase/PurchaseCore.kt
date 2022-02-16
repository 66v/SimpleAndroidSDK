package com.x66vx.simplesdk.core.internal.purchase

import android.app.Activity
import androidx.annotation.VisibleForTesting
import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.util.createNewClassForAdapter
import com.x66vx.simplesdk.core.internal.util.exist
import com.x66vx.simplesdk.core.internal.util.getClassFullNameForAdapter
import com.x66vx.simplesdk.core.internal.util.getStringFields
import com.x66vx.simplesdk.isFailed

@VisibleForTesting
const val CLASS_NAME_STORE_TYPE = "com.x66vx.simplesdk.StoreType"
private const val ADAPTER_PATH = "com.x66vx.simplesdk.purchase.adapter"
private const val ADAPTER_PREFIX = "Purchase"

class PurchaseCore {
    private val adapterMap: Map<String, PurchaseAdapterInterface>

    init {
        adapterMap = hashMapOf()
        val list: List<String> = getStringFields(CLASS_NAME_STORE_TYPE)
        for (type in list) {
            if (exist(getClassFullNameForAdapter(type, ADAPTER_PATH, ADAPTER_PREFIX))) {
                createNewClassForAdapter<PurchaseAdapterInterface>(type, ADAPTER_PATH, ADAPTER_PREFIX)?.let {
                    adapterMap[type] = it
                }
            }
        }
    }

    fun purchase(activity: Activity,
                 storeType: String,
                 callback: ((PurchaseData?, SDKError?) -> Unit)) {
        if (!adapterMap.containsKey(storeType)) {
            callback.invoke(null, SDKError.create(SDKError.PURCHASE_NOT_SUPPORTED, "Failed to create '$storeType' module."))
            return
        }

        val purchaseAdapter = adapterMap[storeType]
        if (!purchaseAdapter!!.isInitialized()) {
            purchaseAdapter.initialize {
                if (it.isFailed()) {
                    callback.invoke(null, it)
                } else {
                    purchaseAdapter.purchase { data, error ->
                        callback.invoke(data, error)
                    }
                }
            }
        } else {
            purchaseAdapter.purchase { data, error ->
                callback.invoke(data, error)
            }
        }
    }
}