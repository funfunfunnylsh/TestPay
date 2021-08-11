package com.example.test

import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dsx.easypay.EasyPay
import com.dsx.easypay.callback.OnPayResultListener
import com.dsx.easypay.enums.PayWay

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {

    private lateinit var  layout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.layout)
    }


//    private fun doPay(orderDetails: Any) {
//        when (payType) {
//            1 -> {
//                val prePayInfo = orderDetails as String
//                EasyPay.newInstance().doPay(this, prePayInfo, PayWay.ALiPay)
//                    .toPay(payResultListener)
//            }
//            2 -> {
//                val prePayInfo = GsonUtils.toJson(orderDetails)
//                EasyPay.newInstance().doPay(this, prePayInfo, PayWay.WechatPay)
//                    .toPay(payResultListener)
//            }
//        }
//    }
//
//    /**
//     * 充值回调
//     */
//    private val payResultListener: OnPayResultListener = object : OnPayResultListener {
//        override fun onPaySuccess(payWay: PayWay?) {
//
//        }
//
//        override fun onPayCancel(payWay: PayWay?) {
//
//        }
//
//        override fun onPayFailure(payWay: PayWay?, errCode: Int) {
//
//        }
//
//    }

}