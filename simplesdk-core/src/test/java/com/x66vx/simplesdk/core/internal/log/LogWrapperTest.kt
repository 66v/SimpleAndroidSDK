package com.x66vx.simplesdk.core.internal.log

import com.x66vx.simplesdk.log.concat
import org.junit.Assert.assertEquals
import org.junit.Test

private const val PREFIX = "[LOG_PREFIX] "

class LogWrapperTest {
    @Test
    fun testLog() {
        val msg = "msg"

        assertEquals("$PREFIX$msg", msg.withPrefix())
        assertEquals("$PREFIX$msg", withPrefixTest1 { msg })
        assertEquals("$PREFIX$msg", withPrefixTest2 { msg })
        assertEquals("$PREFIX$msg", withPrefixTest3 { msg })

        assertEquals("$PREFIX$msg", concat { PREFIX }() { msg })
    }

    private fun String.withPrefix() = "$PREFIX$this"

    private fun withPrefixTest1(block: () -> String): String {
        return "$PREFIX${block()}"
    }

    private fun withPrefixTest2(block: () -> String): String {
        fun concat(prefix: String, block: () -> String): String {
            return prefix + block()
        }
        return concat(PREFIX, block)
    }

    private fun withPrefixTest3(block: () -> String): String {
        fun getPrefix(): () -> String = { PREFIX }
        fun concat(prefix: () -> (String)): (() -> String) -> String {
            return { block ->
                prefix() + block()
            }
        }
        return concat(getPrefix())(block)
    }
}