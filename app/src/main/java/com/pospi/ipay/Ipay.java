package com.pospi.ipay;

import com.loopj.android.http.RequestParams;

/**
 * 向服务器请求访问的抽象类，里面有三个方法
 */
public class Ipay {

    /**
     * 请求建立连接
     * @param response
     * @return
     */
//    public abstract boolean connect(String response);

    /**
     * 开始请求支付
     *
     * @param params 支付的参数
     */
    public void requestPay(RequestParams params, IpayResult result) {

    }

    ;


    /**
     * 关闭连接
     * @return
     */
//    public abstract boolean closeConnect();//关闭连接
}
