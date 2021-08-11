package com.dsx.easypay.callback

import com.dsx.easypay.enums.PayWay

interface OnPayResultListener {
    fun onPaySuccess(payWay: PayWay?)
    fun onPayCancel(payWay: PayWay?)
    fun onPayFailure(payWay: PayWay?, errCode: Int)
}