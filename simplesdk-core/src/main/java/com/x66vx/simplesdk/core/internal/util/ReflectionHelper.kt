package com.x66vx.simplesdk.core.internal.util

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