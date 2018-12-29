package com.pospi.paxprinter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pax.api.PrintException;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.ModifiedDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderPaytype;
import com.pospi.dto.ValueCardDto;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyPrinter;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;

import java.util.List;

import me.xiaopan.android.preference.PreferencesUtils;

/**
 * Created by huangqi on 2016/5/21.
 */
public class PaxPrint implements MyPrinter{

    private String shop_name;
    private String shop_address;
    private String shop_phone;
    private String device_no;
    private String shop_id;//得到店铺的id 也就是门店号
    private String cashier_name;
    private String maxNo;
    private int serial_number;//流水号
    private String payway;
    private Context context;
    private PayWayDao payWayDao;
    private double card_discount ;

    public PaxPrint(Context context, String maxNO, String payway) {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
        shop_address = preferences.getString("Address", "");//得到店铺的地址
        shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话

        SharedPreferences preferences1 = context.getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
        String value = preferences1.getString("value", "");
        String[] names = value.split("\\,");
        shop_id = names[3];//得到店铺的id 也就是门店号

        int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        cashier_name = new CashierLogin_pareseJson().parese(
                context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName();

        //得到最大的小票号
        this.maxNo = maxNO;
        //卡流水
        int len = maxNO.length();
        this.serial_number = Integer.parseInt(String.valueOf(maxNO).substring((len-4),len));
//        if (maxNo.length() == 16) {
//            this.serial_number = Integer.parseInt(String.valueOf(maxNO).substring(12));
//        } else {
//            this.serial_number = Integer.parseInt(String.valueOf(maxNO).substring(6));
//        }


        this.payway = payway;
        payWayDao = new PayWayDao(context);
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
    }

    public PaxPrint(Context context) {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
        shop_address = preferences.getString("Address", "");//得到店铺的地址
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
    }

    /**
     * 小票打印
     *
     * @param goods
     * @param payMoney
     */
    public void print(String goods, String payMoney ,String sid) {
        List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(sid);
        SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
        int receipt_num = sharedPreferences.getInt("receipt_num", 1);
        Log.i("receipt_num", "print: " + receipt_num);
//        for (int i = 0; i < 2; i++) {
//            if (i > 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        PrnTest.prnInit();

        PrnTest.prnLeftIndent((short) 0);
        PrnTest.prnSetGray(4);
        PrnTest.prnFontSet((byte) 7, (byte) 0);
        PrnTest.prnDoubleHeight(2, 2);
        PrnTest.prnDoubleWidth(2, 2);
        PrnTest.prnStr(serial_number + "\n\n", null);
        PrnTest.prnLeftIndent((short) 10);
        PrnTest.prnSetGray(3);
        PrnTest.prnDoubleHeight(1, 1);
        PrnTest.prnDoubleWidth(1, 1);
        PrnTest.prnStr(shop_name + "\n", null);
        PrnTest.prnLeftIndent((short) 0);
        PrnTest.prnSetGray(0);

        PrnTest.prnLeftIndent((short) 0);
        PrnTest.prnFontSet((byte) 1, (byte) 6);
        PrnTest.prnStr("单据: " + maxNo + "\n", null);
        PrnTest.prnStr("收银员: " + cashier_name + "\n", null);
        PrnTest.prnStr("单据日期: " + GetData.getDataTime() + "\n", null);

        //下面商品列表
        PrnTest.prnFontSet((byte) 0, (byte) 0);
        PrnTest.prnStr("\n-----------------------------------------------\n", null);
        PrnTest.prnFontSet((byte) 6, (byte) 3);
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
        PrnTest.prnStr("品名    单价   数量   金额   折扣\n", null);

        int nums = 0;
        double moneys = 0.00;
        double discounts = 0.00;
        for (GoodsDto goodsDto : goodsDtos) {
            double price = goodsDto.getPrice();//单价
            double num = goodsDto.getNum();
            double discount = goodsDto.getDiscount();
            discounts += discount;
            nums += num;
            moneys = moneys + num * price - discount;
            PrnTest.prnFontSet((byte) 6, (byte) 3);
            PrnTest.prnStr(goodsDto.getName() + "\n", null);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("        " + price + "  " + num + "  " + (num * price - discount) + "  " + discount + "\n", null);
            if (goodsDto.getModified() != null) {
                List<ModifiedDto> modifiedDtos = new Gson().fromJson(goodsDto.getModified(), new TypeToken<List<ModifiedDto>>() {
                }.getType());
                Log.i("getModified", "modifiedDtos: " + modifiedDtos.size());
                String modified = "";
                if (modifiedDtos.size() > 0) {
                    for (int j = 0; j < modifiedDtos.size(); j++) {
                        if (j > 0) {
                            modified += "\n";
                        }
                        modified += "  " + modifiedDtos.get(j).getName();
                    }
                }
                PrnTest.prnStr(modified + "\n", null);
            }
        }
        PrnTest.prnFontSet((byte) 0, (byte) 0);
        PrnTest.prnStr("\n-----------------------------------------------\n", null);
        PrnTest.prnFontSet((byte) 6, (byte) 6);
        PrnTest.prnStr("数 量:           " + nums + "\n", null);
        PrnTest.prnStr("折 让:           " + DoubleSave.doubleSaveTwo(discounts) + "\n", null);
        PrnTest.prnFontSet((byte) 0, (byte) 0);
        PrnTest.prnStr("\n-----------------------------------------------\n", null);
        PrnTest.prnFontSet((byte) 6, (byte) 6);
        for (int j=0;j<orderPaytypes.size();j++) {
            PrnTest.prnStr(orderPaytypes.get(j).getPayName()+"\n",null);
            PrnTest.prnStr("应 收:           " + orderPaytypes.get(j).getYs() + "\n", null);
            PrnTest.prnStr("实 收:           " + orderPaytypes.get(j).getSs() + "\n", null);
            PrnTest.prnStr("找 零:           " + orderPaytypes.get(j).getZl() + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 6, (byte) 6);
        }
//        if (payWayDao.findPayName(payway).equals("")) {
//            PrnTest.prnStr("付 款:           " + payway + "\n", null); // 打印文字
//        } else {
//            PrnTest.prnStr("付 款:         " + payWayDao.findPayName(payway) + "\n", null); // 打印文字
//        }
//        if (payway.equals(PayWay.CZK)) {
//            PrnTest.prnStr("应 收:           " + DoubleSave.doubleSaveTwo(moneys * card_discount) + "\n", null);
//            PrnTest.prnStr("实 收:           " + DoubleSave.doubleSaveTwo(moneys * card_discount) + "\n", null);
//            PrnTest.prnStr("折 让:           " + DoubleSave.doubleSaveTwo(moneys * (1-card_discount)) + "\n", null);
//        } else {
//            PrnTest.prnStr("应 收:           " + DoubleSave.doubleSaveTwo(moneys) + "\n", null);
//            PrnTest.prnStr("实 收:           " + payMoney + "\n", null);
//            PrnTest.prnStr("折 让:           " + DoubleSave.doubleSaveTwo(discounts) + "\n", null);
//        }



//        if (payway.equals(PayWay.XJ)) {
//            PrnTest.prnStr("找 零:           " + (Double.parseDouble(payMoney) - moneys) + "\n", null);
//        } else if (payway.equals(PayWay.XJ)) {
//            PrnTest.prnStr("找 零:           " + (Double.parseDouble(payMoney) - moneys) + "\n", null);
//        }

//        PrnTest.prnFontSet((byte) 0, (byte) 0);
//        PrnTest.prnStr("\n-----------------------------------------------\n", null);
//        PrnTest.prnFontSet((byte) 1, (byte) 6);
//        PrnTest.prnStr("公司: " + shop_name + "\n", null);
        Log.i("shop_address", "shop_address: "+shop_address);
        Log.i("shop_address", "shop_phone: "+shop_phone);
        if (!shop_address.trim().equals("")&&shop_address!=null) {
            PrnTest.prnStr("地址: " + shop_address + "\n", null);
        }
        if (!shop_phone.trim().equals("")&&shop_phone!=null) {
            PrnTest.prnStr("电话: " + shop_phone + "\n", null);
        }

        PrnTest.prnFontSet((byte) 0, (byte) 0);
        PrnTest.prnStr("\n-----------------------------------------------\n", null);

        PrnTest.prnStart();
        isfinish();
        PrnTest.prnStep((short) 200);//重要：因为prnStep中加入了prnInit()方法，所以需要先打印前面的内容，否则会直接打印后面的内容！
        PrnTest.prnStart();
//        }
//        PrnTest.finish();
//        finish(); //!请确保不再使用PrintManager中的类再执行该方法，否则可能会出现异常
    }

    /**
     * 日结打印
     *
     * @param date_time
     * @param saleTotal
     * @param discount
     * @param refund
     * @param num_order
     * @param num_refund
     * @param orderDtos
     */
    public void dailyPrint(String date_time, double saleTotal, double discount, double refund, int num_order, int num_refund, List<OrderDto> orderDtos) {
        try {
            PrnTest.prnInit();

            PrnTest.prnLeftIndent((short) 120);
            PrnTest.prnSetGray(0);
            PrnTest.prnDoubleHeight(1, 1);
            PrnTest.prnDoubleWidth(1, 1);
            PrnTest.prnStr("日结" + "\n", null);
//   /* 打印文字 */
            PrnTest.prnLeftIndent((short) 0);
            PrnTest.prnSetGray(0);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("日结日期: " + date_time + "\n", null);
            PrnTest.prnStr("打印时间: " + GetData.getDataTime() + date_time + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("销售合计" + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
//        /* 打印销售详情 */
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("总销售额:           " + DoubleSave.doubleSaveTwo(saleTotal + refund) + "\n", null);
            PrnTest.prnStr("打折销售:           " + DoubleSave.doubleSaveTwo(discount) + "\n", null);
            PrnTest.prnStr("净销售额:           " + DoubleSave.doubleSaveTwo(saleTotal) + "\n", null);
            PrnTest.prnStr("退货:                " + DoubleSave.doubleSaveTwo(refund) + "\n", null);
            PrnTest.prnStr("订单数量:           " + num_order + "\n", null);
            PrnTest.prnStr("退货订单数量:      " + num_refund + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("结算信息" + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            for (int i = 0; i < orderDtos.size(); i++) {
                if(orderDtos.get(i).getPayway().equals(PayWay.CZK)){
                    PrnTest.prnStr(orderDtos.get(i).getPayway() + ":           " +
                            DoubleSave.doubleSaveTwo(Double.parseDouble(orderDtos.get(i).getYs_money())*card_discount)
                            + "\n", null); // 打印文字
                }else {
                    PrnTest.prnStr(orderDtos.get(i).getPayway() + ":           " +
                            DoubleSave.doubleSaveTwo(Double.parseDouble(orderDtos.get(i).getYs_money()))
                            + "\n", null); // 打印文字
                }
            }
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
        /* 打印店铺详情 */
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("店铺" + shop_name + "\n", null);
            if (!shop_address.equals("")) {
                PrnTest.prnStr("地址: " + shop_address + "\n", null);
            }

            PrnTest.prnStart();
            isfinish();
            PrnTest.prnStep((short) 200);//重要：因为prnStep中加入了prnInit()方法，所以需要先打印前面的内容，否则会直接打印后面的内容！
            PrnTest.prnStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        PrnTest.finish();
//        finish(); //!请确保不再使用PrintManager中的类再执行该方法，否则可能会出现异常
    }


    /**
     * 储值卡消费打印打印
     */
    public void print(String goods, String payMoney, ValueCardDto valueCardDto ,String sid) {
        List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(sid);
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
            int receipt_num = sharedPreferences.getInt("receipt_num", 1);
            Log.i("receipt_num", "print: " + receipt_num);
//        for (int i = 0; i < 2; i++) {
//            if (i > 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            PrnTest.prnInit();
            PrnTest.prnSpaceSet((byte) 0, (byte) 2);///////
            PrnTest.prnLeftIndent((short) 0);
            PrnTest.prnSetGray(4);
            PrnTest.prnFontSet((byte) 7, (byte) 0);
            PrnTest.prnDoubleHeight(2, 2);
            PrnTest.prnDoubleWidth(2, 2);
            PrnTest.prnStr(serial_number + "\n\n", null);

            PrnTest.prnLeftIndent((short) 10);
            PrnTest.prnSetGray(3);
            PrnTest.prnFontSet((byte) 6, (byte) 9);
            PrnTest.prnDoubleHeight(1, 1);
            PrnTest.prnDoubleWidth(1, 1);
            PrnTest.prnStr(shop_name + "\n", null);
            PrnTest.prnLeftIndent((short) 0);
            PrnTest.prnSetGray(0);
//        PrnTest.prnFontSet((byte) 0, (byte) 0);
//        PrnTest.prnStr("\n----------------------------------------------- \n", null);

            PrnTest.prnLeftIndent((short) 0);
            PrnTest.prnFontSet((byte) 1, (byte) 6);
            PrnTest.prnStr("门店号: " + shop_id + "\n", null);
            PrnTest.prnStr("单据: " + maxNo + "\n", null);
            PrnTest.prnStr("收银员: " + cashier_name + "\n", null);
            PrnTest.prnStr("单据日期: " + GetData.getDataTime() + "\n", null);

            //下面商品列表
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 6, (byte) 3);
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
            PrnTest.prnStr("品名    单价   数量   金额   折扣\n", null);

            int nums = 0;
            double moneys = 0.00;
            double discounts = 0.00;
            for (GoodsDto goodsDto : goodsDtos) {
                double price = goodsDto.getPrice();//单价
                double num = goodsDto.getNum();
                double discount = goodsDto.getDiscount();
                discounts += discount;
                nums += num;
                moneys = moneys + num * price - discount;
                PrnTest.prnFontSet((byte) 6, (byte) 6);
                PrnTest.prnStr(goodsDto.getName(), null);
                PrnTest.prnFontSet((byte) 1, (byte) 6);
                PrnTest.prnStr("  " + price + "  " + num + "  " + (num * price - discount) + "  " + DoubleSave.doubleSaveTwo(discount) + "\n", null);
                if (goodsDto.getModified() != null) {
                    List<ModifiedDto> modifiedDtos = new Gson().fromJson(goodsDto.getModified(), new TypeToken<List<ModifiedDto>>() {
                    }.getType());
                    Log.i("getModified", "modifiedDtos: " + modifiedDtos.size());
                    String modified = "";
                    if (modifiedDtos.size() > 0) {
                        for (int j = 0; j < modifiedDtos.size(); j++) {
                            if (j > 0) {
                                modified += "\n";
                            }
                            modified += "  " + modifiedDtos.get(j).getName();
                        }
                    }
                    PrnTest.prnStr(modified + "\n", null);
                }
            }

            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 6, (byte) 6);
            PrnTest.prnStr("数 量:           " + nums + "\n", null);
            double xf =0;
            for (int j=0;j<orderPaytypes.size();j++) {
                PrnTest.prnStr(orderPaytypes.get(j).getPayName()+":            "+Double.parseDouble(orderPaytypes.get(j).getSs())+"\n",null);
                if (orderPaytypes.get(j).getPayCode() == PayWay.MIANZHI_CARD) {
                    xf = Double.parseDouble(orderPaytypes.get(j).getYs());
                }
            }
//            PrnTest.prnStr("付 款:       " + payWayDao.findPayName(payway) + "\n", null); // 打印文字
            PrnTest.prnStr("应 收:           " + moneys + "\n", null);

            PrnTest.prnStr("实 收:           " + DoubleSave.doubleSaveTwo(moneys * card_discount) + "\n", null);
            PrnTest.prnStr("折 让:           " + DoubleSave.doubleSaveTwo(discounts + moneys * (1 - card_discount)) + "\n\n", null);

            PrnTest.prnStr("储值卡卡号:  " + valueCardDto.getCardNo() + "\n", null);
            PrnTest.prnStr("消费前卡余额:    " + DoubleSave.doubleSaveTwo(valueCardDto.getCardAmount()) + "\n", null);
            PrnTest.prnStr("本次消费总金额: " + DoubleSave.doubleSaveTwo(moneys * card_discount) + "\n", null);
            if (orderPaytypes.size() == 2) {
                PrnTest.prnStr("消费后卡余额:    " + DoubleSave.doubleSaveTwo(valueCardDto.getCardAmount() - xf) + "\n", null);
            } else {
                PrnTest.prnStr("消费卡后余额:    " + DoubleSave.doubleSaveTwo(valueCardDto.getCardAmount() - moneys * card_discount) + "\n", null);
            }

//            PrnTest.prnStr("  卡押金:        " + valueCardDto.getMortgageAmount() + "\n", null);
            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-----------------------------------------------\n", null);
            PrnTest.prnFontSet((byte) 1, (byte) 6);

            if (!shop_address.trim().equals("")&&shop_address!=null) {
                PrnTest.prnStr("地址: " + shop_address + "\n", null);
            }
            if (!shop_phone.trim().equals("")&&shop_phone!=null) {
                PrnTest.prnStr("电话: " + shop_phone + "\n", null);
            }

            PrnTest.prnFontSet((byte) 0, (byte) 0);
            PrnTest.prnStr("\n-------------------------------------------\n", null);

            PrnTest.prnStart();
            isfinish();
            PrnTest.prnStep((short) 200);//重要：因为prnStep中加入了prnInit()方法，所以需要先打印前面的内容，否则会直接打印后面的内容！

            PrnTest.prnStart();
//        }
        } catch (Exception e) {
            byte status = PrnTest.prnStatus();
            String str_status;
            switch (status) {
                case 0x00:
                    str_status = "打 印机状态为：打印机可用";
                    break;
                case 0x01:
                    str_status = "打印机状态为：打印机忙";
                    break;
                case 0x02:
                    str_status = "打印机状态为：打印机缺纸";
                    break;
                case 0x03:
                    str_status = "打印机状态为：打印数据包格式错";
                    break;
                case 0x04:
                    str_status = "打印机状态为：打印机故障";
                    break;
                case 0x08:
                    str_status = "打印机状态为：打印机过热";
                    break;
                case 0x09:
                    str_status = "打印机状态为：打印机电压过低";
                    break;
                case (byte) 0xf0:
                    str_status = "打印机状态为：打印未完成";
                    break;
                case (byte) 0xfc:
                    str_status = "打印机状态为：打印机未装字库";
                    break;
                case (byte) 0xfe:
                    str_status = "打印机状态为：数据包过长";
                    break;
                case 97:
                    str_status = "打印机状态为：不支持的字符集";
                    break;
                case 98:
                    str_status = "打印机状态为：参数为空";
                    break;
                case 99:
                    str_status = "打印机状态为：RPC连接错误";
                    break;
                default:
                    str_status = "打印机状态为：其它错误";
                    break;
            }
//            String paxPrint_log = PreferencesUtils.getString(App.getContext(), "PaxPrintLog");
//            paxPrint_log += GetData.getDataTime() + " 打印机状态 " + str_status + "; ";
//            PreferencesUtils.putString(App.getContext(), "PaxPrintLog", paxPrint_log);
            Log.i("打印机状态为",str_status);
            e.printStackTrace();
        }
//        PrnTest.finish();
//        finish(); //!请确保不再使用PrintManager中的类再执行该方法，否则可能会出现异常
    }

    //判断是否打印完成，完成之后再执行prnInit()方法;
    private static void isfinish() {
        while (true) {
            if (PrnTest.prnStatus() == 0) {
                break;
            }
        }
    }

    @Override
    public void starPrint() {

    }
}