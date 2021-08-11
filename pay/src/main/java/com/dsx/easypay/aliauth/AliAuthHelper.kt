package com.dsx.easypay.aliauth

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alipay.sdk.app.AuthTask
import com.dsx.easypay.util.ThreadManager.execute

class AliAuthHelper(activity: Activity, authInfo: String, private val mCallback: AuthCallback) {

    companion object {
        private const val SDK_AUTH_FLAG = 2
    }

    init {
        authV2(activity, authInfo)
    }

    private val mHandler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String, String>, true)
                    val resultStatus: String? = authResult.resultStatus
                    val resultCode: String? = authResult.resultCode

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (resultStatus == "9000" && resultCode == "200") {
                        mCallback.onSuccess(authResult)
                    } else {
                        // 其他状态值则为授权失败
                        mCallback.onFail(authResult.memo)
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * 支付宝账户授权业务
     *
     * @param
     */
    private fun authV2(activity: Activity, authInfo: String) {
        val authRun = Runnable { // 构造AuthTask 对象
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        execute(authRun)
    }

    interface AuthCallback {
        fun onSuccess(result : AuthResult)
        fun onFail(msg : Any?)
    }

}