package com.x66vx.simplesdk.auth.adapter.google

import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.auth.AuthAdapterInterface

class AuthGoogle : AuthAdapterInterface {
    override fun initialize(callback: (SDKError?) -> Unit) {
        callback.invoke(SDKError.create())
    }

    override fun isInitialized(): Boolean {
        return false
    }

    override fun login(callback: (AuthData?, SDKError?) -> Unit) {
        callback.invoke(null, SDKError.create())
    }
}