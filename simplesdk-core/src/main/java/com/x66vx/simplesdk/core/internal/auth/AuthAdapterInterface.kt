package com.x66vx.simplesdk.core.internal.auth

import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError

interface AuthAdapterInterface {
    fun initialize(callback: ((SDKError?) -> Unit))
    fun isInitialized(): Boolean
    fun login(callback: ((AuthData?, SDKError?) -> Unit))
}