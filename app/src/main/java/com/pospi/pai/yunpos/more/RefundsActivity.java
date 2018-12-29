package com.pospi.pai.yunpos.more;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.lany.sp.SPHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miya.TcpSend;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.newland.jsums.paylib.NLPay;
import com.newland.jsums.paylib.model.NLResult;
import com.newland.jsums.paylib.model.NetPayRefundRequest;
import com.newland.jsums.paylib.model.NormalPrintRequest;
import com.newland.jsums.paylib.model.RefundRequest;
import com.newland.jsums.paylib.model.ResultData;
import com.newland.jsums.paylib.utils.SignUtils;
import com.pax.api.scanner.ScanResult;
import com.pax.api.scanner.ScannerListener;
import com.pax.api.scanner.ScannerManager;
import com.pospi.adapter.DetailPayAdapter;
import com.pospi.adapter.RefundsDetailAdapter;
import com.pospi.adapter.RestDialogAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dao.OrderMenuDao;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dialog.MyDialog;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderMenu;
import com.pospi.dto.OrderPaytype;
import com.pospi.fragment.TradeListFragment;
import com.pospi.gpprinter.GPprint;
import com.pospi.http.MaxNO;
import com.pospi.http.PayServer;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.been.PermitonBeen;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.pai.yunpos.pay.PayActivity;
import com.pospi.pai.yunpos.util.ChuZhiKaDialogActivity;
import com.pospi.pai.yunpos.util.LoadingDialog;
import com.pospi.pai.yunpos.util.LogUtil;
import com.pospi.pai.yunpos.util.PermitionUtil;
import com.pospi.pai.yunpos.util.SuMiPrint;
import com.pospi.pai.yunpos.util.UnionConfig;
import com.pospi.paxprinter.PaxPrint;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.UpLoadServer;
import com.pospi.util.UploadERP;
import com.pospi.util.Utils;
import com.pospi.util.constant.Key;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;
import com.pospi.zqprinter.ZQPrint;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xiaopan.android.preference.PreferencesUtils;

import static com.newland.jsums.paylib.utils.Key.RESULT_SUCCESS;

/**
 * Created by Qiyan on 2016/6/6.
 */
public class RefundsActivity extends BaseActivity {
    @Bind(R.id.tuikuan)
    ImageView tuikuan;
    @Bind(R.id.detail_print)
    LinearLayout detailPrint;
    @Bind(R.id.detail_no)
    TextView detailNo;
    @Bind(R.id.detail_time)
    TextView detailTime;
    @Bind(R.id.detail_type)
    TextView detailType;

    @Bind(R.id.detail_in_lv)
    ListView detailInLv;
    @Bind(R.id.detail_pay_lv)
    ListView detailPayLv;
//    @Bind(R.id.miYa_number)
//    TextView miYa_number;

    private RefundsDetailAdapter inAdapter;
    private DetailPayAdapter payAdapter;

    private List<OrderDto> dtos;
    private OrderDto orderDto;
    private ScannerManager sm;
    private boolean conn_state;
    private String shopId;
    private String jiju;
    private String opid;
    private String str_status;
    private String cashier_number;
    private String sid;
    private PermitonBeen permiton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);
        UnionConfig.accessToken();
        permiton = new PermitionUtil().getPermiton();
        cashier_number = SPHelper.getInstance().getString(Constant.CUSTOMER);


        /**
         * 获取参数
         */
        shopId = getParam("pay_shopId");
        jiju = getParam("pay_jiju");
        opid = getParam("pay_opid");
        ButterKnife.bind(this);
        tuikuan.setClickable(true);
        orderDto = (OrderDto) getIntent().getSerializableExtra(TradeListFragment.REFUNDS);
        sid = orderDto.getOrderId();
//        Log.i("getMiYaNumber", "getMiYaNumber: "+orderDto.getMiYaNumber());
//        if (orderDto.getMiYaNumber() != "") {
//            miYa_number.setText(orderDto.getMiYaNumber());
//        }
        detailNo.setText(String.valueOf(orderDto.getMaxNo()));
        detailTime.setText(orderDto.getCheckoutTime());
        if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
            detailType.setText("退货");
            detailType.setTextColor(getResources().getColor(R.color.red));
            tuikuan.setVisibility(View.INVISIBLE);
        } else {
            detailType.setText("销售");
            tuikuan.setVisibility(View.VISIBLE);
        }
        List<OrderPaytype> list = new Gson().fromJson(orderDto.getPayway(), new TypeToken<List<OrderPaytype>>() {
        }.getType());
        inAdapter = new RefundsDetailAdapter(getApplicationContext(), Sava_list_To_Json.changeToList(orderDto.getOrder_info()), orderDto.getCashiername());
        detailInLv.setAdapter(inAdapter);
        dtos = new ArrayList<>();
        dtos.add(orderDto);
        payAdapter = new DetailPayAdapter(getApplicationContext(), list);
        detailPayLv.setAdapter(payAdapter);


        conn_state = false;
        if (android.os.Build.MODEL.equalsIgnoreCase(URL.MODEL_T8)) {
            zqPrint = new ZQPrint(getApplicationContext(), orderDto.getMaxNo(), orderDto.getPayway());
            zqPrint.connect("USB0");
            conn_state = true;
        }
    }

    @OnClick({R.id.tuikuan, R.id.detail_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tuikuan:
                if (!permiton.isCanTh()) {
                    Toast.makeText(this, Constant.PERMITION_TOAST, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (orderDto.getHasReturnGoods() == URL.hasReturnGoods_Yes) {
                    showToast("该订单已经退货！");
                } else {
                    showRefundDialog();
                }
                break;
            case R.id.detail_print:
                if (!permiton.isCanPrint()) {
                    Toast.makeText(this, Constant.PERMITION_TOAST, Toast.LENGTH_SHORT).show();
                    return;
                }
                new LogUtil().save(Constant.LOG_PRINT);
                disabledView(view);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            print(orderDto.getSs_money(), orderDto.getOrder_info(), orderDto.getMaxNo());
                        } catch (Exception e) {
                            Log.i("Exception", "外层捕获到异常");
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }


    private ZQPrint zqPrint;

    /**
     * 调用打印的方法
     */
    public void print(String shishou, String goods, String maxNO) {
        if (android.os.Build.MODEL.equalsIgnoreCase(URL.MODEL_T8)) {
            zqPrint.printext(goods, shishou, orderDto.getZl_money());
        } else if (android.os.Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
            try {
                byte status = PrnTest.prnStatus();
                switch (status) {
                    case 0x00:
                        str_status = "打印机状态为：打印机可用";
                        new PaxPrint(getApplicationContext(), maxNO, orderDto.getPayway()).print(goods, shishou ,sid);
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
            GpService mGpService = App.getmGpService();
            if (mGpService == null) {
                showToast("未连接到本地打印机");
                Utils.connection();
                mGpService = App.getmGpService();
                try {
                    mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
                } catch (RemoteException e) {
                    String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
                    gPrint_log += GetData.getDataTime() + " 连接打印机端口时异常 " + e.getMessage() + "; ";
                    PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
                    e.printStackTrace();
                }
            }
            try {
                new GPprint(mGpService, getApplicationContext(), maxNO, String.valueOf(orderDto.getStatus())).print(goods, shishou, null, false,sid);
            } catch (Exception e) {
                Log.i("GPrintLog", "调用打印方法时异常: ");
                String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
                gPrint_log += GetData.getDataTime() + " 调用打印方法时异常 " + e.getMessage() + "; ";
                PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
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
                content2.put("content", Utils.getContent(RefundsActivity.this, maxNO, String.valueOf(orderDto.getStatus()), shishou, true));
                lists.add(content2.toString());
                if (orderDto.getStatus() == PayWay.CASH || orderDto.getStatus() == PayWay.OTHER) {
                    Utils.cashopen();
                }
                request.setContents(lists);
                pay.normalPrint(RefundsActivity.this, request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_T1)) {
           SuMiPrint suMiPrint =  new SuMiPrint(this ,orderDto);
            suMiPrint.beginPrint();
        } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_T2)) {
            SuMiPrint suMiPrint =  new SuMiPrint(this ,orderDto);
            suMiPrint.beginPrint();
        }
    }

    /**
     * 给本地写入一条记录
     */
    public void addorderinfo() {
        String re_sid = GetData.getStringRandom(32);
        try {
            final String cashier_name = SPHelper.getInstance().getString(Constant.CUSTOMER_name);
            String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
            final OrderDto orderDto11 = new OrderDto();
            List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
//            for (int i = 0; i < goodsDtos.size(); i++) {
//                double num = goodsDtos.get(i).getNum();
//                goodsDtos.get(i).setNum(-num);
//            }
            String listinfo = Sava_list_To_Json.changeGoodDtoToJaon(goodsDtos);
            orderDto11.setOrder_info(listinfo);
            orderDto11.setOrderId(re_sid);
            orderDto11.setOut_trade_no(orderDto.getOut_trade_no());
            orderDto11.setPayway(orderDto.getPayway());
            orderDto11.setTime(GetData.getYYMMDDTime());
            orderDto11.setSs_money( orderDto.getYs_money());
            orderDto11.setYs_money( orderDto.getYs_money());
            orderDto11.setZl_money(String.valueOf(0.00));
            orderDto11.setCashiername(cashier_name);
            orderDto11.setVipNumber(orderDto.getVipNumber());
            orderDto11.setShop_id(shopId);
            orderDto11.setStatus(orderDto.getStatus());
            orderDto11.setOrderType(URL.ORDERTYPE_REFUND);
//            orderDto11.setOut_trade_no(String.valueOf(orderDto.getMaxNo()));
            orderDto11.setMaxNo(MaxNO.getMaxNo(getApplicationContext()));
            orderDto11.setCheckoutTime(GetData.getDataTime());
            orderDto11.setDetailTime(GetData.getHHmmssTimet());
//            orderDto11.setHasReturnGoods(Integer.parseInt(orderDto.getMaxNo().substring(6,10)));
            orderDto11.setHasReturnGoods(Integer.parseInt(orderDto.getMaxNo().substring(orderDto.getMaxNo().length()-4,orderDto.getMaxNo().length())));
            orderDto11.setIfFinishOrder(URL.YES);
            orderDto11.setUpLoadServer(UpLoadServer.noUpload);
            orderDto11.setUpLoadERP(UploadERP.noUpload);
            new OrderDao(getApplicationContext()).addOrder(orderDto11);
            SuMiPrint suMiPrint =  new SuMiPrint(this ,orderDto11);
            suMiPrint.beginPrint();

        } catch (Exception e) {
            e.printStackTrace();
        }
        new LogUtil().save(Constant.LOG_REFUND);
        startActivity(new Intent(this, StatisticsActivity.class));
        finish();
    }

    /**
     * 获取米雅配置参数
     *
     * @param param
     * @return
     */
    private String getParam(String param) {
        SharedPreferences sp = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
        return sp.getString(param, "");
    }

    public void showRefundDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("您确定要退货吗？")
                .setMessage("点击[确定]后，完成退款操作！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//
                        startRefund();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    private void startRefund() {
        List<OrderPaytype> orderPaytypes =  new Gson().fromJson(orderDto.getPayway(), new TypeToken<List<OrderPaytype>>() {
        }.getType());
        String jfje = "0";
        String mzje = "0";
        String type = "xj";
        String zfje = "0";
        for (OrderPaytype paytype : orderPaytypes) {
           if (paytype.getPayCode() == PayWay.ALIPAY || paytype.getPayCode() == PayWay.WXPAY) {
                type = "zf";
                zfje = paytype.getSs();
            }
        }
        switch (type) {
            case "xj":
                refund();
                break;
            case "zf":
                unionRefund(orderDto.getMiYaNumber(),zfje);
                break;

        }
    }

    /**
     * 米雅退貨
     *
     * @param numId  门店编码
     * @param userId 机具编码
     * @param opId   收银员ID
     * @param no     订单号
     * @param fee    金额
     */
//    private boolean RmiYa(String saasid, String numId, String userId, String opId, String no, String fee) {
//        no = numId + no;
//        final Map tlvmap = new HashMap();
//        tlvmap.put("VERSION", "1.5");
//        //交易代码-退货
//        tlvmap.put("TRCODE", "1002");
//        //商户编号
//        tlvmap.put("SAASID", saasid);
//        //门店编码
//        tlvmap.put("NUMID", numId);
//        //机具编码
//        tlvmap.put("USERID", userId);
//        //收银员ID
//        tlvmap.put("OPERATOR_ID", cashier_number);
//        //支付方式
//        tlvmap.put("PAYMENTPLATFORM", "A");
//        //操作类型--退款
//        tlvmap.put("SERVEICETYPE", "C");
//        //商家订单（唯一）
//        tlvmap.put("OUT_TRADE_NO", no);
//        //退货订单
//        tlvmap.put("OUT_REQUEST_NO", no);
//        //支付总金额--单位分
//        tlvmap.put("TOTAL_FEE", String.valueOf(Double.parseDouble(fee) * 100));
//        //配置文件路径
//        tlvmap.put("path", Environment.getExternalStorageDirectory().getPath() + "/miyajpos/");
//        try {
//            Map requsetMap = TcpSend.sendMiya(tlvmap, null);
//            Object retcode = requsetMap.get("RETCODE");
//            Object retmsg = requsetMap.get("RETMSG");
//            Log.i("miya", retcode + "----" + retmsg);
//            if ("00".equals(retcode) && "REFUNDSUCCESS".equals(retmsg)) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;

//    }
    //测试
    private boolean RmiYa(String saasid, String numId, String userId, String opId, String no, String fee) {
        no = numId + no;
        final Map tlvmap = new HashMap();
        tlvmap.put("VERSION", "1.5");
        //交易代码-退货
        tlvmap.put("TRCODE", "1002");
        //商户编号
//        tlvmap.put("SAASID", saasid);
        //门店编码
        tlvmap.put("NUMID", "001");
        //机具编码
        tlvmap.put("USERID", "19");
        //收银员ID
        tlvmap.put("OPERATOR_ID", "001");
        //支付方式
        tlvmap.put("PAYMENTPLATFORM", "A");
        //操作类型--退款
        tlvmap.put("SERVEICETYPE", "C");
        //商家订单（唯一）
        tlvmap.put("OUT_TRADE_NO", no);
        //退货订单
        tlvmap.put("OUT_REQUEST_NO", no);
        //支付总金额--单位分
        tlvmap.put("TOTAL_FEE", String.valueOf(Double.parseDouble(fee) * 100));
        //配置文件路径
        tlvmap.put("path", Environment.getExternalStorageDirectory().getPath() + "/miyajpos/");
        try {
            Map requsetMap = TcpSend.sendMiya(tlvmap, null);
            Object retcode = requsetMap.get("RETCODE");
            Object retmsg = requsetMap.get("RETMSG");
            Log.i("miya", retcode + "----" + retmsg);
            if ("00".equals(retcode) && "REFUNDSUCCESS".equals(retmsg)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public void refund(String mode) {
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        String value = preferences.getString("value", "");
//        String value = "245148d996d5404c9d37a4cccd2e791b,f17851cbccb84ce0b55ac6f464d9e13f,100148,183,98";
        String[] names = value.split("\\,");
        String uid = names[1];
        RequestParams params = new RequestParams();
        params.put("pay_mode", mode);
        String money = orderDto.getSs_money();
        if (mode.equals("wx")) {
            money = String.valueOf((int) (Double.parseDouble(orderDto.getSs_money()) * 100));
        }
        params.put("resund_fee", money);
        params.put("total_fee", money);
        params.put("transaction_id", orderDto.getPayReturn());
        params.put("uid", uid);
        Log.i("refund", params.toString());

        new PayServer().getConnect(getApplicationContext(), new URL().REFUND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("refund", new String(bytes));
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    if (object.getInt("Result") == 1) {
                        showToast(object.getString("Message"));
                        new OrderDao(getApplicationContext()).updateOrderInfo(orderDto.getMaxNo());
                        addorderinfo();
                        getCodeDialog.dismiss();
                    } else {
                        showToast(object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Log.i("refundfailure", new String(bytes));
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    showToast(object.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeDialog();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == 110) {
                if (new OrderDao(getApplicationContext()).updateOrderInfo(orderDto.getMaxNo())) {//数据库销售变成退货
                    showToast("退货成功");
//                    ValueCardDto valueCardDto = (ValueCardDto) data.getSerializableExtra(PaySuccessDialogActivity.CARD_INFO);
//                    new UpLoadToServel(getApplicationContext()).upCardRecord(new PayWayDao(getApplicationContext()).findPaySid("6"), orderDto, valueCardDto, 2222);
                    addorderinfo();
                } else {
                    showToast("退货失败");
                }
            }
        }
        if (resultCode == RESULT_SUCCESS) {
            switch (requestCode) {
                case com.newland.jsums.paylib.utils.Key.REQUEST_CANCEL:
                    break;
                case com.newland.jsums.paylib.utils.Key.REQUEST_REFUND:
                    // case com.newland.jsums.paylib.utils.Key.REQUEST_POS_LOGS:

                    if (data != null) {
                        NLResult rslt = (NLResult) data
                                .getParcelableExtra("result");
                        Log.d("RefundActivity", rslt.toString());
                        if (rslt.getResultCode() == 6000) {
                            refund();
                        }
                    }
                    break;
                case com.newland.jsums.paylib.utils.Key.REQUEST_NETPAYREFUND:
                    if (data != null) {
                        NLResult rslt = (NLResult) data
                                .getParcelableExtra("result");
                        Log.d("RefundActivity", rslt.toString());
//                        showResult(rslt, false);
                        if (rslt.getResultCode() == 6000) {
                            refund();
//                            switch (orderDto.getPayway()) {
//                                case PayWay.WX:
//                                    refund();
//                                    break;
//                                case PayWay.ZFB:
//                                    refund();
//                                    break;
//                            }
                        }
                    }
                    break;
            }
        }
    }

    //退货操作
    public void refund() {
        if (new OrderDao(getApplicationContext()).updateOrderInfo(orderDto.getMaxNo())) {//数据库销售变成退货
            showToast("退货成功");
            tuikuan.setClickable(false);
            addorderinfo();
        } else {
            showToast("退货失败");
        }
    }

    /**
     * 显示结果
     *
     * @param rslt
     */
    public void showResult(NLResult rslt, boolean verifySign) {
        String resultMsg = "";
        StringBuilder sb = new StringBuilder();
        sb.append("返回码：" + rslt.getResultCode() + " \n");
        Log.d("malian", rslt.toString());

        if (rslt.getResultCode() == 6000 && rslt.getData() != null) {
            if (new OrderDao(getApplicationContext()).updateOrderInfo(orderDto.getMaxNo())) {//数据库销售变成退货
                showToast("退货成功");
                addorderinfo();
            } else {
                showToast("退货失败");
            }
            // 有订单信息返回的时候需对期进行验签，防止数据被篡改
            ResultData data = rslt.getData();
//			Log.d("malian",rslt.getSignData());
            if (!verifySign) {
                // 无需验签
                // if(rslt.getData() instanceof NLSwiperResult) {
                // resultMsg = ((NLSwiperResult)rslt.getData()).toString();
                // }
                resultMsg = rslt.toString();

            } else if (verifySign
                    && rslt.getSignData() != null
                    && SignUtils.verifySignData(Key.PUBLIC_KEY, data,
                    rslt.getSignData())) {
                // 验签成功
                sb.append("订单信息：" + data.toString() + "\n");
                resultMsg = sb.toString();
                // 存入上次调用成功的信息
//                if (rslt.getData() instanceof OrderInfo) {
//                    lastOrderInfo = (OrderInfo) rslt.getData();
//                }
            } else {
                // 验签失败
                resultMsg = "验签失败";
            }
        } else {
            // 调用失败
            sb.append("错误信息：" + rslt.getResultMsg() + "\n");
            resultMsg = sb.toString();
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(resultMsg).setTitle("调用结果")
                .setPositiveButton("确定", null).create().show();
    }

    private AlertDialog getCodeDialog;
    String scanerCode;

    public String scanerCode(final String way) {

        @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(R.layout.scaner_code, null);
        getCodeDialog = new AlertDialog.Builder(this).setView(layout).create();
        getCodeDialog.show();

        final EditText et = (EditText) layout.findViewById(R.id.message);
//        Button sure = (Button) layout.findViewById(R.id.positiveButton);
//        ImageView cancle = (ImageView) layout.findViewById(R.id.negativeButton);

//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanerCode = et.getText().toString().trim();
//                Log.d("scanerCode", scanerCode);
//                Log.d("return", orderDto.getOut_trade_no());
//                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
//                    getCodeDialog.dismiss();
//                    NLPay pay = NLPay.getInstance();
//                    NetPayRefundRequest refundRequest = new NetPayRefundRequest();
//                    refundRequest.setAppAccessKeyId(Key.APP_SECRET_KEY);
//                    refundRequest.setAmount(orderDto.getSs_money());
//                    refundRequest.setMrchNo(Key.MRCH_NO);
//                    refundRequest.setExtOrderNo(String.valueOf(orderDto.getMaxNo()));
//                    refundRequest.setExt01("");
//                    refundRequest.setExt02("");
//                    refundRequest.setExt03("");
//                    refundRequest.setSignature(SignUtils.signData(Key.PRIVATE_KEY,
//                            refundRequest));
//                    refundRequest.setMrchOrderNo(scanerCode);
//                    pay.netpayrefund(RefundsActivity.this, refundRequest);
//                } else {
//                    if (scanerCode.equalsIgnoreCase(orderDto.getOut_trade_no().trim())) {
//                        getCodeDialog.dismiss();
//                        showDialog("正在退款，请等待");
//                        refund(way);
//                    } else {
//                        getCodeDialog.dismiss();
//                        showToast("商户单号验证失败，不可退款");
//                    }
//                }
//            }
//        });

//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
//                    sm = ScannerManager.getInstance(getApplication());
//                    sm.scanOpen();
//                    sm.scanStart(new ScannerListener() {
//                        @Override
//                        public void scanOnComplete() {
//                            sm.scanClose();
//                        }
//
//                        @Override
//                        public void scanOnCancel() {
//
//                        }
//
//                        @Override
//                        public void scanOnRead(ScanResult arg0) {
//                            if (arg0 != null) {
//                                refund(way);
//                            }
//                        }
//                    });
//                } else {
//                    showToast("暂不支持此功能");
//                }
//            }
//        });
        return scanerCode;
    }

    private Dialog dialog;

    public void showDialog(String msg) {
        if (dialog == null) {
            dialog = MyDialog.createLoadingDialog(this, msg);
            dialog.show();
        }
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    /**
     * 信息提示
     *
     * @param msg
     */
    private void showMsgToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RefundsActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    //设置点击支付按钮间隔时间，防止多次提交订单
    public void disabledView(final View v){
        v.setClickable(false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                v.setClickable(true);
            }
        },800);
    }
    public void unionRefund(String orderId ,String money) {
        int amount  = (int) (Double.parseDouble(money)*100);
        showPayDialog();
        JSONObject object = new JSONObject();
        try {
            object.put("merchantCode", UnionConfig.merchantCode);
            object.put("terminalCode", UnionConfig.terminalCode);
            object.put("originalOrderId", orderId);
            object.put("refundRequestId", GetData.getStringRandom(10)+orderId);
            object.put("transactionAmount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("pay", object.toString());
        App.getInstance().getMyOkHttp().post()
                .url(UnionConfig.URL_REFUND)
                .jsonParams(object.toString())
                .addHeader("Authorization","OPEN-ACCESS-TOKEN AccessToken="+SPHelper.getInstance().getString(UnionConfig.TOKEN_MSG))
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.i("pay", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String errCode = json.getString("errCode");
                            String errInfo = json.getString("errInfo");
                            if (errCode.equals("00")) {
                                Message message = Message.obtain();
                                message.what = PAYSUCCESS;
                                handler.sendMessage(message);
                            } else {
                                Message message = Message.obtain();
                                message.what = PAYFAIL;
                                message.obj = errInfo;
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.i("pay", error_msg);
                        Message message = Message.obtain();
                        message.what = PAYFAIL;
                        message.obj = error_msg;
                        handler.sendMessage(message);
                    }
                });
    }
    private static final int PAYSUCCESS = 1;
    private static final int PAYFAIL = 0;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAYSUCCESS:
                    closePayDialog();
                    refund();
                    Toast.makeText(RefundsActivity.this, "退货成功", Toast.LENGTH_SHORT).show();
                    break;
                case PAYFAIL:
                    closePayDialog();
                    Toast.makeText(RefundsActivity.this, "退货失败"+(String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private LoadingDialog loadingDialog;
    //支付等待提示
    private void showPayDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("请求中，请稍等");
            loadingDialog.show();
        }
    }

    //关闭支付等待提示
    private void closePayDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
