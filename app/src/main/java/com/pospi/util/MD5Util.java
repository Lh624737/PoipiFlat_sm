package com.pospi.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * MD5加密工具类
 */
public class MD5Util {

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String encrypt(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 简单测试
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("system", "0218");
        params.put("seller", "fzd01");
        params.put("time", "20160826100455");
        params.put("price", "1000.00");
        params.put("id", "6f8d452a6b3611e68b7786f30ca893d3");
//        map.put("msg", "");
        String plaintext = getSign(params) + "&key=" + "8934e7d15453e97507ef794cf7b0519d";
//        String plaintext="id=6f8d452a6b3611e68b7786f30ca893d3&price=1000.00&seller=fzd01&system=0218&time=20160826100455&key=8934e7d15453e97507ef794cf7b0519d";
        System.out.println("原文：" + plaintext);

        String ciphertext = MD5Util.encrypt(plaintext).toUpperCase();
        System.out.println("加密成密文：" + ciphertext);
    }

    /**
     * 按照字段名的ascii码从小到大排序后使用QueryString的格式
     */
    private static String getSign(Map<String, String> params) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        // 以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if (StringUtils.isBlank(v)) {// 过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


//    04-27 18:03:48.963 3430-3430/com.pospi.pai.pospiflat I/上传订单: paramsOrder_Menus=[{"sid":"0EF59BCB36444D6B9A3D3D02449BAE0B","m_price":1,"createTime":"2017-04-27 18:03:48","saleprice":1,"totalDiscount":0,"status":4,"order_sid":"D5C6OX5279V72U4JBZYXNQWZ057H16Z7","lineNum":0,"number":1,"menus_name":"蛋糕面包类","type":1,"obj3":"0","price":1,"discountType":"0","singleDiscount":0,"saleMethod":0,"obj1":"ad18163b22b14245bf131f6d04051643","obj2":"9787115362865","callCount":0,"Uid":"15","parentSid":""}]&Order_PayType=[{"sid":"994717OG47N5RLMTGG9AD6HEZ3P569ZT","orderBy":0,"status":4,"order_sid":"D5C6OX5279V72U4JBZYXNQWZ057H16Z7","lineNum":1,"price_ys":"1.0","payId":"f130d4f14fc903caeaf0573009d88a18","obj3":"(null)","price_zl":"0.0","obj4":"(null)","obj5":"(null)","price":"1.0","name":"储值卡","obj2":"0","payType":6,"Uid":"15"}]&Order=[{"uid":"15","no":1704270005,"by":"0000","date":"2017-04-27 18:03:11","type":"1","paxNumber":"1","obj3":"","obj4":"","acmoney":"1.0","obj5":"","saleMethod":0,"obj1":"","obj2":"","sid":"D5C6OX5279V72U4JBZYXNQWZ057H16Z7","imel":"00176f592c2c","status":4,"obj10":"","cgmoney":"0.0","remoney":"1.0","number":1,"obj9":"","shopNo":"14","obj8":"","discount":0,"obj7":"","cashNo":"100002"}]
//            04-27 18:03:49.288 3430-3430/com.pospi.pai.pospiflat I/上传订单: 向服务器发送数据成功{"Result":1,"Message":"\u4e0a\u4f20\u6210\u529f\uff01","Value":""}

}