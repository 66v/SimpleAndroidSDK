package com.x66vx.simplesdk.core.internal.auth

import android.app.Activity
import android.content.Intent
import androidx.annotation.VisibleForTesting
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.OnActivityResultListener
import com.x66vx.simplesdk.core.internal.util.createNewClassForAdapter
import com.x66vx.simplesdk.core.internal.util.exist
import com.x66vx.simplesdk.core.internal.util.getClassFullNameForAdapter
import com.x66vx.simplesdk.core.internal.util.getStringFields
import com.x66vx.simplesdk.isFailed

@VisibleForTesting
const val CLASS_NAME_AUTH_TYPE = "com.x66vx.simplesdk.AuthType"
private const val ADAPTER_PATH = "com.x66vx.simplesdk.auth.adapter"
private const val ADAPTER_PREFIX = "Auth"

class AuthCore : OnActivityResultListener {
    private val adapterMap: Map<String, AuthAdapterInterface>

    init {
        adapterMap = hashMapOf()
        val list: List<String> = getStringFields(CLASS_NAME_AUTH_TYPE)
        for (type in list) {
            if (exist(getClassFullNameForAdapter(type, ADAPTER_PATH, ADAPTER_PREFIX))) {
                createNewClassForAdapter<AuthAdapterInterface>(type, ADAPTER_PATH, ADAPTER_PREFIX)?.let {
                    adapterMap[type] = it
                }
            }
        }
    }

    fun login(activity: Activity,
              authType: String,
              callback: ((AuthData?, SDKError?) -> Unit)) {
        if (!adapterMap.containsKey(authType)) {
            callback.invoke(null, SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Failed to create '$authType' module."))
            return
        }

        val authAdapter = adapterMap[authType]
        if (!authAdapter!!.isInitialized()) {
            authAdapter.initialize(activity) {
                if (it.isFailed()) {
                    callback.invoke(null, it)
                } else {
                    authAdapter.login(activity) { data, error ->
                        callback.invoke(data, error)
                    }
                }
            }
        } else {
            authAdapter.login(activity) { data, error ->
                callback.invoke(data, error)
            }
        }
    }

    fun logout(activity: Activity?,
               authType: String,
               callback: (SDKError?) -> Unit) {
        if (!adapterMap.containsKey(authType)) {
            callback.invoke(null)
            return
        }

        val authAdapter = adapterMap[authType]
        if (!authAdapter!!.isInitialized()) {
            callback.invoke(null)
        } else {
            authAdapter.logout(activity) { error ->
                callback.invoke(error)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (adapter in adapterMap) {
            adapter.value.onActivityResult(requestCode, resultCode, data)
        }
    }
}