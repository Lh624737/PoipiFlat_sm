package com.pospi.pai.pospiflat.pay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miya.TcpSend;
import com.newland.jsums.paylib.NLPay;
import com.newland.jsums.paylib.model.ConsumeRequest;
import com.newland.jsums.paylib.model.NLBillInfo;
import com.newland.jsums.paylib.model.NLResult;
import com.newland.jsums.paylib.model.NetPayConsumeRequest;
import com.newland.jsums.paylib.model.NormalPrintRequest;
import com.newland.jsums.paylib.model.OrderInfo;
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
import com.pax.baselib.scanner.Scanner;
import com.pax.dal.IDAL;
import com.pax.dal.entity.EScannerType;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pospi.adapter.PaymentAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dao.OrderMenuDao;
import com.pospi.dao.OrderPaytypeDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dialog.MyDialog;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderMenu;
import com.pospi.dto.OrderPaytype;
import com.pospi.dto.PayWayDto;
import com.pospi.dto.Tablebeen;
import com.pospi.dto.ValueCardDto;
import com.pospi.gpprinter.GPprint;
import com.pospi.greendao.TablebeenDao;
import com.pospi.http.MaxNO;
import com.pospi.http.PayServer;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.PayBaseActivity;
import com.pospi.pai.pospiflat.util.ChuZhiKaDialogActivity;
import com.pospi.pai.pospiflat.util.LoadingDialog;
import com.pospi.pai.pospiflat.util.PaySuccessDialogActivity;
import com.pospi.paxprinter.PaxPrint;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyPrinter;
import com.pospi.util.PrinterFactory;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.UpLoadServer;
import com.pospi.util.UploadERP;
import com.pospi.util.Utils;
import com.pospi.util.WriteToSDcard;
import com.pospi.util.constant.Key;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;
import com.pospi.util.constant.tableinfo.TableStatusConstance;
import com.pospi.zqprinter.ZQPrint;

import org.apache.http.Header;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.android.preference.PreferencesUtils;

import static com.newland.jsums.paylib.utils.Key.RESULT_SUCCESS;

public class PayActivity extends PayBaseActivity implements View.OnClickListener {
    @Bind(R.id.lv_payment)
    ListView lvPayment;
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
    String chexiao;

    byte port = 0;

    private Dialog dialog;
//    private ScannerManager sm;
    private PaymentAdapter paymentAdapter;
    private List<PayWayDto> payWayDtos;
    //    private static MediaPlayer mp = new MediaPlayer();
//    private String payType;
    //    public static final int UPLOADSERVER = 111;
    public static final int SEND_REQUEST = 12345;
    private PayWayDto payWayDto;
    private String type;
    private ZQPrint zqPrint;
    private GpService mGpService = null;
    private boolean conn_state = false;
    private String out_trade_no = null;
    private String str_status;
    private String saasid = "1458393002";
    private String shopId_miya;
    private String jiju;
    private String opid;
    private String cashier_number;
    private double discount;
    private MyPrinter myPrinter;
    private int typeNumber;
    private int firstChoose;
    private List<OrderPaytype> payList = new ArrayList<>();
    /**
     * 米雅支付相关
     */
    private static final int PAYSUCCESS = 1;
    private static final int PAYFAIL = 0;
    private static final int PAYERROR = -1;
    //金额
    private String fee;
    //付款方式代号
    private int payWayCode;
    //付款方式名称
    private String payWayName;
    public static boolean isRun ;
    private LoadingDialog loadingDialog;
    private IDAL dal = null;


//    private String miYaNumber = "";
    /**
     * 处理微信，支付宝支付结果
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAYSUCCESS:
                    closePayDialog();

                    showFinshDialog(payWayName, payWayCode, shishou.getText().toString(), true, null, null);
                    print(shishou.getText().toString(), String.valueOf(payWayCode));
                    sendIntent(payWayName, shishou.getText().toString(), zhaoling.getText().toString());
                    paySuccess(payWayName);
                    break;
                case PAYFAIL:
                    closePayDialog();
                    Toast.makeText(PayActivity.this, "支付失败,请重新支付", Toast.LENGTH_SHORT).show();
                    break;
                case PAYERROR:
                    closePayDialog();
                    Toast.makeText(PayActivity.this, "支付异常", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("test", "onStart");
//        isRun = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("test", "onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test", "onCreate");
        setContentView(R.layout.pay);
        try {
            dal = NeptuneLiteUser.getInstance().getDal(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sid_m = GetData.getStringRandom();
        discount = getSharedPreferences("StoreMessage", MODE_PRIVATE).getFloat("Discount", 1);
        int whichOne = getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        cashier_number = new CashierLogin_pareseJson().parese(
                getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getNumber();
        isChecked = getSharedPreferences("receipt_num", Context.MODE_PRIVATE).getBoolean("scannerConfig", false);
        tablebeenDao = App.getInstance().getDaoSession().getTablebeenDao();

/**
 * 获取参数
 * 门店号
 * 机具号
 * 操作员
 */
        shopId_miya = getParam("pay_shopId");
        jiju = getParam("pay_jiju");
        opid = getParam("pay_opid");
        ButterKnife.bind(this);

        Intent intent = getIntent();
        value = intent.getStringExtra("money");
//        Log.i("value", value);
        orderType = intent.getIntExtra("orderType", URL.ORDERTYPE_SALE);
        tableNumber = intent.getStringExtra("tableNumber");
        tableId = intent.getStringExtra("tableId");
        maxNO = intent.getStringExtra("maxNo");
        Log.i("maxNo", maxNO + "");
//        Log.d("orderType", orderType + "");
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

    private void paySuccess(String payType) {

        Toast.makeText(PayActivity.this, payType + "已成功支付", Toast.LENGTH_SHORT).show();

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

    public void initWidgets() {
        detail = (TextView) findViewById(R.id.pay_detail);
        detail2 = (TextView) findViewById(R.id.pay_detail2);
        detail3 = (TextView) findViewById(R.id.pay_detail3);
        detail4 = (TextView) findViewById(R.id.pay_detail4);
        show = (TextView) findViewById(R.id.show);
        TextView zhenghao = (TextView) findViewById(R.id.zhenghao);
        zhenghao.setOnClickListener(this);

        TextView delete = (TextView) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        yingshou = (TextView) findViewById(R.id.pay_yingshou);
        shishou = (TextView) findViewById(R.id.pay_shihou);
        zhaoling = (TextView) findViewById(R.id.pay_zhaoling);

        ImageView qingkong = (ImageView) findViewById(R.id.qingkong);
        qingkong.setOnClickListener(this);

        LinearLayout reset = (LinearLayout) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

        PayWayDao payWayDao = new PayWayDao(PayActivity.this);
        payWayDtos = payWayDao.findAllPayWay();
        for (int i = 0; i < payWayDtos.size(); i++) {
//            Log.i("payWayDtos", "payWayDtos第一次删除时的元素Name：" + payWayDtos.get(i).getName());
            PayWayDto good1 = payWayDtos.get(i);
            String good_name1 = good1.getName();
            for (int j = payWayDtos.size() - 1; j > i; j--) {
                PayWayDto good2 = payWayDtos.get(j);
                String good_name2 = good2.getName();
                if (good_name1.equals(good_name2)) {
                    payWayDtos.remove(good2);
                }
            }
        }

        paymentAdapter = new PaymentAdapter(PayActivity.this, payWayDtos);
        lvPayment.setAdapter(paymentAdapter);
        Log.i(TAG, "payWayDtos: " + payWayDtos.size());
        if (payWayDtos.size() == 1) {
            int payType = payWayDtos.get(0).getPayType1();
            Log.i(TAG, "payType: " + payType);
            if (payType != PayWay.CASH) {
                chooseWay = true;
                switchPaytype(payType);
            }
        }
        lvPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                payWayDto = payWayDtos.get(position);
                int py = payWayDto.getPayType1();
//                if (payWayDto.getPayType1() == 1 || payWayDto.getPayType1() == 3 || payWayDto.getPayType1() == 5 || payWayDto.getPayType1() == 6) {
//                    chooseWay = false;
//                } else {
//                    chooseWay = true;
//                }
                if (typeNumber > 1) {
                    Toast.makeText(PayActivity.this, "已选择支付方式，请重置再操作", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (typeNumber) {
                    case 0:
                        firstChoose = py;
                        if (py == 1 || py == 3 || py == 5 || py == 6) {
                            if (show.getText().toString().equals("")) {
                                Toast.makeText(PayActivity.this, "该付款方式请先输入金额", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            detail.setText(payWayDto.getName());
                            double mo = Double.parseDouble(show.getText().toString());
                            double yi = Double.parseDouble(yingshou.getText().toString());
                            if (mo >= yi) {
                                if (py == 1) {
                                    detail2.setText(show.getText().toString());
                                } else {
                                    detail2.setText(yingshou.getText().toString());
                                }
                                shishou.setText(detail2.getText().toString());
                                double s = Double.parseDouble(shishou.getText().toString());

                                zhaoling.setText(DoubleSave.doubleSaveTwo(s - yi) + "");
                                whatPay = py;
                                typeNumber = 3;
                                Toast.makeText(PayActivity.this, "请支付", Toast.LENGTH_SHORT).show();
                            } else {
                                detail2.setText(show.getText().toString());
                                pays[0] = py;

                                typeNumber = 1;
                            }


//                            shishou.setText();

                        } else {
                            switchPaytype(py);
                            typeNumber = 3;
                        }

                        break;
                    case 1:
                        if (py == 1 || py == 3 || py == 5 || py == 6) {
                            if (show.getText().toString().equals("")) {
                                Toast.makeText(PayActivity.this, "该付款方式请先输入金额", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (firstChoose == py) {
                                Toast.makeText(PayActivity.this, "已选择该支付方式", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (py == 3) {
                                Toast.makeText(PayActivity.this, "礼券必须先选择", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (py == 5 || py == 6) {
                                detail3.setText(payWayDto.getName());
                                detail4.setText(DoubleSave.doubleSaveTwo(Double.parseDouble(yingshou.getText().toString()) - Double.parseDouble(detail2.getText().toString())) + "");

                            } else if (py == 1) {
                                detail3.setText(payWayDto.getName());
                                detail4.setText(show.getText().toString());
                            }
                            double y1 = Double.parseDouble(detail2.getText().toString());
                            double y2 = Double.parseDouble(detail4.getText().toString());

                            shishou.setText(DoubleSave.doubleSaveTwo(y1 + y2) + "");
                            if (py == 1) {
                                double s = DoubleSave.doubleSaveTwo(y1 + y2);
                                double y = Double.parseDouble(yingshou.getText().toString());
                                zhaoling.setText(DoubleSave.doubleSaveTwo(s - y) + "");
                            } else {
                                zhaoling.setText("0.0");
                            }
                            pays[1] = py;
                            typeNumber = 2;
                        } else {
                            Toast.makeText(PayActivity.this, "改付款方式不支持混合支付", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                Log.i("type", payWayDtos.get(position).getName());
//                if (chooseWay) {//如果选择了支付的类型   则点击没有效果
//                    showToast("已选择支付类型");
//                } else {//如果没有选择支付的类型，进入选择支付类型
////                  Log.i("payWayDtos", "onItemClick了");
//                    int payType = payWayDtos.get(position).getPayType1();
//                    Log.i("payType1", "已选择付款方式"+payType);
//                    switchPaytype(payType);
//                }
                show.setText("");
                test = "";
            }
        });
    }

    public void switchPaytype(int payType) {
        switch (payType) {
            case PayWay.CASH://现金
                if (!show.getText().toString().isEmpty()) {
                    if (Double.parseDouble(show.getText().toString()) >= Double.parseDouble(value)) {
                        whatPay = PayWay.CASH;
                        afterChoosePayWay(PayWay.XJ, show.getText().toString());
                        double zl = Double.parseDouble(shishou.getText().toString()) - Double.parseDouble(yingshou.getText().toString());
                        zl = DoubleSave.doubleSaveTwo(zl);
                        if (zl >= 0) {
                            zhaoling.setText(String.valueOf(zl));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "收款金额请大于应收金额", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入收款金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case PayWay.CREDIT_CARD://信用卡
                if (!show.getText().toString().isEmpty()) {
                    if (Double.parseDouble(show.getText().toString()) >= Double.parseDouble(value)) {
                        whatPay = PayWay.CREDIT_CARD;
                        afterChoosePayWay(PayWay.XJ, show.getText().toString());
                        double zl = Double.parseDouble(shishou.getText().toString()) - Double.parseDouble(yingshou.getText().toString());
                        zl = DoubleSave.doubleSaveTwo(zl);
                        if (zl >= 0) {
                            zhaoling.setText(String.valueOf(zl));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "收款金额请大于应收金额", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入收款金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case PayWay.OTHER://其他
                if (!show.getText().toString().isEmpty()) {
                    if (Double.parseDouble(show.getText().toString()) >= Double.parseDouble(value)) {
                        whatPay = PayWay.OTHER;
                        afterChoosePayWay(PayWay.OTH, show.getText().toString());
                        double zl = Double.parseDouble(shishou.getText().toString()) - Double.parseDouble(yingshou.getText().toString());
                        zl = DoubleSave.doubleSaveTwo(zl);
                        if (zl >= 0) {
                            zhaoling.setText(String.valueOf(zl));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "收款金额请大于应收金额", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入收款金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case PayWay.BANK_CARD://银行卡
//                            Log.i("payWayDtos", "点击银行卡了");
                if (orderType == URL.ORDERTYPE_SALE) {
                    whatPay = PayWay.BANK_CARD;
                    afterChoosePayWay(PayWay.YHK, value);
                } else {
                    showToast("仅支持现金！");
                }
                break;
            case PayWay.MIANZHI_CARD://储值卡
                if (orderType == URL.ORDERTYPE_SALE) {
                    whatPay = PayWay.MIANZHI_CARD;
                    afterChoosePayWay(PayWay.CZK, value);
                } else {
                    Intent intent = new Intent(PayActivity.this, ChuZhiKaDialogActivity.class);
                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(value));
                    intent.putExtra("orderNo", maxNO);
                    intent.putExtra("orderTime", GetData.getDataTime());
                    intent.putExtra("orderType", URL.ORDERTYPE_REFUND);
                    startActivityForResult(intent, 111);
                }
                break;
            case PayWay.WXPAY://微信
                if (orderType == URL.ORDERTYPE_SALE) {
                    whatPay = PayWay.WXPAY;
                    afterChoosePayWay(PayWay.WX, value);
                } else {
                    showToast("仅支持现金！");
                }
                break;
            case PayWay.ALIPAY://支付宝
                if (orderType == URL.ORDERTYPE_SALE) {
                    whatPay = PayWay.ALIPAY;
                    afterChoosePayWay(PayWay.ZFB, value);
                } else {
                    showToast("仅支持现金！");
                }
                break;
            case PayWay.E_COUPON://优惠券
                if (orderType == URL.ORDERTYPE_SALE) {
                    whatPay = PayWay.E_COUPON;
                    afterChoosePayWay(PayWay.YHQ, value);
                } else {
                    showToast("仅支持现金！");
                }
                break;
            default:
                showToast("暂不支持此支付方式！");
                break;
        }
    }

    /**
     * 点击事件的调用
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qingkong:
                show.setText("");
                test = "";
                break;
            case R.id.reset:
                detail.setText("");
                detail2.setText("");
                detail3.setText("");
                detail4.setText("");
                zhaoling.setText("");
                shishou.setText("");
                typeNumber = 0;
                chooseWay = false;
                break;
            case R.id.zhenghao:
                if (!"".equals(detail2.getText().toString().trim())) {
                    double p1 = Double.parseDouble(detail2.getText().toString().trim());
                    double p2 = Double.parseDouble(value);
                    show.setText(DoubleSave.doubleSaveTwo(p2 - p1) + "");
                } else {
                    show.setText(value);
                }

                break;
            case R.id.delete:
                show.setText(R.string.zero);
                test = "";
                break;
            case R.id.btn_pay:
//                clickPay();
//                if (!isRun) {
//                    isRun = true;
                disabledView(v);
                pay();
//                } else {
//                    Toast.makeText(this, "请勿重复提交订单", Toast.LENGTH_SHORT).show();
//                }

                break;
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
     * 点击支付之后会调用的方法
     */

    public void pay() {
        if (typeNumber == 1) {
            Toast.makeText(this, "支付金额不能小于商品价格", Toast.LENGTH_SHORT).show();
//            isRun = false;
            return;
        }
        //没有混合支付
        if (typeNumber == 3) {
            clickPay();
        } else if (typeNumber == 2) {
            double ys = Double.parseDouble(yingshou.getText().toString());
            double ss = Double.parseDouble(shishou.getText().toString());
            if (ys > ss) {
                Toast.makeText(this, "支付金额应大于应收金额", Toast.LENGTH_SHORT).show();
                return;
            }
            if (payList.size() != 0) {
                payList.clear();
            }
            OrderPaytype p1 = new OrderPaytype();
            p1.setPayName(detail.getText().toString());
            p1.setPayCode(pays[0]);
            OrderPaytype p2 = new OrderPaytype();
            p2.setPayName(detail3.getText().toString());
            p2.setPayCode(pays[1]);
            payList.add(p1);
            payList.add(p2);
            //有混合支付
            if (pays[0] == 6) {
                if (Build.MODEL.equals(URL.MODEL_D800)) {
                    Intent intent = new Intent(PayActivity.this, ChuZhiKaDialogActivity.class);
//                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(value));
                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(detail2.getText().toString()));
                    intent.putExtra("orderNo", maxNO);
                    intent.putExtra("discount", discount);
                    intent.putExtra("orderTime", GetData.getDataTime());
                    startActivityForResult(intent, 112);
                } else {
                    Toast.makeText(this, "不支持储值卡消费", Toast.LENGTH_SHORT).show();
                }
            } else if (pays[1] == 6) {
                if (Build.MODEL.equals(URL.MODEL_D800)) {
                    Intent intent = new Intent(PayActivity.this, ChuZhiKaDialogActivity.class);
//                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(value));
                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(detail4.getText().toString()));
                    intent.putExtra("orderNo", maxNO);
                    intent.putExtra("discount", discount);
                    intent.putExtra("orderTime", GetData.getDataTime());
                    startActivityForResult(intent, 112);
                } else {
                    Toast.makeText(this, "不支持储值卡消费", Toast.LENGTH_SHORT).show();
                }
            } else {

                showFinshDialog2(payList, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.CASH));
                sendIntent(payList.get(0).getPayName() + "," + payList.get(1).getPayName(), shishou.getText().toString(), zhaoling.getText().toString());
            }

        } else {
            Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickPay() {
        switch (whatPay) {
            case PayWay.CASH:
                if (Build.MODEL.equals(URL.MODEL_D800)) {
                    try {
                        BaseSystemManager baseSystemManager = BaseSystemManager.getInstance();
                        baseSystemManager.cashBoxPopup();
                    } catch (BaseSystemException e) {
                        e.printStackTrace();
                    }
                }

                //如果选择了现金支付 或者是没有选择支付方式，并且应收的钱大于需要支付的钱   直接进入现金支付的模式
                if (!Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
                    showFinshDialog(PayWay.XJ, PayWay.CASH, shishou.getText().toString(), true, null, null);
                    print(shishou.getText().toString(), String.valueOf(PayWay.CASH));
                    sendIntent(PayWay.XJ, shishou.getText().toString(), zhaoling.getText().toString());
                }

                break;
            case PayWay.CREDIT_CARD://信用卡

                showFinshDialog(PayWay.XJ, PayWay.CASH, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.CREDIT_CARD));
                sendIntent(PayWay.XJ, shishou.getText().toString(), zhaoling.getText().toString());
                break;
            case PayWay.OTHER://其他

                showFinshDialog(PayWay.OTH, PayWay.OTHER, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.OTHER));
                sendIntent(PayWay.OTH, shishou.getText().toString(), zhaoling.getText().toString());
                break;
            case PayWay.WXPAY:
//                judgeCarmer("wx", PayWay.WX);
                scanner(EScannerType.REAR);

//                scanerCode("wx", PayWay.WX, PayWay.WXPAY);
                break;
            case PayWay.ALIPAY:
                scanner(EScannerType.REAR);
//                judgeCarmer("ali", PayWay.ZFB);
//                scanerCode("ali", PayWay.ZFB, PayWay.ALIPAY);
                break;
            case PayWay.MIANZHI_CARD://储值卡
                if (Build.MODEL.equals(URL.MODEL_D800)) {
                    Intent intent = new Intent(PayActivity.this, ChuZhiKaDialogActivity.class);
                    intent.putExtra(ChuZhiKaDialogActivity.PAY_MONEY, Double.parseDouble(value));
                    intent.putExtra("orderNo", maxNO);
                    intent.putExtra("discount", discount);
                    intent.putExtra("orderTime", GetData.getDataTime());
                    startActivityForResult(intent, 111);
                } else {
                    Toast.makeText(this, "不支持储值卡消费", Toast.LENGTH_SHORT).show();
                }

                break;
            case PayWay.BANK_CARD://银行卡
                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
                    requestCardPay(value);
                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
                    NLPay pay = NLPay.getInstance();    //取一个支付实例
                    ConsumeRequest request = new ConsumeRequest();    //支付交易请求对象
                    request.setAppAccessKeyId(Key.APP_SECRET_KEY);    //设置appSecretKey
                    request.setMrchNo(Key.MRCH_NO);        //设置商户号
                    request.setCurrency(Key.CURRENCY);//设置货币代码
                    request.setAmount(value);            //设置金额
                    request.setExtOrderNo(String.valueOf(maxNO));//设置外部订单号
                    request.setExt01("");
                    request.setExt02("");
                    request.setExt03("");
                    request.setSignature(SignUtils.signData(Key.PRIVATE_KEY, request));//传入私钥生成签名数据,这个要放在最后
                    request.setTrmnlNo(Key.TRMNL_NO);
//		request.setExt05(ext05);
//		request.setExt06(ext06);
                    pay.consume(PayActivity.this, request);
                    Log.d("debug", request.toString());
                }
                break;
            case PayWay.E_COUPON:

                showFinshDialog(PayWay.YHQ, PayWay.E_COUPON, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.E_COUPON));
                sendIntent(PayWay.YHQ, shishou.getText().toString(), "0.0");
                break;
            case PayWay.CASH_GIFT:

                showFinshDialog(PayWay.LQ, PayWay.CASH_GIFT, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.CASH_GIFT));
                sendIntent(PayWay.LQ, shishou.getText().toString(), "0.0");
                break;
            case PayWay.SELL_ON_CREDIT:
                showFinshDialog(PayWay.SZ, PayWay.SELL_ON_CREDIT, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.SELL_ON_CREDIT));
                sendIntent(PayWay.SZ, shishou.getText().toString(), "0.0");
                break;

        }

    }

    /**
     * 调用打印的方法
     */
    public void print(final String shishou, String payway) {
        final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);
        switch (Build.MODEL) {
            case URL.MODEL_E500:
                myPrinter = PrinterFactory.getEPrinter(this, "123", "123");
                break;
            case URL.MODEL_D800:
                byte status = PrnTest.prnStatus();
                if (status == 0x00) {
                    myPrinter = PrinterFactory.dPrinter(PayActivity.this, maxNO, payway);
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
                myPrinter = PrinterFactory.jbPrinter(mGpService, getApplicationContext(), maxNO, payway);
                break;
            case URL.MODEL_T1:
                myPrinter = PrinterFactory.getSuMiPrinter(PayActivity.this, maxNO, payway);
                break;
        }
//        SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//        int receipt_num = sharedPreferences.getInt("receipt_num", 1);

//        for (int i = 0; i < receipt_num; i++) {
//            if (i > 0) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    myPrinter.starPrint(goods, shishou, null, true, valueCardDto, sid_m);
//                }
//            }).start();
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
                int receipt_num = sharedPreferences.getInt("receipt_num", 1);
                for (int i = 0; i < receipt_num; i++) {
                    if (i > 0) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myPrinter.starPrint(goods, shishou, null, true, valueCardDto, sid_m,tableId);
                }
            }
        }).start();
    }

    //    public void print(final String shishou, final String payway) {
//        final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_T8)) {
//                    try {
//                        zqPrint = new ZQPrint(getApplicationContext(), maxNO, payway);
//                        zqPrint.connect("USB0");
//                        conn_state = true;
//                        zqPrint.printext(goods, shishou, zhaoling.getText().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
//                    try {
//                        SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//                        int receipt_num = sharedPreferences.getInt("receipt_num", 1);
//                        Log.i("receipt_num", "print: " + receipt_num);
//
//                        byte status = PrnTest.prnStatus();
//                        switch (status) {
//                            case 0x00:
//                                str_status = "打印机状态为：打印机可用";
//                                if (payway.equals(String.valueOf(PayWay.MIANZHI_CARD))) {
//                                    new PaxPrint(PayActivity.this, maxNO, payway).print(goods, shishou, valueCardDto);
//                                    Thread.sleep(3000);
//                                    new PaxPrint(PayActivity.this, maxNO, payway).print(goods, shishou, valueCardDto);
//                                } else {
//                                    new PaxPrint(PayActivity.this, maxNO, payway).print(goods, shishou);
//                                    Thread.sleep(3000);
//                                    new PaxPrint(PayActivity.this, maxNO, payway).print(goods, shishou);
//                                }
//                                break;
//                            case 0x01:
//                                str_status = "打印机状态为：打印机忙";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x02:
//                                str_status = "打印机状态为：打印机缺纸";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x03:
//                                str_status = "打印机状态为：打印数据包格式错";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x04:
//                                str_status = "打印机状态为：打印机故障";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x08:
//                                str_status = "打印机状态为：打印机过热";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x09:
//                                str_status = "打印机状态为：打印机电压过低";
//                                showMsgToast(str_status);
//                                break;
//                            case (byte) 0xf0:
//                                str_status = "打印机状态为：打印未完成";
//                                showMsgToast(str_status);
//                                break;
//                            case (byte) 0xfc:
//                                str_status = "打印机状态为：打印机未装字库";
//                                showMsgToast(str_status);
//                                break;
//                            case (byte) 0xfe:
//                                str_status = "打印机状态为：数据包过长";
//                                showMsgToast(str_status);
//                                break;
//                            case 97:
//                                str_status = "打印机状态为：不支持的字符集";
//                                showMsgToast(str_status);
//                                break;
//                            case 98:
//                                str_status = "打印机状态为：参数为空";
//                                showMsgToast(str_status);
//                                break;
//                            case 99:
//                                str_status = "打印机状态为：RPC连接错误";
//                                showMsgToast(str_status);
//                                break;
//                            default:
//                                str_status = "打印机状态为：其它错误";
//                                showMsgToast(str_status);
//                                break;
//                        }
//                    } catch (Exception e) {
//                        byte status = PrnTest.prnStatus();
//                        switch (status) {
//                            case 0x00:
//                                str_status = "打印机状态为：打印机可用";
//                                break;
//                            case 0x01:
//                                str_status = "打印机状态为：打印机忙";
//                                showMsgToast(str_status);
//                                break;
//                            case 0x02:
//                                str_status = "打印机状态为：打印机缺纸";
//                                break;
//                            case 0x03:
//                                str_status = "打印机状态为：打印数据包格式错";
//                                break;
//                            case 0x04:
//                                str_status = "打印机状态为：打印机故障";
//                                break;
//                            case 0x08:
//                                str_status = "打印机状态为：打印机过热";
//                                break;
//                            case 0x09:
//                                str_status = "打印机状态为：打印机电压过低";
//                                break;
//                            case (byte) 0xf0:
//                                str_status = "打印机状态为：打印未完成";
//                                break;
//                            case (byte) 0xfc:
//                                str_status = "打印机状态为：打印机未装字库";
//                                break;
//                            case (byte) 0xfe:
//                                str_status = "打印机状态为：数据包过长";
//                                break;
//                            case 97:
//                                str_status = "打印机状态为：不支持的字符集";
//                                break;
//                            case 98:
//                                str_status = "打印机状态为：参数为空";
//                                break;
//                            case 99:
//                                str_status = "打印机状态为：RPC连接错误";
//                                break;
//                            default:
//                                str_status = "打印机状态为：其它错误";
//                                break;
//                        }
//                        showMsgToast(str_status);
//                        e.printStackTrace();
//                    }
//                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_DT92)) {
//                    mGpService = App.getmGpService();
//                    if (mGpService == null) {
//                        showToast("未连接到本地打印机");
//                        Utils.connection();
//                        mGpService = App.getmGpService();
//                        try {
//                            mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
//                        } catch (RemoteException e) {
//                            String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
//                            gPrint_log += GetData.getDataTime() + " 连接打印机端口时异常 " + e.getMessage() + "; ";
//                            PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
//                            e.printStackTrace();
//                        }
//                    }
//                    try {
////                        Map<String, String> params = new HashMap<>();
////                        params.put("system", "0427");
////                        params.put("seller", "家乐福");
////                        params.put("time", "20160826100455");
////                        params.put("price", "1000.00");
////                        params.put("id", GetData.getStringRandom(32));
////                        params.put("msg", "");
////                        String md5 = Utils.getSign(params) + "&key=" + "8934e7d15453e97507ef794cf7b0519d";
////                        String qrCode = "http://www.subinwechat.com/p/ParkingCounponAuthorize.html?system=" +
////                                params.get("system") + "&seller=" +
////                                params.get("seller") + "&time=" +
////                                params.get("time") + "&price=" +
////                                params.get("price") + "&id=" +
////                                params.get("id") + "&msg=" +
////                                params.get("msg") + "&sign=" + Utils.md5Encrypt(md5).toUpperCase();
////                        Log.i("qrCode", "qrCode: "+qrCode);
////                        String qrCode = "123456789012345678901234567890" + "123456789012345678901234567890" + "123456789012345678901234567890" + "123456789012345678901234567890" + "123456789012345678901234567890";
//                        GPprint gPprint = new GPprint(mGpService, getApplicationContext(), maxNO, payway);
//                        SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//                        int receipt_num = sharedPreferences.getInt("receipt_num", 1);
//
//                        for (int i = 0; i < receipt_num; i++) {
//                            if (i > 0) {
//                                try {
//                                    Thread.sleep(2000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            gPprint.print(goods, shishou, null, true);//Utils.generateBitmap(qrCode, 300, 300)
//                        }
////                        gPprint.printQrCode(Utils.generateBitmap(qrCode, 300, 300));
//                    } catch (Exception e) {
//                        String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
//                        gPrint_log += GetData.getDataTime() + " 调用打印方法时异常 " + e.getMessage() + "; ";
//                        PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
//                        e.printStackTrace();
//                    }
//                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
//                    try {
//                        NLPay pay = NLPay.getInstance();
//                        NormalPrintRequest request = new NormalPrintRequest();
//                        request.setAppAccessKeyId(Key.APP_SECRET_KEY);
//                        request.setMrchNo(Key.MRCH_NO);
//                        ArrayList<String> lists = new ArrayList<String>();
//                        JSONObject content2 = new JSONObject();
//                        content2.put("type", "0");
//                        content2.put("content", Utils.getContent(PayActivity.this, maxNO, payway, shishou));
//                        lists.add(content2.toString());
//                        if (whatPay == PayWay.CASH || whatPay == PayWay.OTHER) {
//                            Utils.cashopen();
//                        }
//                        request.setContents(lists);
//                        pay.normalPrint(PayActivity.this, request);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//        if (str_status != null) {
//            showToast(str_status);
//        }
//    }
    public void handleRslt(NLResult rslt) {
        boolean success = false;
        String resultMsg = "";
        StringBuilder sb = new StringBuilder();
        sb.append("返回码：" + rslt.getResultCode() + " \n");
        if (rslt.getResultCode() == 6000 && rslt.getData() != null) {
            //有订单信息返回的时候需对期进行验签，防止数据被篡改
            ResultData data = rslt.getData();
            if (SignUtils.verifySignData(Key.PUBLIC_KEY, data, rslt.getSignData())) {
                //验签成功
                sb.append("订单信息：" + data.toString() + "\n");
                resultMsg = sb.toString();
                success = true;
                showBillInfo((NLBillInfo) data);
            } else {
                //验签失败
                resultMsg = "验签失败";
                showError(resultMsg);
            }
        } else {
            //调用失败
            sb.append("错误信息：" + rslt.getResultMsg() + "\n");
            resultMsg = sb.toString();
            showError(resultMsg);
        }
    }

    private void showError(String errMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
        builder.setMessage(errMsg)
                .setTitle("调用结果")
                .setPositiveButton("确定", null)
                .create()
                .show();
    }

    private void showBillInfo(final NLBillInfo billInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
        builder.setMessage(billInfo.toString())
                .setTitle("调用结果")
                .setPositiveButton("打印", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String appAccessKeyId = Key.APP_SECRET_KEY;
                        String mrchNo = Key.MRCH_NO;
                        Gson gson = new Gson();
                        String billInfoJson = gson.toJson(billInfo);
                        PrintRequest request = new PrintRequest(billInfoJson, "我是一段附加信息>>>");
                        request.setAppAccessKeyId(appAccessKeyId);
                        request.setMrchNo(mrchNo);
                        NLPay.getInstance().print(PayActivity.this, request);
                    }
                })
                .create()
                .show();
    }

    public void showResult(NLResult rslt) {
        showResult(rslt, true);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(resultMsg).setTitle("调用结果")
                .setPositiveButton("确定", null).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //111---存储卡
        if (requestCode == 111) {
            if (resultCode == 110) {
//                valueCardDto = new ValueCardDto();
                valueCardDto = (ValueCardDto) data.getSerializableExtra(PaySuccessDialogActivity.CARD_INFO);


                showFinshDialog(PayWay.CZK, PayWay.MIANZHI_CARD, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.MIANZHI_CARD));
                sendIntent(PayWay.CZK, value, "0.0");
            }
        } else if (requestCode == 112) {
            //混合储值卡支付
            if (resultCode == 110) {
//                valueCardDto = new ValueCardDto();
                valueCardDto = (ValueCardDto) data.getSerializableExtra(PaySuccessDialogActivity.CARD_INFO);
                showFinshDialog2(payList, shishou.getText().toString(), true, null, null);
                print(shishou.getText().toString(), String.valueOf(PayWay.MIANZHI_CARD));
                sendIntent(PayWay.CZK, value, "0.0");
            }
        }
        if (requestCode == SEND_REQUEST) {
            if (resultCode == RESULT_OK) {
                print(shishou.getText().toString(), String.valueOf(PayWay.BANK_CARD));
                showFinshDialog(PayWay.YHK, PayWay.BANK_CARD, shishou.getText().toString(), true, null, null);
                sendIntent(PayWay.YHK, value, "0.0");
            }
            if (resultCode == RESULT_CANCELED) {
                showToast("支付取消！");
            }
        }
        if (resultCode == RESULT_SUCCESS) {
            switch (requestCode) {
                case com.newland.jsums.paylib.utils.Key.REQUEST_CONSUME:
                    if (data != null) {
                        NLResult rslt = (NLResult) data
                                .getParcelableExtra("result");
                        if (rslt.getResultCode() == 6000) {
                            showFinshDialog(PayWay.YHK, PayWay.BANK_CARD, shishou.getText().toString(), true, null, null);
                            sendIntent(PayWay.YHK, shishou.getText().toString(), "0.0");
                        }
                    }
                    break;
                case com.newland.jsums.paylib.utils.Key.REQUEST_WECHATCONSUME:
                    break;
                case com.newland.jsums.paylib.utils.Key.REQUEST_PRINT:
                    if (data != null) {
                        NLResult rslt = (NLResult) data
                                .getParcelableExtra("result");
                        if (rslt.getResultCode() == 6000) {
                            showFinshDialog(PayWay.XJ, PayWay.CASH, shishou.getText().toString(), true, null, null);
                            sendIntent(PayWay.XJ, shishou.getText().toString(), zhaoling.getText().toString());
                        }
                    }
                    break;
                case com.newland.jsums.paylib.utils.Key.REQUEST_NETPAYCONSUME:
                    if (data != null) {
                        NLResult rslt = (NLResult) data
                                .getParcelableExtra("result");
                        if (rslt.getResultCode() == 6000) {
                            switch (whatPay) {
                                case PayWay.WXPAY:
                                    showFinshDialog(PayWay.WX, PayWay.WXPAY, shishou.getText().toString(), true, null, null);
                                    sendIntent(PayWay.WX, value, "0.0");
                                    break;
                                case PayWay.ALIPAY:
                                    showFinshDialog(PayWay.ZFB, PayWay.ALIPAY, shishou.getText().toString(), true, null, null);
                                    sendIntent(PayWay.ZFB, value, "0.0");
                                    break;
                            }
                        }
                    }

                    break;
            }
        }
//        if (resultCode == com.newland.jsums.paylib.utils.Key.RESULT_SUCCESS) {
//            closeDialog();
//            if (data != null) {
//                NLResult rslt = (NLResult) data
//                        .getParcelableExtra("result");
//                showError(rslt.getResultCode() + "\n" + rslt.getResultMsg());
//            }
//        }
    }


    /**
     * 选择支付方式之后的一系列反应
     *
     * @param payname 支付方式的名字
     * @param money   支付的金额
     */

    public void afterChoosePayWay(String payname, String money) {
        detail.setText(payname);
        detail2.setText(money);
        shishou.setText(money);
//        chooseWay = true;
    }

    public void showFinshDialog(final String payWay, int status, final String shishou,
                                boolean success, String reason, final String aliPayNumber) {
        try {
            final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);//
            int whichOne = getSharedPreferences("islogin", MODE_PRIVATE).getInt("which", 0);
            final String cashier_name = new CashierLogin_pareseJson().parese(
                    getSharedPreferences("cashierMsgDtos", MODE_PRIVATE)
                            .getString("cashierMsgDtos", ""))
                    .get(whichOne).getName();

            final String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");

            /**
             * 给本地写入一条记录
             */
//        UUID uuid = UUID.randomUUID();
//        String sid = uuid.toString();
            String paySid = GetData.getStringRandom(32);
//        String sid = GetData.getStringRandom(32);
            final OrderDto orderDto = new OrderDto();
            orderDto.setOrder_info(goods);
            orderDto.setPayway(payWay);
            orderDto.setStatus(status);
            Log.i("payType1", "订单存储的付款方式" + status);
            orderDto.setTime(GetData.getYYMMDDTime());
            orderDto.setSs_money(shishou);
            orderDto.setYs_money(yingshou.getText().toString());
            if (zhaoling.getText().toString().equals("")) {
                orderDto.setZl_money("0.0");
            } else {
                orderDto.setZl_money(zhaoling.getText().toString());
            }
//        orderDto.setZl_money(zhaoling.getText().toString());
            orderDto.setCashiername(cashier_name);
            orderDto.setShop_id(shopId);
            orderDto.setOrderType(orderType);
            orderDto.setMaxNo(maxNO);
            orderDto.setCheckoutTime(GetData.getDataTime());
            orderDto.setDetailTime(GetData.getHHmmssTimet());
            orderDto.setHasReturnGoods(URL.hasReturnGoods_No);
            orderDto.setOut_trade_no(out_trade_no);
            orderDto.setPayReturn(aliPayNumber);
            orderDto.setIfFinishOrder(URL.YES);
            orderDto.setOrderId(sid_m);
            orderDto.setUpLoadServer(UpLoadServer.noUpload);
            orderDto.setUpLoadERP(UploadERP.noUpload);
            //生成订单的付款方式的sid
            orderDto.setOrderSid(paySid);
            orderDto.setTableId(tableId);
            orderDto.setTableNumber(tableNumber);
//        orderDto.setOrderSid(sid);
            //上次订单数据
//        OrderInfo lastOrderInfo = App.getLastOrderInfo();
//        String orderNo = "";
//        if (lastOrderInfo != null) {
//            orderNo = lastOrderInfo.getOrderNo();
//            orderDto.setOrderNo(orderNo);
//        }
            //订单详情
            List<GoodsDto> goodsList = Sava_list_To_Json.changeToList(goods);
            for (GoodsDto gd :
                    goodsList) {
                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setSid(GetData.getStringRandom(32));
                orderMenu.setName(gd.getName());
                orderMenu.setNumber(gd.getNum());
                orderMenu.setPrice(gd.getPrice());
                orderMenu.setDiscount(gd.getDiscount());
                orderMenu.setOrderSid(sid_m);
                new OrderMenuDao(getApplicationContext()).addMenu(orderMenu);
            }
            //付款方式
            OrderPaytype orderPaytype = new OrderPaytype();
            orderPaytype.setSid(GetData.getStringRandom(32));
            orderPaytype.setPayName(payWay);
            orderPaytype.setPayCode(status);
            orderPaytype.setOrderSid(sid_m);
            orderPaytype.setSs(shishou);
            orderPaytype.setYs(yingshou.getText().toString());
            if (zhaoling.getText().toString().equals("")) {
                orderPaytype.setZl("0.0");
            } else {
                orderPaytype.setZl(zhaoling.getText().toString());
            }
            new OrderPaytypeDao(getApplicationContext()).addPaytype(orderPaytype);

            //给本地添加一条记录
//测试撤销，抛异常
//            chexiao.equals("");
            new OrderDao(getApplicationContext()).addOrder(orderDto);
            //检查是否存在桌号信息
            if (!tableId.isEmpty()) {
                updateTableMsg(tableId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //撤销米雅
            new Thread(new Runnable() {
                @Override
                public void run() {
                    abandon();
                }
            }).start();
        }
//        List<PrintDto> printDtos = new PrintDao(getApplicationContext()).findAllPrinter();
//        for (int i = 0; i < printDtos.size(); i++) {
//            PrintDto printDto = printDtos.get(i);
//            Log.i("printDto.getIp()", printDto.getIp() + i + printDto.getPort());
//            PrintTest.connevt(printDto.getIp(), printDto.getPort(), Sava_list_To_Json.changeToList(goods), orderDto);
//        }
//        UpLoadToServel upLoadToServel = new UpLoadToServel(getApplicationContext());
//
//        upLoadToServel.uploadOrderToServer(orderDto, payWayDto, type, getApplication(), UpdateOrder.normal);

//        new UpLoadToServel(getApplicationContext()).SendErpServer(orderDto,payWay,true);

        //上传卡流水
//        if (payWay.equals(PayWay.CZK)) {
//            if (orderType == URL.ORDERTYPE_REFUND) {
//                upLoadToServel.upCardRefund(payWayDto.getSid(), orderDto, valueCardDto, orderType, -Double.parseDouble(yingshou.getText().toString()));
//            } else {
//                upLoadToServel.upCardRecord(new PayWayDao(getApplicationContext()).findPaySid("6"), orderDto, valueCardDto, 1111);
//            }
//        }

    }
    private void updateTableMsg(String sid){
        Tablebeen t = tablebeenDao.queryBuilder().where(TablebeenDao.Properties.TId.eq(sid)).unique();
        if (t != null) {
            t.setStatus(TableStatusConstance.Status_Free);
            tablebeenDao.update(t);
        }
    }

    public void showFinshDialog2(List<OrderPaytype> paytypes, final String shishou,
                                 boolean success, String reason, final String aliPayNumber) {

        final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);//
        int whichOne = getSharedPreferences("islogin", MODE_PRIVATE).getInt("which", 0);
        final String cashier_name = new CashierLogin_pareseJson().parese(
                getSharedPreferences("cashierMsgDtos", MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName();

        final String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");

        /**
         * 给本地写入一条记录
         */
//        UUID uuid = UUID.randomUUID();
//        String sid = uuid.toString();
        String paySid = GetData.getStringRandom(32);

        final OrderDto orderDto = new OrderDto();
        orderDto.setOrder_info(goods);
        orderDto.setPayway(paytypes.get(0).getPayName());
        orderDto.setStatus(paytypes.get(0).getPayCode());
        orderDto.setTime(GetData.getYYMMDDTime());
        orderDto.setSs_money(shishou);
        if (zhaoling.getText().toString().equals("")) {
            orderDto.setZl_money("0.0");
        } else {
            orderDto.setZl_money(zhaoling.getText().toString());
        }
        orderDto.setYs_money(yingshou.getText().toString());

        orderDto.setCashiername(cashier_name);
        orderDto.setShop_id(shopId);
        orderDto.setOrderType(orderType);
        orderDto.setMaxNo(maxNO);
        orderDto.setCheckoutTime(GetData.getDataTime());
        orderDto.setDetailTime(GetData.getHHmmssTimet());
        orderDto.setHasReturnGoods(URL.hasReturnGoods_No);
        orderDto.setOut_trade_no(out_trade_no);
        orderDto.setPayReturn(aliPayNumber);
        orderDto.setIfFinishOrder(URL.YES);
        orderDto.setOrderId(sid_m);
        orderDto.setUpLoadServer(UpLoadServer.noUpload);
        orderDto.setUpLoadERP(UploadERP.noUpload);
        //生成订单的付款方式的sid
        orderDto.setOrderSid(paySid);
//        orderDto.setOrderSid(sid);
        //上次订单数据
//        OrderInfo lastOrderInfo = App.getLastOrderInfo();
//        String orderNo = "";
//        if (lastOrderInfo != null) {
//            orderNo = lastOrderInfo.getOrderNo();
//            orderDto.setOrderNo(orderNo);
//        }
        List<GoodsDto> goodsList = Sava_list_To_Json.changeToList(goods);
        for (GoodsDto gd :
                goodsList) {
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setSid(GetData.getStringRandom(32));
            orderMenu.setName(gd.getName());
            orderMenu.setNumber(gd.getNum());
            orderMenu.setPrice(gd.getPrice());
            orderMenu.setDiscount(gd.getDiscount());
            orderMenu.setOrderSid(sid_m);
            new OrderMenuDao(getApplicationContext()).addMenu(orderMenu);
        }
        for (int i = 0; i < paytypes.size(); i++) {
            OrderPaytype orderPaytype = new OrderPaytype();
            orderPaytype.setSid(GetData.getStringRandom(32));
            orderPaytype.setPayName(paytypes.get(i).getPayName());
            orderPaytype.setPayCode(paytypes.get(i).getPayCode());
            if (i == 0) {
                orderPaytype.setYs(detail2.getText().toString());
                orderPaytype.setSs(detail2.getText().toString());
                orderPaytype.setZl("0.0");
            } else {
                double y1 = Double.parseDouble(detail2.getText().toString());
                double y2 = Double.parseDouble(yingshou.getText().toString());
                orderPaytype.setYs(DoubleSave.doubleSaveTwo(y2 - y1) + "");
                orderPaytype.setSs(detail4.getText().toString());
                if (zhaoling.getText().toString().equals("")) {
                    orderPaytype.setZl("0.0");
                } else {
                    orderPaytype.setZl(zhaoling.getText().toString());
                }

            }
            Log.i("pay", orderPaytype.getPayName() + "--" + orderPaytype.getPayCode() + "--" + orderPaytype.getYs() + "--" + orderPaytype.getSs() + "--" + orderPaytype.getZl());
            orderPaytype.setOrderSid(sid_m);
            new OrderPaytypeDao(getApplicationContext()).addPaytype(orderPaytype);
        }
        //给本地添加一条记录
        try {

            new OrderDao(getApplicationContext()).addOrder(orderDto);
        } catch (Exception e) {
            e.printStackTrace();


        }
//        List<PrintDto> printDtos = new PrintDao(getApplicationContext()).findAllPrinter();
//        for (int i = 0; i < printDtos.size(); i++) {
//            PrintDto printDto = printDtos.get(i);
//            Log.i("printDto.getIp()", printDto.getIp() + i + printDto.getPort());
//            PrintTest.connevt(printDto.getIp(), printDto.getPort(), Sava_list_To_Json.changeToList(goods), orderDto);
//        }
//        UpLoadToServel upLoadToServel = new UpLoadToServel(getApplicationContext());
//
//        upLoadToServel.uploadOrderToServer(orderDto, payWayDto, type, getApplication(), UpdateOrder.normal);

//        new UpLoadToServel(getApplicationContext()).SendErpServer(orderDto,payWay,true);

        //上传卡流水
//        if (payWay.equals(PayWay.CZK)) {
//            if (orderType == URL.ORDERTYPE_REFUND) {
//                upLoadToServel.upCardRefund(payWayDto.getSid(), orderDto, valueCardDto, orderType, -Double.parseDouble(yingshou.getText().toString()));
//            } else {
//                upLoadToServel.upCardRecord(new PayWayDao(getApplicationContext()).findPaySid("6"), orderDto, valueCardDto, 1111);
//            }
//        }

    }

    /**
     * 请求支付
     *
     * @param barcode 条形码
     * @param way     传送给服务器的支付方式
     * @param name    支付成功后告诉用户的支付方式
     */
    public void requestPay(String barcode, final String way, final String name,
                           final int status) {
        //得到现在进行请求的是支付宝还是微信
        String host = "";
        try {
            host = this.getSharedPreferences("url", Context.MODE_PRIVATE).getString("url", "http://pos.pospi.com");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        String aa = preferences.getString("value", "");
//        String aa = "245148d996d5404c9d37a4cccd2e791b,f17851cbccb84ce0b55ac6f464d9e13f,100148,183,98";

        String[] names = aa.split("\\,");
        String deviceNo = names[2];//收银机号
        String user_Id = names[3];//用户ID
        String ServerNo = names[4];//服务器号
        String uid = names[1];

        String now = GetData.getHHmmssTime();
//        Log.i("获取到的当前时间", now);
//        out_trade_no = String.valueOf(maxNO) + deviceNo + now + user_Id + ServerNo;//交易号
        out_trade_no = maxNO;
        if (!Build.MODEL.equalsIgnoreCase(URL.MODEL_IPOS100)) {
            showDialog("正在请求支付，请等待");
//        Log.i("out_trade_no", out_trade_no);
            RequestParams payParamsMaps = new RequestParams();
            payParamsMaps.put("auth_code", barcode);//扫描到的条形码数据
            payParamsMaps.put("body", "统销商品自定义价格");//死数据
            payParamsMaps.put("pay_mode", way);//支付方式,支付宝或者是微信
            payParamsMaps.put("fee", getPayMoney(way));//订单的金额
            payParamsMaps.put("out_trade_no", out_trade_no);//交易号
            payParamsMaps.put("uid", uid);//用户的id

//        Log.i("onSuccess", "payParamsMaps: " + payParamsMaps.toString());
            AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Log.i("onSuccess", new String(bytes));
                    LoginReturnDto returnDto = new LoginReturnDto();

                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getString("Value")));
                        if (returnDto.getResult() == 1) {
                            print(shishou.getText().toString(), String.valueOf(status));
                            showFinshDialog(name, status, shishou.getText().toString(), true, null, returnDto.getValue());
                            sendIntent(name, value, "0.0");
                        } else {
                            payFailDialog(returnDto.getMessage(), name, status);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("连接异常，请稍后再试");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    LoginReturnDto returnDto = new LoginReturnDto();
                    showToast("支付失败，请检查网络设置");
                    try {
                        Log.i("onFailure", new String(bytes));
                        JSONObject object = new JSONObject(new String(bytes));
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getInt("Value")));
                        showToast(returnDto.getMessage());
//                    showFinshDialog(name, shishou.getText().toString(), false, returnDto.getMessage(), returnDto.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    closeDialog();
                }
            };
            String url = new URL().PAYMENT;
            new PayServer().getConnect(getApplicationContext(), url, payParamsMaps, handler);
//        Log.i("url", url);
        } else {
            //TODO 微信支付宝消费示例
            NLPay pay = NLPay.getInstance();//取一个支付实例
            NetPayConsumeRequest request = new NetPayConsumeRequest(); // 支付交易请求对象
            request.setAppAccessKeyId(Key.APP_SECRET_KEY); // 设置appSecretKey
            request.setMrchNo(Key.MRCH_NO); // 设置商户号
            request.setCurrency(Key.CURRENCY); // 设置货币代码
            request.setAmount(String.valueOf(0.01)); // 设置金额
            request.setExtOrderNo(String.valueOf(maxNO)); // 设置外部订单号
            request.setExt01("");
            request.setExt02("");
            request.setExt03("");
            request.setSignature(SignUtils.signData(Key.PRIVATE_KEY, request));// 传入私钥生成签名数据,这个要放在最后
            request.setMrchOrderNo(barcode);
            Log.i("mrchOrderNo", "mrchOrderNo: " + barcode);

            pay.netpayconsume(PayActivity.this, request);
            Log.e("request", request.toString());
//            switch (way) {
//                case "wx":
//                    pay.wechatconsume(PayActivity.this, request);
//                    break;
//                case "ali":
//                    pay.alpayConsume(PayActivity.this, request);
//                    break;
//            }
        }
    }

    public void payFailDialog(String message, final String name, final int status) {
        final AlertDialog payFailDialog = new AlertDialog.Builder(this)
                .setTitle("支付失败提示")
                .setMessage("返回信息：" + message + "\n请手动确认支付是否成功")
                .setPositiveButton("支付成功", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showFinshDialog(name, status, shishou.getText().toString(), true, null, "noCode");
                        sendIntent(name, value, "0.0");
                    }
                }).setNegativeButton("支付失败", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("请重新扫码进行支付");
                    }
                })
                .create();
        payFailDialog.show();

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

    public String getPayMoney(String way) {
        String payMoney = null;

        if (way.equals("wx")) {
            payMoney = String.valueOf((int) (Double.parseDouble(shishou.getText().toString()) * 100));
        } else if (way.equals("ali")) {
            payMoney = shishou.getText().toString();
        }
        return payMoney;
    }

    public AlertDialog getCodeDialog;
    public String scanerCode;

    //开启扫码
    private void beginScan(final EditText et) {

        tag = true;

        try {
            pm = PortManager.getInstance();
            pm.portOpen(port, "9600,8,n,1");
            Log.e(TAG, "串口打开成功");

        } catch (PortException e) {
            e.printStackTrace();
            Log.e(TAG, "串口打开失败");
            return;
        }
        while (tag) {
            try {
                final byte[] result = PortManager.getInstance().portRecvs(port, 18, 500);
                if (result.length != 0) {
                    tag = false;
                    pm.portClose(port);
                    pm = null;
                    Log.i(TAG, "scanerCode: " + byteToString(result));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et.setText(byteToString(result));
                        }
                    });
                    return;
                }
            } catch (PortException e) {
                e.printStackTrace();
                Log.e(TAG, "扫码失败");
            }
        }
    }

    //将读取到的byte[]数组转换成String
    public String byteToString(byte[] b) {
        Log.i("cardno", "byteToString: " + b);
        char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[((b[i] & 0xF0) >>> 4)]);
            sb.append(HEX_DIGITS[(b[i] & 0xF)]);
        }
        String str = sb.toString();
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                if (!str.substring(i, i + 2).equals("00")) {
                    str2 = str2 + (char) Integer.parseInt(str.substring(i, i + 2), 16);
                } else {
                    break;
                }
            }
        }
        Log.i("cardno2", "byteToString: " + str2);
        return str2;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("on", "执行onResume");
        tag = false;
        if (pm != null) {
            try {
                pm.portClose(port);
            } catch (PortException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 弹出对话框扫描出条形码
     */
    public String scanerCode(final String way, final String wayName, final int status) {
        //设置支付按钮可点击
//        isRun = false;

        @SuppressLint("InflateParams") View layout = getLayoutInflater().inflate(R.layout.scaner_code, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//设置软键盘不弹
        getCodeDialog = new AlertDialog.Builder(this).setView(layout).create();
        getCodeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.i("test", "onCancel");
                tag = false;
                if (pm != null) {
                    try {
                        pm.portClose(port);
                        Log.i("test", "端口关闭成功");
                    } catch (PortException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getCodeDialog.show();

        final EditText et = (EditText) layout.findViewById(R.id.message);
        Button sure = (Button) layout.findViewById(R.id.positiveButton);
        ImageView cancle = (ImageView) layout.findViewById(R.id.negativeButton);

        //扫码枪
        if (isChecked) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    beginScan(et);
                }
            }).start();
        }


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                showDialog("正在请求支付，请等待");
                //点击确认键，关闭串口
                tag = false;
                if (pm != null) {
                    try {
                        pm.portClose(port);
                        Log.i("port", "端口关闭成功");
                    } catch (PortException e) {
                        e.printStackTrace();
                    }
                }
                scanerCode = et.getText().toString().trim();
                if (!scanerCode.isEmpty()) {

                    getCodeDialog.dismiss();
                    //requestPay(scanerCode, way, wayName, status);
                    if (shopId_miya.equals("") || jiju.equals("") || opid.equals("")) {
//                        closeDialog();
                        Toast.makeText(PayActivity.this, "配置参数不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showPayDialog();
                    //付款金额
                    fee = String.valueOf(Double.parseDouble(shishou.getText().toString().trim()) * 100);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            payToMiYa(scanerCode);
                        }
                    }).start();
                   /* new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            miYaNumber = MaxNO.getPhoneMac() + GetData.getYYMMDDNoSpellingTime() + GetData.getHHmmssTime();
                            boolean isSuccess = payToMiYa(scanerCode);
                            if (way.equals("ali")) {
                                payType = "3";
                            } else if (way.equals("wx")) {
                                payType = "1";
                            }
                            if (isSuccess) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //关闭等待对话框
//                                        closeDialog();
                                        //开始打印小票
                                        print(shishou.getText().toString(), String.valueOf(whatPay));
                                        showFinshDialog(wayName, whatPay, shishou.getText().toString(), true, null, null);
                                        sendIntent(wayName, shishou.getText().toString(), zhaoling.getText().toString());
                                        //InductionCard();
                                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        closeDialog();
                                        Toast.makeText(PayActivity.this, "支付失败，请检查网络", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();*/
                }
            }
        });
        /**
         * 扫描的时候对enter键有一个监听事件，当触发的时候就会直接调用
         */
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == KeyEvent.KEYCODE_ENTER) {
//                    scanerCode = et.getText().toString().trim();
//                    if (!scanerCode.isEmpty()) {
//                        getCodeDialog.dismiss();
////                        showDialog("正在请求支付，请等待");
//                        //requestPay(scanerCode, way, wayName, status);
//                        if (way.equals("ali")) {
//                            payType = "3";
//                        } else if (way.equals("wx")) {
//                            payType = "1";
//                        }
//                        if (shopId.equals("") || jiju.equals("") || opid.equals("")) {
//                            Toast.makeText(PayActivity.this, "配置参数不能为空", Toast.LENGTH_SHORT).show();
//                        }
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
////                                miYaNumber = MaxNO.getPhoneMac() + GetData.getYYMMDDNoSpellingTime() + GetData.getHHmmssTime();
//                                boolean isSuccess = payToMiYa(saasid, scanerCode, shopId, jiju, opid, maxNO, String.valueOf(Double.parseDouble(shishou.getText().toString()) * 100), payType, "");
//                                if (way.equals("ali")) {
//                                    payType = "3";
//                                } else if (way.equals("wx")) {
//                                    payType = "1";
//                                }
//                                if (isSuccess) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            print(shishou.getText().toString(), String.valueOf(whatPay));
////                                            closeDialog();
//                                            showFinshDialog(wayName, whatPay, shishou.getText().toString(), true, null, null);
//                                            sendIntent(wayName, shishou.getText().toString(), zhaoling.getText().toString());
//                                            //InductionCard();
//                                            // Toast.makeText(PayActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
////                                            closeDialog();
//                                            Toast.makeText(PayActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        }).start();
//                    }
//                }
//                return false;
//            }
//        });

        /**
         * 调用机器的摄像头
         */
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确认键，关闭串口
//                tag = false;
//                if (pm != null) {
//                    try {
//                        pm.portClose(port);
//                    } catch (PortException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final ScannerManager scannerManager = ScannerManager.getInstance(PayActivity.this);
//                            BarcodeParam param = scannerManager.getBarcodeParam();
//
//                            param.paramMap.put("preferences_use_front_ccd", true);
//                            scannerManager.setBarcodeParam(param);
//                            boolean ret = scannerManager.scanOpen();
//                            Log.i(TAG, "ret: " + ret);
//                            scannerManager.scanStart(new ScannerListener() {
//                                @Override
//                                public void scanOnRead(final ScanResult arg0) {
//                                    if (arg0 != null) {
//                                        //requestPay(arg0.getContent(), way, wayName, status);
//                                        Log.i(TAG, "ScanResult: " + arg0.getContent());
//                                        et.setText(arg0.getContent());
////                                        if (way.equals("ali")) {
////                                            payType = "3";
////                                        } else if (way.equals("wx")) {
////                                            payType = "1";
////                                        }
////                                        if (shopId.equals("") || jiju.equals("") || opid.equals("")) {
////                                            Toast.makeText(PayActivity.this, "配置参数不能为空", Toast.LENGTH_SHORT).show();
////                                        }
//////                                        new Thread(new Runnable() {
//////                                            @Override
//////                                            public void run() {
////                                        miYaNumber = MaxNO.getPhoneMac() + GetData.getYYMMDDNoSpellingTime() + GetData.getHHmmssTime();
////                                        boolean isSuccess = payToMiYa(saasid, arg0.getContent(), shopId, jiju, opid, miYaNumber, String.valueOf(Double.parseDouble(shishou.getText().toString()) * 100), payType, "");
////                                        if (way.equals("ali")) {
////                                            payType = "3";
////                                        } else if (way.equals("wx")) {
////                                            payType = "1";
////                                        }
////                                        if (isSuccess) {
////                                            runOnUiThread(new Runnable() {
////                                                @Override
////                                                public void run() {
////                                                    getCodeDialog.dismiss();
////                                                    print(shishou.getText().toString(), String.valueOf(whatPay));
////                                                    showFinshDialog(wayName, whatPay, shishou.getText().toString(), true, null, null);
////                                                    sendIntent(wayName, shishou.getText().toString(), zhaoling.getText().toString());
////                                                    //InductionCard();
////                                                    // Toast.makeText(PayActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
////                                                }
////                                            });
////                                        } else {
////                                            runOnUiThread(new Runnable() {
////                                                @Override
////                                                public void run() {
////                                                    closeDialog();
////                                                    Toast.makeText(PayActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
////                                                }
////                                            });
////                                        }
////                                            }
////                                        }).start();
//                                    }
//                                }
//
//                                @Override
//                                public void scanOnComplete() {
//                                    scannerManager.scanClose();
//                                }
//
//                                @Override
//                                public void scanOnCancel() {
//                                    scannerManager.scanClose();
//                                }
//                            });
//
//                        }
//                    }).start();

                } else if (Build.MODEL.equalsIgnoreCase(URL.MODEL_E500)){
                    scanner(EScannerType.REAR);
//                    showToast("暂不支持");
                }
            }
        });
        return scanerCode;
    }
    private void scanner(EScannerType type) {
        final IScanner scanner = dal.getScanner(type);
        scanner.open();
        scanner.start(new IScanner.IScanListener() {

            @Override
            public void onRead(String arg0) {
//                scannerResultTv.setText("Result:" + arg0);
                Log.i("scan", arg0);
                scanner.close();
            }

            @Override
            public void onFinish() {
                scanner.close();
            }

            @Override
            public void onCancel() {
                scanner.close();
            }
        });
    }

    public void sendIntent(String payWayString, String ss, String zl) {
        Intent intent = new Intent(PayActivity.this, PaySuccessDialogActivity.class);
        intent.putExtra(PaySuccessDialogActivity.PUT_PAYWAY, payWayString);
        intent.putExtra(PaySuccessDialogActivity.PUT_YS, value);
        intent.putExtra(PaySuccessDialogActivity.PUT_ZL, zl);
        intent.putExtra(PaySuccessDialogActivity.PUT_SS, ss);
        intent.putExtra("sid", sid_m);
        intent.putExtra(PaySuccessDialogActivity.PUT_MAXNO, String.valueOf(maxNO));

        if (payWayString.equals(PayWay.CZK)) {
            Bundle bundle = new Bundle();
//            Log.i("cardInfo", "valueCardDtobalance----" + valueCardDto.getCardAmount());
            bundle.putSerializable(PaySuccessDialogActivity.CARD_INFO, valueCardDto);
            intent.putExtras(bundle);
        }
        startActivity(intent);
        chooseWay = false;
    }

    /**
     * 获取米雅配置参数
     *
     * @param param
     */
    private String getParam(String param) {
        SharedPreferences sp = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
        return sp.getString(param, "");
    }

    /**
     * 米雅支付
     *
     * @param code 付款码
     */
//    private void payToMiYa(String code) {
//        Map tlvmap = new HashMap();
//        tlvmap.put("VERSION", "1.5");
//        //交易代码-支付
//        tlvmap.put("TRCODE", "1001");
//        //商户编号
//        tlvmap.put("SAASID", saasid);
//        //门店编码
//        tlvmap.put("NUMID", shopId_miya);
//        //机具编码
//        tlvmap.put("USERID", jiju);
//        //操作员ID
//        tlvmap.put("OPERATOR_ID", cashier_number);
//        //支付方式
//        tlvmap.put("PAYMENTPLATFORM", "A");
//        //操作类型--支付
//        tlvmap.put("SERVEICETYPE", "A");
//        //商家订单（唯一）
//        tlvmap.put("OUT_TRADE_NO", shopId_miya+maxNO);
//        //支付总金额--单位分
//        tlvmap.put("TOTAL_FEE", fee);
//        //付款码
//        tlvmap.put("F1", code);
//
//        //配置文件路径
//        tlvmap.put("path", Environment.getExternalStorageDirectory().getPath() + "/miyajpos/");
//        try {
//            Map requsetMap = TcpSend.sendMiya(tlvmap, null);
//            Object retcode = requsetMap.get("RETCODE");
//            Object retmsg = requsetMap.get("RETMSG");
//            Object typeCode = requsetMap.get("PAYMENTPLATFORM");
//            Log.i("miya", retcode + "----" + retmsg + "--------" + typeCode);
//            if ("00".equals(retcode) && "PAYSUCCESS".equals(retmsg)) {
//                switch (typeCode.toString()) {
//                    case "1":
//                        payWayName = PayWay.WX;
//                        payWayCode = PayWay.WXPAY;
//                        break;
//                    default:
//                    case "3":
//                        payWayName = PayWay.ZFB;
//                        payWayCode = PayWay.ALIPAY;
//                        break;
//                }
//                //paySuccess(payType);
//                //支付成功
//                Message msg = Message.obtain();
//                msg.what = 1;
//                handler.sendMessage(msg);
//            } else {
//                //支付失败
//                Message msg = Message.obtain();
//                msg.what = 0;
//                handler.sendMessage(msg);
//            }
//        } catch (Exception e) {
//            //支付异常
//            Message msg = Message.obtain();
//            msg.what = -1;
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        }
//
//    }
    //测试支付
    private void payToMiYa(String code) {
        Map tlvmap = new HashMap();
        tlvmap.put("VERSION", "1.5");
        //交易代码-支付
        tlvmap.put("TRCODE", "1001");
        //商户编号
//        tlvmap.put("SAASID", "001");
        //门店编码
        tlvmap.put("NUMID", "001");
        //机具编码
        tlvmap.put("USERID", "19");
        //操作员ID
        tlvmap.put("OPERATOR_ID", "001");
        //支付方式
        tlvmap.put("PAYMENTPLATFORM", "A");
        //操作类型--支付
        tlvmap.put("SERVEICETYPE", "A");
        //商家订单（唯一）
        tlvmap.put("OUT_TRADE_NO", shopId_miya + maxNO);
        //支付总金额--单位分
        tlvmap.put("TOTAL_FEE", fee);
//        tlvmap.put("GOODSDETAIL", "690000000,啤酒,10.00,2|690000001,香烟,5.00,2");
        //付款码
        tlvmap.put("F1", code);

        //配置文件路径
        tlvmap.put("path", Environment.getExternalStorageDirectory().getPath() + "/miyajpos/");
        try {
            Map requsetMap = TcpSend.sendMiya(tlvmap, null);
            Object retcode = requsetMap.get("RETCODE");
            Object retmsg = requsetMap.get("RETMSG");
            Object typeCode = requsetMap.get("PAYMENTPLATFORM");
            Log.i("miya", retcode + "----" + retmsg + "--------" + typeCode);
            if ("00".equals(retcode) && "PAYSUCCESS".equals(retmsg)) {
                switch (typeCode.toString()) {
                    case "1":
                        payWayName = PayWay.WX;
                        payWayCode = PayWay.WXPAY;
                        break;
                    default:
                    case "3":
                        payWayName = PayWay.ZFB;
                        payWayCode = PayWay.ALIPAY;
                        break;
                }
                //paySuccess(payType);
                //支付成功
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            } else {
                //支付失败
                Message msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            //支付异常
            Message msg = Message.obtain();
            msg.what = -1;
            handler.sendMessage(msg);
            e.printStackTrace();
        }

    }

    //米雅撤销
    private void abandon() {
        Map tlvmap = new HashMap();
        tlvmap.put("VERSION", "1.5");
        //交易代码-支付
        tlvmap.put("TRCODE", "1001");
        //商户编号
        tlvmap.put("SAASID", saasid);
        //门店编码
        tlvmap.put("NUMID", shopId_miya);
        //机具编码
        tlvmap.put("USERID", jiju);
        //操作员ID
        tlvmap.put("OPERATOR_ID", cashier_number);
        //支付方式
        tlvmap.put("PAYMENTPLATFORM", "A");
        //操作类型--撤销
        tlvmap.put("SERVEICETYPE", "E");
        //商家订单（唯一）
        tlvmap.put("OUT_TRADE_NO", shopId_miya + maxNO);
        //支付总金额--单位分
        tlvmap.put("TOTAL_FEE", fee);
        //付款码
//        tlvmap.put("F1", code);

        //配置文件路径
        tlvmap.put("path", Environment.getExternalStorageDirectory().getPath() + "/miyajpos/");
        try {
            Map requsetMap = TcpSend.sendMiya(tlvmap, null);
            Object retcode = requsetMap.get("RETCODE");
            Object retmsg = requsetMap.get("RETMSG");
            Object typeCode = requsetMap.get("PAYMENTPLATFORM");
            Log.i("miya", retcode + "----" + retmsg + "--------" + typeCode);

        } catch (Exception e) {

        }

    }

    public void requestCardPay(String money) {
        Intent intent = new Intent("com.pax.CALL_PAYMENT");
        intent.putExtra("TRANS_TYPE", "001");

        BigDecimal bd = new BigDecimal(money);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//        Log.i("adssaf", String.valueOf(bd) + "+double+" + bd);
        intent.putExtra("TRANS_AMT", String.valueOf(bd));
        try {
            startActivityForResult(intent, SEND_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog mAlertDialog;

    /**
     * 信息提示
     *
     * @param msg
     */
    private void showMsgToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PayActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
        Log.i("test", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test", "onPause");
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
}