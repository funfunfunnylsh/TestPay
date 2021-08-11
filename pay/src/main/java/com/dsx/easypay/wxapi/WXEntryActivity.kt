package com.dsx.easypay.wxapi

import android.app.Activity
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : Activity(), IWXAPIEventHandler {
    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(baseResp: BaseResp) {}
}