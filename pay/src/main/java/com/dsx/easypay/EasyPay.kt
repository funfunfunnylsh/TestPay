package com.dsx.easypay

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.blankj.utilcode.util.NetworkUtils
import com.dsx.easypay.callback.OnPayResultListener
import com.dsx.easypay.enums.PayWay
import com.dsx.easypay.pay.paystrategy.ALiPayStrategy
import com.dsx.easypay.pay.paystrategy.WeChatPayStrategy

/**
 * Description: 支付SDK封装工具类.
 * 1	当前网络无连接（尚未进入支付阶段）
 * 2	请求APP服务器超时（尚未进入支付阶段）
 * -1	支付失败-原因未知，需要开发者手动排查
 * 微信errCode	一般不会碰到
 * -3	微信接收支付请求失败
 * -4	微信支付认证失败，拒绝支付交易
 * -5	微信版本低，不支持交易
 * -6	微信拒绝了支付交易
 * -7	未安装微信客户端，交易失败
 * 支付宝errCode	一般不会碰到
 * 8000	支付结果待确认,生成了交易订单，但是未支付。
 * 6002	网络差导致支付失败
 * 6004	支付结果未知
 * 6005	支付失败，原因未知
 */
class EasyPay {
    private var mOnPayResultListener: OnPayResultListener? = null
    private var payWay: PayWay? = null

    companion object {
        private var sInstance: EasyPay? = null

        // 通用结果码
        const val COMMON_PAY_OK = 0
        const val COMMON_PAY_ERR = -1
        const val COMMON_USER_CACELED_ERR = -2
        const val COMMON_NETWORK_NOT_AVAILABLE_ERR = 1
        const val COMMON_REQUEST_TIME_OUT_ERR = 2

        // 微信结果码
        const val WECHAT_SENT_FAILED_ERR = -3
        const val WECHAT_AUTH_DENIED_ERR = -4
        const val WECHAT_UNSUPPORT_ERR = -5
        const val WECHAT_BAN_ERR = -6
        const val WECHAT_NOT_INSTALLED_ERR = -7

        // 支付宝结果码
        const val ALI_PAY_WAIT_CONFIRM_ERR = 8000
        const val ALI_PAY_NET_ERR = 6002
        const val ALI_PAY_UNKNOW_ERR = 6004
        const val ALI_PAY_OTHER_ERR = 6005
        fun newInstance(): EasyPay {
            if (sInstance == null) {
                sInstance = EasyPay()
                return sInstance!!
            }
            return sInstance!!
        }
    }


    @SuppressLint("MissingPermission")
    fun toPay(onPayResultListener: OnPayResultListener) {
        mOnPayResultListener = onPayResultListener
        if (!NetworkUtils.isConnected()) {
            sendPayResult(COMMON_NETWORK_NOT_AVAILABLE_ERR)
        }
    }

    /**
     * 进行支付策略分发
     *
     * @param prePayInfo
     * @param way
     */
    fun doPay(activity: Activity, prePayInfo: String, way: PayWay): EasyPay {
        payWay = way
        val callBack: PayCallBack = object : PayCallBack {
            override fun onPayCallBack(code: Int) {
                sendPayResult(code)
            }
        }
        when (payWay) {
            PayWay.WechatPay -> WeChatPayStrategy(activity, prePayInfo, callBack).doPay()
            PayWay.ALiPay -> ALiPayStrategy(activity, prePayInfo, callBack).doPay()
            else -> {
            }
        }
        return this
    }

    /**
     * 请求APP服务器获取预支付信息：微信，支付宝，银联都需要此步骤
     *
     * @return
     */
    fun requestPayInfo(context: Context?, orderNum: String?, payType: String?) {}

    /**
     * 回调支付结果到请求界面
     *
     * @param code
     */
    private fun sendPayResult(code: Int) {
        try {
            when (code) {
                COMMON_PAY_OK -> mOnPayResultListener!!.onPaySuccess(payWay)
                COMMON_USER_CACELED_ERR -> mOnPayResultListener!!.onPayCancel(payWay)
                else -> mOnPayResultListener!!.onPayFailure(payWay, code)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        releaseMemory()
    }

    private fun releaseMemory() {
        mOnPayResultListener = null
        sInstance = null
    }

    interface PayCallBack {
        fun onPayCallBack(code: Int)
    }
}