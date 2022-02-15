package com.x66vx.simplesdk.core.internal.purchase

import androidx.annotation.VisibleForTesting
import com.x66vx.simplesdk.PurchaseData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.util.exist
import com.x66vx.simplesdk.core.internal.util.getStringFields
import com.x66vx.simplesdk.isFailed
import java.util.*

@VisibleForTesting
const val CLASS_NAME_STORE_TYPE = "com.x66vx.simplesdk.StoreType"
private const val CLASS_NAME_PURCHASE_ADAPTER_PATH = "com.x66vx.simplesdk.purchase.adapter"

class PurchaseCore {
    private val adapterMap = hashMapOf<String, PurchaseAdapterInterface>().toMutableMap()

    companion object {
        fun create(): PurchaseCore? {
            val list: List<String> = getStringFields(CLASS_NAME_STORE_TYPE)
            for (type in list) {
                if (exist(getClassFullName(type))) {
                    return PurchaseCore()
                }
            }
            return null
        }
    }

    fun purchase(storeType: String,
                 callback: ((PurchaseData?, SDKError?) -> Unit)) {
        val (purchaseAdapter, findError) = getAuthAdapter(storeType)
        if (findError.isFailed()) {
            callback.invoke(null, findError)
            return
        }

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

    private fun getAuthAdapter(type: String) : Pair<PurchaseAdapterInterface?, SDKError?> {
        val className = getClassFullName(type)
        if (!exist(className)) {
            return null to SDKError.create(SDKError.PURCHASE_NOT_SUPPORTED, "'$type' module is not exist.")
        }

        if (!adapterMap.containsKey(type)) {
            createNewClass(type)?.let {
                adapterMap[type] = it
            } ?: return null to SDKError.create(SDKError.PURCHASE_NOT_SUPPORTED, "Failed to create '$type' module.")
        }

        return adapterMap[type] to null
    }
}

fun PurchaseCore?.purchase(storeType: String,
                           callback: ((PurchaseData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.PURCHASE_NOT_SUPPORTED, "Purchase module is not exist."))
        return
    }

    this.purchase(storeType, callback)
}

private fun getClassFullName(type: String) =
    "$CLASS_NAME_PURCHASE_ADAPTER_PATH.$type.Purchase${type.replaceFirstChar { it.uppercase(Locale.ROOT) }}"

private fun createNewClass(type: String): PurchaseAdapterInterface? {
    val className = getClassFullName(type)
    return try {
        val clazz = Class.forName(className)
        clazz.newInstance() as PurchaseAdapterInterface
    } catch (e: Exception) {
        null
    }
}