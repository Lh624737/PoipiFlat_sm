package com.pospi.ipay;

/**
 * Created by Qiyan on 2016/4/18.
 */
public class PayFactroy {
    public static Ipay rmbPay;

//    //实例化工厂
//    public static Ipay GetPayImpl(int payType) {
//        switch (payType) {
//            case EPayType.WX:
//                return null;
//            case EPayType.ZFB:
//                if (rmbPay.equals(null)) {
//                    rmbPay = new Ipay() {
//                        @Override
//                        public boolean connect(String response) {
//                            return false;
//                        }
//
//                        @Override
//                        public void requestPay(RequestParams payParamsMaps, IpayResult result) {
//
//                        }
//
//                        @Override
//                        public boolean closeConnect() {
//                            return false;
//                        }
//                    };
//                    return rmbPay;
//                } else {
//                    return rmbPay;
//                }
//
//            case EPayType.XJ:
//                break;
//        }
//        return null;
//    }
}
