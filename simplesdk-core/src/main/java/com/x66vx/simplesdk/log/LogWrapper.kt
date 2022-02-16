package com.x66vx.simplesdk.log

import android.util.Log
import androidx.annotation.VisibleForTesting

private const val LOG_PREFIX = "SimpleSDK"
private fun getPrefix(): () -> String = { "[$LOG_PREFIX] " }

class LogWrapper {
    companion object {
        fun v(tag: String?, msg: String) = Log.v(  tag, withPrefix { msg })
        fun d(tag: String?, msg: String) = Log.d(  tag, withPrefix { msg })
        fun i(tag: String?, msg: String) = Log.i(  tag, withPrefix { msg })
        fun w(tag: String?, msg: String) = Log.w(  tag, withPrefix { msg })
        fun e(tag: String?, msg: String) = Log.e(  tag, withPrefix { msg })
        fun a(tag: String?, msg: String) = Log.wtf(tag, withPrefix { msg })
    }
}

private fun withPrefix(block: () -> String): String {
    return concat(getPrefix())(block)
}

@VisibleForTesting
fun concat(prefix: () -> (String)): (() -> String) -> String {
    return { block ->
        prefix() + block()
    }
}