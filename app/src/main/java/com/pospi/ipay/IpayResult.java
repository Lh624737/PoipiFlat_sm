package com.pospi.ipay;

/**
 * 支付之后给用户发送数据
 * Created by Qiyan on 2016/4/18.
 */
public interface IpayResult {
    /**
     * 支付成功的回调函数
     *
     * @param value 支付成功后的返回内容
     */
    void paySuccessBlock(String value);

    /**
     * 支付失败的回调函数
     *
     * @param Message
     */
    void payFaiullBlock(String Message);
}
