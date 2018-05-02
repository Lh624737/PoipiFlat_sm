package com.pospi.pai.pospiflat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.newland.jsums.paylib.NLPay;
import com.newland.jsums.paylib.model.NormalPrintRequest;
import com.pospi.dto.OrderDto;
import com.pospi.dto.ValueCardDto;
import com.pospi.gpprinter.GPprint;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.PayBaseActivity;
import com.pospi.pai.pospiflat.pay.PayActivity;
import com.pospi.paxprinter.PaxPrint;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyPrinter;
import com.pospi.util.Printer;
import com.pospi.util.PrinterFactory;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.Utils;
import com.pospi.util.constant.Key;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;
import com.pospi.zqprinter.ZQPrint;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xiaopan.android.preference.PreferencesUtils;

/**
 * Created by Qiyan on 2016/7/13.
 */
public class PaySuccessDialogActivity extends PayBaseActivity {

    @Bind(R.id.tv_payment)
    TextView tvPayment;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.tv_ss)
    TextView tvSs;
    @Bind(R.id.tv_change)
    TextView tvChange;
    @Bind(R.id.tv_order_id)
    TextView tvOrderId;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.tv_cashier)
    TextView tvCashier;
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.ll_zl)
    LinearLayout llZl;
    @Bind(R.id.tv_card_id)
    TextView tvCardId;
    @Bind(R.id.ll_card_id)
    LinearLayout llCardId;
    @Bind(R.id.tv_card_balance)
    TextView tvCardBalance;
    @Bind(R.id.ll_card_balance)
    LinearLayout llCardBalance;
    @Bind(R.id.ll_line)
    View llLine;

    private ValueCardDto valueCardDto;
    public final static String PUT_MAXNO = "maxNo";
    public final static String PUT_YS = "ys";
    public final static String PUT_SS = "sss";
    public final static String PUT_ZL = "zl";
    public final static String PUT_PAYWAY = "payway";
    public final static String CARD_INFO = "card";
    private GpService mGpService;
    private String payway, maxNo, zl, ys, ss, goods;

    private OrderDto orderDto = new OrderDto();
    private double card_discount;
    private Button print_paper;
    private String str_status;
    private MyPrinter myPrinter;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success);
        print_paper = (Button) findViewById(R.id.btn_print);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);

        card_discount = getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        int whichOne = getSharedPreferences("islogin", MODE_PRIVATE).getInt("which", 0);
        final String cashier_name = new CashierLogin_pareseJson().parese(
                getSharedPreferences("cashierMsgDtos", MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName();

        String shopName = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Name", "");

        payway = getIntent().getStringExtra(PUT_PAYWAY);
        maxNo = getIntent().getStringExtra(PUT_MAXNO);

        zl = getIntent().getStringExtra(PUT_ZL);
        ys = getIntent().getStringExtra(PUT_YS);
        ss = getIntent().getStringExtra(PUT_SS);
        sid = getIntent().getStringExtra("sid");

        orderDto.setYs_money(ys);
        orderDto.setSs_money(ss);
        orderDto.setZl_money(zl);
        orderDto.setMaxNo(maxNo);
        orderDto.setOrder_info(goods);

        if (payway.equals(PayWay.CZK)) {
            valueCardDto = new ValueCardDto();
            valueCardDto = (ValueCardDto) getIntent().getSerializableExtra(PaySuccessDialogActivity.CARD_INFO);
            llZl.setVisibility(View.GONE);
            llCardId.setVisibility(View.VISIBLE);
            tvCardId.setText(valueCardDto.getCardNo());
            llCardBalance.setVisibility(View.VISIBLE);
            llLine.setVisibility(View.VISIBLE);
            tvPayMoney.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(ys) * card_discount)));
            tvSs.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(ss) * card_discount)));
            tvCardBalance.setText(String.valueOf(DoubleSave.doubleSaveTwo(valueCardDto.getCardAmount() - Double.parseDouble(ss) * card_discount)));
        } else {
            tvPayMoney.setText(ys);
            tvSs.setText(ss);
            tvChange.setText(zl);
        }
        tvPayment.setText(payway);
        tvCashier.setText(cashier_name);
        tvOrderId.setText(maxNo);
        tvShopName.setText(shopName);
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        killAllActivity();
        //清空购物车
        Sava_list_To_Json.clearGoodsMsg(this);
    }
    public void printPaper(View v) {
        if (v.getId() == R.id.btn_print) {
            new Printer().print(this ,ss,payway ,maxNo ,valueCardDto ,sid );

        }
    }

//    public void print(final String shishou, final String payway) {
//        final String goods = getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);//
//
//        switch (Build.MODEL) {
//            case URL.MODEL_D800:
//                byte status = PrnTest.prnStatus();
//                if (status == 0x00) {
//                    myPrinter = PrinterFactory.dPrinter(PaySuccessDialogActivity.this, maxNo, payway);
//                } else if (status == 0x02) {
//                    Toast.makeText(this, "打印机缺纸", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (status == 0x08) {
//                    Toast.makeText(this, "打印机发热", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    Toast.makeText(this, "打印机故障", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                break;
//            case URL.MODEL_DT92:
//                mGpService = App.getmGpService();
//                if (mGpService == null) {
//                    showToast("未连接到本地打印机");
//                    Utils.connection();
//                    mGpService = App.getmGpService();
//                    try {
//                        mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
//                    } catch (RemoteException e) {
//                        String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
//                        gPrint_log += GetData.getDataTime() + " 连接打印机端口时异常 " + e.getMessage() + "; ";
//                        PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
//                        e.printStackTrace();
//                    }
//                }
//                myPrinter = PrinterFactory.jbPrinter(mGpService, getApplicationContext(), maxNo, payway);
//                break;
//
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                myPrinter.starPrint(goods, shishou, null, true, valueCardDto);
//            }
//
//        }).start();
//    }



    private void showMsgToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PaySuccessDialogActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        PayActivity.isRun = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
         return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空购物车
        Sava_list_To_Json.clearGoodsMsg(this);
    }
}
