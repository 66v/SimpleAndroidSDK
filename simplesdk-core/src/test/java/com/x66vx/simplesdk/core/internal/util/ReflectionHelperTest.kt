package com.x66vx.simplesdk.core.internal.util

import com.x66vx.simplesdk.core.internal.auth.CLASS_NAME_AUTH_TYPE
import org.junit.Assert.*
import org.junit.Test

class ReflectionHelperTest {
    @Test
    fun findInnerClass() {
        val clazz = Class.forName(CLASS_NAME_AUTH_TYPE)
        assertNotNull(clazz)
        val stringFields = clazz.declaredFields
            .filter { it.get(null) is String }
        assertNotNull(stringFields)
        assertNotEquals(0, stringFields.size)
    }

    @Test
    fun testGetStringFields() {
        var list: List<String> = getStringFields("invalidPackage")
        assertNotNull(list)
        assertEquals(0, list.size)

        list = getStringFields(CLASS_NAME_AUTH_TYPE)
        assertNotNull(list)
        assertNotEquals(0, list.size)

        val emptyList: List<Int> = getStringFields(CLASS_NAME_AUTH_TYPE)
        assertNotNull(emptyList)
        assertEquals(0, emptyList.size)
    }

    @Test
    fun testExist() {
        assertTrue(exist(CLASS_NAME_AUTH_TYPE))
        assertFalse(exist("${CLASS_NAME_AUTH_TYPE}2"))
        assertFalse(exist("invalidPackage"))
    }
}