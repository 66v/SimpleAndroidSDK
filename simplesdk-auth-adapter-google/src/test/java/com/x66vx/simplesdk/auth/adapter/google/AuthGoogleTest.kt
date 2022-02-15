package com.x66vx.simplesdk.auth.adapter.google

import com.x66vx.simplesdk.AuthData
import com.x66vx.simplesdk.AuthType
import com.x66vx.simplesdk.SDKError
import com.x66vx.simplesdk.core.internal.SimpleSDKCore
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthGoogleTest {
    // SDKCore 에서는 Adapter 의존성이 없으므로
    // SDKCore 를 통한 Adapter 실행을 여기서 테스트 한다.
    @Test
    fun testLoginWithSDKCore() = runBlocking {
        data class LoginResult<out A, out B>(
            val authData: A,
            val sdkError: B
        )

        val core = SimpleSDKCore()
        val result = suspendCoroutine<LoginResult<AuthData?, SDKError?>> { cont ->
            core.login(AuthType.GOOGLE) { authData, sdkError ->
                cont.resume(LoginResult(authData, sdkError))
            }
        }
        Assert.assertNull(result.authData)
        Assert.assertNotNull(result.sdkError)
        // 현재 미구현
        Assert.assertEquals(SDKError.NOT_DEFINED, result.sdkError!!.code)
    }
}