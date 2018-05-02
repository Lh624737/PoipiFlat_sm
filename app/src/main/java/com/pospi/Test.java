package com.pospi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pospi.pai.pospiflat.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Test extends AppCompatActivity {
    private Button btn1;
    private String result;

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    Toast.makeText(Test.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
//                case 1:
//                    Toast.makeText(Test.this, result, Toast.LENGTH_LONG).show();
//                default:
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test);
//
//        btn1 = (Button) findViewById(R.id.btn1);
//
//        String text = "02-13 15:38:11.853 12544-12544/com.pospi.pai.pospiflat I/responseERPL: salestenders=[{\"linenoField\":0,\"tendercodeField\":\"CH\",\"tendertypeField\":0,\"tendercategoryField\":0,\"excessamountField\":0,\"extendparamField\":\"\",\"remarkField\":\"\",\"payamountField\":-0.01,\"baseamountField\":-0.01}]&header={\"licensekeyField\":\"\",\"usernameField\":\"\",\"passwordField\":\"\",\"langField\":\"\",\"pagerecordsField\":100,\"pagenoField\":1,\"updatecountField\":0,\"messagetypeField\":\"SALESDATA\",\"messageidField\":\"332\",\"versionField\":\"V332M\"}&salestotal={\"localstorecodeField\":\"\",\"reservedocnoField\":\"\",\"txdate_yyyymmddField\":\"20170213\",\"txtime_hhmmssField\":\"152234\",\"mallidField\":\"HKC001\",\"storecodeField\":\"10227503\",\"tillidField\":\"01\",\"txdocnoField\":\"17021300021\",\"orgtxdate_yyyymmddField\":\"\",\"orgstorecodeField\":\"\",\"orgtillidField\":\"\",\"orgtxdocnoField\":\"\",\"salestypeField\":\"SR\",\"netqtyField\":1,\"sellingamountField\":-0.01,\"netamountField\":-0.01,\"paidamountField\":-0.01,\"changeamountField\":0,\"mallitemcodeField\":\"1022750301\",\"cashierField\":\"0000\",\"vipcodeField\":\"\",\"salesmanField\":\"\",\"demographiccodeField\":\"\",\"demographicdataField\":\"\",\"originalamountField\":0,\"couponnumberField\":\"\",\"coupongroupField\":\"\",\"coupontypeField\":\"\",\"couponqtyField\":0,\"totaldiscountField\":[{\"discountapproveField\":\"\",\"discountmodeField\":\"0\",\"discountvalueField\":0,\"discountlessField\":0}],\"ttltaxamount1Field\":0,\"ttltaxamount2Field\":0,\"priceincludetaxField\":\"\",\"shoptaxgroupField\":\"\",\"extendparamField\":\"\",\"invoicetitleField\":\"\",\"invoicecontentField\":\"\",\"issuebyField\":\"0000\",\"issuedate_yyyymmddField\":\"20170213\",\"issuetime_hhmmssField\":\"153811\",\"ecordernoField\":\"\",\"buyerremarkField\":\"\",\"orderremarkField\":\"\",\"ttpossalesdocnoField\":\"\",\"statusField\":\"\"}&url=http://180.166.29.82:8186/salestrans.asmx&salesitems=[{\"invttypeField\":\"1\",\"iscounteritemcodeField\":\"1\",\"linenoField\":0,\"storecodeField\":\"10227503\",\"mallitemcodeField\":\"1022750301\",\"counteritemcodeField\":\"1022750301\",\"itemcodeField\":\"1022750301\",\"plucodeField\":\"1022750301\",\"colorcodeField\":\"\",\"sizecodeField\":\"\",\"itemlotnumField\":\"\",\"serialnumField\":0,\"isdepositField\":\"\",\"iswholesaleField\":\"\",\"qtyField\":1,\"netamountField\":0.01,\"originalpriceField\":0.01,\"sellingpriceField\":0.01,\"exstk2salesField\":0,\"pricemodeField\":\"\",\"priceapproveField\":\"\",\"couponnumberField\":\"\",\"coupongroupField\":\"\",\"coupontypeField\":\"\",\"vipdiscountpercentField\":0,\"itemdiscountField\":[{\"discountapproveField\":\"\",\"discountmodeField\":\"0\",\"discountvalueField\":0,\"discountlessField\":0}],\"vipdiscountlessField\":0,\"promotionField\":[{\"promotionidField\":\"\",\"promotionuseqtyField\":0,\"promotionlessField\":0,\"promotionpkgcountField\":0}],\"totaldiscountless1Field\":0,\"totaldiscountless2Field\":0,\"totaldiscountlessField\":0,\"taxField\":[{\"taxrateField\":0,\"taxamountField\":0}],\"bonusearnField\":0,\"salesitemremarkField\":\"\",\"refundreasoncodeField\":\"\",\"extendparamField\":\"\"}]\n";
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BtnClick();
//            }
//        });
//    }
//
//    private void BtnClick() {
//
//        final String SERVICE_NS = "http://tempuri.org/";//命名空间
//        final String SOAP_ACTION = "http://tempuri.org/HelloWorld";//用来定义消息请求的地址，也就是消息发送到哪个操作
//        final String SERVICE_URL = "http://110.240.192.182/WebService.asmx";//URL地址，这里写发布的网站的本地地址
//        String methodName = "HelloWorld";
//        //创建HttpTransportSE传输对象，该对象用于调用Web Service操作
//        final HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
//        ht.debug = true;
//        //使用SOAP1.1协议创建Envelop对象。从名称上来看,SoapSerializationEnvelope代表一个SOAP消息封包；但ksoap2-android项目对
//        //SoapSerializationEnvelope的处理比较特殊，它是HttpTransportSE调用Web Service时信息的载体--客户端需要传入的参数，需要通过uiop【
//        //SoapSerializationEnvelope对象的bodyOut属性传给服务器；服务器响应生成的SOAP消息也通过该对象的bodyIn属性来获取。
//        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        //实例化SoapObject对象，创建该对象时需要传入所要调用的Web Service的命名空间、Web Service方法名
//        SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
////        SoapObject header = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("TransHeaderV61",header);
////        SoapObject saleTotal = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("SalesTotalLiteV61",saleTotal);
////        SoapObject salesItem = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("SalesItemLiteV61",salesItem);
////        SoapObject header = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("TransHeaderV61",header);
////        SoapObject header = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("TransHeaderV61",header);
////        SoapObject header = new SoapObject(SERVICE_NS, methodName);
////        soapObject.addProperty("TransHeaderV61",header);
//
//
//        //对dotnet webservice协议的支持,如果dotnet的webservice
//        envelope.dotNet = true;
//        //调用SoapSerializationEnvelope的setOutputSoapObject()方法，或者直接对bodyOut属性赋值，将前两步创建的SoapObject对象设为
//        //SoapSerializationEnvelope的付出SOAP消息体
//        envelope.bodyOut = soapObject;
//
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //调用WebService，调用对象的call()方法，并以SoapSerializationEnvelope作为参数调用远程Web Service
//                    ht.call(SOAP_ACTION, envelope);
//                    if (envelope.getResponse() != null) {
//                        //获取服务器响应返回的SOAP消息，调用完成后，访问SoapSerializationEnvelope对象的bodyIn属性，该属性返回一个
//                        //SoapObject对象，该对象就代表了Web Service的返回消息。解析该SoapObject对象，即可获取调用Web Service的返回值
//                        SoapObject so = (SoapObject) envelope.bodyIn;
//                        //接下来就是从SoapObject对象中解析响应数据的过程了
//                        result = so.getPropertyAsString(0);
//                        Message msg = new Message();
//                        msg.what = 1;
//                        handler.sendMessage(msg);
//                    } else {
//                        Message msg = new Message();
//                        msg.what = 0;
//                        handler.sendMessage(msg);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }


}