package com.x66vx.simplesdk.core.internal.util

import com.x66vx.simplesdk.core.internal.auth.CLASS_NAME_AUTH_TYPE
import com.x66vx.simplesdk.core.internal.purchase.CLASS_NAME_STORE_TYPE
import org.junit.Assert.*
import org.junit.Test

class ReflectionHelperTest {
    private val testClassNames = arrayOf(CLASS_NAME_AUTH_TYPE, CLASS_NAME_STORE_TYPE)

    @Test
    fun findInnerClass() {
        for (className in testClassNames) {
            val clazz = Class.forName(className)
            assertNotNull(clazz)
            val stringFields = clazz.declaredFields
                .filter { it.get(null) is String }
            assertNotNull(stringFields)
            assertNotEquals(0, stringFields.size)
        }
    }

    @Test
    fun testGetStringFields() {
        var list: List<String> = getStringFields("invalidPackage")
        assertNotNull(list)
        assertEquals(0, list.size)

        for (className in testClassNames) {
            list = getStringFields(className)
            assertNotNull(list)
            assertNotEquals(0, list.size)

            val emptyList: List<Int> = getStringFields(className)
            assertNotNull(emptyList)
            assertEquals(0, emptyList.size)
        }
    }

    @Test
    fun testExist() {
        assertFalse(exist("invalidPackage"))

        for (className in testClassNames) {
            assertTrue(exist(className))
            assertFalse(exist("${className}2"))
        }
    }

    @Test
    fun testGetClassFullNameForAdapter() {
        val type = "type"
        val path = "abc.bcd.cde"
        val prefix = "abcdeee"
        assertEquals("$path.$type.${prefix}Type", getClassFullNameForAdapter(type, path, prefix))
        assertEquals("$path.$type.Type", getClassFullNameForAdapter(type, path))
    }
}