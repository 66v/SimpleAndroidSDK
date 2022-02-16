package com.x66vx.simplesdk.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    fun onClickSimpleSDKInitialize(view: View) {
        SimpleSDK.initialize(object: VoidCallback {
            override fun onResult(error: SDKError?) {
                if (error.isSuccess()) {
                    val resultLog = "Initialize Succeeded"
                    LogWrapper.i(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val resultLog = "Initialize Failed : ${error.toString()}"
                    LogWrapper.e(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun onClickSimpleSDKLoginGoogle(view: View) {
        SimpleSDK.Auth.login(this, AuthType.GOOGLE, object: DataCallback<AuthData> {
            override fun onResult(data: AuthData?, error: SDKError?) {
                if (error.isSuccess()) {
                    val resultLog = "Login Google Succeeded : $data"
                    LogWrapper.i(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val resultLog = "Login Google Failed : ${error.toString()}"
                    LogWrapper.e(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun onClickSimpleSDKLoginFacebook(view: View) {
        SimpleSDK.Auth.login(this, AuthType.FACEBOOK, object: DataCallback<AuthData> {
            override fun onResult(data: AuthData?, error: SDKError?) {
                if (error.isSuccess()) {
                    val resultLog = "Login Facebook Succeeded : $data"
                    LogWrapper.i(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val resultLog = "Login Facebook Failed : ${error.toString()}"
                    LogWrapper.e(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun onClickSimpleSDKLogout(view: View) {
        SimpleSDK.Auth.logout(this, object: VoidCallback {
            override fun onResult(error: SDKError?) {
                if (error.isSuccess()) {
                    val resultLog = "Logout Succeeded"
                    LogWrapper.i(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val resultLog = "Logout Failed : ${error.toString()}"
                    LogWrapper.e(TAG, resultLog)
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(this@MainActivity, resultLog, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}