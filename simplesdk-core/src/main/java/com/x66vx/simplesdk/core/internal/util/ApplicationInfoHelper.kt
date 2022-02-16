package com.x66vx.simplesdk.core.internal.util

import android.app.Activity
import android.content.pm.PackageManager

@Suppress("UNCHECKED_CAST")
fun <T> getMetaData(activity: Activity,
                    key: String): T? = try {
        val applicationInfo = activity.packageManager.getApplicationInfo(activity.packageName, PackageManager.GET_META_DATA)
        if (applicationInfo.metaData != null &&
            applicationInfo.metaData.containsKey(key)) {
            applicationInfo.metaData.get(key) as T?
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }