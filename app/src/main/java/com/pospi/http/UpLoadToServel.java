package com.pospi.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lany.sp.SPHelper;
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
import com.pospi.dto.VipBeen;
import com.pospi.pai.yunpos.been.CustomerBeen;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.pai.yunpos.util.CreateFiles;
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

import static android.content.Context.MODE_PRIVATE;

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


    public void uploadOrderToServer(final OrderDto orderDto, PayWayDto payWayDto, String orderType, final Context context, final int from) {
        String saleType;
        if (orderDto.getOrderType() == URL.ORDERTYPE_SALE) {
            saleType = "505";
        } else {
            saleType = "506";
        }
        List<OrderPaytype> orderPaytypes =  new Gson().fromJson(orderDto.getPayway(), new TypeToken<List<OrderPaytype>>() {
        }.getType());


        //得到店铺的Id
        String shopid = SPHelper.getInstance().getString(Constant.STORE_ID);//得到店铺的ID
        JSONObject order = new JSONObject();
        JSONObject head = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray pay = new JSONArray();
        JSONArray headArray = new JSONArray();

        try {
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            double discount = 0;
            double hyzk = 0;
            double lszk = 0;
            double cxzk = 0;
            double lsj = 0;
            double jfje = 0;
            //订单详情
            for (int i = 0; i < goodsDtos.size(); i++) {
                GoodsDto goodsDto = goodsDtos.get(i);
                if (goodsDto.getUsejf().equals("1")) {
                    jfje += goodsDto.getPrice() * goodsDto.getNum() - goodsDto.getDiscount();
                }

                lsj += goodsDto.getOldPrice() * goodsDto.getNum();
                discount += goodsDto.getDiscount();
                hyzk += goodsDto.getHyzk();
                lszk += goodsDto.getLszk();
                cxzk += goodsDto.getCxzk();
                double sj = 0;
                double xsj = 0;
                if (goodsDto.getNum() < 0) {
                    sj = DoubleSave.doubleSaveTwo((goodsDto.getPrice() * goodsDto.getNum() + goodsDto.getDiscount())) / goodsDto.getNum();
                    xsj = DoubleSave.doubleSaveTwo(goodsDto.getPrice() * goodsDto.getNum() + goodsDto.getDiscount());
                } else if (goodsDto.getNum() == 0) {
                    sj = 0;
                    xsj = 0;
                } else {
                    sj = DoubleSave.doubleSaveTwo((goodsDto.getPrice()*goodsDto.getNum()-goodsDto.getDiscount()))/goodsDto.getNum();
                    xsj = DoubleSave.doubleSaveTwo(goodsDto.getPrice()*goodsDto.getNum()-goodsDto.getDiscount());
                }



                JSONObject json = new JSONObject();
                json.put("goodsid", goodsDto.getSid());//商品id
                json.put("sl", goodsDto.getNum());//销售数量
                json.put("lsj", goodsDto.getOldPrice());//零售价
                json.put("xsj", sj);//销售价
                json.put("zzk", goodsDto.getDiscount());//总折扣
                json.put("hyzk", goodsDto.getHyzk());//会员折扣
                json.put("popzk", goodsDto.getCxzk());//促销折扣
                json.put("lszk", goodsDto.getLszk());//临时折扣
                json.put("syje", "");//损益金额

                json.put("popid", "");//促销单id
                json.put("popbillno", "");//

                json.put("xsjje", xsj);
                json.put("lsjje", DoubleSave.doubleSaveTwo(goodsDto.getOldPrice() * goodsDto.getNum()));
                array.put(json);
            }
            String jf = "";
            double vipje = 0;
            if (!orderDto.getVipNumber().equals("")) {
                VipBeen vipBeen = new Gson().fromJson(orderDto.getVipNumber(), VipBeen.class);
                jf = String.valueOf((int) (jfje / Double.parseDouble(vipBeen.getJfjs())) * (int) Double.parseDouble((vipBeen.getJfbs())));
                vipje = jfje;
            }

            String no = String.valueOf(orderDto.getMaxNo());
            head.put("djlb", saleType);//订单类型(505|前台销售,506|前台退货/507换货
            head.put("billno", orderDto.getMaxNo());//小票号
            head.put("fphm", no.substring(no.length() - 4, no.length()));//流水号
            head.put("market", shopid);//店铺号

            head.put("syjh", "");//收银机号
            head.put("syyid", SPHelper.getInstance().getString(Constant.CUSTOMER));//收银员
            head.put("syyname", SPHelper.getInstance().getString(Constant.CUSTOMER_name));//营业员
            if (orderDto.getOut_trade_no().equals("")) {
                head.put("yyyid", "");//收银员
                head.put("yyyname", "");//收银员
            } else {

                head.put("yyyid", new Gson().fromJson(orderDto.getOut_trade_no(), CustomerBeen.class).getId());//营业员
                head.put("yyyname",  new Gson().fromJson(orderDto.getOut_trade_no(), CustomerBeen.class).getName());//营业员
            }


            head.put("lsjje", DoubleSave.doubleSaveThree(lsj));//零售价金额
            head.put("xsjje", orderDto.getYs_money());//销售价金额

            head.put("sfje", orderDto.getSs_money());//实付金额
            head.put("zl", orderDto.getZl_money());//找零
            head.put("zzk", discount);//总折扣
            head.put("vipqkje","");//会员欠款
            head.put("hyzk",hyzk);//会员
            head.put("popzk", cxzk);//促销折扣
            head.put("lszk", lszk);//临时折扣
            head.put("syje", 0);//损益金额
            head.put("vipjf", jf);//积分
            head.put("memo", "");//
            Log.i("vip", orderDto.getVipNumber());
            if (orderDto.getVipNumber().equals("")) {
                head.put("vipid", "");
            } else {
                head.put("vipid", new Gson().fromJson(orderDto.getVipNumber(), VipBeen.class).getId());
            }

            head.put("vipje", vipje);//积分金额
            head.put("grantid", "");
            head.put("sendrq", orderDto.getCheckoutTime());

            //    "paytype": "微信",              //支付方式
            //        "payje": "2",                      //支付金额
            //        "paynum": "",                    //卡号
            //        "ye": "0",                          //消费前余额
            //        "memo": ""                       //备注

            for (OrderPaytype ob:orderPaytypes) {
                JSONObject object = new JSONObject();
                object.put("payid", ob.getPayCode());
                object.put("paytype", "");
                object.put("payje", ob.getSs());
                object.put("paynum", "");
                object.put("ye", "");
                object.put("memo", "");
                pay.put(object);
            }



            headArray.put(head);
            order.put("head", headArray);
            order.put("pay", pay);
            order.put("detail", array);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestParams params = new RequestParams();
        params.put("model", "bill.mpossell");
        params.put("fun", "save");
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("pds", order.toString());
        Log.i("上传订单", "params" + params.toString());

        new Server().getConnect(context,URL.HOST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        if (object.getString("errCode").equals("100")) {
                            new OrderDao(context).updateServerInfo(orderDto);
                        }
                        Log.i("up", object.getString("errMsg"));
                    } catch (JSONException e) {
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









}
