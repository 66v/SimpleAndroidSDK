package com.x66vx.simplesdk

private const val TAG = "SDKError"

class SDKError private constructor(val code: Int,
                                   message: String?,
                                   cause: Throwable?) : RuntimeException(message, cause) {
    companion object {
        const val NOT_DEFINED = -1
        const val SUCCESS = 0
        const val FAILED = 1
        const val NOT_INITIALIZED = 2
        const val INVALID_PARAMETER = 3

        const val AUTH_NOT_SUPPORTED = 101
        const val AUTH_EXTERNAL_LIBRARY_ERROR = 110

        const val PURCHASE_NOT_SUPPORTED = 201

        fun create(code: Int = NOT_DEFINED,
                   message: String? = null,
                   cause: Throwable? = null) = SDKError(code, message, cause)
    }

    override fun toString(): String {
        return "[$TAG] code: $code, message: $message" + if (cause != null) " cause: $cause" else ""
    }
}

fun SDKError?.isSuccess() = if (this == null) {
    true
} else {
    this.code == SDKError.SUCCESS
}

fun SDKError?.isFailed() = !this.isSuccess()