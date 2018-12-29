package com.pospi.pai.yunpos.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lany.sp.SPHelper;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderPaytype;
import com.pospi.dto.Tablebeen;
import com.pospi.dto.ValueCardDto;
import com.pospi.dto.VipBeen;
import com.pospi.greendao.TablebeenDao;
import com.pospi.pai.yunpos.been.PrintBeen;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyPrinter;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *  * 在此写用途
 *  * @FileName:com.pospi.pai.pospiflat.util.SuMiPrint.java
 *  * @author: myName
 *  * @date: 2017-06-06 13:44
 *  * @version V1.0 <描述当前版本功能>
 *  
 */
public class SuMiPrint implements MyPrinter{
    private static final String TAG = "SuMiPrint";
    private static Context context;
    private String shop_name;
    private String shop_address;
    private String shop_phone;
    private String device_no;
    private String shop_id;//得到店铺的id 也就是门店号
    private String cashier_name;
    private int whichOne;
    private String maxNo;
    private String payWay = "";
    private String zl;



    int nums = 0;
    double prices = 0.00;
    int goodsNum = 0;
    double moneys = 0.00;
    double discounts = 0.0;
    double goodsDiscount = 0.0;
    private Tablebeen tablebeen;
    private String tableNumber="";
    OrderDto mOrderDto;
    List<GoodsDto> goodsList = new ArrayList<>();
    String jf = "";
    String vipnum = "";
    private String head;
    private String foot;
    private final PrintBeen printBeen;


    public SuMiPrint(Context context, OrderDto orderDto) {
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
        shop_name = SPHelper.getInstance().getString(Constant.STORE_NAME);//得到存储的店铺的名字
        shop_address = preferences.getString("Address", "");//得到店铺的地址
        shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话

        shop_id = "";//得到店铺的id 也就是门店号

        cashier_name = orderDto.getCashiername();


        //得到最大的小票号
        this.maxNo = orderDto.getMaxNo(); //订单的小票号
        List<OrderPaytype> list = new Gson().fromJson(orderDto.getPayway(), new TypeToken<List<OrderPaytype>>() {
        }.getType());
        for (OrderPaytype paytype : list) {
            payWay +=paytype.getPayName()+"["+paytype.getSs()+"]"+"  ";
        }
        zl = orderDto.getZl_money();
        mOrderDto = orderDto;
        this.context = context;


        goodsList = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
        double jeAll = 0;
        for (GoodsDto dto : goodsList) {

            if (dto.getUsejf().equals("1")) {
                jeAll += dto.getPrice() * dto.getNum() - dto.getDiscount();
            }
        }

        if (!orderDto.getVipNumber().equals("")) {
            VipBeen vipBeen = new Gson().fromJson(orderDto.getVipNumber(), VipBeen.class);
            jf = String.valueOf((int) (jeAll / Double.parseDouble(vipBeen.getJfjs())) * (int) Double.parseDouble((vipBeen.getJfbs())));
            vipnum = vipBeen.getNum();
        }
        head = "";
        if (mOrderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
            head = shop_name + "    退货单";
        } else if (Constant.LX.equals(mOrderDto.getMode())) {
            head = shop_name + "    练习模式";
        }
        printBeen = new Gson().fromJson(SPHelper.getInstance().getString(Constant.POS_PRINT), PrintBeen.class);
        if (!printBeen.getHead().isEmpty()) {
            head = printBeen.getHead();
        }




    }
    public void beginPrint(){

        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            Log.i("test", "蓝牙未打开");
//			Toast.makeText(context, "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            Log.i("test", "未找到打印设备");
//			Toast.makeText(context, "Please Make Sure Bluetooth have InnterPrinter!",
//					Toast.LENGTH_LONG).show();
            return;
        }
        // 3: Generate a order data
//				byte[] data = ESCUtil.generateMockData();
        byte[] data = printTop(maxNo);
        byte[] data1 = printBody(mOrderDto.getOrder_info());
        byte[] data2 = printFoot(mOrderDto.getSs_money());
        byte[] data3 = ESCUtil.byteMerger(data, data1);
        byte[] result = ESCUtil.byteMerger(data3, data2);
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);

            BluetoothUtil.sendData(result, socket);
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public byte[] printTop(String maxNo ) {

        try {
            byte[] underoff = ESCUtil.underlineOff();
            byte[] next2Line = ESCUtil.nextLine(2);
//			byte[] title = "The menu（launch）**wanda plaza".getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize3 = ESCUtil.fontSizeSetBig(1);
            byte[] center = ESCUtil.alignCenter();

            byte[] Focus = shop_name.getBytes("gb2312");
            if (mOrderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                Focus = (shop_name + "    退货单").getBytes("gb2312");
            } else if (Constant.LX.equals(mOrderDto.getMode())) {
                Focus = (shop_name + "    练习模式").getBytes("gb2312");
            }
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSizeNormal = ESCUtil.fontSizeSetBig(1);
            byte[] line = "                                             ".getBytes("gb2312");
            byte[] left = ESCUtil.alignLeft();
            byte[] tNumber = ("桌号 "+mOrderDto.getTableNumber()+"       "+"取餐号 "+maxNo.substring(maxNo.length()-4,maxNo.length())).getBytes("gb2312");
            byte[] number = ("取餐号："+maxNo.substring(maxNo.length()-4,maxNo.length())).getBytes("gb2312");
            byte[] vipNumber = ("会员号："+vipnum+"      本次积分："+jf).getBytes("gb2312");
            byte[] orderNumber = ("单据："+maxNo).getBytes("gb2312");
            byte[] cashier = ("收银员："+cashier_name).getBytes("gb2312");
            byte[] orderTime = ("单据时间："+ GetData.getDataTime()).getBytes("gb2312");
            byte[] underline = ESCUtil.underlineWithOneDotWidthOn();
            byte[] menu = "品名    单价     数量    金额     折扣".getBytes("gb2312");
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

            int nums = 0;
            double price = 0.00;
            int goodsNum = 0;
            double moneys = 0.00;
            double discounts = 0.0;
            double goodsDiscount = 0.0;
//            for (GoodsDto gd:goodsList) {
//
//
//            }

            byte[] detail = "豆浆    2.00     1    2.00     0.0".getBytes("gb2312");


//            byte[] number = "数量：       1".getBytes("gb2312");
            byte[] payWay = "付款：       现金".getBytes("gb2312");
            byte[] yinshou = "应收：       2.00".getBytes("gb2312");
            byte[] shihsou = "实收：       2.00".getBytes("gb2312");
            byte[] discount = "折让：       0.0".getBytes("gb2312");
            byte[] zhaolin = "找零：       0.00".getBytes("gb2312");
            byte[] address = "地址：中山路8号".getBytes("gb2312");
            byte[] phone = "电话：1242343535".getBytes("gb2312");
//			boldOn = ESCUtil.boldOn();
//			byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
//			byte[] FocusOrderContent = "Big hamburger(single)".getBytes("gb2312");
//			boldOff = ESCUtil.boldOff();
////			byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
//
//			next2Line = ESCUtil.nextLine(2);
//
//			byte[] priceInfo = "Receivable:$22  Discount：$2.5 ".getBytes("gb2312");
//			byte[] nextLine = ESCUtil.nextLine(1);
//
//			byte[] priceShouldPay = "Actual collection:$19.5".getBytes("gb2312");
//			nextLine = ESCUtil.nextLine(1);
//
//			byte[] takeTime = "Pickup time:2015-02-13 12:51:59".getBytes("gb2312");
//			nextLine = ESCUtil.nextLine(1);
//			byte[] setOrderTime = "Order time：2015-02-13 12:35:15".getBytes("gb2312");
//
//			byte[] tips_1 = "Follow twitter\"**\"order for $1 discount".getBytes("gb2312");
//			nextLine = ESCUtil.nextLine(1);
//			byte[] tips_2 = "Commentary reward 50 cents".getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            byte[][] cmdBytes = { underoff ,center, boldOn, fontSize2Big, Focus, boldOff, nextLine,
                    fontSizeNormal, left,nextLine, vipNumber ,nextLine,orderNumber, nextLine, cashier,nextLine,orderTime,nextLine,underline, line ,underoff ,nextLine,menu,nextLine
            };


            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  byte[] printBody(String goods) {



        try {
            byte[][] b = new byte[goodsList.size()*4][];
            for (int i = 0,j =0 ;i<goodsList.size()*4;i+=4) {
                String name = goodsList.get(j).getName();
                Double price = goodsList.get(j).getPrice();
                double num = goodsList.get(j).getNum();
                Double discount = goodsList.get(j).getDiscount();
                Double money = num *price -discount;


                nums += num;
                discounts += discount;
                moneys += money;
                b[i] = (name ).getBytes("gb2312");
                b[i+1] = ESCUtil.nextLine(1);
                b[i+2] = ("         "+ price + "     "+num +"     "+ DoubleSave.doubleSaveTwo(money) + "     "+DoubleSave.doubleSaveTwo(discount)).getBytes("gb2312");
                if (mOrderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                    b[i+2] = ("        "+ price + "     "+(-num) +"     "+ (-DoubleSave.doubleSaveTwo(money)) + "     "+DoubleSave.doubleSaveTwo(discount)).getBytes("gb2312");
                }
                b[i+3] = ESCUtil.nextLine(1);
                j++;
            }
            return ESCUtil.byteMerger(b);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] printFoot(String payMoney) {

        try {
            byte[] underoff = ESCUtil.underlineOff();
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] number = ("数量：       "+nums).getBytes("gb2312");

            byte[] payway = ("付款：       "+payWay).getBytes("gb2312");
            byte[] yinshou = ("应收：       "+DoubleSave.doubleSaveTwo(moneys)).getBytes("gb2312");
            byte[] shihsou = ("实收：       "+payMoney).getBytes("gb2312");
            if (mOrderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                number = ("数量：       "+(-nums)).getBytes("gb2312");
                yinshou = ("应收：       "+(-DoubleSave.doubleSaveTwo(moneys))).getBytes("gb2312");
                shihsou = ("实收：       "+(-Double.parseDouble(payMoney))).getBytes("gb2312");
            }

            byte[] discount = ("折让：       "+DoubleSave.doubleSaveTwo(discounts)).getBytes("gb2312");
            byte[] zhaolin =("找零：          "+zl).getBytes("gb2312");
            byte[] address = ("地址："+shop_address).getBytes("gb2312");
            byte[] phone = ("电话："+shop_phone).getBytes("gb2312");
            byte[] foot1 = (printBeen.getFoot1()).getBytes("gb2312");
            byte[] foot2 = (printBeen.getFoot2()).getBytes("gb2312");
            byte[] foot3 = (printBeen.getFoot3()).getBytes("gb2312");
            byte[] foot4 = (printBeen.getFoot4()).getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] line = "                                             ".getBytes("gb2312");
            byte[] underline = ESCUtil.underlineWithOneDotWidthOn();
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
            byte[][] cmdBytes = {underline ,line ,underoff ,nextLine ,number, nextLine, payway,
                    nextLine, yinshou, nextLine, shihsou, nextLine, discount, nextLine,
                    zhaolin, nextLine, underline, line, underoff, nextLine, foot1, nextLine,
                     foot2,nextLine, foot3,nextLine,  foot4,nextLine,   underline,
                    nextLine, breakPartial};
            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void starPrint() {
//        tablebeen = App.getInstance().getDaoSession().getTablebeenDao().queryBuilder().where(TablebeenDao.Properties.TId.eq(tableId)).unique();
//        if (tablebeen != null) {
//            tableNumber = tablebeen.getNumber();
//        }
        beginPrint();
    }
}
