package com.x66vx.simplesdk.core.internal

import com.x66vx.simplesdk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SimpleSDKCoreTest {
    @Test
    fun testInitialize() = runBlocking {
        val core = SimpleSDKCore()
        val error = suspendCoroutine<SDKError?> { cont ->
            core.initialize { sdkError ->
                cont.resume(sdkError)
            }
        }
        assertNull(error)
    }

    @Test
    fun testLogin() = runBlocking {
        data class LoginResult<out A, out B>(
            val authData: A,
            val sdkError: B
        )

        suspend fun login(core: SimpleSDKCore?, authType: String?) =
            suspendCoroutine<LoginResult<AuthData?, SDKError?>> { cont ->
                core.login(authType) { authData, sdkError ->
                    cont.resume(LoginResult(authData, sdkError))
                }
            }

        val nullCore: SimpleSDKCore? = null
        var result = login(nullCore, null)
        assertNull(result.authData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.NOT_INITIALIZED, result.sdkError!!.code)

        val core = SimpleSDKCore()
        result = login(core, null)
        assertNull(result.authData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.INVALID_PARAMETER, result.sdkError!!.code)

        result = login(core, "")
        assertNull(result.authData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.AUTH_NOT_SUPPORTED, result.sdkError!!.code)

        result = login(core, "invalid_idp")
        assertNull(result.authData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.AUTH_NOT_SUPPORTED, result.sdkError!!.code)

        // Core 는 Adapter 에 대한 의존성이 없으므로
        // 유효한 AuthType 일지라도 리플렉션은 실패한다.
        result = login(core, AuthType.GOOGLE)
        assertNull(result.authData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.AUTH_NOT_SUPPORTED, result.sdkError!!.code)
    }

    @Test
    fun testPurchase() = runBlocking {
        data class PurchaseResult<out A, out B>(
            val purchaseData: A,
            val sdkError: B
        )

        suspend fun purchase(core: SimpleSDKCore?, storeType: String?) =
            suspendCoroutine<PurchaseResult<PurchaseData?, SDKError?>> { cont ->
                core.purchase(storeType) { storeData, sdkError ->
                    cont.resume(PurchaseResult(storeData, sdkError))
                }
            }

        val nullCore: SimpleSDKCore? = null
        var result = purchase(nullCore, null)
        assertNull(result.purchaseData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.NOT_INITIALIZED, result.sdkError!!.code)

        val core = SimpleSDKCore()
        result = purchase(core, null)
        assertNull(result.purchaseData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.INVALID_PARAMETER, result.sdkError!!.code)

        result = purchase(core, "")
        assertNull(result.purchaseData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.PURCHASE_NOT_SUPPORTED, result.sdkError!!.code)

        result = purchase(core, "invalid_store")
        assertNull(result.purchaseData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.PURCHASE_NOT_SUPPORTED, result.sdkError!!.code)

        // Core 는 Adapter 에 대한 의존성이 없으므로
        // 유효한 StoreType 일지라도 리플렉션은 실패한다.
        result = purchase(core, StoreType.GOOGLE)
        assertNull(result.purchaseData)
        assertNotNull(result.sdkError)
        assertEquals(SDKError.PURCHASE_NOT_SUPPORTED, result.sdkError!!.code)
    }
}