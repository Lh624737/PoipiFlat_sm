package com.pospi.zqprinter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.pospi.dao.PayWayDao;
import com.pospi.dto.GoodsDto;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;
import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

import java.math.BigDecimal;
import java.util.List;

public class ZQPrint {
    private static ZQPrinterSDK prn = null;
    private static Context context;
    private String shop_name;
    private String shop_address;
    private String shop_phone;
    private String device_no;
    private String shop_id;//得到店铺的id 也就是门店号
    private String cashier_name;
    private int whichOne;
    private String maxNo;
    private String payWay;

    public ZQPrint(Context context, String MaxNo, String payWay) {
        this.context = context;
        prn = new ZQPrinterSDK();// 实例化SDk
//        connect("USB0");

        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
        shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
        shop_address = preferences.getString("Address", "");//得到店铺的地址
        shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话

        SharedPreferences preferences1 = context.getSharedPreferences("Token", context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
        String value = preferences1.getString("value", "");
        String[] names = value.split("\\,");
        shop_id = names[3];//得到店铺的id 也就是门店号

        whichOne = context.getSharedPreferences("islogin", context.MODE_PRIVATE).getInt("which", 0);
        cashier_name = new CashierLogin_pareseJson().parese(
                context.getSharedPreferences("cashierMsgDtos", context.MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName();

        //得到最大的小票号
        this.maxNo = MaxNo; //订单的小票号
        this.payWay = payWay;
    }

    public void connect(String address) {
        int nRet = prn.Prn_Connect(address, context);
        if (nRet != PrinterConst.ErrorCode.SUCCESS) {
            Toast.makeText(context, "连接失败！", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(context, "连接成功！", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = context.getSharedPreferences(
                    "com.example.zqprintersdkdemo_preferences",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("DefaultPort", address);
            editor.apply();
        }
    }

    public void printext(String goods, String payMoney, String Zl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
        int receipt_num = sharedPreferences.getInt("receipt_num", 1);
        if (payWay.equals(PayWay.XJ) || payWay.equals(PayWay.OTH)) {
            prn.Prn_OpenCashbox();//打开钱箱
        }
        for (int i = 0; i < receipt_num; i++) {
            if (i > 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            prn.Prn_PrintText(shop_name + "\r\n\r\n",
                    PrinterConst.Alignment.CENTER,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.WidthSize.SIZE1
                            | PrinterConst.HeightSize.SIZE1);
            prn.Prn_PrintText(
                    "单据: " + maxNo + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_PrintText(
                    "收银员: " + cashier_name + "\r\n" +
                            "单据时间: " + GetData.getDataTime() + "\r\n" +
                            "--------------------------------\r\n" +
                            "品名   单价   数量   金额   折扣\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);

            //打印商品详情
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
            int nums = 0;
            double goodsNum = 0;
            double moneys = 0.00;
            double discount = 0.0;
            double goodsDiscount = 0.0;
            for (GoodsDto goodsDto : goodsDtos) {
                double price = goodsDto.getPrice();//单价
                goodsNum = goodsDto.getNum();
                moneys += price * goodsNum;
                nums += goodsNum;
                discount += goodsDto.getDiscount();
                goodsDiscount = goodsDto.getDiscount();

                prn.Prn_PrintText(
                        goodsDto.getName() + "\r\n",
                        PrinterConst.Alignment.LEFT,
                        PrinterConst.Font.DEFAULT,
                        PrinterConst.HeightSize.SIZE0);

                prn.Prn_PrintText(price + "   " + goodsNum + "   " + price * goodsNum + "   " + goodsDiscount + "\r\n",
                        PrinterConst.Alignment.RIGHT,
                        PrinterConst.Font.DEFAULT,
                        PrinterConst.HeightSize.SIZE0);
            }

            prn.Prn_PrintText(
                    "--------------------------------\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_PrintText(
                    "数量:            " + nums + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            double zongji = new BigDecimal(moneys).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            prn.Prn_PrintText(
                    "付款:            " + new PayWayDao(context).findPayName(payWay) + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_PrintText(
                    "总计:            " + zongji + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            double paymoney = new BigDecimal(Double.parseDouble(payMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            prn.Prn_PrintText(
                    "实收:            " + paymoney + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_PrintText(
                    "折扣:            " + discount + "\r\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            if (payWay.equals(PayWay.XJ) || payWay.equals(PayWay.OTH)) {
                prn.Prn_PrintText(
                        "找零:            " + Zl + "\r\n",
                        PrinterConst.Alignment.LEFT,
                        PrinterConst.Font.DEFAULT,
                        PrinterConst.HeightSize.SIZE0);
            }
            prn.Prn_PrintText(
                    "--------------------------------\r\n" +
                            "地址:" + shop_address + "\r\n" +
                            "电话:" + shop_phone + "\r\n\n",
                    PrinterConst.Alignment.LEFT,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_PrintText(
                    "谢谢惠顾，欢迎下次光临！\r\n\r\n\r\n\r\n\r\n\r\n",
                    PrinterConst.Alignment.CENTER,
                    PrinterConst.Font.DEFAULT,
                    PrinterConst.HeightSize.SIZE0);
            prn.Prn_CutPaper();//切纸
        }
        prn.Prn_EndTransaction();//结束
    }

//    public List<String> getConnList() {
//        List<String> dataEdu = new ArrayList<String>();
//        String[] strList = prn.Prn_GetPortList(PrinterConst.PortType.USB, context);
//        if (strList != null) {
//            for (int i = 0; i < strList.length; i++)
//                dataEdu.add(strList[i]);
//        }
//        strList = prn.Prn_GetPortList(PrinterConst.PortType.BLUETOOTH, null);
//        if (strList != null) {
//            for (int i = 0; i < strList.length; i++)
//                dataEdu.add(strList[i]);
//        }
//        strList = prn.Prn_GetPortList(PrinterConst.PortType.WIFI, context);
//        if (strList != null) {
//            for (int i = 0; i < strList.length; i++)
//                dataEdu.add(strList[i] + ":9100");
//        }
//        strList = prn.Prn_GetPortList(PrinterConst.PortType.COM, context);
//        if (strList != null) {
//            for (int i = 0; i < strList.length; i++)
//                dataEdu.add(strList[i] + ":9600");
//        }
//        return dataEdu;
//    }
}
