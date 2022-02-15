package com.x66vx.simplesdk.internal

import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.SimpleSDKCore
import com.x66vx.simplesdk.isFailed

/**
 * User Interface 의 Entry Point 에 해당하는
 * SimpleSDK 클래스 구문을 가급적 간결하게 유지하기 위해,
 * 로직은 이 클래스에 기술하도록 한다.
 */
fun initializeCore(prevCore: SimpleSDKCore?,
                   callback: ((SimpleSDKCore?, SDKError?) -> Unit)) {
    prevCore?.destroy()
    val newCore = SimpleSDKCore()
    newCore.initialize { error ->
        if (error.isFailed()) {
            callback.invoke(null, error)
            return@initialize
        }
        callback.invoke(newCore, null)
    }
}