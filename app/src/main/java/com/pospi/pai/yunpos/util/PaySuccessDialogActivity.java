package com.pospi.pai.yunpos.util;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.lany.sp.SPHelper;
import com.pospi.dto.OrderDto;
import com.pospi.dto.ValueCardDto;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.PayBaseActivity;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.MyPrinter;
import com.pospi.util.Printer;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Qiyan on 2016/7/13.
 */
public class PaySuccessDialogActivity extends PayBaseActivity {

//    @Bind(R.id.tv_payment)
//    TextView tvPayment;
//    @Bind(R.id.tv_pay_money)
//    TextView tvPayMoney;
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
//        print_paper = (Button) findViewById(R.id.btn_print);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);

        card_discount = getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        final String cashier_name = SPHelper.getInstance().getString(Constant.CUSTOMER_name);

        String shopName = SPHelper.getInstance().getString(Constant.STORE_NAME);

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

//        if (payway.equals(PayWay.CZK)) {
//            valueCardDto = new ValueCardDto();
//            valueCardDto = (ValueCardDto) getIntent().getSerializableExtra(PaySuccessDialogActivity.CARD_INFO);
//            llZl.setVisibility(View.GONE);
//            llCardId.setVisibility(View.VISIBLE);
//            tvCardId.setText(valueCardDto.getCardNo());
//            llCardBalance.setVisibility(View.VISIBLE);
//            llLine.setVisibility(View.VISIBLE);
////            tvPayMoney.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(ys) * card_discount)));
//            tvSs.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(ss) * card_discount)));
//            tvCardBalance.setText(String.valueOf(DoubleSave.doubleSaveTwo(valueCardDto.getCardAmount() - Double.parseDouble(ss) * card_discount)));
//        } else {
//            tvPayMoney.setText(ys);
            tvSs.setText("￥"+ss);
            tvChange.setText("￥"+zl);
//        }
////        tvPayment.setText(payway);
//        tvCashier.setText(cashier_name);
//        tvOrderId.setText(maxNo);
//        tvShopName.setText(shopName);
    }

    @OnClick(R.id.btn_back)
    public void onClick() {

        //清空购物车
        Sava_list_To_Json.clearGoodsMsg(this);
        startActivity(PointActivity.class);
        killAllActivity();
    }




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
