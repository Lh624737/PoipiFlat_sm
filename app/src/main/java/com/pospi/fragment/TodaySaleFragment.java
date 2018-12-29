package com.pospi.fragment;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.newland.jsums.paylib.NLPay;
import com.newland.jsums.paylib.model.NormalPrintRequest;
import com.pospi.adapter.TodaySaleAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderPaytype;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.util.BluetoothUtil;
import com.pospi.pai.yunpos.util.ESCUtil;
import com.pospi.paxprinter.PaxPrint;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.App;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.Utils;
import com.pospi.util.constant.Key;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;
import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TodaySaleFragment extends android.app.Fragment {

    private TodaySaleAdapter adapter;
    private ListView lv;
    private TextView totalNum;
    private TextView totalSale;
    private TextView totalDiscount;
    private TextView totalShouru;
    private TextView today_print;
    private List<OrderDto> orderDtos;
    private Context context;
    private double refund = 0;
    private OrderDao dao;
    private String date_time;
    private String str_status;
    private double card_discount;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        View todaySale = inflater.inflate(R.layout.fragment_today_sale, container, false);

        final TextView data = (TextView) todaySale.findViewById(R.id.today_sale_data);
        data.setText(GetData.getYYMMDDTime());//进入界面的时候就会把日期定位在当天
        lv = (ListView) todaySale.findViewById(R.id.today_sale_lv);

        totalNum = (TextView) todaySale.findViewById(R.id.today_sale_total_num);
        totalSale = (TextView) todaySale.findViewById(R.id.today_sale_total_sale);
        totalDiscount = (TextView) todaySale.findViewById(R.id.today_sale_total_discount);
        totalShouru = (TextView) todaySale.findViewById(R.id.today_sale_total_shouru);
        today_print = (TextView) todaySale.findViewById(R.id.today_print);

        dao = new OrderDao(context);
        date_time = data.getText().toString().trim();
        AdapteAdapter(data.getText().toString().trim());
//        orderDtos = dao.selectGoods(GetData.getYYMMDDTime());
//        for (OrderDto orderDto : orderDtos) {
//            if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
//                refund += Double.parseDouble(orderDto.getSs_money().replace("-", ""));
//            }
//        }
//        for (int i = 0; i < orderDtos.size(); i++) {
//            OrderDto orderDto1 = orderDtos.get(i);
//            for (int j = orderDtos.size() - 1; j > i; j--) {
//                OrderDto orderDto2 = orderDtos.get(j);
//                if (orderDto1.getPayway().equals(orderDto2.getPayway())) {
//                    orderDto1.setSs_money(String.valueOf(Double.parseDouble(orderDto1.getSs_money())
//                            + Double.parseDouble(orderDto2.getSs_money())));
//                    orderDtos.remove(orderDto2);
//                }
//            }
//        }
        today_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = context.getSharedPreferences("userMsg", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
                String shop_name = preferences.getString("name", "");//得到存储的店铺的名字
                String shop_address = preferences.getString("address", "");//得到店铺的地址
                String shop_phone = preferences.getString("phone", "");//得到店铺的联系电话
                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_T8)) {
                    try {
                        ZQPrinterSDK prn = new ZQPrinterSDK();// 实例化SDk
                        prn.Prn_Connect("USB0", context);
                        prn.Prn_PrintText(
                                "日结",
                                PrinterConst.Alignment.CENTER,
                                PrinterConst.Font.DEFAULT,
                                PrinterConst.HeightSize.SIZE0);
                        prn.Prn_PrintText(
                                "日结" +
                                        "总销售额:           " + DoubleSave.doubleSaveTwo(saleTotal ) + "\r\n" +
                                        "打折销售:           " + DoubleSave.doubleSaveTwo(discount) + "\r\n" +
                                        "净销售额:           " + DoubleSave.doubleSaveTwo(saleTotal  + refund) + "\r\n" +
                                        "退货:               " + DoubleSave.doubleSaveTwo(refund) + "\r\n" +
                                        "订单数量:           " + num_order + "\r\n" +
                                        orderDtos.get(0).getPayway() + ":           " +
                                        DoubleSave.doubleSaveTwo(Double.parseDouble(orderDtos.get(0).getYs_money()))
                                        + "\r\n\r\n\r\n\r\n\r\n",
                                PrinterConst.Alignment.LEFT,
                                PrinterConst.Font.DEFAULT,
                                PrinterConst.HeightSize.SIZE0);
                        prn.Prn_CutPaper();//切纸
                        prn.Prn_EndTransaction();//结束
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
                    try {
                        byte status = PrnTest.prnStatus();
                        switch (status) {
                            case 0x00:
                                str_status = "打印机状态为：打印机可用";
                                new PaxPrint(App.getContext()).dailyPrint(date_time, saleTotal, discount, refund, num_order, num_refund, orderDtos);
                                break;
                            case 0x01:
                                str_status = "打印机状态为：打印机忙";
                                showMsgToast(str_status);
                                break;
                            case 0x02:
                                str_status = "打印机状态为：打印机缺纸";
                                showMsgToast(str_status);
                                break;
                            case 0x03:
                                str_status = "打印机状态为：打印数据包格式错";
                                showMsgToast(str_status);
                                break;
                            case 0x04:
                                str_status = "打印机状态为：打印机故障";
                                showMsgToast(str_status);
                                break;
                            case 0x08:
                                str_status = "打印机状态为：打印机过热";
                                showMsgToast(str_status);
                                break;
                            case 0x09:
                                str_status = "打印机状态为：打印机电压过低";
                                showMsgToast(str_status);
                                break;
                            case (byte) 0xf0:
                                str_status = "打印机状态为：打印未完成";
                                showMsgToast(str_status);
                                break;
                            case (byte) 0xfc:
                                str_status = "打印机状态为：打印机未装字库";
                                showMsgToast(str_status);
                                break;
                            case (byte) 0xfe:
                                str_status = "打印机状态为：数据包过长";
                                showMsgToast(str_status);
                                break;
                            case 97:
                                str_status = "打印机状态为：不支持的字符集";
                                showMsgToast(str_status);
                                break;
                            case 98:
                                str_status = "打印机状态为：参数为空";
                                showMsgToast(str_status);
                                break;
                            case 99:
                                str_status = "打印机状态为：RPC连接错误";
                                showMsgToast(str_status);
                                break;
                            default:
                                str_status = "打印机状态为：其它错误";
                                showMsgToast(str_status);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_DT92)) {
                    EscCommand esc = new EscCommand();
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
                    esc.addText("日结\n\n", "utf-8"); // 打印文字
        /* 打印文字 */
                    esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF,
                            EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF,
                            EscCommand.ENABLE.OFF);// 取消倍高倍宽
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
//        esc.addText("门店号: " + shop_id + "\n", "utf-8"); // 打印文字

        /* 打印文字 */
                    esc.addText("日结日期: " + date_time + "\n", "utf-8"); // 打印结束
                    esc.addText("打印时间: " + GetData.getDataTime() + "\n", "utf-8"); // 打印结束
                    esc.addText("--------------------------------\n\n", "utf-8"); // 打印文字
                    esc.addText("销售合计\n", "utf-8"); // 打印文字
                    esc.addText("--------------------------------\n", "utf-8"); // 打印文字
//        esc.addText("--------------------------------\n", "utf-8"); // 打印文字
        /* 打印销售详情 */
                    esc.addText("总销售额:           " + DoubleSave.doubleSaveTwo(saleTotal - discount + refund) + "\n", "utf-8"); // 打印文字
                    esc.addText("打折销售:           " + DoubleSave.doubleSaveTwo(discount) + "\n", "utf-8"); // 打印文字
                    esc.addText("净销售额:           " + DoubleSave.doubleSaveTwo(saleTotal - discount) + "\n", "utf-8"); // 打印文字
                    esc.addText("退货:               " + DoubleSave.doubleSaveTwo(refund) + "\n", "utf-8"); // 打印文字
                    esc.addText("订单数量:           " + num_order + "\n", "utf-8"); // 打印文字
                    esc.addText("退货订单数量:       " + num_refund + "\n", "utf-8"); // 打印文字
                    esc.addText("--------------------------------\n\n", "utf-8"); // 打印文字
        /* 打印店铺详情 */
                    esc.addText("结算信息\n", "utf-8"); // 打印文字
                    esc.addText("--------------------------------\n", "utf-8"); // 打印文字·
//                    HashMap<String, Double> map = new HashMap<String, Double>();
                    List<OrderPaytype> payList = new ArrayList<OrderPaytype>();
                    for (int i = 0; i < orderDtos.size(); i++) {
//                        String sid = orderDtos.get(i).getOrderId();
//                        List<OrderPaytype> paytypes = new OrderPaytypeDao(App.getContext()).query(sid);
////                        String payname = paytypes.get(0).getPayName();
//                        for (OrderPaytype op :
//                                paytypes) {
//                            OrderPaytype o = new OrderPaytype();
//                            o.setPayName(op.getPayName());
//                            o.setPayCode(op.getPayCode());
//                            o.setYs(op.getYs());
//                            payList.add(o);
//                        }
                        esc.addText(orderDtos.get(i).getPayway() + ":           " +
                                DoubleSave.doubleSaveTwo(Double.parseDouble(orderDtos.get(i).getYs_money()))
                                + "\n", "utf-8"); // 打印文字
                    }

                    esc.addText("\n--------------------------------\n", "utf-8"); // 打印文字
                     /* 打印店铺详情 */
                    esc.addText("店铺: " + shop_name + "\n", "utf-8"); // 打印文字
                    esc.addText("地址: " + shop_address + "\n", "utf-8"); // 打印文字
//        esc.addText("公司网址: " + "www.pos.pospi.com" + "\n", "utf-8"); // 打印文字
                    esc.addText("电话: " + shop_phone + "\n", "utf-8"); // 打印文字
                    esc.addPrintAndFeedLines((byte) 7);// 打印8个空行

                    Vector<Byte> datas = esc.getCommand(); // 发送数据
                    Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
                    byte[] bytes = ArrayUtils.toPrimitive(Bytes);
                    String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据

//                    Log.i("GPrint", "打印的字符串数据: " + sss);
                    GpService mGpService = App.getmGpService();
                    try {
                        if (mGpService == null) {
                            Utils.connection();
                            mGpService = App.getmGpService();
                            mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
                        }
                        int e = mGpService.sendEscCommand(0, sss);
                        Log.i("GPrintLog", "发送打印指令返回码: " + e);
                    } catch (RemoteException e) {
                        Log.i("GPrintLog", "日结打印发送打印数据时异常: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
                    try {
                        NLPay pay = NLPay.getInstance();
                        NormalPrintRequest request = new NormalPrintRequest();
                        request.setAppAccessKeyId(Key.APP_SECRET_KEY);
                        request.setMrchNo(Key.MRCH_NO);
                        ArrayList<String> lists = new ArrayList<String>();
                        JSONObject content2 = new JSONObject();
                        content2.put("type", "0");
                        String content = "";

                        content += "              日结\n\n";
                        content += "日结日期: " + date_time + "\n";
                        content += "打印时间: " + GetData.getDataTime() + "\n";
                        content += "--------------------------------\n\n";
                        content += "销售合计\n";
                        content += "--------------------------------\n";
                        content += "总销售额:           " + DoubleSave.doubleSaveTwo(saleTotal - discount + refund) + "\n";
                        content += "打折销售:           " + DoubleSave.doubleSaveTwo(discount) + "\n";
                        content += "净销售额:           " + DoubleSave.doubleSaveTwo(saleTotal - discount) + "\n";
                        content += "退货:               " + DoubleSave.doubleSaveTwo(refund) + "\n";
                        content += "订单数量:           " + num_order + "\n";
                        content += "退货订单数量:       " + num_refund + "\n";
                        content += "--------------------------------\n\n";
                        content += "结算信息\n";
                        content += "--------------------------------\n";
                        for (int i = 0; i < orderDtos.size(); i++) {
                            content += orderDtos.get(i).getPayway() + ":           " +
                                    DoubleSave.doubleSaveTwo(Double.parseDouble(orderDtos.get(i).getYs_money()))
                                    + "\n";
                        }
                        content += "\n--------------------------------\n";
                        content += "店铺: " + shop_name + "\n";
                        content += "地址: " + shop_address + "\n";
                        content += "电话: " + shop_phone + "\n\n\n";
                        content2.put("content", content);
                        lists.add(content2.toString());
                        request.setContents(lists);
                        pay.normalPrint(getActivity(), request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Build.MODEL.equals(URL.MODEL_T1)) {
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

                    byte[] body = printDayMsgTop();
                    byte[] pays = printPayWay();
                    byte[] foot = printFoot();
                    byte[] data1 = ESCUtil.byteMerger(body, pays);
                    byte[] result = ESCUtil.byteMerger(data1, foot);
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
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);
                        date_time = (String) DateFormat.format("yyyy-MM-dd", c);
                        data.setText(DateFormat.format("yyyy-MM-dd", c));
                        AdapteAdapter(data.getText().toString().trim());//当日期改变之后会调用此方法再对数据库进行查询
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        return todaySale;
    }
    public byte[] printDayMsgTop() {

        try {
            byte[] underoff = ESCUtil.underlineOff();
            byte[] next2Line = ESCUtil.nextLine(2);
//			byte[] title = "The menu（launch）**wanda plaza".getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(2);
            byte[] center = ESCUtil.alignCenter();
            byte[] Focus = "日结".getBytes("gb2312");
            byte[] date = ("日结日期："+date_time).getBytes("gb2312");
            byte[] dateTime = ("单据时间："+ GetData.getDataTime()).getBytes("gb2312");
            byte[] totalSale = "销售统计".getBytes("gb2312");
            byte[] pay = "支付方式统计".getBytes("gb2312");

            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSizeNormal = ESCUtil.fontSizeSetBig(1);
            byte[] line = "-----------------------------------------".getBytes("gb2312");
            byte[] left = ESCUtil.alignLeft();
            byte[] totalSaleMoney = ("总销售额："+ DoubleSave.doubleSaveTwo(saleTotal+refund )).getBytes("gb2312");
            byte[] discountSale = ("总折扣额："+ DoubleSave.doubleSaveTwo(discount)).getBytes("gb2312");
            byte[] sales = ("净销售额："+ DoubleSave.doubleSaveTwo(saleTotal )).getBytes("gb2312");
            byte[] refunds = ("退货："+  DoubleSave.doubleSaveTwo(refund)).getBytes("gb2312");
            byte[] underline = ESCUtil.underlineWithOneDotWidthOn();
            byte[] ordeNum = ("订单数量："+num_order).getBytes("gb2312");
            byte[] refundsNum = ("退货订单数量："+ num_refund).getBytes("gb2312");
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            byte[][] cmdBytes = { underoff ,center, boldOn, fontSize2Big, Focus, boldOff, nextLine,
                    fontSizeNormal, left,nextLine, date, nextLine, dateTime,nextLine,line ,nextLine ,totalSale,
                    nextLine,line, nextLine ,totalSaleMoney ,nextLine,sales ,nextLine ,refunds ,nextLine ,ordeNum ,nextLine,
                    refundsNum ,nextLine ,line,nextLine,pay,nextLine,line,nextLine
            };


            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    //打印支付方式统计
    private byte[] printPayWay(){
        List<OrderDto> dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(date_time);
        List<OrderPaytype> orderPaytypes = getPays(dtos);
        byte[][] b = new byte[orderPaytypes.size()*2][];
        for (int i = 0,j =0 ;i<orderPaytypes.size()*2;i+=2) {

            try {
                b[i] = (orderPaytypes.get(j).getPayName()+"    "+orderPaytypes.get(j).getYs() ).getBytes("gb2312");
                b[i+1] = ESCUtil.nextLine(1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            j++;

        }

        return ESCUtil.byteMerger(b);
    }
    private byte[] printFoot(){
        byte[] next4Line = ESCUtil.nextLine(4);
        byte[] underline = ESCUtil.underlineWithOneDotWidthOn();
        byte[] breakPartial = ESCUtil.feedPaperCutPartial();
        byte[][] cmdBytes = {next4Line, breakPartial};
        return ESCUtil.byteMerger(cmdBytes);
    }

    int num_order;//订单数量
    int num_refund;//退货订单数量
    double saleTotal;
    double discount;

    /**
     * 用数据去绑定adapter
     */
    public void AdapteAdapter(String date) {
        orderDtos = dao.selectGoods(date);
        refund = 0;
        for (OrderDto orderDto : orderDtos) {
            if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    refund += Double.parseDouble(orderDto.getSs_money().replace("-", "")) * card_discount;
                } else {
                    refund += Double.parseDouble(orderDto.getSs_money().replace("-", ""));
                }
            }
        }
        for (int i = 0; i < orderDtos.size(); i++) {
            OrderDto orderDto1 = orderDtos.get(i);
            for (int j = orderDtos.size() - 1; j > i; j--) {
                OrderDto orderDto2 = orderDtos.get(j);
                if (orderDto1.getPayway().equals(orderDto2.getPayway())) {
                    orderDto1.setYs_money(String.valueOf(Double.parseDouble(orderDto1.getYs_money())
                            + Double.parseDouble(orderDto2.getYs_money())));
                    orderDtos.remove(orderDto2);
                }
            }
        }
        num_order = 0;//订单数量
        num_refund = 0;//退货订单数量
        saleTotal = 0;
        discount = 0;
        Log.d("todaysale", "开始绑定数据");
        List<OrderDto> dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(date);
//        Log.d(dtos.get(0).getOrder_info(), "名称");
        Log.d("数据大小", "size:" + dtos.size());
        List<GoodsDto> goodsDtoList = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            num_order += 1;
            if (dtos.get(i).getOrderType() == URL.ORDERTYPE_REFUND) {
                num_refund += 1;
            }
            String orderInfo = dtos.get(i).getOrder_info();

            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderInfo);
//            if (dtos.get(i).getPayway().equals(PayWay.CZK)) {
//                saleTotal += Double.parseDouble(dtos.get(i).getYs_money()) * card_discount;
//                discount += Double.parseDouble(dtos.get(i).getYs_money()) * (1 - card_discount);
//                for (GoodsDto goodsDto : goodsDtos) {
//                    goodsDto.setSetFlag(true);
//                }
//            } else {
                saleTotal += Double.parseDouble(dtos.get(i).getYs_money());
                for (GoodsDto goodsDto : goodsDtos) {
                    discount += goodsDto.getDiscount();
                }
//            }
            goodsDtoList.addAll(goodsDtos);
        }
        //把每次提交的订单累加在一起
//        Log.i("size", goodsDtoList.size() + "");
//        for (int i = 0; i < goodsDtoList.size(); i++) {
//            GoodsDto good1 = goodsDtoList.get(i);
//            for (int j = goodsDtoList.size() - 1; j > i; j--) {
//                GoodsDto good2 = goodsDtoList.get(j);
//                if (good1.getSid().equals(good2.getSid())) {
//                    good1.setNum(good1.getNum() + good2.getNum());
//                    goodsDtoList.remove(good2);
//                }
//            }
//        }


        adapter = new TodaySaleAdapter(getActivity().getApplicationContext(), setGoods(goodsDtoList));

        lv.setAdapter(adapter);
/********************************************************************************/
//        for (int i = 0; i < goodsDtoList.size(); i++) {
//            saleTotal += (goodsDtoList.get(i).getPrice()) * (goodsDtoList.get(i).getNum() * card_discount);
//            discount += goodsDtoList.get(i).getDiscount();
//        }
        totalNum.setText(String.valueOf(num_order));
        totalSale.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleTotal)));
        totalDiscount.setText(String.valueOf(DoubleSave.doubleSaveTwo(discount)));
        totalShouru.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleTotal)));
    }

    //合并相同商品
    public List<GoodsDto> setGoods(List<GoodsDto> dtos) {
        GoodsDto tempDto = null;
        Map<String, GoodsDto> dtoMap = new HashMap<>();//合并后的数据
        for (GoodsDto gd : dtos) {
            tempDto = dtoMap.get(gd.getSid());
            if (tempDto != null) {
                tempDto.setNum(tempDto.getNum() + gd.getNum());
                tempDto.setDiscount(DoubleSave.doubleSaveTwo(tempDto.getDiscount()+gd.getDiscount()));
            } else {
                dtoMap.put(gd.getSid(), gd);
            }
        }
        List<GoodsDto> dtos1 = new ArrayList<>();
        for (Map.Entry<String, GoodsDto> entry : dtoMap.entrySet()) {
            dtos1.add(entry.getValue());
        }
        Collections.sort(dtos1, new Comparator<GoodsDto>() {
            @Override
            public int compare(GoodsDto o1, GoodsDto o2) {
                return (int)(o2.getNum() - o1.getNum());
            }
        });
        return dtos1;
    }
    //合并相同支付方式的金额
//    public List<OrderDto> getPriceDetail(List<OrderDto> dtos) {
//        OrderDto tempDto = null;
//        Map<String, OrderDto> dtoMap = new HashMap<>();//合并后的数据
//        for (OrderDto gd : dtos) {
//            tempDto = dtoMap.get(gd.getPayway());
//            if (tempDto != null) {
//                tempDto.setSs_money(DoubleSave.doubleSaveTwo(Double.parseDouble(tempDto.getSs_money()) + Double.parseDouble(gd.getSs_money()))+"");
//            } else {
//                dtoMap.put(gd.getPayway(), gd);
//            }
//        }
//        List<OrderDto> dtos1 = new ArrayList<>();
//        for (Map.Entry<String, OrderDto> entry : dtoMap.entrySet()) {
//            dtos1.add(entry.getValue());
//        }
//        return dtos1;
//    }

    /**
     * 信息提示
     *
     * @param msg
     */
    private void showMsgToast(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    //获取所有支付方式的金额
    private List<OrderPaytype> getPays(List<OrderDto> dtos) {
        List<OrderPaytype> list = new ArrayList<>();
        for (OrderDto dto : dtos) {
            List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(dto.getOrderId());
            for (OrderPaytype op : orderPaytypes) {
                list.add(op);
            }
        }

        return setPays(list);
    }
    //合并相同支付方式
    public List<OrderPaytype> setPays(List<OrderPaytype> dtos) {
        OrderPaytype tempDto = null;
        Map<String, OrderPaytype> dtoMap = new HashMap<>();//合并后的数据
        for (OrderPaytype gd : dtos) {
            tempDto = dtoMap.get(gd.getPayName());
            if (tempDto != null) {
                tempDto.setYs(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(tempDto.getYs()) + Double.parseDouble(gd.getYs()))));
                tempDto.setSs(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(tempDto.getSs()) + Double.parseDouble(gd.getSs()))));
                tempDto.setZl(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(tempDto.getZl()) + Double.parseDouble(gd.getZl()))));
            } else {
                dtoMap.put(gd.getPayName(), gd);
            }
        }
        List<OrderPaytype> dtos1 = new ArrayList<>();
        for (Map.Entry<String, OrderPaytype> entry : dtoMap.entrySet()) {
            dtos1.add(entry.getValue());
        }
        return dtos1;
    }

}
