package com.dsx.easypay.pay;

import com.google.gson.annotations.SerializedName;

/**
 * Description: 用于微信支付.// TODO 集成时请按照自身需求修改此类
 */

public class WeChatPayInfo {
    public String appid;
    @SerializedName("mch_id")
    public String partnerid;
    @SerializedName("prepay_id")
    public String prepayid;
    @SerializedName("package")
    public String packageValue;
    @SerializedName("nonce_str")
    public String noncestr;
    public String timestamp;
    public String sign;

}
