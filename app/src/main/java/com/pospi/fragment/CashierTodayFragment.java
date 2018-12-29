package com.pospi.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lany.sp.SPHelper;
import com.pospi.adapter.Cashier_Today_sale_Adapter;
import com.pospi.dao.OrderDao;
import com.pospi.dto.OrderDto;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.constant.PayWay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CashierTodayFragment extends Fragment {
    @Bind(R.id.cashier_data)
    TextView Data;
    @Bind(R.id.cashier_name)
    TextView cashierName;
    @Bind(R.id.cashier_today_num)
    TextView Num;
    @Bind(R.id.cashier_payable)
    TextView Payable;
    @Bind(R.id.cashier_paid)
    TextView Paid;
    @Bind(R.id.cashier_gap)
    TextView Gap;
    @Bind(R.id.cashier_lv)
    ListView cashierLv;
    @Bind(R.id.ll_detail)
    LinearLayout ll_detail;
    @Bind(R.id.cashier_today_ss)
    TextView cashier_today_ss;

    private Context context;
    private String cashier_name;


    private View layoutView;

    private Cashier_Today_sale_Adapter adapter;
    private double card_discount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        layoutView = inflater.inflate(R.layout.fragment_cashier_today, container, false);
        ButterKnife.bind(this, layoutView);
        ll_detail.setVisibility(View.GONE);
        getShareperformancedata();
        Data.setText(GetData.getYYMMDDTime());//进入界面的时候就会把日期定位在当天

        AdapteAdapter(Data.getText().toString().trim());
        return layoutView;
    }


    /**
     * 得到存储的登录的信息，例如收银员的信息
     */
    public void getShareperformancedata() {
        int whichOne = context.getSharedPreferences("islogin", context.MODE_PRIVATE).getInt("which", 0);
        cashier_name = SPHelper.getInstance().getString(Constant.CUSTOMER_name);

        cashierName.setText(cashier_name);
    }

    /**
     * 从数据库里面查询数据
     *
     * @param date 传入需要查询的日期
     */
    public void AdapteAdapter(String date) {

        List<OrderDto> dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(date);


        List<OrderDto> nowCashierOrderList = new ArrayList<>();
        List<OrderDto> xjPay = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            if (dtos.get(i).getCashiername().equals(cashier_name)) {//判断当前的收银员
                nowCashierOrderList.add(dtos.get(i));//查询当前收银员在收银的过程中进行的订单信息，判断后加入新的list里面

            }
        }
        Num.setText(String.valueOf(nowCashierOrderList.size()));



        //把每次提交的订单累加在一起
//        Log.i("size", nowCashierOrderList.size() + "");
        for (int i = 0; i < nowCashierOrderList.size(); i++) {
            OrderDto good1 = nowCashierOrderList.get(i);
            for (int j = nowCashierOrderList.size() - 1; j > i; j--) {
                OrderDto good2 = nowCashierOrderList.get(j);
                if (good1.getPayway().equals(good2.getPayway())) {
                    good1.setYs_money(String.valueOf(Double.parseDouble(good1.getYs_money()) + Double.parseDouble(good2.getYs_money())));
                    good1.setSs_money(String.valueOf(Double.parseDouble(good1.getSs_money()) + Double.parseDouble(good2.getSs_money())));
                    if (good1.getZl_money().equals("")) {
                        good1.setZl_money(String.valueOf("0.0"));
                    } else {
                        good1.setZl_money(String.valueOf(Double.parseDouble(good1.getZl_money()) + Double.parseDouble(good2.getZl_money())));
                    }


                    nowCashierOrderList.remove(good2);
                }
            }
        }

//        Log.i("size", nowCashierOrderList.size() + "");
        adapter = new Cashier_Today_sale_Adapter(context, nowCashierOrderList);
        cashierLv.setAdapter(adapter);
        double payShould = 0;

        for (int i = 0; i < nowCashierOrderList.size(); i++) {
//            if (nowCashierOrderList.get(i).getPayway().equals(PayWay.XJ)) {
//                xjPay.add(dtos.get(i));
                payShould += Double.parseDouble(dtos.get(i).getYs_money());
//            }
        }
        cashier_today_ss.setText(String.valueOf(DoubleSave.doubleSaveTwo(payShould)));
//        Payable.setText(String.valueOf(DoubleSave.doubleSaveTwo(payShould)));
//        Gap.setText(String.valueOf(DoubleSave.doubleSaveTwo(payShould)));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.cashier_data)
    public void onClick() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                c.set(year, monthOfYear, dayOfMonth);
                Data.setText(DateFormat.format("yyyy-MM-dd", c));
                AdapteAdapter(Data.getText().toString());//当日期改变之后会调用此方法再对数据库进行查询
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}

