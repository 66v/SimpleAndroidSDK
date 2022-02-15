package com.x66vx.simplesdk.core.internal.auth

import androidx.annotation.VisibleForTesting
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.util.exist
import com.x66vx.simplesdk.core.internal.util.getStringFields
import com.x66vx.simplesdk.isFailed
import java.util.*

@VisibleForTesting
const val CLASS_NAME_AUTH_TYPE = "com.x66vx.simplesdk.AuthType"
private const val CLASS_NAME_AUTH_ADAPTER_PATH = "com.x66vx.simplesdk.auth.adapter"

class AuthCore {
    private val adapterMap = hashMapOf<String, AuthAdapterInterface>().toMutableMap()

    companion object {
        fun create(): AuthCore? {
            val list: List<String> = getStringFields(CLASS_NAME_AUTH_TYPE)
            for (type in list) {
                if (exist(getClassFullName(type))) {
                    return AuthCore()
                }
            }
            return null
        }
    }

    fun login(authType: String,
              callback: ((AuthData?, SDKError?) -> Unit)) {
        val (authAdapter, findError) = getAuthAdapter(authType)
        if (findError.isFailed()) {
            callback.invoke(null, findError)
            return
        }

        if (!authAdapter!!.isInitialized()) {
            authAdapter.initialize {
                if (it.isFailed()) {
                    callback.invoke(null, it)
                } else {
                    authAdapter.login { data, error ->
                        callback.invoke(data, error)
                    }
                }
            }
        } else {
            authAdapter.login { data, error ->
                callback.invoke(data, error)
            }
        }
    }

    private fun getAuthAdapter(type: String) : Pair<AuthAdapterInterface?, SDKError?> {
        val className = getClassFullName(type)
        if (!exist(className)) {
            return null to SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "'$type' module is not exist.")
        }

        if (!adapterMap.containsKey(type)) {
            createNewClass(type)?.let {
                adapterMap[type] = it
            } ?: return null to SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Failed to create '$type' module.")
        }

        return adapterMap[type] to null
    }
}

fun AuthCore?.login(authType: String,
                    callback: ((AuthData?, SDKError?) -> Unit)) {
    if (this == null) {
        callback.invoke(null, SDKError.create(SDKError.AUTH_NOT_SUPPORTED, "Auth module is not exist."))
        return
    }

    this.login(authType, callback)
}

private fun getClassFullName(type: String) =
    "$CLASS_NAME_AUTH_ADAPTER_PATH.$type.Auth${type.replaceFirstChar { it.uppercase(Locale.ROOT) }}"

private fun createNewClass(type: String): AuthAdapterInterface? {
    val className = getClassFullName(type)
    return try {
        val clazz = Class.forName(className)
        clazz.newInstance() as AuthAdapterInterface
    } catch (e: Exception) {
        null
    }
}