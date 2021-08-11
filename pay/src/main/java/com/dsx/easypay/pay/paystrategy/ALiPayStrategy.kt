package com.dsx.easypay.pay.paystrategy

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alipay.sdk.app.PayTask
import com.dsx.easypay.EasyPay
import com.dsx.easypay.EasyPay.PayCallBack
import com.xinyue.a30seconds.utils.pay.pay.ALiPayResult
import com.dsx.easypay.util.ThreadManager.execute
import com.dsx.easypay.util.ThreadManager.shutdown

/**
 * Description: 支付宝策略.
 */
class ALiPayStrategy(activity: Activity, prePayInfo: String, resultListener: PayCallBack) {

    private val mActivity: Activity = activity
    private var mPrePayInfo: String = prePayInfo
    private var mOnPayResultListener: PayCallBack = resultListener


    companion object {
        private const val PAY_RESULT_MSG = 0
    }

    @SuppressLint("HandlerLeak")
    var mHandler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what != PAY_RESULT_MSG) {
                return
            }
            shutdown()
            val result = ALiPayResult(msg.obj as Map<String?, String?>)
            when (result.resultStatus) {
                ALiPayResult.PAY_OK_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.COMMON_PAY_OK)
                ALiPayResult.PAY_CANCLE_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.COMMON_USER_CACELED_ERR)
                ALiPayResult.PAY_FAILED_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.COMMON_PAY_ERR)
                ALiPayResult.PAY_WAIT_CONFIRM_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.ALI_PAY_WAIT_CONFIRM_ERR)
                ALiPayResult.PAY_NET_ERR_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.ALI_PAY_NET_ERR)
                ALiPayResult.PAY_UNKNOWN_ERR_STATUS -> mOnPayResultListener.onPayCallBack(EasyPay.ALI_PAY_UNKNOW_ERR)
                else -> mOnPayResultListener.onPayCallBack(EasyPay.ALI_PAY_OTHER_ERR)
            }
            removeCallbacksAndMessages(null)
        }
    }

    fun doPay() {
        val payRun = Runnable {
            val task = PayTask(mActivity)
//            // 需要做正式解析，AliPayInfo.java类
//            val gson = Gson()
//            val payInfo = gson.fromJson(mPrePayInfo, AliPayInfo::class.java)
            // TODO 请根据自身需求解析mPrePayinfo，最终的字符串值应该为一连串key=value形式
            val result = task.payV2(mPrePayInfo, true)
            val message = mHandler.obtainMessage()
            message.what = PAY_RESULT_MSG
            message.obj = result
            mHandler.sendMessage(message)
        }
        execute(payRun)
    }


}