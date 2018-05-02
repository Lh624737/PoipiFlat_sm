package com.pospi.util;

import android.util.Log;

import com.pospi.callbacklistener.HttpCallBackListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 科传JAVA 61 接口实现
 *
 * @author ...
 */
public class KeChuanV61 {

//    http://116.228.196.235:9185/SalesTrans/rest/salestransaction/salestranslitev61
//    apiKey:                           CRMINT
//    storeCode:                     SHZ000027
//    tillId:                              01
//    cashier:                         010201
//    itemCode:                     SHZ00002701
//    tenderCode:                 CH
//    baseCurrencyCode:      RMB
//    itemorgid:                     000013
//    http://180.166.29.82:8185/SalesTrans/rest/salestransaction/salestranslitev61
//    测试账号信息
//    apiKey:                      		CRMINT
//    storeCode:                   		SC000002
//    tillId:                        	01
//    cashier:                      	001
//    itemCode:                    	0001
//    tenderCode:                     	CH
//    baseCurrencyCode:               	CNY
//    itemorgid:                      	000003
    static String url = "http://180.166.29.82:8185/SalesTrans/rest/salestransaction/salestranslitev61";
    static String contentType = "application/json;charset=utf-8";

    static String apiKey = "CRMINT";//apiKey
    static String signature = "";//签名
    static String storeCode = "SC000002";//店铺编码
    static String tillId = "01";//商场ID
    static String cashier = "001";//收银员
    static String itemCode = "0001";//料号
    static String tenderCode = "CH";//交易码 CH----现金 CI----国内银行卡 CO----国外银行卡 OT-----其他付款方式。
    static String baseCurrencyCode = "RMB";//
    static String itemOrgId = "000003";//

    /**
     * 传递单张销售单据
     */
    public static void sendData() {
        try {
            StringBuffer jsonStr = new StringBuffer();

            jsonStr.append("{\"apiKey\":null,\"signature\":null,\"docKey\":\"@docKey@\",\"transHeader\":{\"txDate\":\"@txDate@\",\"ledgerDatetime\":\"@ledgerDatetime@\",\"storeCode\":\"@storeCode@\",\"tillId\":\"@tillId@\",\"docNo\":\"@docNo@\",\"voidDocNo\":\"@voidDocNo@\",\"txAttrib\":\"@txAttrib@\"},\"salesTotal\":{\"cashier\":\"@cashier@\",\"vipCode\":\"@vipCode@\",\"netQty\":@netQty@,\"netAmount\":@netAmount@,\"extendParameter\":null,\"calculateVipBonus\":\"@calculateVipBonus@\"},\"salesItem\":[{\"salesLineNumber\":@salesLineNumber@,\"salesman\":null,\"itemCode\":\"@itemCode@\",\"itemOrgId\":\"@itemOrgId@\",\"itemLotNum\":\"@itemLotNum@\",\"serialNumber\":null,\"inventoryType\":@inventoryType@,\"qty\":@qty@,\"itemDiscountLess\":@itemDiscountLess@,\"totalDiscountLess\":@totalDiscountLess@,\"netAmount\":@netAmount@,\"salesItemRemark\":null,\"extendParameter\":null}],\"salesTender\":[{\"baseCurrencyCode\":\"@baseCurrencyCode@\",\"tenderCode\":\"@tenderCode@\",\"payAmount\":@payAmount@,\"baseAmount\":@baseAmount@,\"excessAmount\":@excessAmount@,\"extendParameter\":null}],\"orgSalesMemo\":null}");

            String nty = "1";
            String fee = "1.0";

            String json = jsonStr.toString();

            json = json.replace("@docKey@", tillId + storeCode + "201603210001");//交易号
            json = json.replace("@txDate@", "2017-03-21");//交易日期
            json = json.replace("@ledgerDatetime@", "2017-03-21 10:00:14");//交易时间
            json = json.replace("@storeCode@", storeCode);
            json = json.replace("@tillId@", tillId);
            json = json.replace("@docNo@", "201603210001");//交易号
            json = json.replace("@voidDocNo@", "");//
            json = json.replace("@txAttrib@", "");
            json = json.replace("@cashier@", cashier);
            json = json.replace("@vipCode@", "");
            json = json.replace("@netQty@", nty);
            json = json.replace("@netAmount@", fee);
            json = json.replace("@calculateVipBonus@", "0");
            json = json.replace("@salesLineNumber@", "1");
            json = json.replace("@salesman@", cashier);
            json = json.replace("@itemCode@", itemCode);
            json = json.replace("@itemOrgId@", itemOrgId);
            json = json.replace("@itemLotNum@", "*");
            json = json.replace("@serialNumber@", "");
            json = json.replace("@inventoryType@", "0");
            json = json.replace("@qty@", nty);
            json = json.replace("@itemDiscountLess@", "0.00");
            json = json.replace("@totalDiscountLess@", "0.00");
            json = json.replace("@netAmount@", fee);
            json = json.replace("@salesItemRemark@", "");
            json = json.replace("@baseCurrencyCode@", baseCurrencyCode);
            json = json.replace("@tenderCode@", tenderCode);
            json = json.replace("@payAmount@", fee);
            json = json.replace("@baseAmount@", fee);
            json = json.replace("@excessAmount@", "0.00");
            Log.i("KeChuanV61", "json: " + json);
            signature = Utils.md5Encrypt(json);
            Log.i("KeChuanV61", "signature: " + signature);
            json = json.replaceFirst("null", "\"CRMINT\"");
            json = json.replaceFirst("null", "\"" + signature + "\"");
            Log.i("KeChuanV61", "json: " + json);
            uploadPcrf(json, url, contentType, new HttpCallBackListener() {
                @Override
                public void CallBack(String Response) {
                    Log.i("KeChuanV61", "Response: " + Response);
                }
            }, null);
//            String result = "\"errorCode\":0";
//            Log.i("KeChuanV61", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************
     * 上传请求
     **************************************/
//
//    private synchronized static void uploadPcrf(String xml, String url, String contentType) {
//        uploadPcrf(xml, url, contentType, null);
//    }
    private synchronized static void uploadPcrf(final String xml, final String url, final String contentType, final HttpCallBackListener listener, final String soapAction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = url;
                    HttpURLConnection httpConn = null;
                    OutputStream out = null;
//                    String returnXml = "";
                    httpConn = (HttpURLConnection) new URL(urlString).openConnection();
                    httpConn.setRequestProperty("Content-Type", contentType);
                    if (null != soapAction) {
                        httpConn.setRequestProperty("SOAPAction", soapAction);
                    }
                    httpConn.setRequestMethod("POST");
                    httpConn.setDoOutput(true);
                    httpConn.setDoInput(true);
                    httpConn.connect();
                    out = httpConn.getOutputStream(); // 获取输出流对象
                    httpConn.getOutputStream().write(xml.getBytes()); // 将要提交服务器的SOAP请求字符流写入输出流
                    out.flush();
                    out.close();
                    int code = httpConn.getResponseCode(); // 用来获取服务器响应状态
                    String tempString = null;
                    StringBuffer sb1 = new StringBuffer();
                    if (code == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                        while ((tempString = reader.readLine()) != null) {
                            sb1.append(tempString);
                        }
                        if (null != reader) {
                            listener.CallBack(sb1.toString());
                            reader.close();
                        }
                    } else {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), "UTF-8"));
                        // 一次读入一行，直到读入null为文件结束
                        while ((tempString = reader.readLine()) != null) {
                            sb1.append(tempString);
                        }
                        if (null != reader) {
                            listener.CallBack(sb1.toString());
                            reader.close();
                        }
                    }
                    // 响应报文
//                    returnXml = sb1.toString();
                } catch (Exception e) {
                }
            }
        }).start();
    }
    /********************************上传请求**************************************/


}
