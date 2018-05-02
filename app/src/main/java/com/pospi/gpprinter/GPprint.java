package com.pospi.gpprinter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.pospi.dao.OrderDao;
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
import com.pospi.util.Utils;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.Vector;

import me.xiaopan.android.preference.PreferencesUtils;

/**
 * Created by huangqi on 2016/5/18.
 */
public class GPprint implements MyPrinter{
    private SharedPreferences preferences;
    private String shop_name;
    private String shop_address;
    private String shop_phone;
    private String device_no;
    private String shop_id;//得到店铺的id 也就是门店号
    private String cashier_name;
    private String maxNo;
    private String payway;
    private Context context;

    private GpService mGpService = null;

    public GPprint(GpService mGpService, Context context, String maxNO, String payway) {
        this.context = context;
        this.mGpService = mGpService;
        preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
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
        this.payway = payway;
    }

    public GPprint(GpService mGpService) {
        this.mGpService = mGpService;
    }

    // 发送小票打印命令
    public void print(String goods, String payMoney, Bitmap qrCode, boolean isSale ,String sid) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//        int receipt_num = sharedPreferences.getInt("receipt_num", 1);
//        for (int i = 0; i < receipt_num; i++) {
//            if (i > 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

        List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(sid);

            EscCommand esc = new EscCommand();

//        TscCommand tsc = new TscCommand();
//        tsc.addCashdrwer(TscCommand.FOOT.F2, 2, 2);
//            esc.addPrintAndFeedLines((byte) 3);

            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF
                    , EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
            esc.addText(shop_name + "\n"); // 打印文字
            esc.addPrintAndLineFeed();

		/* 打印单据信息 */
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF,
                    EscCommand.ENABLE.OFF);// 取消倍高倍宽
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
//        esc.addText("门店号: " + shop_id + "\n", "utf-8"); // 打印文字
            esc.addText("单据: " + maxNo + "\n", "utf-8"); // 打印文字
            esc.addText("收银员: " + cashier_name + "\n", "utf-8"); // 打印文字
            esc.addText("单据时间: " + GetData.getDataTime() + "\n", "utf-8"); // 打印文字

            esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        /* 打印商品详情 */
            esc.addText("品名   " + "单价   " + "数量   " + "金额   " + "折扣" + "\n"); // 打印文字

            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
            int nums = 0;
            double moneys = 0.00;
            double discounts = 0.00;
            for (GoodsDto goodsDto : goodsDtos) {
                esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
                double price = goodsDto.getPrice();//单价
                int num = goodsDto.getNum();
                double discount = goodsDto.getDiscount();
                discounts += discount;
                nums += num;
                moneys = moneys + num * price - discount;
                esc.addText(goodsDto.getName() + "\n"); // 打印文字
                esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);// 设置打印居右
                esc.addText(price + "    " + num + "    " + DoubleSave.doubleSaveTwo(num * price - discount) + "  " + DoubleSave.doubleSaveTwo(discount) + "\n"); // 打印文字
                if (goodsDto.getModified() != null) {
                    List<ModifiedDto> modifiedDtos = new Gson().fromJson(goodsDto.getModified(), new TypeToken<List<ModifiedDto>>() {
                    }.getType());
                    Log.i("getModified", "modifiedDtos: " + modifiedDtos.size());
                    String modified = "";
                    for (int j = 0; j < modifiedDtos.size(); j++) {
                        if (j > 0) {
                            modified += "\n";
                        }
                        modified += modifiedDtos.get(j).getName();
                    }
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印居左
                    esc.addText(modified + "\n"); // 打印文字
                }
            }

            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印居左
            esc.addText("--------------------------------\n", "utf-8"); // 打印文字
         /* 打印收银详情 */
            esc.addText("数 量:           " + nums + "\n", "utf-8"); // 打印文字
            esc.addText("折 让:           " + DoubleSave.doubleSaveTwo(discounts) + "\n", "utf-8"); // 打印文字
            esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        for (int i=0 ;i<orderPaytypes.size();i++) {
                esc.addText(orderPaytypes.get(i).getPayName()+"\n", "utf-8");
                esc.addText("应 收:           " + orderPaytypes.get(i).getYs() + "\n", "utf-8"); // 打印文字
                esc.addText("实 收:           " + orderPaytypes.get(i).getSs() + "\n", "utf-8"); // 打印文字
                esc.addText("找 零:           " + orderPaytypes.get(i).getZl() + "\n", "utf-8"); // 打印文字
                esc.addText("--------------------------------\n", "utf-8"); // 打印文字
            }
        if (orderPaytypes.size() == 0) {
            esc.addText("付 款:           " + new PayWayDao(context).findPayName(payway) + "\n", "utf-8"); // 打印文字
//        }
////            esc.addText("付 款:           " + new PayWayDao(context).findPayName(payway) + "\n", "utf-8"); // 打印文字
//            esc.addText("应 收:           " + DoubleSave.doubleSaveTwo(moneys) + "\n", "utf-8"); // 打印文字
//            esc.addText("实 收:           " + payMoney + "\n", "utf-8"); // 打印文字
//
//            if (payway.equals(String.valueOf(PayWay.CASH)) || payway.equals(String.valueOf(PayWay.OTHER))) {
//                esc.addText("找 零:           " + (Double.parseDouble(payMoney) - moneys) + "\n", "utf-8"); // 打印文字
//            }

            esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        /* 打印店铺详情 */
            esc.addText("地址: " + shop_address + "\n", "utf-8"); // 打印文字
//        esc.addText("公司网址: " + "www.pos.pospi.com" + "\n", "utf-8"); // 打印文字
            esc.addText("电话: " + shop_phone + "\n", "utf-8"); // 打印文字
//        esc.addText("服务专线: " + "4008-123-456" + "\n", "utf-8"); // 打印文字
            esc.addText("--------------------------------\n", "utf-8"); // 打印文字
            esc.addPrintAndLineFeed();

            if (qrCode != null) {
                esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
                esc.addRastBitImage(qrCode, qrCode.getWidth(), 0); // 打印图片
                esc.addPrintAndLineFeed();
            }

		/* 打印文字 */
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
            esc.addText("谢谢惠顾，欢迎下次光临!\r\n"); // 打印结束
            esc.addPrintAndFeedLines((byte) 7);// 打印7个空行

            if (isSale) {
                if (payway.equals(String.valueOf(PayWay.CASH)) || payway.equals(String.valueOf(PayWay.OTHER))) {
                    esc.addGeneratePluseAtRealtime(LabelCommand.FOOT.F2, (byte) 2);//开钱箱
                }
            }

            Vector<Byte> datas = esc.getCommand(); // 发送数据
            Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
            byte[] bytes = ArrayUtils.toPrimitive(Bytes);
            String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据

//            Log.i("GPrint", "打印的字符串数据: " + sss);
            try {
                if (mGpService == null) {
                    Utils.connection();
                    mGpService = App.getmGpService();
                    mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
                }
                int e = mGpService.sendEscCommand(0, sss);
//                Log.i("GPrintLog", "发送打印指令返回码: " + e);
            } catch (RemoteException e) {
//                Log.i("GPrintLog", "发送打印指令异常: ");
                e.printStackTrace();
            }
//        }
        }
    }

    // 日结打印
    public void printDaySale() {
        EscCommand esc = new EscCommand();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addText("日结\n", "utf-8"); // 打印文字

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF,
                EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
//        esc.addText("门店号: " + shop_id + "\n", "utf-8"); // 打印文字
        esc.addText(shop_name + "\n", "utf-8"); // 打印文字
        esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        esc.addText("销售合计\n", "utf-8"); // 打印文字
        esc.addText("--------------------------------\n", "utf-8"); // 打印文字
//        esc.addText("单据时间: " + GetData.getDataTime() + "\n", "utf-8"); // 打印文字
//        esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        /* 打印销售详情 */

        OrderDao dao = new OrderDao(context);
        List<OrderDto> orderDtos = dao.selectGoods(GetData.getYYMMDDTime());
        double saleTotal = 0, discountTotal = 0, netSales = 0, refund = 0;
        int order_number = 0;
        for (OrderDto orderDto : orderDtos) {
            order_number += 1;
            if (orderDto.getOrderType() == URL.ORDERTYPE_SALE) {
                List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
                for (GoodsDto goodsDto : goodsDtos) {
                    discountTotal += goodsDto.getDiscount() * goodsDto.getNum();
                }
                saleTotal += Double.parseDouble(orderDto.getSs_money());
                netSales += Double.parseDouble(orderDto.getSs_money());
            } else if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                refund -= Double.parseDouble(orderDto.getSs_money());
                netSales += Double.parseDouble(orderDto.getSs_money());
                List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
                for (GoodsDto goodsDto : goodsDtos) {
                    discountTotal -= goodsDto.getDiscount() * goodsDto.getNum();
                }
            }
        }
        esc.addText("总销售额:           " + saleTotal + "\n", "utf-8"); // 打印文字
        esc.addText("打折销售:           " + discountTotal + "\n", "utf-8"); // 打印文字
        esc.addText("净销售额:           " + netSales + "\n", "utf-8"); // 打印文字
        esc.addText("退货:               " + refund + "\n", "utf-8"); // 打印文字
        esc.addText("订单数量:           " + order_number + "\n\n\n", "utf-8"); // 打印文字

        esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        /* 打印店铺详情 */
        esc.addText("结算信息\n", "utf-8"); // 打印文字
        esc.addText("--------------------------------\n", "utf-8"); // 打印文字·
        esc.addText("现金:           " + 1 + "\n\n\n", "utf-8"); // 打印文字

		/* 打印文字 */
        esc.addText("打印时间: " + GetData.getDataTime() + "\n", "utf-8"); // 打印结束
        esc.addPrintAndFeedLines((byte) 7);// 打印8个空行

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据

        try {
            mGpService.sendEscCommand(0, sss);
        } catch (RemoteException e) {
//            Log.i("GPrintLog", "发送打印指令异常: ");
            String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
            gPrint_log += GetData.getDataTime() + " 发送打印指令异常 " + e.getMessage();
            PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
            e.printStackTrace();
        }
    }

    public void printQrCode(Bitmap qrCode) {

        EscCommand esc = new EscCommand();
        if (qrCode != null) {
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
            esc.addRastBitImage(qrCode, qrCode.getWidth(), 0); // 打印图片
            esc.addPrintAndLineFeed();
        }
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据

        try {
            int e = mGpService.sendEscCommand(0, sss);
//                Log.i("GPrintLog", "发送打印指令返回码: " + e);
        } catch (RemoteException e) {
//                Log.i("GPrintLog", "发送打印指令异常: ");
            String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
            gPrint_log += GetData.getDataTime() + " 发送打印指令异常 " + e.getMessage() + "; ";
            PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
            e.printStackTrace();
        }
    }

    @Override
    public void starPrint(String goods, String payMoney, Bitmap qrCode, boolean isSale ,ValueCardDto valueCardDto,String sid ,String tableId) {
        print(goods ,payMoney ,qrCode ,isSale ,sid);
    }
}
