package com.dsx.easypay.pay.paystrategy

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.dsx.easypay.EasyPay
import com.dsx.easypay.EasyPay.PayCallBack
import com.dsx.easypay.pay.WeChatPayInfo
import com.google.gson.Gson

/**
 * Description:微信支付策略.
 */
class WeChatPayStrategy(activity: Activity, prePayInfo: String, resultListener: PayCallBack) {

    private var mBroadcastManager: LocalBroadcastManager? = null
    private val mContext: Context = activity
    private var mPrePayInfo: String = prePayInfo
    private var mOnPayResultListener: PayCallBack = resultListener

    companion object {
        @JvmField
        var mWechatAppID = ""
        const val WECHAT_PAY_RESULT_ACTION = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_ACTION"
        const val WECHAT_PAY_RESULT_EXTRA = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_EXTRA"
    }


    fun doPay() {
        // TODO 需要做正式解析，WeChatPayInfo.java类
        val gson = Gson()
        val payInfo = gson.fromJson(mPrePayInfo, WeChatPayInfo::class.java)
        mWechatAppID = payInfo.appid

        val wxApi = WXAPIFactory.createWXAPI(mContext, mWechatAppID, true)
        if (!wxApi.isWXAppInstalled) {
            mOnPayResultListener.onPayCallBack(EasyPay.WECHAT_NOT_INSTALLED_ERR)
            return
        }
        val isPaySupported = wxApi.wxAppSupportAPI >= Build.PAY_SUPPORTED_SDK_INT
        if (!isPaySupported) {
            mOnPayResultListener.onPayCallBack(EasyPay.WECHAT_UNSUPPORT_ERR)
            return
        }
        wxApi.registerApp(mWechatAppID)
        registerPayResultBroadcast()

        val req = PayReq()
        req.appId = payInfo.appid
        req.partnerId = payInfo.partnerid
        req.prepayId = payInfo.prepayid
        req.packageValue = "Sign=WXPay"
        req.nonceStr = payInfo.noncestr
        req.timeStamp = payInfo.timestamp
        req.sign = "MD5"

        // 发送支付请求：跳转到微信客户端
        val b = wxApi.sendReq(req)
    }

    private fun registerPayResultBroadcast() {
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext.applicationContext)
        val filter = IntentFilter(WECHAT_PAY_RESULT_ACTION)
        mBroadcastManager!!.registerReceiver(mReceiver!!, filter)
    }

    private fun unRegisterPayResultBroadcast() {
        if (mBroadcastManager != null && mReceiver != null) {
            mBroadcastManager!!.unregisterReceiver(mReceiver!!)
            mBroadcastManager = null
            mReceiver = null
        }
    }

    private var mReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getIntExtra(WECHAT_PAY_RESULT_EXTRA, -100)
            mOnPayResultListener.onPayCallBack(result)
            unRegisterPayResultBroadcast()
        }
    }


}