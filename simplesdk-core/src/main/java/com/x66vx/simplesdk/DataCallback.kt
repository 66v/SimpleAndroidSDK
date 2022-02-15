package com.x66vx.simplesdk

interface DataCallback <T> {
    fun onResult(data: T?, error: SDKError?)
}