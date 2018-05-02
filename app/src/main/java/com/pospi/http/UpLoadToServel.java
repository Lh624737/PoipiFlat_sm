package com.pospi.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.dao.OrderDao;
import com.pospi.dao.OrderMenuDao;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.CashierMsgDto;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderMenu;
import com.pospi.dto.OrderPaytype;
import com.pospi.dto.PayWayDto;
import com.pospi.dto.ValueCardDto;
import com.pospi.pai.pospiflat.util.CreateFiles;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.Utils;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

import me.xiaopan.android.preference.PreferencesUtils;

public class UpLoadToServel {

    static String url = "http://116.228.196.235:9185/SalesTrans/rest/salestransaction/salestranslitev61";
    static String contentType = "application/json;charset=utf-8";

    static String apiKey = "CRMINT";//apiKey
    static String signature = "";//签名
    static String storeCode = "SHZ000027";//店铺编码
    static String tillId = "01";//商场ID
    static String cashier = "010201";//收银员
    static String itemCode = "SHZ00002701";//料号
    static String tenderCode = "CH";//交易码
    static String baseCurrencyCode = "RMB";//
    static String itemOrgId = "000013";//
    private Context context;

    public UpLoadToServel(Context context) {
        this.context = context;
    }

    /**
     * 上传订单到服务器
     */
   /* public void uploadOrderToServer(final OrderDto orderDto, PayWayDto payWayDto, String orderType, final Context context, final int from) {
        //得到店铺的Id
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");//得到店铺的ID

        //得到当前的时间
        String date = GetData.getDataTime();
        //得到就餐的人数
        double paxNumber = 0;
        preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String value = preferences.getString("value", "");
        String[] names = value.split(",");
        String deviceNo = names[2];//收银机号
        String uid = names[1];

        preferences = context.getSharedPreferences("islogin", Context.MODE_PRIVATE);
        int whichOne = preferences.getInt("which", 0);

        preferences = context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE);
        String Response = preferences.getString("cashierMsgDtos", "");

        List<CashierMsgDto> cashierMsgDtos = new CashierLogin_pareseJson().parese(Response);

        String by = cashierMsgDtos.get(whichOne).getNumber();//得到收银员的账号

        JSONObject Order = new JSONObject();

        JSONObject Order_PayType = new JSONObject();

        JSONArray jsonArrayOrder = new JSONArray();
        jsonArrayOrder.put(Order);
        JSONArray jsonArrayOrder_Menus = new JSONArray();
        JSONArray jsonArrayOrder_PayType = new JSONArray();
        jsonArrayOrder_PayType.put(Order_PayType);

        try {
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            double discount = 0;
            int totalNum = 0;
            double card_discount = 1;
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
            }
            for (int i = 0; i < goodsDtos.size(); i++) {
                GoodsDto goodsDto = goodsDtos.get(i);
                discount += goodsDto.getDiscount();
                totalNum += goodsDto.getNum();

                JSONObject Order_Menus = new JSONObject();

//                Log.i("sidsid", "uploadOrderToServer: sid" + goodsDto.getSid());

                Order_Menus.put("obj1", "ad18163b22b14245bf131f6d04051643");
                Order_Menus.put("obj2", "9787115362865");
                Order_Menus.put("obj3", "0");//
                Order_Menus.put("createTime", date); //当前的时间

                Order_Menus.put("discountType", "0");//折扣类型

                Order_Menus.put("order_sid", orderDto.getOrderId());//order表的sid
                Order_Menus.put("parentSid", "");//父id
                Order_Menus.put("type", 1);//订单的类型 1是销售，2是退货i
                Order_Menus.put("saleMethod", 0);//销售方式
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("totalDiscount", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * (1 - card_discount)));//折扣总额
                } else {
                    Order_Menus.put("totalDiscount", goodsDto.getDiscount());//折扣总额
                }
                Order_Menus.put("number", goodsDto.getNum());//销售数量
                Order_Menus.put("m_price", goodsDto.getPrice());//商品的单价
                //订单详情表的sid
                Order_Menus.put("sid", goodsDto.getGoodsId());//本地数据库主键
                Log.i("sid", "Order_Menus------------->"+goodsDto.getGoodsId());
                Order_Menus.put("price", goodsDto.getPrice() * goodsDto.getNum());//商品的总价

                Order_Menus.put("Uid", uid);//用户id
                Order_Menus.put("menus_sid", goodsDto.getSid());//商品的sid。。
                Log.i("menus_sid", "menus_sid: " + goodsDto.getSid());
                Order_Menus.put("menus_name", goodsDto.getName());//商品名称
                Order_Menus.put("callCount", 0);//
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("saleprice", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * card_discount));//销售总价
                } else {
                    Order_Menus.put("saleprice", goodsDto.getPrice() * goodsDto.getNum() - goodsDto.getDiscount());//销售总价
                }
                Order_Menus.put("lineNum", i);//行号
                Order_Menus.put("status", 4);//订单的状态，4是正常销售，上传的时候用
                Order_Menus.put("singleDiscount", goodsDto.getDiscount());//单品折扣
                jsonArrayOrder_Menus.put(Order_Menus);
            }
            Order_PayType.put("obj2", "0");
            Order_PayType.put("obj3", "(null)");
            Order_PayType.put("obj4", "(null)");
            Order_PayType.put("obj5", "(null)");
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order_PayType.put("price_ys", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
            } else {
                Order_PayType.put("price_ys", orderDto.getYs_money());//应收
            }

            Order_PayType.put("orderBy", 0);//排序
            Order_PayType.put("lineNum", 1);//行号
            Order_PayType.put("payId", payWayDto.getSid());//付款方式id
            Order_PayType.put("payType", payWayDto.getPayType1());//付款类型
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order_PayType.put("price", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//实收金额
            } else {
                Order_PayType.put("price", orderDto.getSs_money());//实收金额
            }
            Order_PayType.put("price_zl", orderDto.getZl_money());//找零
            //Order_PayType.put("sid", payWayDto.getPayWayId());//本地数据库主键
            Order_PayType.put("sid", orderDto.getOrderSid());//本地数据库主键
            Log.i("sid", "Order_PayType------------->"+orderDto.getOrderSid());
            Order_PayType.put("order_sid", orderDto.getOrderId());//order表的sid。。
            Order_PayType.put("name", orderDto.getPayway());//付款方式名
            Order_PayType.put("Uid", uid);//用户id
            Order_PayType.put("status", 4);//订单的状态，4是正常销售，上传的时候用

            Order.put("obj1", "");//空
            Order.put("obj2", "");
            Order.put("shopNo", shopid);//
            Order.put("obj3", "");
            Order.put("obj4", "");
            Order.put("obj5", "");
            Order.put("obj6", orderDto.getPayReturn());//微信支付宝支付，返回码流水
            Order.put("obj10", "");
            Order.put("obj9", "");
            Order.put("obj7", "");
            Order.put("obj8", "");
            Order.put("paxNumber", String.valueOf(goodsDtos.size()));//本单商品数量
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("remoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
            } else {
                Order.put("remoney", orderDto.getYs_money());//应收
            }
            Order.put("cashNo", deviceNo);//收银机号
            Order.put("number", totalNum);//销售数量
            Order.put("type", orderType);//订单的类型 1是销售，2是退货
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("acmoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//应收
            } else {
                Order.put("acmoney", orderDto.getSs_money());//应收
            }
            Order.put("date", orderDto.getCheckoutTime());//orderDto.getCheckoutTime()startTime

            //获取登录手机的Mac。但是该方法必须要连接上了wifi才可以
            String macAddress = "000000000000";
            try {
                //首先得到系统的服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
                //当info 不是为空的时候
                if (info != null) {
                    //当info.getMacAddress不为空的时候
                    if (!TextUtils.isEmpty(info.getMacAddress())) {
                        macAddress = info.getMacAddress().replace(":", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Order.put("imel", macAddress);//设备唯一识别码
            Order.put("saleMethod", 0);//销售方式
            Order.put("sid", orderDto.getOrderId());//本地数据库主键
            Log.i("sid", "Order---------------->" + orderDto.getOrderId());
            Order.put("no", orderDto.getMaxNo());//小票号
            Order.put("uid", uid);//用户id
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("discount", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * (1 - card_discount)));//折扣总额
            } else {
                Order.put("discount", discount);//折扣金额
            }

            Order.put("cgmoney", orderDto.getZl_money());//找零
            Order.put("by", by);//收银员号
//            Order.put("shopNo", orderDto.getShop_id());//店铺号
            Order.put("status", 4);//订单的状态，4是正常销售，上传的时候用

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();

        params.put("Order", jsonArrayOrder.toString());
        params.put("Order_Menus", jsonArrayOrder_Menus.toString());
        params.put("Order_PayType", jsonArrayOrder_PayType.toString());

        Log.i("上传订单", "params" + params.toString());

        new Server().getConnect(context, new URL().SYNCORDER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    LoginReturnDto returnDto = new LoginReturnDto();

                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getString("Value")));
                        if (returnDto.getResult() == 1) {
                            Log.i("上传订单", "向服务器发送数据成功" + new String(bytes));
                            new OrderDao(context).updateServerInfo(orderDto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    public void uploadOrderToServer(final OrderDto orderDto, PayWayDto payWayDto, String orderType, final Context context, final int from) {


        List<OrderMenu> orderMenus = new OrderMenuDao(App.getContext()).query(orderDto.getOrderId());
        List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(orderDto.getOrderId());
        //得到店铺的Id
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");//得到店铺的ID

        //得到当前的时间
        String date = GetData.getDataTime();
        //得到就餐的人数
        double paxNumber = 0;
        preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String value = preferences.getString("value", "");
        String[] names = value.split(",");
        String deviceNo = names[2];//收银机号
        String uid = names[1];

        preferences = context.getSharedPreferences("islogin", Context.MODE_PRIVATE);
        int whichOne = preferences.getInt("which", 0);

        preferences = context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE);
        String Response = preferences.getString("cashierMsgDtos", "");

        List<CashierMsgDto> cashierMsgDtos = new CashierLogin_pareseJson().parese(Response);

        String by = cashierMsgDtos.get(whichOne).getNumber();//得到收银员的账号

        JSONObject Order = new JSONObject();

//        JSONObject Order_PayType = new JSONObject();

        JSONArray jsonArrayOrder = new JSONArray();
        jsonArrayOrder.put(Order);
        JSONArray jsonArrayOrder_Menus = new JSONArray();
        JSONArray jsonArrayOrder_PayType = new JSONArray();
//        jsonArrayOrder_PayType.put(Order_PayType);
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
        double discount = 0;
        int totalNum = 0;
        double card_discount = 1;
        try {
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
            }
            //订单详情
            for (int i = 0; i < goodsDtos.size(); i++) {
                GoodsDto goodsDto = goodsDtos.get(i);
                discount += goodsDto.getDiscount();
                totalNum += goodsDto.getNum();

                JSONObject Order_Menus = new JSONObject();

//                Log.i("sidsid", "uploadOrderToServer: sid" + goodsDto.getSid());

                Order_Menus.put("obj1", "ad18163b22b14245bf131f6d04051643");
                Order_Menus.put("obj2", "9787115362865");
                Order_Menus.put("obj3", "0");//
                Order_Menus.put("createTime", date); //当前的时间

                Order_Menus.put("discountType", "0");//折扣类型

                Order_Menus.put("order_sid", orderDto.getOrderId());//order表的sid
                Order_Menus.put("parentSid", "");//父id
                Order_Menus.put("type", 1);//订单的类型 1是销售，2是退货i
                Order_Menus.put("saleMethod", 0);//销售方式
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("totalDiscount", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * (1 - card_discount)));//折扣总额
                } else {
                    Order_Menus.put("totalDiscount", goodsDto.getDiscount());//折扣总额
                }
                Order_Menus.put("number", goodsDto.getNum());//销售数量
                Order_Menus.put("m_price", goodsDto.getPrice());//商品的单价
                //订单详情表的sid
                Order_Menus.put("sid", orderMenus.get(i).getSid());//本地数据库主键
                Log.i("sid", "Order_Menus------------->"+orderMenus.get(i).getSid());
                Order_Menus.put("price", goodsDto.getPrice() * goodsDto.getNum());//商品的总价

                Order_Menus.put("Uid", uid);//用户id
                Order_Menus.put("menus_sid", goodsDto.getSid());//商品的sid。。
                Log.i("menus_sid", "menus_sid: " + goodsDto.getSid());
                Order_Menus.put("menus_name", goodsDto.getName());//商品名称
                Order_Menus.put("callCount", 0);//
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("saleprice", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * card_discount));//销售总价
                } else {
                    Order_Menus.put("saleprice", goodsDto.getPrice() * goodsDto.getNum() - goodsDto.getDiscount());//销售总价
                }
                Order_Menus.put("lineNum", i);//行号
                Order_Menus.put("status", 4);//订单的状态，4是正常销售，上传的时候用
                Order_Menus.put("singleDiscount", goodsDto.getDiscount());//单品折扣
                jsonArrayOrder_Menus.put(Order_Menus);
            }
            //支付方式
            for (int j =0;j<orderPaytypes.size();j++) {
                JSONObject Order_PayType = new JSONObject();
                Order_PayType.put("obj2", "0");
                Order_PayType.put("obj3", "(null)");
                Order_PayType.put("obj4", "(null)");
                Order_PayType.put("obj5", "(null)");
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_PayType.put("price_ys", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
                } else {
//                    Order_PayType.put("price_ys", orderDto.getYs_money());//应收
                    Order_PayType.put("price_ys", orderPaytypes.get(j).getYs());//应收
                }

                Order_PayType.put("orderBy", 0);//排序
                Order_PayType.put("lineNum", 1);//行号
                String paysid = new PayWayDao(App.getContext()).findPaySid(orderPaytypes.get(j).getPayCode() + "");
                Order_PayType.put("payId", paysid);//付款方式id
                Order_PayType.put("payType", orderPaytypes.get(j).getPayCode());//付款类型


                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_PayType.put("price", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//实收金额
                } else {
//                    Order_PayType.put("price", orderDto.getSs_money());//实收金额
                    Order_PayType.put("price", orderPaytypes.get(j).getSs());//实收金额
                }
//                Order_PayType.put("price_zl", orderDto.getZl_money());//找零
                Order_PayType.put("price_zl", orderPaytypes.get(j).getZl());//找零
                //Order_PayType.put("sid", payWayDto.getPayWayId());//本地数据库主键
                Order_PayType.put("sid", orderPaytypes.get(j).getSid());//本地数据库主键
                Log.i("sid", "Order_PayType------------->"+orderPaytypes.get(j).getSid()+"---"+orderPaytypes.get(j).getPayName());
                Order_PayType.put("order_sid", orderDto.getOrderId());//order表的sid。。
//                Order_PayType.put("name", orderDto.getPayway());//付款方式名
                Order_PayType.put("name", orderPaytypes.get(j).getPayName());//付款方式名
                Order_PayType.put("Uid", uid);//用户id
                Order_PayType.put("status", 4);//订单的状态，4是正常销售，上传的时候用
                jsonArrayOrder_PayType.put(Order_PayType);
            }

            //订单
            Order.put("obj1", "");//空
            Order.put("obj2", "");
            Order.put("shopNo", shopid);//
            Order.put("obj3", "");
            Order.put("obj4", "");
            Order.put("obj5", "");
            Order.put("obj6", orderDto.getPayReturn());//微信支付宝支付，返回码流水
            Order.put("obj10", "");
            Order.put("obj9", "");
            Order.put("obj7", "");
            Order.put("obj8", "");
            Order.put("paxNumber", String.valueOf(goodsDtos.size()));//本单商品数量
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("remoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
            } else {
                Order.put("remoney", orderDto.getYs_money());//应收
            }
            Order.put("cashNo", deviceNo);//收银机号
            Order.put("number", totalNum);//销售数量
            Order.put("type", orderType);//订单的类型 1是销售，2是退货
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("acmoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//应收
            } else {
                Order.put("acmoney", orderDto.getSs_money());//应收
            }
            Order.put("date", orderDto.getCheckoutTime());//orderDto.getCheckoutTime()startTime

            //获取登录手机的Mac。但是该方法必须要连接上了wifi才可以
            String macAddress = "000000000000";
            try {
                //首先得到系统的服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
                //当info 不是为空的时候
                if (info != null) {
                    //当info.getMacAddress不为空的时候
                    if (!TextUtils.isEmpty(info.getMacAddress())) {
                        macAddress = info.getMacAddress().replace(":", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Order.put("imel", macAddress);//设备唯一识别码
            Order.put("saleMethod", 0);//销售方式
            Order.put("sid", orderDto.getOrderId());//本地数据库主键
            Log.i("sid", "Order---------------->" + orderDto.getOrderId());
            Order.put("no", orderDto.getMaxNo());//小票号
            Order.put("uid", uid);//用户id
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("discount", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * (1 - card_discount)));//折扣总额
            } else {
                Order.put("discount", discount);//折扣金额
            }

            Order.put("cgmoney", orderDto.getZl_money());//找零
            Order.put("by", by);//收银员号
//            Order.put("shopNo", orderDto.getShop_id());//店铺号
            Order.put("status", 4);//订单的状态，4是正常销售，上传的时候用

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* 生成订单文件txt*/
        List<OrderDto> list = new OrderDao(context).selectGoods(GetData.getYYMMDDTime());
        CreateFiles createFiles = new CreateFiles();
        String str = orderDto.getMaxNo();
        str = str.substring(str.length() - 3, str.length());
        String name = "S10122"+ GetData.getDate()+".txt";
        String Storecode="S10122";//销售店铺号
        String Tillid ="001";//收银机号码,写常量:001（因为广场不需要对租户的收银机进行区分）
        String Txdate =GetData.getDate2();//销售日期
        String Txtime =GetData.getHHmm();//销售时间
        String Docno = "S"+GetData.getYYMMDD()+str;//单据号,用于存放每年交易的流水号，在同一结算年度内应不可重复；以S开头，宽10位，后9位为数字，顺序递增，即从S000000000开始，一直累加，每日以上日尾数加一为起始，开始重新累加。
        String Plu ="S101220001";//销售货号
        String Vipcode;//VIP编号,用于存放大宁的VIP卡卡号，在未统一VIP消费协定前一律为空，即不填写任何数据。
        String CH ="0";//付款方式（现金）
        String CI ="0";//付款方式（内卡）
        String CO ="0";//付款方式（外卡）
        String XF ="0";//付款方式（消费卡）
        String DJ ="0";//付款方式（有价券）
        String HY ="0";//付款方式（会员卡）
        String TG ="0";//付款方式（团购）
        String OT ="0";//付款方式（其他）
        String Ttldiscount = discount==0?"0":String.valueOf(discount);//付款方式（整单折扣金额）
        String Netamt = orderDto.getSs_money();//总金额
        String MK;//

        for (int i = 0; i < orderPaytypes.size(); i++) {
            switch (orderPaytypes.get(i).getPayCode()) {
                case 1:
                    CH = orderPaytypes.get(i).getSs();
                    break;
                default:
                    CI = "0";
                    CO = "0";
                    XF = "0";
                    DJ = "0";
                    HY = "0";
                    TG = "0";
                    OT = orderPaytypes.get(i).getSs();
                    break;
            }
        }

        try {
            createFiles.CreateText(name);
            createFiles.write(name ,Storecode+"\t"+Tillid+"\t"+Txdate+"\t"+Txtime+"\t"+Docno+"\t"+Plu+"\t\t"+CH+"\t"+CI+"\t"+CO+"\t"+XF+"\t"+DJ+"\t"+HY+"\t"+TG+"\t"+OT+"\t"+Ttldiscount+"\t"+Netamt+"\t0");

        } catch (IOException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams();

        params.put("Order", jsonArrayOrder.toString());
        params.put("Order_Menus", jsonArrayOrder_Menus.toString());
        params.put("Order_PayType", jsonArrayOrder_PayType.toString());

        Log.i("上传订单", "params" + params.toString());

        new Server().getConnect(context, new URL().SYNCORDER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    LoginReturnDto returnDto = new LoginReturnDto();

                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getString("Value")));
                        Log.i("upload", object.toString());
                        if (returnDto.getResult() == 1) {
                            Log.i("上传订单", "向服务器发送数据成功" + new String(bytes));
                            new OrderDao(context).updateServerInfo(orderDto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
   /* public void uploadOrderToServer(final OrderDto orderDto, PayWayDto payWayDto, String orderType, final Context context, final int from) {

        List<OrderMenu> orderMenus = new OrderMenuDao(App.getContext()).query(orderDto.getOrderId());
        List<OrderPaytype> orderPaytypes = new OrderPaytypeDao(App.getContext()).query(orderDto.getOrderId());
        //得到店铺的Id
        SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");//得到店铺的ID

        //得到当前的时间
        String date = GetData.getDataTime();
        //得到就餐的人数
        double paxNumber = 0;
        preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
        String value = preferences.getString("value", "");
        String[] names = value.split(",");
        String deviceNo = names[2];//收银机号
        String uid = names[1];

        preferences = context.getSharedPreferences("islogin", Context.MODE_PRIVATE);
        int whichOne = preferences.getInt("which", 0);

        preferences = context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE);
        String Response = preferences.getString("cashierMsgDtos", "");

        List<CashierMsgDto> cashierMsgDtos = new CashierLogin_pareseJson().parese(Response);

        String by = cashierMsgDtos.get(whichOne).getNumber();//得到收银员的账号

        JSONObject Order = new JSONObject();

        JSONObject Order_PayType = new JSONObject();

        JSONArray jsonArrayOrder = new JSONArray();
        jsonArrayOrder.put(Order);
        JSONArray jsonArrayOrder_Menus = new JSONArray();
        JSONArray jsonArrayOrder_PayType = new JSONArray();
        jsonArrayOrder_PayType.put(Order_PayType);

        try {
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            double discount = 0;
            int totalNum = 0;
            double card_discount = 1;
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
            }
            for (int i = 0; i < goodsDtos.size(); i++) {
                GoodsDto goodsDto = goodsDtos.get(i);
                discount += goodsDto.getDiscount();
                totalNum += goodsDto.getNum();

                JSONObject Order_Menus = new JSONObject();

//                Log.i("sidsid", "uploadOrderToServer: sid" + goodsDto.getSid());

                Order_Menus.put("obj1", "ad18163b22b14245bf131f6d04051643");
                Order_Menus.put("obj2", "9787115362865");
                Order_Menus.put("obj3", "0");//
                Order_Menus.put("createTime", date); //当前的时间

                Order_Menus.put("discountType", "0");//折扣类型

                Order_Menus.put("order_sid", orderDto.getOrderId());//order表的sid
                Order_Menus.put("parentSid", "");//父id
                Order_Menus.put("type", 1);//订单的类型 1是销售，2是退货i
                Order_Menus.put("saleMethod", 0);//销售方式
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("totalDiscount", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * (1 - card_discount)));//折扣总额
                } else {
                    Order_Menus.put("totalDiscount", goodsDto.getDiscount());//折扣总额
                }
                Order_Menus.put("number", goodsDto.getNum());//销售数量
                Order_Menus.put("m_price", goodsDto.getPrice());//商品的单价
                //订单详情表的sid
                Order_Menus.put("sid", goodsDto.getGoodsId());//本地数据库主键
                Log.i("sid", "Order_Menus------------->"+goodsDto.getGoodsId());
                Order_Menus.put("price", goodsDto.getPrice() * goodsDto.getNum());//商品的总价

                Order_Menus.put("Uid", uid);//用户id
                Order_Menus.put("menus_sid", goodsDto.getSid());//商品的sid。。
                Log.i("menus_sid", "menus_sid: " + goodsDto.getSid());
                Order_Menus.put("menus_name", goodsDto.getName());//商品名称
                Order_Menus.put("callCount", 0);//
                if (orderDto.getPayway().equals(PayWay.CZK)) {
                    Order_Menus.put("saleprice", DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() * card_discount));//销售总价
                } else {
                    Order_Menus.put("saleprice", goodsDto.getPrice() * goodsDto.getNum() - goodsDto.getDiscount());//销售总价
                }
                Order_Menus.put("lineNum", i);//行号
                Order_Menus.put("status", 4);//订单的状态，4是正常销售，上传的时候用
                Order_Menus.put("singleDiscount", goodsDto.getDiscount());//单品折扣
                jsonArrayOrder_Menus.put(Order_Menus);
            }
            Order_PayType.put("obj2", "0");
            Order_PayType.put("obj3", "(null)");
            Order_PayType.put("obj4", "(null)");
            Order_PayType.put("obj5", "(null)");
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order_PayType.put("price_ys", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
            } else {
                Order_PayType.put("price_ys", orderDto.getYs_money());//应收
            }

            Order_PayType.put("orderBy", 0);//排序
            Order_PayType.put("lineNum", 1);//行号
            Log.i("order", "付款方式" + payWayDto);
            Order_PayType.put("payId", payWayDto.getSid());//付款方式id
            Order_PayType.put("payType", payWayDto.getPayType1());//付款类型
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order_PayType.put("price", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//实收金额
            } else {
                Order_PayType.put("price", orderDto.getSs_money());//实收金额
            }
            Order_PayType.put("price_zl", orderDto.getZl_money());//找零
            //Order_PayType.put("sid", payWayDto.getPayWayId());//本地数据库主键
            Order_PayType.put("sid", orderDto.getOrderSid());//本地数据库主键
            Log.i("sid", "Order_PayType------------->"+orderDto.getOrderSid());
            Order_PayType.put("order_sid", orderDto.getOrderId());//order表的sid。。
            Order_PayType.put("name", orderDto.getPayway());//付款方式名
            Order_PayType.put("Uid", uid);//用户id
            Order_PayType.put("status", 4);//订单的状态，4是正常销售，上传的时候用

            Order.put("obj1", "");//空
            Order.put("obj2", "");
            Order.put("shopNo", shopid);//
            Order.put("obj3", "");
            Order.put("obj4", "");
            Order.put("obj5", "");
            Order.put("obj6", orderDto.getPayReturn());//微信支付宝支付，返回码流水
            Order.put("obj10", "");
            Order.put("obj9", "");
            Order.put("obj7", "");
            Order.put("obj8", "");
            Order.put("paxNumber", String.valueOf(goodsDtos.size()));//本单商品数量
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("remoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getYs_money()) * card_discount));//应收
            } else {
                Order.put("remoney", orderDto.getYs_money());//应收
            }
            Order.put("cashNo", deviceNo);//收银机号
            Order.put("number", totalNum);//销售数量
            Order.put("type", orderType);//订单的类型 1是销售，2是退货
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("acmoney", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * card_discount));//应收
            } else {
                Order.put("acmoney", orderDto.getSs_money());//应收
            }
            Order.put("date", orderDto.getCheckoutTime());//orderDto.getCheckoutTime()startTime

            //获取登录手机的Mac。但是该方法必须要连接上了wifi才可以
            String macAddress = "000000000000";
            try {
                //首先得到系统的服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
                //当info 不是为空的时候
                if (info != null) {
                    //当info.getMacAddress不为空的时候
                    if (!TextUtils.isEmpty(info.getMacAddress())) {
                        macAddress = info.getMacAddress().replace(":", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Order.put("imel", macAddress);//设备唯一识别码
            Order.put("saleMethod", 0);//销售方式
            Order.put("sid", orderDto.getOrderId());//本地数据库主键
            Log.i("sid", "Order---------------->" + orderDto.getOrderId());
            Order.put("no", orderDto.getMaxNo());//小票号
            Order.put("uid", uid);//用户id
            if (orderDto.getPayway().equals(PayWay.CZK)) {
                Order.put("discount", DoubleSave.doubleSaveTwo(Double.parseDouble(orderDto.getSs_money()) * (1 - card_discount)));//折扣总额
            } else {
                Order.put("discount", discount);//折扣金额
            }

            Order.put("cgmoney", orderDto.getZl_money());//找零
            Order.put("by", by);//收银员号
//            Order.put("shopNo", orderDto.getShop_id());//店铺号
            Order.put("status", 4);//订单的状态，4是正常销售，上传的时候用

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();

        params.put("Order", jsonArrayOrder.toString());
        params.put("Order_Menus", jsonArrayOrder_Menus.toString());
        params.put("Order_PayType", jsonArrayOrder_PayType.toString());

        Log.i("上传订单", "params" + params.toString());

        new Server().getConnect(context, new URL().SYNCORDER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    LoginReturnDto returnDto = new LoginReturnDto();

                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getString("Value")));
                        if (returnDto.getResult() == 1) {
                            Log.i("上传订单", "向服务器发送数据成功" + new String(bytes));
                            new OrderDao(context).updateServerInfo(orderDto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    Log.i("上传订单", "上传订单失败：" + new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
    public void postWebServer(final OrderDto orderDto, String payWay, boolean sale) {




        //得到店铺的Id
        String Shopid = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getString("Id", "");//得到店铺的ID
        String Response = context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE).getString("cashierMsgDtos", "");
        List<CashierMsgDto> cashierMsgDtos = new CashierLogin_pareseJson().parese(Response);
        int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        String cashier = cashierMsgDtos.get(whichOne).getNumber();//得到收银员的账号

        SharedPreferences preferences = context.getSharedPreferences("ERP", Context.MODE_PRIVATE);
        int position = preferences.getInt("position", 0);//
        String url = preferences.getString("url", "");//地址
        String mallid = preferences.getString("mallid", "");
        String storecode = preferences.getString("storecode", "");
        String id = preferences.getString("id", "010201");
        String pwd = preferences.getString("pwd", "010201");
        String itemcode = preferences.getString("itemcode", "");


        JSONObject header = new JSONObject();
        JSONObject salesTotal = new JSONObject();
        JSONArray salesItems = new JSONArray();
        JSONArray salesTenders = new JSONArray();

        OrderDao dao = new OrderDao(context);
        OrderDto oldOrderDto = new OrderDto();
        if (!sale) {
            oldOrderDto = dao.findOrderDetailMessage(String.valueOf(orderDto.getHasReturnGoods()));
        }

        switch (position) {
            case 0:
                try {
                    List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
                    int totalNum = 0;
                    header.put("licensekeyField", "");
                    header.put("usernameField", id);//*
                    header.put("passwordField", pwd);//*
                    header.put("langField", "");
                    header.put("pagerecordsField", 100);
                    header.put("pagenoField", 1);
                    header.put("updatecountField", 0);
                    header.put("messagetypeField", "SALESDATA"); //
                    header.put("messageidField", "332");//
                    header.put("versionField", "V332M");//

                    for (int i = 0; i < goodsDtos.size(); i++) {
                        totalNum += goodsDtos.get(i).getNum();
                        JSONObject salesItem = new JSONObject();
                        salesItem.put("invttypeField", "1");//0:坏货退回/1:好货退回  默认为1
                        salesItem.put("iscounteritemcodeField", "1");//是否专柜货号默认为1 长度:1
                        salesItem.put("linenoField", i);//行号
                        salesItem.put("storecodeField", storecode);//*店铺号
                        salesItem.put("mallitemcodeField", itemcode);//*货号
                        salesItem.put("counteritemcodeField", itemcode);//*专柜货号
                        salesItem.put("itemcodeField", itemcode);//*商品编号
                        salesItem.put("plucodeField", itemcode);//*商品内部编号
                        salesItem.put("colorcodeField", "");//商品颜色
                        salesItem.put("sizecodeField", "");//商品尺码
                        salesItem.put("itemlotnumField", "");//商品批次
                        salesItem.put("serialnumField", 0);//序列号
                        salesItem.put("isdepositField", "");//是否定金单
                        salesItem.put("iswholesaleField", "");//是否批发
//                        if (sale) {
                        salesItem.put("qtyField", goodsDtos.get(i).getNum());//数量
                        salesItem.put("netamountField", (goodsDtos.get(i).getPrice() - goodsDtos.get(i).getDiscount()) * goodsDtos.get(i).getNum());//净金额
//                        } else {
//                            salesItem.put("qtyField", -goodsDtos.get(i).getNum());//数量
//                            salesItem.put("netamountField", -(goodsDtos.get(i).getPrice() - goodsDtos.get(i).getDiscount()) * goodsDtos.get(i).getNum());//净金额
//                        }
                        salesItem.put("originalpriceField", 0.00);//原始售价
                        salesItem.put("sellingpriceField", 0.00);//售价
                        salesItem.put("exstk2salesField", 0);//库存销售比例
                        salesItem.put("pricemodeField", "");//价格模式
                        salesItem.put("priceapproveField", "");//允许改价
                        salesItem.put("couponnumberField", "");//优惠劵号码
                        salesItem.put("coupongroupField", "");//优惠劵组
                        salesItem.put("coupontypeField", "");//优惠劵类型
                        salesItem.put("vipdiscountpercentField", 0);//VIP折扣率

                        JSONArray itemdiscounts = new JSONArray();
                        JSONObject itemdiscount = new JSONObject();
                        itemdiscount.put("discountapproveField", "");//折扣允许
                        if (goodsDtos.get(i).getDiscount() > 0) {
                            itemdiscount.put("discountmodeField", "2");//0：没有折扣;1：百分比; 2：金额
                            itemdiscount.put("discountvalueField", goodsDtos.get(i).getDiscount());//折扣值
                            itemdiscount.put("discountlessField", goodsDtos.get(i).getDiscount() * goodsDtos.get(i).getNum());//折扣金额
                        } else {
                            itemdiscount.put("discountmodeField", "0");//0：没有折扣;1：百分比; 2：金额
                            itemdiscount.put("discountvalueField", 0);//折扣值
                            itemdiscount.put("discountlessField", 0);//折扣金额
                        }
                        itemdiscounts.put(itemdiscount);
                        salesItem.put("itemdiscountField", itemdiscounts);//单品折扣信息
                        salesItem.put("vipdiscountlessField", 0);//VIP折扣差额
                        JSONArray promotions = new JSONArray();
                        JSONObject promotion = new JSONObject();
                        promotion.put("promotionidField", "");//促销编号
                        promotion.put("promotionuseqtyField", 0);//促销数量
                        promotion.put("promotionlessField", 0);//促销差额
                        promotion.put("promotionpkgcountField", 0);//中促销次数
                        promotions.put(promotion);
                        salesItem.put("promotionField", promotions);
                        salesItem.put("totaldiscountless1Field", 0);//整单折扣差额1
                        salesItem.put("totaldiscountless2Field", 0);//整单折扣差额2
                        salesItem.put("totaldiscountlessField", 0);//整单折扣差额
                        JSONArray taxs = new JSONArray();
                        JSONObject tax = new JSONObject();
                        tax.put("taxrateField", 0);//商品税率
                        tax.put("taxamountField", 0);//商品税额
                        taxs.put(tax);
                        salesItem.put("taxField", taxs);
                        salesItem.put("bonusearnField", 0);//获得积分
                        salesItem.put("salesitemremarkField", "");//交易明细备注
                        salesItem.put("refundreasoncodeField", "");//退货原因
                        salesItem.put("extendparamField", "");//扩展参数

                        salesItems.put(salesItem);
                    }

                    salesTotal.put("localstorecodeField", "");
                    salesTotal.put("reservedocnoField", "");
                    salesTotal.put("txdate_yyyymmddField", orderDto.getCheckoutTime().split(" ")[0].replace("-", ""));
                    salesTotal.put("txtime_hhmmssField", orderDto.getCheckoutTime().split(" ")[1].replace(":", ""));
                    salesTotal.put("mallidField", mallid);//商场编号*橘果信息：100001;测试5012
                    salesTotal.put("storecodeField", storecode);//店铺号*橘果信息：L104B1;测试：A00001
                    if (url.equals("http://180.168.57.11:353/TTPOS/sales.asmx") || storecode.equals("L108A3")) {
                        salesTotal.put("tillidField", "00");//收银机号
                    } else {
                        salesTotal.put("tillidField", "01");//收银机号
                    }

                    salesTotal.put("txdocnoField", String.valueOf(orderDto.getMaxNo()));//单号String.valueOf(orderDto.getMaxNo())

                    salesTotal.put("orgtxdate_yyyymmddField", "");//原交易日期
                    salesTotal.put("orgstorecodeField", "");//原交易店铺号
                    salesTotal.put("orgtillidField", "");//原收银机号
                    salesTotal.put("orgtxdocnoField", "");//原销售单号
                    if (sale) {
                        salesTotal.put("salestypeField", "SA");//SA:店内销售 SR:店内退货/取消交易
                        salesTotal.put("netqtyField", totalNum);//销售总数量
                        salesTotal.put("sellingamountField", orderDto.getYs_money());//销售金额
                        salesTotal.put("netamountField", orderDto.getYs_money());//净金额
                        salesTotal.put("paidamountField", orderDto.getYs_money());//付款金额
                        salesTotal.put("changeamountField", 0.00);//找零金额
                    } else {
                        salesTotal.put("salestypeField", "SR");//SA:店内销售 SR:店内退货/取消交易
//                        String date = oldOrderDto.getCheckoutTime().split(" ")[0].replace("-", "");
//                        salesTotal.put("orgtxdate_yyyymmddField", date);//原交易日期
//                        salesTotal.put("orgstorecodeField", storecode);//原交易店铺号
//                        if (url.equals("http://180.168.57.11:353/TTPOS/sales.asmx") || storecode.equals("L108A3")) {
//                            salesTotal.put("orgtillidField", "00");//原收银机号
//                        } else {
//                            salesTotal.put("orgtillidField", "01");//原收银机号
//                        }
//                        salesTotal.put("orgtxdocnoField", String.valueOf(orderDto.getHasReturnGoods()) + 1);//原销售单号
                        salesTotal.put("netqtyField", -totalNum);//销售总数量
                        salesTotal.put("sellingamountField", -Double.parseDouble(oldOrderDto.getYs_money()));//销售金额
                        salesTotal.put("netamountField", -Double.parseDouble(oldOrderDto.getYs_money()));//净金额
                        salesTotal.put("paidamountField", -Double.parseDouble(oldOrderDto.getYs_money()));//付款金额
                        salesTotal.put("changeamountField", 0.00);//找零金额
                    }
                    salesTotal.put("mallitemcodeField", itemcode);//*橘果信息：L104B1001;测试：A000011
                    salesTotal.put("cashierField", cashier);//收银员编号cashier
                    salesTotal.put("vipcodeField", "");//VIP卡号
                    salesTotal.put("salesmanField", "");//销售员
                    salesTotal.put("demographiccodeField", "");//顾客统计代码
                    salesTotal.put("demographicdataField", "");//顾客统计值
                    salesTotal.put("originalamountField", 0);//原始金额
                    salesTotal.put("couponnumberField", "");//优惠券号码
                    salesTotal.put("coupongroupField", "");//优惠券组
                    salesTotal.put("coupontypeField", "");//优惠券类型
                    salesTotal.put("couponqtyField", 0);//优惠券数量
                    JSONArray totaldiscount = new JSONArray();
                    JSONObject salesdiscount = new JSONObject();
                    salesdiscount.put("discountapproveField", "");//折扣允许
                    salesdiscount.put("discountmodeField", "0");//0：没有折扣;1：百分比; 2：金额
                    salesdiscount.put("discountvalueField", 0);//折扣值
                    salesdiscount.put("discountlessField", 0);//折扣金额
                    totaldiscount.put(salesdiscount);

                    salesTotal.put("totaldiscountField", totaldiscount);//

                    salesTotal.put("ttltaxamount1Field", 0);//总税额1
                    salesTotal.put("ttltaxamount2Field", 0);//总税额2
                    salesTotal.put("priceincludetaxField", "");//售价是否含税
                    salesTotal.put("shoptaxgroupField", "");//店铺税组
                    salesTotal.put("extendparamField", "");//扩展参数
                    salesTotal.put("invoicetitleField", "");//发票抬头
                    salesTotal.put("invoicecontentField", "");//发票内容
                    salesTotal.put("issuebyField", cashier);//创建人
                    salesTotal.put("issuedate_yyyymmddField", GetData.getYYMMDDTime().replace("-", ""));//创建日期
                    salesTotal.put("issuetime_hhmmssField", GetData.getHHmmssTime());//创建时间
                    salesTotal.put("ecordernoField", "");//网购订单号
                    salesTotal.put("buyerremarkField", "");//卖家备注
                    salesTotal.put("orderremarkField", "");//交易备注
                    salesTotal.put("ttpossalesdocnoField", "");//
                    salesTotal.put("statusField", "");//10:新增/ 20:付款/30:付款取消/40:订单取消

                    JSONObject salesTender = new JSONObject();
                    salesTender.put("linenoField", 0);//行号
                    if (payWay.equals(PayWay.XJ)) {
                        salesTender.put("tendercodeField", "CH");//CH 现金 CI 国内银行卡 CO 国外银行卡 OT 其他付款方式。
                    } else if (payWay.equals(PayWay.YHK)) {
                        salesTender.put("tendercodeField", "CI");
                    } else {
                        salesTender.put("tendercodeField", "OT");
                    }
                    salesTender.put("tendertypeField", 0);//付款类型
                    salesTender.put("tendercategoryField", 0);//付款种类
                    salesTender.put("excessamountField", 0);//超额金额
                    salesTender.put("extendparamField", "");//扩展参数
                    salesTender.put("remarkField", "");//remark	备注
                    if (sale) {
                        salesTender.put("payamountField", Double.parseDouble(orderDto.getYs_money()));//付款金额
                        salesTender.put("baseamountField", Double.parseDouble(orderDto.getYs_money()));//本位币金额
                    } else {
                        salesTender.put("payamountField", -Double.parseDouble(oldOrderDto.getYs_money()));//付款金额
                        salesTender.put("baseamountField", -Double.parseDouble(oldOrderDto.getYs_money()));//本位币金额
                    }
                    salesTenders.put(salesTender);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestParams params = new RequestParams();

                params.put("url", url);
                params.put("header", header.toString());
                params.put("salestotal", salesTotal.toString());
                params.put("salesitems", salesItems.toString());
                params.put("salestenders", salesTenders.toString());
                JsonObject jsonObject = new JsonObject();
                Log.i("responseERPL", params.toString());

                new Server().getConnect(context, new URL().postsalescreate, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        try {
                            Log.i("responseerpL", new String(bytes));
                            JSONObject object = new JSONObject(new String(bytes));
                            if (object.getInt("Result") == 0 || object.getInt("Result") == 1000) {
//                                Toast.makeText(context, "ERP上传成功！", Toast.LENGTH_SHORT).show();
                                new OrderDao(context).updateERPInfo(orderDto);
                            } else {
                                Log.i("WebServiceLog", " 上传订单失败，返回码：" + i + ",返回信息：" + new String(bytes));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        try {
//                            Log.i("responseERpL", new String(bytes));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 1:
                /**
                 * 传递单张销售单据
                 * @param data 销售单据
                 * @return
                 */
                try {
                    StringBuffer jsonStr = new StringBuffer();

                    jsonStr.append("{\"apiKey\":null,\"signature\":null,\"docKey\":\"@docKey@\",\"transHeader\":{\"txDate\":\"@txDate@\",\"ledgerDatetime\":\"@ledgerDatetime@\",\"storeCode\":\"@storeCode@\",\"tillId\":\"@tillId@\",\"docNo\":\"@docNo@\",\"voidDocNo\":\"@voidDocNo@\",\"txAttrib\":\"@txAttrib@\"},\"salesTotal\":{\"cashier\":\"@cashier@\",\"vipCode\":\"@vipCode@\",\"netQty\":@netQty@,\"netAmount\":@netAmount@,\"extendParameter\":null,\"calculateVipBonus\":\"@calculateVipBonus@\"},\"salesItem\":[{\"salesLineNumber\":@salesLineNumber@,\"salesman\":null,\"itemCode\":\"@itemCode@\",\"itemOrgId\":\"@itemOrgId@\",\"itemLotNum\":\"@itemLotNum@\",\"serialNumber\":null,\"inventoryType\":@inventoryType@,\"qty\":@qty@,\"itemDiscountLess\":@itemDiscountLess@,\"totalDiscountLess\":@totalDiscountLess@,\"netAmount\":@netAmount@,\"salesItemRemark\":null,\"extendParameter\":null}],\"salesTender\":[{\"baseCurrencyCode\":\"@baseCurrencyCode@\",\"tenderCode\":\"@tenderCode@\",\"payAmount\":@payAmount@,\"baseAmount\":@baseAmount@,\"excessAmount\":@excessAmount@,\"extendParameter\":null}],\"orgSalesMemo\":null}");

                    String nty = "10";
                    String fee = "874.4";

                    String json = jsonStr.toString();

                    json = json.replace("@docKey@", tillId + storeCode + "PCS100206921342");//交易号
                    json = json.replace("@txDate@", "2016-11-25");//交易日期
                    json = json.replace("@ledgerDatetime@", "2016-11-25 10:44:14");//交易时间
                    json = json.replace("@storeCode@", storeCode);
                    json = json.replace("@tillId@", tillId);
                    json = json.replace("@docNo@", "PCS100206921342");//交易号
                    json = json.replace("@voidDocNo@", "");//
                    json = json.replace("@txAttrib@", "");
                    json = json.replace("@cashier@", UpLoadToServel.cashier);
                    json = json.replace("@vipCode@", "");
                    json = json.replace("@netQty@", nty);
                    json = json.replace("@netAmount@", fee);
                    json = json.replace("@calculateVipBonus@", "0");
                    json = json.replace("@salesLineNumber@", "1");
                    json = json.replace("@salesman@", UpLoadToServel.cashier);
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
                    System.out.println("json:" + json);
                    signature = Utils.md5Encrypt(json);
                    json = json.replaceFirst("null", "\"CRMINT\"");
                    json = json.replaceFirst("null", "\"" + signature + "\"");
                    System.out.println("json:" + json);
                    String returnXML = uploadPcrf(json, UpLoadToServel.url, contentType, null);
                    String result = "\"errorCode\":0";
                    System.out.println("returnXML:" + returnXML);
                    Log.i("returnXML", "returnXML: " + returnXML);
                    if (returnXML.indexOf(result) > -1) {//判断返回码是否为0
                        new OrderDao(context).updateERPInfo(orderDto);
                    } else {
                        Log.i("WebServiceLog", " 上传订单失败，返回码：" + returnXML.indexOf(result) + ",返回信息：" + "; ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    /********************************
     * 上传请求
     **************************************/
    private synchronized static String uploadPcrf(String xml, String url, String contentType, String soapAction) {
        String urlString = url;
        HttpURLConnection httpConn = null;
        OutputStream out = null;
        String returnXml = "";
        try {
            httpConn = (HttpURLConnection) new java.net.URL(urlString).openConnection();
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
            Log.i("code", "code: " + code);
            String tempString = null;
            StringBuffer sb1 = new StringBuffer();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((tempString = reader.readLine()) != null) {
                    sb1.append(tempString);
                }
                if (null != reader) {
                    reader.close();
                }
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), "UTF-8"));
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    sb1.append(tempString);
                }
                if (null != reader) {
                    reader.close();
                }
            }
            // 响应报文
            returnXml = sb1.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnXml;
    }


    public void upCardRecord(String sid, OrderDto orderDto, ValueCardDto valueCardDto, int orderType) {
        //上传储值卡交易流水
        try {
            SharedPreferences preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
            String value = preferences.getString("value", "");
            String[] names = value.split(",");
            String deviceNo = names[2];//收银机号
            String uid = names[1];

            preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
            String Shopid = preferences.getString("Id", "");//得到店铺的ID

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", sid);//客户端id
            jsonObject.put("uid", uid);//收银员id
            jsonObject.put("shopId", Shopid);//店铺id
            jsonObject.put("SYYId", uid);//收银员id
            jsonObject.put("deviceNo", deviceNo);//设备id
            jsonObject.put("OrderNo", String.valueOf(orderDto.getMaxNo()));//订单号
            jsonObject.put("OrderTime", orderDto.getCheckoutTime());//订单时间
            jsonObject.put("cardUid", valueCardDto.getCardUid());//卡原始id
            jsonObject.put("cardNo", valueCardDto.getCardNo());//卡号
            jsonObject.put("cardOldAmount", valueCardDto.getCardAmount());//卡交易前金额
//            jsonObject.put("cardMortgage", Double.parseDouble(valueCardDto.getDeposit()));//卡押金
            jsonObject.put("UpLoadTime", GetData.getDataTime());//记录上传时间
            jsonObject.put("receivableAmount", Double.parseDouble(orderDto.getYs_money()));//应收金额
            jsonObject.put("paidInAmount", Double.parseDouble(orderDto.getYs_money()));//实收金额
            jsonObject.put("giveChangeAmount", 0);//找零金额
            jsonObject.put("payType", "C");//付款方式，C:餐卡 X:现金，W:微信，Z:支付宝，Y:银行卡，L:礼品券，T:团购，R:人工修正，O:Other
            if (orderType == URL.ORDERTYPE_REFUND) {
                jsonObject.put("saleType", "TH");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
                jsonObject.put("cardNowAmount", valueCardDto.getCardAmount() + Double.parseDouble(orderDto.getYs_money()));//卡交易后金额
                jsonObject.put("changeAmount", Double.parseDouble(orderDto.getYs_money()));//变更金额
            } else {
                jsonObject.put("saleType", "X");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
                jsonObject.put("cardNowAmount", valueCardDto.getCardAmount() - Double.parseDouble(orderDto.getYs_money()));//卡交易后金额
                jsonObject.put("changeAmount", -Double.parseDouble(orderDto.getYs_money()));//变更金额
            }
            jsonObject.put("onlinePayInfo", "");//在线支付的返回值

            JSONArray jsonArrayOrder = new JSONArray();
            jsonArrayOrder.put(jsonObject);

            RequestParams params = new RequestParams();
            params.put("value", jsonArrayOrder.toString());

//            Log.i("1上传订单", "params" + params.toString());

            new Server().getConnect(context, new URL().SyncTime, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    if (i == 200) {
//                        Log.i("检查服务器连接", "检查服务器连接成功");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
//                        Log.i("检查服务器连接", "检查服务器连接成功" + new String(bytes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            new Server().getConnect(context, new URL().AddCardRecord, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        Log.i("1上传订单", "请求成功" + new String(bytes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (i == 200) {
                        Log.i("1上传订单", "上传成功" + new String(bytes));
                    } else {
                        Log.i("上传订单失败", new String(bytes));
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        Log.i("1上传订单", "上传订单失败" + new String(bytes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //无小票储值卡退款流水
    public void upCardRefund(String sid, OrderDto orderDto, ValueCardDto valueCardDto, int orderType, double money) {
        //上传储值卡交易流水
        try {
            SharedPreferences preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
            String value = preferences.getString("value", "");
            String[] names = value.split(",");
            String deviceNo = names[2];//收银机号
            String uid = names[1];

            preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
            String Shopid = preferences.getString("Id", "");//得到店铺的ID

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", sid);//客户端id
            jsonObject.put("uid", uid);//收银员id
            jsonObject.put("shopId", Shopid);//店铺id
            jsonObject.put("SYYId", uid);//收银员id
            jsonObject.put("deviceNo", deviceNo);//设备id
            jsonObject.put("OrderNo", String.valueOf(orderDto.getMaxNo()));//订单号
            jsonObject.put("OrderTime", orderDto.getCheckoutTime());//订单时间
            jsonObject.put("cardUid", "16479206188");//卡原始id
            jsonObject.put("cardNo", valueCardDto.getCardNo());//卡号
            jsonObject.put("cardOldAmount", valueCardDto.getCardAmount());//卡交易前金额
            jsonObject.put("changeAmount", money);//变更金额
            jsonObject.put("cardNowAmount", valueCardDto.getCardAmount() + money);//卡交易后金额
//            jsonObject.put("cardMortgage", valueCardDto.getMortgageAmount());//卡押金
            jsonObject.put("UpLoadTime", GetData.getDataTime());//记录上传时间
            jsonObject.put("receivableAmount", -money);//应收金额
            jsonObject.put("paidInAmount", -money);//实收金额
            jsonObject.put("giveChangeAmount", 0);//找零金额
            jsonObject.put("payType", "C");//付款方式，C:餐卡 X:现金，W:微信，Z:支付宝，Y:银行卡，L:礼品券，T:团购，R:人工修正，O:Other
            jsonObject.put("saleType", "TH");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
            jsonObject.put("onlinePayInfo", "");//在线支付的返回值

            JSONArray jsonArrayOrder = new JSONArray();
            jsonArrayOrder.put(jsonObject);

            RequestParams params = new RequestParams();
            params.put("value", jsonArrayOrder.toString());

//            Log.i("上传订单1", "params" + params.toString());

            new Server().getConnect(context, new URL().AddCardRecord, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                    Log.i("1上传订单", "上传订单成功" + new String(bytes));
                    if (i == 200) {
//                        Log.i("1上传订单", "向服务器发送数据成功" + new String(bytes));
                    } else {
                        try {
                            String uploadLog = PreferencesUtils.getString(App.getContext(), "UploadLog");
                            uploadLog += GetData.getDataTime() + " 上传卡交易流水失败，返回码：" + i + ",返回信息：" + new String(bytes) + "; ";
                            PreferencesUtils.putString(App.getContext(), "UploadLog", uploadLog);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                    Log.i("1上传订单", "上传订单失败" + new String(bytes));
                    try {
                        String uploadLog = PreferencesUtils.getString(App.getContext(), "UploadLog");
                        uploadLog += GetData.getDataTime() + " 上传卡交易流水失败，返回码：" + i + ",返回信息：" + new String(bytes) + "; ";
                        PreferencesUtils.putString(App.getContext(), "UploadLog", uploadLog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求服务器获取卡的信息
     */
//    private void getCardInfo() {
//        SharedPreferences preferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
//        String value = preferences.getString("value", "");
//        String[] names = value.split(",");
//        String deviceNo = names[2];//收银机号
//        String uid = names[1];
//
//        preferences = context.getSharedPreferences("StoreMessage", context.MODE_PRIVATE);
//        String Shopid = preferences.getString("Id", "");//得到店铺的ID
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("sid", sid);//客户端id
//        jsonObject.put("uid", uid);//收银员id
//        jsonObject.put("shopId", Shopid);//店铺id
//        jsonObject.put("SYYId", uid);//收银员id
//        jsonObject.put("deviceNo", deviceNo);//设备id
//        jsonObject.put("OrderNo", String.valueOf(orderDto.getMaxNo()));//订单号
//        jsonObject.put("OrderTime", orderDto.getCheckoutTime());//订单时间
//        jsonObject.put("cardUid", "16479206188");//卡原始id
//        jsonObject.put("cardNo", valueCardDto.getCardId());//卡号
//        jsonObject.put("cardOldAmount", Double.parseDouble(valueCardDto.getbLnce()));//卡交易前金额
//        jsonObject.put("changeAmount", money);//变更金额
//        jsonObject.put("cardNowAmount", Double.parseDouble(valueCardDto.getbLnce()) + money);//卡交易后金额
//        jsonObject.put("cardMortgage", Double.parseDouble(valueCardDto.getDeposit()));//卡押金
//        jsonObject.put("UpLoadTime", GetData.getDataTime());//记录上传时间
//        jsonObject.put("receivableAmount", -money);//应收金额
//        jsonObject.put("paidInAmount", -money);//实收金额
//        jsonObject.put("giveChangeAmount", 0);//找零金额
//        jsonObject.put("payType", "C");//付款方式，C:餐卡 X:现金，W:微信，Z:支付宝，Y:银行卡，L:礼品券，T:团购，R:人工修正，O:Other
//        jsonObject.put("saleType", "TH");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
//        jsonObject.put("onlinePayInfo", "");//在线支付的返回值
//
//        JSONArray jsonArrayOrder = new JSONArray();
//        jsonArrayOrder.put(jsonObject);
//
//        RequestParams params = new RequestParams();
//        params.put("value", jsonArrayOrder.toString());
//
//        Log.i("上传订单1", "params" + params.toString());
//
//        new Server().getConnect(context, new URL(context).AddCardRecord, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("1上传订单", "上传订单成功" + new String(bytes));
//                if (i == 200) {
//                    Log.i("1上传订单", "向服务器发送数据成功" + new String(bytes));
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("1上传订单", "上传订单失败" + new String(bytes));
//            }
//        });
//    }
    //存储到本地

}
