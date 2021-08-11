package com.dsx.easypay.enums

/**
 * PayWay
 */
enum class PayWay(var payway: Int) {
    /**
     * 支付方式
     */
    WechatPay(1), ALiPay(2);

    override fun toString(): String {
        return payway.toString()
    }
}