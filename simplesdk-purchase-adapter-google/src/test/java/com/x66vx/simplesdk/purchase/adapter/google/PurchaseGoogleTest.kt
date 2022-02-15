package com.x66vx.simplesdk.purchase.adapter.google

import com.x66vx.simplesdk.*
import com.x66vx.simplesdk.core.internal.SimpleSDKCore
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PurchaseGoogleTest {
    // SDKCore 에서는 Adapter 의존성이 없으므로
    // SDKCore 를 통한 Adapter 실행을 여기서 테스트 한다.
    @Test
    fun testPurchaseWithSDKCore() = runBlocking {
        data class PurchaseResult<out A, out B>(
            val purchaseData: A,
            val sdkError: B
        )

        val core = SimpleSDKCore()
        val result = suspendCoroutine<PurchaseResult<PurchaseData?, SDKError?>> { cont ->
            core.purchase(StoreType.GOOGLE) { purchaseData, sdkError ->
                cont.resume(PurchaseResult(purchaseData, sdkError))
            }
        }
        Assert.assertNull(result.purchaseData)
        Assert.assertNotNull(result.sdkError)
        // 현재 미구현
        Assert.assertEquals(SDKError.NOT_DEFINED, result.sdkError!!.code)
    }
}