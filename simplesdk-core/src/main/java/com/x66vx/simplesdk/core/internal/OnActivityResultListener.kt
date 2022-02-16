package com.x66vx.simplesdk.core.internal

import android.content.Intent

interface OnActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}