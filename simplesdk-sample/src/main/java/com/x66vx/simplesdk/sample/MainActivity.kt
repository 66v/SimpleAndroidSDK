package com.x66vx.simplesdk.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import com.x66vx.simplesdk.*
import com.x66vx.simplesdk.log.LogWrapper

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SimpleSDK.onActivityResult(requestCode, resultCode, data)
    }

    fun onClickSimpleSDKInitialize(@Suppress("UNUSED_PARAMETER") view: View) {
        SimpleSDK.initialize(object: VoidCallback {
            override fun onResult(error: SDKError?) {
                showResult(this@MainActivity, "Initialize", null, error)
            }
        })
    }

    fun onClickSimpleSDKLoginGoogle(@Suppress("UNUSED_PARAMETER") view: View) {
        SimpleSDK.Auth.login(this, AuthType.GOOGLE, object: DataCallback<AuthData> {
            override fun onResult(data: AuthData?, error: SDKError?) {
                showResult(this@MainActivity, "Login Google", data, error)
            }
        })
    }

    fun onClickSimpleSDKLoginFacebook(@Suppress("UNUSED_PARAMETER") view: View) {
        SimpleSDK.Auth.login(this, AuthType.FACEBOOK, object: DataCallback<AuthData> {
            override fun onResult(data: AuthData?, error: SDKError?) {
                showResult(this@MainActivity, "Login Facebook", data, error)
            }
        })
    }

    fun onClickSimpleSDKLogout(@Suppress("UNUSED_PARAMETER") view: View) {
        SimpleSDK.Auth.logout(this, object: VoidCallback {
            override fun onResult(error: SDKError?) {
                showResult(this@MainActivity, "Logout", null, error)
            }
        })
    }

    private fun showResult(activity: Activity?,
                           funcName: String,
                           data: Any?,
                           error: SDKError?) {
        val logFunc: (String?, String) -> Int
        val resultLog: String
        val toastLength: Int
        if (error.isSuccess()) {
            val dataString = data?.let { " : $it" } ?: ""
            resultLog = "$funcName Succeeded$dataString"
            logFunc = LogWrapper.Companion::i
            toastLength = Toast.LENGTH_SHORT
        } else {
            resultLog = "$funcName Failed : ${error.toString()}"
            logFunc = LogWrapper.Companion::e
            toastLength = Toast.LENGTH_LONG
        }
        showLog(logFunc, resultLog)
        showToast(activity, resultLog, toastLength)
    }

    private fun showLog(func: (String?, String) -> Int, msg: String) {
        func(TAG, msg)
    }

    @IntDef(value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class Duration
    private fun showToast(activity: Activity?,
                          text: CharSequence?,
                          @Duration duration: Int) {
        activity?.runOnUiThread { Toast.makeText(activity, text, duration).show() }
    }
}