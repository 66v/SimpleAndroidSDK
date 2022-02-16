package com.x66vx.simplesdk.core.internal.util

import java.util.*

inline fun <reified T> getStringFields(name: String) : List<T> {
    return try {
        val clazz = Class.forName(name)
        clazz.declaredFields
            .map { it.get(null) }
            .filterIsInstance<T>()
    } catch (e: Exception) {
        listOf()
    }
}

fun exist(name: String) : Boolean {
    return try {
        Class.forName(name)
        return true
    } catch (e: Exception) {
        false
    }
}

fun getClassFullNameForAdapter(adapterType: String,
                               adapterPath: String,
                               classNamePrefix: String = "") =
    "$adapterPath.$adapterType.$classNamePrefix${adapterType.replaceFirstChar { it.uppercase(Locale.ROOT) }}"

@Suppress("UNCHECKED_CAST")
fun <T> createNewClassForAdapter(adapterType: String,
                                 adapterPath: String,
                                 classNamePrefix: String = ""): T? {
    val className = getClassFullNameForAdapter(adapterType, adapterPath, classNamePrefix)
    return try {
        val clazz = Class.forName(className)
        clazz.newInstance() as T?
    } catch (e: Exception) {
        null
    }
}