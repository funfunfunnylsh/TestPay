package com.dsx.easypay.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.dsx.easypay.pay.paystrategy.WeChatPayStrategy


class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    private var mWxApi: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWxApi = WXAPIFactory.createWXAPI(this, WeChatPayStrategy.mWechatAppID)
        mWxApi!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        mWxApi!!.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {
        Log.d(TAG, "请求发出的回调")
    }

    override fun onResp(baseResp: BaseResp) {
        val errCode = baseResp.errCode
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            sendPayResultBroadcast(errCode)
        }
    }

    private fun sendPayResultBroadcast(resultCode: Int) {
        val broadcastManager = LocalBroadcastManager.getInstance(applicationContext)
        val payResult = Intent()
        payResult.action = WeChatPayStrategy.WECHAT_PAY_RESULT_ACTION
        payResult.putExtra(WeChatPayStrategy.WECHAT_PAY_RESULT_EXTRA, resultCode)
        broadcastManager.sendBroadcast(payResult)
        finish()
    }

    companion object {
        private const val TAG = "WXPayEntryActivity"
    }
}