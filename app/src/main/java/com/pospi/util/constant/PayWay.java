package com.pospi.util.constant;

/**
 * Created by Qiyan on 2016/5/13.
 * 几种不同的支付方式
 */
public class PayWay {
//    public final static int Xianjin = 1;
//    public final static int yinhangka = 2;
//    public final static int weixin = 3;
//    public final static int zhifubao = 4;
//    public final static int youhuiquan = 6;
//    public final static int other = 7;
//    public final static int chuzhika = 5;
//    public final static int xinyongka = 8;

    public final static String XJ = "现金";
    public final static String YHK = "银行卡";
    public final static String WX = "微信";
    public final static String ZFB = "支付宝";
    public final static String YHQ = "优惠券";
    public final static String LQ = "礼券";
    public final static String CZK = "会员卡";
    public final static String OTH = "其他";
    public final static String SZ = "赊账";
    public final static String JF = "积分抵扣";

    //现金
    public final static int CASH = 1;
    //信用卡
    public final static int CREDIT_CARD = 2;
    //礼券
    public final static int CASH_GIFT = 3;
    //银行卡
    public final static int BANK_CARD = 4;
    //积分
    public final static int JF_CODE = 5;
    //面值卡
    public final static int MIANZHI_CARD = 6;
    //优惠券
    public final static int YHQ_CODE = 7;
    ////支付宝
//public final static String ALIPAY 8
//团够
    public final static int GROUPON = 9;
    //其他
    public final static int OTHER = 10;
    //中银mPOS
    public final static int ZYMPOSPAY = 11;
    //D180
    public final static int ONLINE_CHUZHI_CARD = 12;
    //微信支付
    public final static int WXPAY = 13;
    //支付宝支付
    public final static int ALIPAY = 14;
    //verifone E355
    public final static int VERIFONE_E355 = 15;


}
