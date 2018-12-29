package com.pospi.pai.yunpos.pay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.lany.sp.SPHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miya.TcpSend;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.newland.jsums.paylib.NLPay;
import com.newland.jsums.paylib.model.ConsumeRequest;
import com.newland.jsums.paylib.model.NLBillInfo;
import com.newland.jsums.paylib.model.NLResult;
import com.newland.jsums.paylib.model.NetPayConsumeRequest;
import com.newland.jsums.paylib.model.PrintRequest;
import com.newland.jsums.paylib.model.ResultData;
import com.newland.jsums.paylib.utils.SignUtils;
import com.pax.api.BaseSystemException;
import com.pax.api.BaseSystemManager;
import com.pax.api.PortException;
import com.pax.api.PortManager;
//import com.pax.api.scanner.BarcodeParam;
//import com.pax.api.scanner.ScanResult;
//import com.pax.api.scanner.ScannerListener;
//import com.pax.api.scanner.ScannerManager;
import com.pax.dal.IScanner;
import com.pax.dal.IDAL;
import com.pax.dal.entity.EScannerType;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pospi.adapter.PayAdapter;
import com.pospi.adapter.PayListAdapter;
import com.pospi.adapter.PaymentAdapter;
import com.pospi.adapter.RestDialogAdapter;
import com.pospi.adapter.YhqAdapter;
import com.pospi.callbacklistener.HttpCallBackListener;
import com.pospi.dao.OrderDao;
import com.pospi.dao.OrderMenuDao;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dialog.MyDialog;
import com.pospi.dto.CouponsBeen;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderMenu;
import com.pospi.dto.OrderPaytype;
import com.pospi.dto.PayWayDto;
import com.pospi.dto.Tablebeen;
import com.pospi.dto.ValueCardDto;
import com.pospi.dto.VipBeen;
import com.pospi.greendao.TablebeenDao;
import com.pospi.http.HttpConnection;
import com.pospi.http.PayServer;
import com.pospi.http.UpLoadToServel;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.PayBaseActivity;
import com.pospi.pai.yunpos.been.CustomerBeen;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.pai.yunpos.util.ChuZhiKaDialogActivity;
import com.pospi.pai.yunpos.util.HorizontalListView;
import com.pospi.pai.yunpos.util.LoadingDialog;
import com.pospi.pai.yunpos.util.LogUtil;
import com.pospi.pai.yunpos.util.PaySuccessDialogActivity;
import com.pospi.pai.yunpos.util.UnionConfig;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.AidlUtil;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyPrinter;
import com.pospi.util.PrinterFactory;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.UpLoadServer;
import com.pospi.util.UpdateOrder;
import com.pospi.util.UploadERP;
import com.pospi.util.Utils;
import com.pospi.util.constant.Key;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;
import com.pospi.util.constant.tableinfo.TableStatusConstance;
import com.pospi.zqprinter.ZQPrint;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.android.preference.PreferencesUtils;

import static com.newland.jsums.paylib.utils.Key.RESULT_SUCCESS;

public class PayActivity extends PayBaseActivity implements View.OnClickListener {
//    @Bind(R.id.lv_payment)
//    ListView lvPayment;
    @Bind(R.id.pay_list)
    ListView pay_list;
    @Bind(R.id.pay_way_list)
    HorizontalListView pay_way_list;
    @Bind(R.id.tv_100)
    TextView tv_100;
    @Bind(R.id.tv_65)
    TextView tv_65;
    @Bind(R.id.tv_50)
    TextView tv_50;
    @Bind(R.id.zfpt_return)
    ImageView zfpt_return;
    private final static String TAG = "PayActivity";

    private TextView detail, detail2, show, yingshou, shishou, zhaoling;
    private Button btn_pay;
    private static int whatPay = 0;
    private boolean chooseWay = false;
    private String test = "";
    private String value = "2";
    private int orderType;
    private String maxNO;
    private ValueCardDto valueCardDto;

    byte port = 0;

    private Dialog dialog;
    private PaymentAdapter paymentAdapter;
    private List<PayWayDto> payWayDtos;
    public static final int SEND_REQUEST = 12345;
    private PayWayDto payWayDto;
    private String type;
    private ZQPrint zqPrint;
    private GpService mGpService = null;
    private boolean conn_state = false;
    private String out_trade_no = null;
    private double discount;
    private MyPrinter myPrinter;
    private int typeNumber;
    private int firstChoose;
    private List<OrderPaytype> payList = new ArrayList<>();

    //金额
    private String fee;
    //付款方式代号
    private int payWayCode;
    //付款方式名称
    private String payWayName;
    public static boolean isRun;
    private LoadingDialog loadingDialog;
    private IDAL dal = null;
    private boolean tag;
    private PortManager pm;
    private boolean isChecked;
    private TextView detail3;
    private TextView detail4;
    //混合支付指令数组
    private int[] pays = new int[2];
    private String sid_m;
    private String tableNumber;
    private String tableId;
    private TablebeenDao tablebeenDao;
    private String vip_number;
    private OrderDto orderDto;
    private VipBeen vipBeen;
    private String vip = "";
    private String denomination;
    private double proReduce;
    private double ysMoney;
    private String cashier_number;
    private PayListAdapter payListAdapter;
    private PayAdapter payAdapter;
    private CustomerBeen customerBeen;
    private String cusId;
    private String mode;
    private String union_orderid ="";
    private static final int PAYSUCCESS = 1;
    private static final int LODIND_PAYSUCCESS = 2;
    private static final int PAYFAIL = 0;
    private String zfje;
    private String payType;
    private String jfje;
    private String mzje;
    private String zfCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test", "onCreate");
        setContentView(R.layout.pay);
//        try {
//            dal = NeptuneLiteUser.getInstance().getDal(this.getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        sid_m = GetData.getStringRandom();
        discount = getSharedPreferences("StoreMessage", MODE_PRIVATE).getFloat("Discount", 1);
        int whichOne = getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        cashier_number = SPHelper.getInstance().getString(Constant.CUSTOMER);
        isChecked = getSharedPreferences("receipt_num", Context.MODE_PRIVATE).getBoolean("scannerConfig", false);
        tablebeenDao = App.getInstance().getDaoSession().getTablebeenDao();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        value = intent.getStringExtra("money");
        mode = intent.getStringExtra("mode");
        ysMoney = Double.parseDouble(value);
        orderType = intent.getIntExtra("orderType", URL.ORDERTYPE_SALE);
        vipBeen = (VipBeen) intent.getSerializableExtra("vip");
        customerBeen = (CustomerBeen) intent.getSerializableExtra("cus");

        if (vipBeen != null) {
            vip = vipBeen.getId();
            searchYhq();
        }
        maxNO = intent.getStringExtra("maxNo");
        vip_number = intent.getStringExtra("vip_number");

        ysMoney = DoubleSave.doubleSaveTwo(ysMoney);
        Log.i("maxNo", maxNO + "");
        initWidgets();
        if (orderType == URL.ORDERTYPE_REFUND) {
            btn_pay.setText("退款");
            type = "2";
        } else {
            type = "1";
        }
        yingshou.setText(value);
        chooseWay = false;


    }

    //支付等待提示
    private void showPayDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("正在支付，请稍等");
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

    private boolean chooseJF = false;

    public void initWidgets() {



        show = (TextView) findViewById(R.id.show);
        TextView zhenghao = (TextView) findViewById(R.id.zhenghao);
        zhenghao.setOnClickListener(this);

        TextView delete = (TextView) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        yingshou = (TextView) findViewById(R.id.pay_yingshou);
        shishou = (TextView) findViewById(R.id.pay_shihou);
        zhaoling = (TextView) findViewById(R.id.pay_zhaoling);
//
//        ImageView qingkong = (ImageView) findViewById(R.id.qingkong);
//        qingkong.setOnClickListener(this);

        LinearLayout reset = (LinearLayout) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

        tv_50.setOnClickListener(this);
        tv_65.setOnClickListener(this);
        tv_100.setOnClickListener(this);
        zfpt_return.setOnClickListener(this);
        payWayDtos = new ArrayList<>();
        savePayway();
        paymentAdapter = new PaymentAdapter(PayActivity.this, payWayDtos);
//        lvPayment.setAdapter(paymentAdapter);
//        lvPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                payWayDto = payWayDtos.get(position);
//
//                if (payWayDto.getPayType1() == PayWay.JF_CODE) {
//                    if (vipBeen == null) {
//                        Toast.makeText(PayActivity.this, "当前不是会员", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (canPay()) {
//                        Toast.makeText(PayActivity.this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (chooseJF) {
//                        Toast.makeText(PayActivity.this, "积分已经抵扣", Toast.LENGTH_SHORT).show();
//                    } else {
//                        getJfDialog();
//                    }
//
//                } else if (payWayDto.getPayType1() == PayWay.YHQ_CODE) {
//                    if (vipBeen == null) {
//                        Toast.makeText(PayActivity.this, "当前不是会员", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (canPay()) {
//                        Toast.makeText(PayActivity.this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    getYhqDialog();
//
//                } else {
//                    startPay(payWayDto);
//                }
//
//            }
//        });
        payListAdapter = new PayListAdapter(this, payList);
        pay_list.setAdapter(payListAdapter);
    }

    private boolean isYhq() {
        for (OrderPaytype paytype : payList) {
            if (paytype.getPayCode() == PayWay.YHQ_CODE) {
                paytype.setSs(couponsBeen.getDenomination());
                return true;
            }
        }
        return false;
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAYSUCCESS:
                    closePayDialog();
                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    addOrder(payList, shishou.getText().toString());
                    break;
                case PAYFAIL:
                    closePayDialog();
                    Toast.makeText(PayActivity.this, "支付失败"+(String)msg.obj, Toast.LENGTH_SHORT).show();
                    if (!jfje.equals("0") || !mzje.equals("0")) {
                        requestHy_cx(jfje, mzje);
                    }
                    break;
                case LODIND_PAYSUCCESS:
                    unionPay(zfCode, zfje, maxNO);
                    break;
            }
        }
    };

    //开始混合支付
    private void startPay(PayWayDto dto) {
        if (checkPay() || canPay()) {
            Toast.makeText(this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
            return;
        }
        payWayDto.setStatus(1);
        payAdapter.notifyDataSetChanged();
        if (isYhq() && dto.getPayType1() == PayWay.YHQ_CODE) {

        } else {
            OrderPaytype paytype = new OrderPaytype();
            if (dto.getPayType1() == PayWay.ALIPAY || dto.getPayType1() == PayWay.WXPAY) {
                paytype.setSs(String.valueOf(getlastMoney()));
            } else if (dto.getPayType1() == PayWay.YHQ_CODE) {
                paytype.setSs(couponsBeen.getDenomination());
            } else if (dto.getPayType1() == PayWay.JF_CODE) {//积分
                paytype.setSs(vipBeen.getJfdxje());
            }else if (dto.getPayType1() == PayWay.MIANZHI_CARD) {//会员卡余额
                if (getlastMoney() > 0) {
                    if (getlastMoney() > Double.parseDouble(vipBeen.getMzye())) {
                        paytype.setSs(vipBeen.getMzye());
                    } else {
                        paytype.setSs(getlastMoney()+"");
                    }

                }

            }  else {
                if (Double.parseDouble(show.getText().toString()) == 0) {
                    paytype.setSs(String.valueOf(getlastMoney()));
                } else {
                    paytype.setSs(show.getText().toString());
                }

            }
            paytype.setPayName(dto.getName());
            paytype.setPayCode(dto.getPayType1());
            payList.add(paytype);
        }
        payListAdapter.notifyDataSetChanged();
        setPayDetail();
        show.setText("0.00");
        test = "";
    }

    private void setPayDetail() {
        double ss = 0;
        for (OrderPaytype paytype : payList) {
            ss += Double.parseDouble(paytype.getSs());
        }
        shishou.setText(String.valueOf(DoubleSave.doubleSaveTwo(ss)));
        if (ss > ysMoney) {
            zhaoling.setText(String.valueOf(DoubleSave.doubleSaveTwo(ss - ysMoney)));
        } else {
            zhaoling.setText("0.00");
        }

    }

    private boolean checkPay() {
        for (OrderPaytype paytype : payList) {
            if (paytype.getPayCode() == PayWay.ALIPAY || paytype.getPayCode() == PayWay.WXPAY) {
                return true;
            }
        }
        return false;

    }

    private double getlastMoney() {
        double ss = 0;
        for (OrderPaytype paytype : payList) {
            ss += Double.parseDouble(paytype.getSs());
        }
        if (ss < ysMoney) {
            return DoubleSave.doubleSaveTwo(ysMoney - ss);
        } else {
            return 0;
        }

    }

    private boolean canPay() {
        double ss = 0;
        for (OrderPaytype paytype : payList) {
            ss += Double.parseDouble(paytype.getSs());
        }
        if (ss < ysMoney) {
            return false;
        } else {
            return true;
        }

    }

    private void savePayway() {
        PayWayDto dto = new PayWayDto();
        dto.setPayType1(PayWay.CASH);
        dto.setName(PayWay.XJ);
        PayWayDto dto1 = new PayWayDto();
        dto1.setPayType1(PayWay.ALIPAY);
        dto1.setName(PayWay.ZFB);
        PayWayDto dto2 = new PayWayDto();
        dto2.setPayType1(PayWay.WXPAY);
        dto2.setName(PayWay.WX);
        PayWayDto dto3 = new PayWayDto();
        dto3.setPayType1(PayWay.JF_CODE);
        dto3.setName(PayWay.JF);
        PayWayDto dto4 = new PayWayDto();
        dto4.setPayType1(PayWay.YHQ_CODE);
        dto4.setName(PayWay.YHQ);
        PayWayDto dto5 = new PayWayDto();
        dto5.setPayType1(PayWay.MIANZHI_CARD);
        dto5.setName(PayWay.CZK);
        payWayDtos.add(dto);
        payWayDtos.add(dto1);
        payWayDtos.add(dto2);
        payWayDtos.add(dto3);
        payWayDtos.add(dto4);
        payWayDtos.add(dto5);
        payAdapter = new PayAdapter(payWayDtos, this);
        pay_way_list.setAdapter(payAdapter);
        pay_way_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                payWayDto = payWayDtos.get(position);
                if (checkSamePay(payWayDto)) {
                    return;
                }
                if (payWayDto.getPayType1() == PayWay.JF_CODE) {
                    if (vipBeen == null) {
                        Toast.makeText(PayActivity.this, "当前不是会员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (canPay()) {
                        Toast.makeText(PayActivity.this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (chooseJF) {
                        Toast.makeText(PayActivity.this, "积分已经抵扣", Toast.LENGTH_SHORT).show();
                    } else {
                        getJfDialog();
                    }

                } else if (payWayDto.getPayType1() == PayWay.YHQ_CODE) {
                    if (vipBeen == null) {
                        Toast.makeText(PayActivity.this, "当前不是会员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (canPay()) {
                        Toast.makeText(PayActivity.this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getYhqDialog();

                } else if (payWayDto.getPayType1() == PayWay.MIANZHI_CARD) {
                    if (vipBeen == null) {
                        Toast.makeText(PayActivity.this, "当前不是会员", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (canPay()) {
                        Toast.makeText(PayActivity.this, "已达到支付金额,如需更改支付方式请重置", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getHykDialog();

                } else {
                    startPay(payWayDto);
                }

            }
        });
        pay_way_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 点击事件的调用
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zfpt_return:
                finish();
                break;
            case R.id.tv_50:
               show.setText("50.0");
                break;
            case R.id.tv_65:
                show.setText("65.0");
                break;
            case R.id.tv_100:
                show.setText("100.0");
                break;
            case R.id.reset:
                zhaoling.setText("0.00");
                shishou.setText("0.00");
                show.setText("0.00");
                payList.clear();
                test = "";
                resetPay();
                payAdapter.notifyDataSetChanged();
                payListAdapter.notifyDataSetChanged();
                chooseJF = false;
                couponsBeen = null;
                break;
            case R.id.zhenghao:
                show.setText(String.valueOf(getlastMoney()));

                break;
            case R.id.delete:
                show.setText(R.string.zero);
                test = "";
                break;
            case R.id.btn_pay:
                if (payList.size() == 0) {
                    Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Constant.LX.equals(mode)) {
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("当前模式为练习模式，订单将不会上传，以及记录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (canPay()) {
                                        startRequest(payList);
                                    } else {
                                        Toast.makeText(PayActivity.this, "支付金额应大于应收金额", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).show();
                } else {
                    if (canPay()) {
                        startRequest(payList);
                    } else {
                        Toast.makeText(PayActivity.this, "支付金额应大于应收金额", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
        }
    }

    //开始调用支付通到
    private void startRequest(List<OrderPaytype> payList) {
        jfje = "0";
        mzje = "0";
        payType = "xj";
        zfje = "0";
        for (OrderPaytype paytype : payList) {
            if (paytype.getPayCode() == PayWay.JF_CODE ) {//积分
                payType = "hy";
                jfje = paytype.getSs();
            } else if (paytype.getPayCode() == PayWay.MIANZHI_CARD) {
                payType = "hy";
                mzje = paytype.getSs();
            } else if (paytype.getPayCode() == PayWay.ALIPAY || paytype.getPayCode() == PayWay.WXPAY) {
                payType = "zf";
                zfje = paytype.getSs();
            }
        }
        if (!jfje.equals("0") || !mzje.equals("0")) {
            if (payType.equals("zf")) {
                payType = "hh";
            }
        }
        switch (payType) {
            case "hy":
                requestHy(jfje, mzje);
                break;
            case "xj":
                addOrder(payList, shishou.getText().toString());
                break;
            case "zf":
                requestZf(zfje);
                break;
            case "hh":
                requestZf(zfje);
                break;
        }
    }

    //微信支付宝
    private void requestZf(String zfje) {
        Log.i("je", zfje);
        if (SPHelper.getInstance().getString(UnionConfig.MERCHANTCODE).equals("")||
                SPHelper.getInstance().getString(UnionConfig.TERMINALCODE).equals("")) {
            Toast.makeText(this, "商户号或终端号未配置", Toast.LENGTH_SHORT).show();
            return;
        }
        scanerCode(zfje);
    }

    private void requestHy(String jfje, String mzje) {
        showPayDialog();
        Map<String, String> map = new HashMap<>();
        map.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        map.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        map.put("model", "vip.vvip");
        map.put("fun", "hx");

        JSONObject object = new JSONObject();
        try {
            object.put("id", vipBeen.getId());
            object.put("dkje", jfje);
            object.put("dkmz", mzje);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("pds", object.toString());
        new HttpConnection().postNet(map, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    String errCode = jsonObject.getString("errCode");
                    String errMsg = jsonObject.getString("errMsg");
                    if (errCode.equals("100")) {
                        Message message = Message.obtain();
                        message.what = PAYSUCCESS;
                        handler.sendMessage(message);

                    } else {
                        Message message = Message.obtain();
                        message.what = PAYFAIL;
                        message.obj = errMsg;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = PAYFAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //撤销
    private void requestHy_cx(String jfje, String mzje) {
        showPayDialog();
        Map<String, String> map = new HashMap<>();
        map.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        map.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        map.put("model", "vip.vvip");
        map.put("fun", "fhx");

        JSONObject object = new JSONObject();
        try {
            object.put("id", vipBeen.getId());
            object.put("dkje", jfje);
            object.put("dkmz", mzje);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("pds", object.toString());
        new HttpConnection().postNet(map, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    String errCode = jsonObject.getString("errCode");
                    String errMsg = jsonObject.getString("errMsg");
                    if (errCode.equals("100")) {
                        Message message = Message.obtain();
                        message.what = PAYSUCCESS;
                        handler.sendMessage(message);

                    } else {
                        Message message = Message.obtain();
                        message.what = PAYFAIL;
                        message.obj = errMsg;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = PAYFAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }
    private void requestHylodind(String jfje, String mzje) {
        showPayDialog();
        Map<String, String> map = new HashMap<>();
        map.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        map.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        map.put("model", "vip.vvip");
        map.put("fun", "hx");

        JSONObject object = new JSONObject();
        try {
            object.put("id", vipBeen.getId());
            object.put("dkje", jfje);
            object.put("dkmz", mzje);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("pds", object.toString());
        new HttpConnection().postNet(map, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    String errCode = jsonObject.getString("errCode");
                    String errMsg = jsonObject.getString("errMsg");
                    if (errCode.equals("100")) {
                        Message message = Message.obtain();
                        message.what = LODIND_PAYSUCCESS;
                        handler.sendMessage(message);

                    } else {
                        Message message = Message.obtain();
                        message.what = PAYFAIL;
                        message.obj = errMsg;
                        handler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = PAYFAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }
    private void resetPay() {
        for (PayWayDto dto : payWayDtos) {
            dto.setStatus(0);
        }
    }

    //计算器界面的功能实现
    public void qiqi(View v) {
        TextView tt = (TextView) v;
        if (v.getId() == R.id.dian) {//当是点的时候
            if (!(test == null)) {
                test = test + tt.getText().toString();
            }
        } else {
            test = test + tt.getText().toString();
        }
        show.setText(test);
    }


    /**
     * 调用打印的方法
     */
    public void print(OrderDto orderDto) {
        switch (Build.MODEL) {
            case URL.MODEL_E500:
                myPrinter = PrinterFactory.getEPrinter(this, "123", "123");
                break;
            case URL.MODEL_D800:
                byte status = PrnTest.prnStatus();
                if (status == 0x00) {
                    myPrinter = PrinterFactory.dPrinter(PayActivity.this, maxNO, orderDto.getPayway());
                } else if (status == 0x02) {
                    Toast.makeText(this, "打印机缺纸", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status == 0x08) {
                    Toast.makeText(this, "打印机发热", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "打印机故障", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case URL.MODEL_DT92:
                mGpService = App.getmGpService();
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
                        return;
                    }
                }
                myPrinter = PrinterFactory.jbPrinter(mGpService, getApplicationContext(), maxNO, orderDto.getPayway());
                break;
            case URL.MODEL_T1:
                myPrinter = PrinterFactory.getSuMiPrinter(PayActivity.this, orderDto);
                break;
            case URL.MODEL_T2:
                myPrinter = PrinterFactory.getSuMiPrinter(PayActivity.this, orderDto);
                break;
            default:
                myPrinter = PrinterFactory.getSGrinter(PayActivity.this, orderDto);
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
//                SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//                int receipt_num = sharedPreferences.getInt("receipt_num", 1);
                for (int i = 0; i < 1; i++) {
                    if (i > 0) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (myPrinter != null) {
                        myPrinter.starPrint();
                    }

                }
            }
        }).start();
    }


    public void addOrder(List<OrderPaytype> paytypes, final String shishou) {

        final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);//
        final String cashier_name = SPHelper.getInstance().getString(Constant.CUSTOMER_name);
        final String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);

        /**
         * 给本地写入一条记录
         */
        orderDto = new OrderDto();
        orderDto.setOrder_info(goods);//商品信息
        orderDto.setPayway(new Gson().toJson(paytypes));
        orderDto.setStatus(paytypes.get(0).getPayCode());
        orderDto.setTime(GetData.getYYMMDDTime());
        orderDto.setSs_money(shishou);
        if (zhaoling.getText().toString().equals("")) {
            orderDto.setZl_money("0.0");
        } else {
            orderDto.setZl_money(zhaoling.getText().toString());
        }
        orderDto.setYs_money(yingshou.getText().toString());
        if (vipBeen == null) {
            orderDto.setVipNumber("");//会员信息
        } else {
            orderDto.setVipNumber(new Gson().toJson(vipBeen, VipBeen.class));//会员信息
        }
        if (customerBeen == null) {
            orderDto.setOut_trade_no("");//导购员信息
        } else {
            orderDto.setOut_trade_no(new Gson().toJson(customerBeen, CustomerBeen.class));//导购员信息
        }

        orderDto.setCashiername(cashier_name);
        orderDto.setShop_id(shopId);
        orderDto.setOrderType(orderType);
        orderDto.setMaxNo(maxNO);
        orderDto.setCheckoutTime(GetData.getDataTime());
        orderDto.setDetailTime(GetData.getHHmmssTimet());
        orderDto.setHasReturnGoods(URL.hasReturnGoods_No);

        orderDto.setPayReturn("");
        orderDto.setIfFinishOrder(URL.YES);
        orderDto.setOrderId(sid_m);
        orderDto.setUpLoadServer(UpLoadServer.noUpload);
        orderDto.setUpLoadERP(UploadERP.noUpload);
        orderDto.setMiYaNumber(union_orderid);


        //给本地添加一条记录
        if (mode.equals(Constant.LX)) {
            orderDto.setMode(Constant.LX);
        } else {
            try {
                new OrderDao(getApplicationContext()).addOrder(orderDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //调用打印
        print(orderDto);
        new LogUtil().save(Constant.LOG_SYJZ);
        new UpLoadToServel(App.getContext()).uploadOrderToServer(orderDto, payWayDto, "1", App.getContext(), UpdateOrder.findInfo);

        //进入支付成功界面
        String way = "";
        for (OrderPaytype paytype : paytypes) {
            way += paytype.getPayName() + "[" + paytype.getSs() + "]" + "   ";
        }
        sendIntent(way, orderDto.getSs_money(), orderDto.getZl_money());

    }


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


    public AlertDialog getCodeDialog;
    public String scanerCode;



    /**
     * 弹出对话框扫描出条形码
     */
    public String scanerCode(final String je) {

        @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(R.layout.scaner_code, null);
        getCodeDialog = new AlertDialog.Builder(this).setView(layout).create();
        getCodeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        getCodeDialog.show();
        final EditText et = (EditText) layout.findViewById(R.id.message);
        et.setInputType(InputType.TYPE_NULL);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
//        Button sure = (Button) layout.findViewById(R.id.positiveButton);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getCodeDialog.dismiss();
                if (payType.equals("hh")) {
                    requestHylodind(jfje,mzje);
                    zfCode = et.getText().toString().trim();
                } else {
                    unionPay(et.getText().toString().trim(), je, maxNO);
                }
                return true;
            }
        });
        return scanerCode;
    }


    public void sendIntent(String payWayString, String ss, String zl) {
        //核销优惠券
        hxYhq();
        Intent intent = new Intent(PayActivity.this, PaySuccessDialogActivity.class);
        intent.putExtra(PaySuccessDialogActivity.PUT_PAYWAY, payWayString);
        intent.putExtra(PaySuccessDialogActivity.PUT_YS, value);
        intent.putExtra(PaySuccessDialogActivity.PUT_ZL, zl);
        intent.putExtra(PaySuccessDialogActivity.PUT_SS, ss);
        intent.putExtra("sid", sid_m);
        intent.putExtra(PaySuccessDialogActivity.PUT_MAXNO, String.valueOf(maxNO));
        startActivity(intent);
        chooseWay = false;
        App.isAidl = true;
        AidlUtil.getInstance().openCash();
    }

    private void hxYhq() {
        if (couponsBeen != null) {
            Map<String, String> params = new HashMap<>();
            params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
            params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
            params.put("model", "vip.vvip");
            params.put("fun", "destroycoupon");
            params.put("vid", vipBeen.getId());
            params.put("no", couponsBeen.getNo());
            params.put("mid", SPHelper.getInstance().getString(Constant.STORE_ID));
            Log.i("desto", params.toString());
            new HttpConnection().postNet(params, new HttpCallBackListener() {
                @Override
                public void CallBack(String Response) {
                    Log.i("re",Response);
                }
            });
        }
    }


    //设置点击支付按钮间隔时间，防止多次提交订单
    public void disabledView(final View v) {
        v.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setClickable(true);
            }
        }, 800);
    }

    private DialogFragment yhqDialog;
    private CouponsBeen couponsBeen;

    private void getYhqDialog() {
        yhqDialog = new CircleDialog.Builder(this)
                .setBodyView(R.layout.yhq_dialog, new OnCreateBodyViewListener() {
                    @Override
                    public void onCreateBodyView(View view) {
                        ListView yhq_list = view.findViewById(R.id.yhq_list);

                        yhq_list.setAdapter(new YhqAdapter(couponsBeens, PayActivity.this));
                        yhq_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                couponsBeen = couponsBeens.get(position);
                                PayWayDto dto = new PayWayDto();
                                dto.setName(PayWay.YHQ);
                                dto.setPayType1(PayWay.YHQ_CODE);
                                startPay(dto);
                                yhqDialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(0.5f)
                .show();
    }

    private void closeYhq() {
        yhqDialog.dismiss();
    }
    private boolean checkSamePay(PayWayDto dto) {//检测是否存在相同支付方式
        for (OrderPaytype paytype : payList) {
            if (dto.getPayType1() == paytype.getPayCode()) {
                return true;
            }
        }
        return false;
    }


    //积分
    private DialogFragment jfDialog;

    private void getJfDialog() {
        jfDialog = new CircleDialog.Builder(this)
                .setText("积分余额：" + vipBeen.getJfdxje())
                .setTitle("使用积分抵扣")
                .setWidth(0.5f)
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Double.parseDouble(vipBeen.getJfye()) > 0) {
                            PayWayDto dto = new PayWayDto();
                            dto.setPayType1(PayWay.JF_CODE);
                            dto.setName(PayWay.JF);
                            startPay(dto);
                            chooseJF = true;
                        } else {
                            Toast.makeText(PayActivity.this, "积分余额不足", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegative("取消", null)
                .show();
    }
    private DialogFragment hykDialog;

    //会员卡
    private void getHykDialog() {
        hykDialog = new CircleDialog.Builder(this)
                .setText("面值余额：" + vipBeen.getMzye())
                .setTitle("使用卡余额抵扣")
                .setWidth(0.5f)
                .setPositive("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Double.parseDouble(vipBeen.getMzye()) > 0) {
                            PayWayDto dto = new PayWayDto();
                            dto.setPayType1(PayWay.MIANZHI_CARD);
                            dto.setName(PayWay.CZK);
                            startPay(dto);
                        } else {
                            Toast.makeText(PayActivity.this, "卡余额不足", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegative("取消", null)
                .show();
    }

    List<CouponsBeen> couponsBeens = new ArrayList<CouponsBeen>();

    private void searchYhq() {
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "vip.vvip");
        params.put("fun", "getusercoupon");
        params.put("vid", vipBeen.getId());
        new HttpConnection().postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("data", Response);
                try {
                    JSONObject object = new JSONObject(Response);
                    if (object.getString("errCode").equals("100")) {
                        JSONArray array = object.getJSONArray("result");
                        couponsBeens = new Gson().fromJson(array.toString(), new TypeToken<List<CouponsBeen>>() {
                        }.getType());
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void unionPay(String code,String money,String orderId){

        showPayDialog();
        int amount  = (int) (Double.parseDouble(money)*100);
        JSONObject object = new JSONObject();
        try {
            object.put("merchantCode", UnionConfig.merchantCode);
            object.put("terminalCode", UnionConfig.terminalCode);
            object.put("transactionAmount", amount);
            object.put("transactionCurrencyCode", "156");
            object.put("merchantOrderId",UnionConfig.merchantCode+GetData.getStringRandom(6)+orderId);
//            object.put("systemTraceNum", GetData.getHHmmssTime());
            object.put("merchantRemark", GetData.getStringRandom(10));
            object.put("payMode", "E_CASH");
            object.put("payCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("pay", object.toString());
        App.getInstance().getMyOkHttp().post()
                .url(UnionConfig.URL_PAY)
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
                                union_orderid = json.getString("orderId");
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
}