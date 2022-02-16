package com.x66vx.simplesdk.core.internal.auth

import android.app.Activity
import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.OnActivityResultListener

interface AuthAdapterInterface : OnActivityResultListener {
    fun initialize(activity: Activity, callback: ((SDKError?) -> Unit))
    fun isInitialized(): Boolean
    fun login(activity: Activity, callback: ((AuthData?, SDKError?) -> Unit))
    fun logout(activity: Activity?, callback: (SDKError?) -> Unit)
}