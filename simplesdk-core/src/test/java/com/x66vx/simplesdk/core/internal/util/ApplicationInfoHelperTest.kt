package com.x66vx.simplesdk.core.internal.util

import com.x66vx.simplesdk.core.internal.TestActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ApplicationInfoHelperTest {
    private val activity = Robolectric.buildActivity(TestActivity::class.java).setup().get()

    @Test
    fun testGetMetaData() {
        val metaKey = "com.x66vx.simplesdk.google.serverclientid"
        val serverClientId: String? = getMetaData(activity, metaKey)
        assertEquals("GoogleServerID", serverClientId)
    }
}