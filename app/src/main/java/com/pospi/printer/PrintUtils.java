package com.pospi.printer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.android.print.sdk.PrinterConstants.Command;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.Table;
import com.pospi.dto.CashierMsgDto;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.ValueCardDto;
import com.pospi.pai.yunpos.R;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;

import java.io.OutputStreamWriter;
import java.util.List;

public class PrintUtils {
    private static int whichOne;
    private static String Response;
    private static List<CashierMsgDto> cashierMsgDtos;
    private static CashierMsgDto cashierMsgDto;

    private static String shop_user;
    private static String shop_name;
    private static String shop_address;
    private static String shop_phone;
    private static String shop_id;

    private static String device_no;
    double discounts = 0.00;
    double moneys = 0.00;
    double nums = 0;

    public void getShareformanceData(Context context) {


        //服务器返回的一个数据，包含所有收银员的信息，我直接把它存储起来了
        SharedPreferences preferences = context.getSharedPreferences("cashierMsgDtos", context.MODE_PRIVATE);
        Response = preferences.getString("cashierMsgDtos", "");


        //调用方法来解析数据，会得到一个关于收银员信息的一个list
        cashierMsgDtos = new CashierLogin_pareseJson().parese(Response);

        //选择收银员登陆的时候我就把选择的序号记录了下来，选择的是哪一个，cashierMsgDtos.get(whichOne).getName()得到当前的收银员的信息
        preferences = context.getSharedPreferences("islogin", context.MODE_PRIVATE);
        whichOne = preferences.getInt("which", 0);
        Log.i("which", "" + whichOne);

        //得到所选择的店铺的名字

        /**
         * StoreMessage  存储的店铺的信息
         */
        preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
        shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
        shop_address = preferences.getString("Address", "");//得到店铺的地址
        shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话


        preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
        String value = preferences.getString("value", "");
        String[] names = value.split("\\,");
        device_no = names[2];//得到收银机的设备号，向服务器请求数据的时候服务器给分配的一个数据
        shop_id = names[3];//得到店铺的id 也就是门店号
    }

    public void printNote(Context context, Resources resources, PrinterInstance mPrinter,
                          boolean is58mm, OrderDto orderDto, ValueCardDto valueCardDto) {

        getShareformanceData(context);
        mPrinter.init();

        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        mPrinter.printText(resources.getString(R.string.str_note));
        mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);


        StringBuffer sb = new StringBuffer();

        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
        mPrinter.setCharacterMultiple(1, 1);
        mPrinter.printText(shop_name//resources.getString(R.string.shop_company_title
                + "\n");

        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        // 字号使用默认
        mPrinter.setCharacterMultiple(0, 0);
        sb.append(resources.getString(R.string.shop_num) + shop_id + "\n");
        sb.append(resources.getString(R.string.shop_receipt_num)
                + orderDto.getMaxNo() + "\n");
        sb.append(resources.getString(R.string.shop_cashier_num)
                + cashierMsgDtos.get(whichOne).getName() + "\n");
        sb.append(resources.getString(R.string.shop_print_time)
                + GetData.getDataTime() + "\n");
        mPrinter.printText(sb.toString()); // 打印
        printTable1(resources, mPrinter, is58mm, orderDto); // 打印表格

        if (valueCardDto == null) {
            sb = new StringBuffer();
            if (is58mm) {
                sb.append(resources.getString(R.string.shop_goods_number)
                        + "                " + nums + "\n");
                sb.append(resources.getString(R.string.shop_goods_total_price)
                        + "                " + moneys + "\n");
                sb.append(resources.getString(R.string.shop_payment)
                        + "                " + orderDto.getSs_money() + "\n");
                sb.append(resources.getString(R.string.shop_change)
                        + "                " + orderDto.getZl_money() + "\n");
            } else {
                sb.append(resources.getString(R.string.shop_goods_number)
                        + "                               " + nums + "\n");
                sb.append(resources.getString(R.string.shop_goods_total_price)
                        + "                               " + moneys + "\n");
                sb.append(resources.getString(R.string.shop_payment)
                        + "                               " + orderDto.getSs_money() + "\n");
                sb.append(resources.getString(R.string.shop_change)
                        + "                               " + orderDto.getZl_money() + "\n");
            }
        } else {
            PrnTest.prnStr("储值卡卡号:  " + valueCardDto.getCardNo() + "\n", null);
            PrnTest.prnStr("  消费前余额:    " + valueCardDto.getCardAmount() + "\n", null);
            PrnTest.prnStr("  本次消费金额:  " + orderDto.getYs_money() + "\n", null);
            PrnTest.prnStr("  消费后余额:    " + (valueCardDto.getCardAmount() - Double.parseDouble(orderDto.getYs_money())) + "\n", null);
            PrnTest.prnStr("  卡押金:        " + valueCardDto.getMortgageAmount() + "\n", null);
        }

        sb.append("公司名称：" + shop_name + "\n");
        sb.append(resources.getString(R.string.shop_company_site)
                + "www.pospi.com\n");
        sb.append(resources.getString(R.string.shop_company_address) + shop_address + "\n");
        sb.append(resources.getString(R.string.shop_company_tel)
                + shop_phone + "\n");
        if (is58mm) {
            sb.append("==============================\n");
        } else {
            sb.append("==============================================\n");
        }
        mPrinter.printText(sb.toString());

        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
        mPrinter.setCharacterMultiple(0, 1);
        mPrinter.printText(resources.getString(R.string.shop_thanks) + "\n\n\n\n\n");
        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);


    }


    private OutputStreamWriter writer = null;

    public void cutPaper() throws Exception {
        writer.write(0x1D);
        writer.write(86);
        writer.write(65);
//        writer.write(0);
//切纸前走纸多少
        writer.write(100);
        writer.flush();
    }

    public void printTable1(Resources resources,
                            PrinterInstance mPrinter, boolean is58mm, OrderDto orderDto) {
        mPrinter.init();
        String column = resources.getString(R.string.note_title);
        Table table;
        if (is58mm) {
            table = new Table(column, ";", new int[]{12, 5, 5, 5, 5});
        } else {
            table = new Table(column, ";", new int[]{16, 8, 8, 10, 8});
        }
        //品名;单价;数量;金额;折扣
        double discount = 0.00;
        double money = 0.00;
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
        for (int i = 0; i < goodsDtos.size(); i++) {
            GoodsDto goodsDto = goodsDtos.get(i);
            discount = goodsDto.getDiscount();
            money = goodsDto.getNum() * goodsDto.getNum() - discount;
            moneys += money;
            discounts += discount;
            nums += goodsDto.getNum();
            table.addRow(goodsDto.getName() + ";" + goodsDto.getPrice() + ";" + goodsDto.getNum() + ";" +
                    money + ";" + discount);
        }
        mPrinter.printTable(table);
    }


}
