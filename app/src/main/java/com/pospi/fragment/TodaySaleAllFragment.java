package com.pospi.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pospi.adapter.TodaySaleAllAdapter;
import com.pospi.adapter.TodaysaleAllPayAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dto.CashierCashDto;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderPaytype;
import com.pospi.pai.yunpos.R;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodaySaleAllFragment extends Fragment {

    @Bind(R.id.todaysallall_data)
    TextView data;
    @Bind(R.id.todaysaleall_num)
    TextView num;
    @Bind(R.id.todaysaleall_sold)
    TextView sold;
    @Bind(R.id.todaysaleall_discount)
    TextView discount;
    @Bind(R.id.todaysaleall_in)
    TextView in;
    @Bind(R.id.todaysaleall_in_lv)
    ListView inLv;
    @Bind(R.id.todaysaleall_out_lv)
    ListView outLv;
    private Context context;
    private TodaysaleAllPayAdapter payAdapter;
    private TodaySaleAllAdapter adapter;
    private double card_discount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        View view = inflater.inflate(R.layout.fragment_today_sale_all, container, false);
        ButterKnife.bind(this, view);
        data.setText(GetData.getYYMMDDTime());
        getOrderDate(data.getText().toString());


        return view;
    }


    public void getOrderDate(String data) {
        List<OrderDto> dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(data);
        Log.d(dtos.size() + "数据大小", "内容");
        List<GoodsDto> goodsDtoList = new ArrayList<>();
        double saleMoney = 0;
        double discountTotal = 0;
        for (int i = 0; i < dtos.size(); i++) {
            goodsDtoList = Sava_list_To_Json.changeToList(dtos.get(i).getOrder_info());
            if (dtos.get(i).getPayway().equals(PayWay.CZK)) {
                saleMoney += Double.parseDouble(dtos.get(i).getYs_money()) * card_discount;
                discountTotal += Double.parseDouble(dtos.get(i).getYs_money()) * (1 - card_discount);
            } else {
                saleMoney += Double.parseDouble(dtos.get(i).getYs_money());
                for (GoodsDto goodsDto : goodsDtoList) {
                    discountTotal += goodsDto.getDiscount();
                }
            }
//            for (int j = 0; j < goodsDtoList.size(); j++) {
//                discountTotal += goodsDtoList.get(j).getDiscount();
//                saleMoney += goodsDtoList.get(j).getNum() * goodsDtoList.get(j).getPrice();
//            }
        }

        num.setText(String.valueOf(dtos.size()));
        sold.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleMoney)));
        discount.setText(String.valueOf(DoubleSave.doubleSaveTwo(discountTotal)));
        in.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleMoney)));

        /**
         * 把订单的list转化成收银员的订单信息的list
         */
        List<CashierCashDto> cashierCashDtos = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            CashierCashDto cashDto = new CashierCashDto();
            cashDto.setNum(1);
            cashDto.setCashierName(dtos.get(i).getCashiername());
            if(dtos.get(i).getPayway().equals(PayWay.CZK)){
                cashDto.setDiscount(DoubleSave.doubleSaveTwo(Double.parseDouble(dtos.get(i).getYs_money())*(1-card_discount)));
                cashDto.setGap(DoubleSave.doubleSaveTwo(Double.parseDouble(dtos.get(i).getYs_money())*card_discount));
                cashDto.setIn(DoubleSave.doubleSaveTwo(Double.parseDouble(dtos.get(i).getYs_money())*card_discount));
                cashDto.setSale(DoubleSave.doubleSaveTwo(Double.parseDouble(dtos.get(i).getYs_money())*card_discount));
            }else {
                cashDto.setDiscount(0);
                cashDto.setGap(Double.parseDouble(dtos.get(i).getYs_money()));
                cashDto.setIn(Double.parseDouble(dtos.get(i).getYs_money()));
                cashDto.setSale(Double.parseDouble(dtos.get(i).getYs_money()));
            }

            cashierCashDtos.add(cashDto);
        }

        /**
         * 把同一个收银员所处理的订单累加在一起
         */
        Log.i("size", cashierCashDtos.size() + "");
        for (int i = 0; i < cashierCashDtos.size(); i++) {
            CashierCashDto good1 = cashierCashDtos.get(i);
            String good_name1 = good1.getCashierName();
            for (int j = cashierCashDtos.size() - 1; j > i; j--) {
                CashierCashDto good2 = cashierCashDtos.get(j);
                String good_name2 = good2.getCashierName();
                if (good_name1.equals(good_name2)) {
                    good1.setNum(good1.getNum() + good2.getNum());
                    good1.setSale(good1.getSale() + good2.getSale());
                    good1.setIn(good1.getIn() + good2.getIn());
                    good1.setGap(good1.getGap() + good2.getGap());
                    good1.setDiscount(good1.getDiscount() + good2.getDiscount());
                    good1.setPayment(good1.getPayment() + good2.getPayment());
                    cashierCashDtos.remove(good2);
                }
            }
        }

        adapter = new TodaySaleAllAdapter(context, cashierCashDtos);
        Log.i("开始绑定数据", "绑定Adapter");
        inLv.setAdapter(adapter);


        /**
         * 把所有的支付方式相同的订单累加在一起
         */
        Log.i("size", dtos.size() + "");
        List<OrderPaytype> orderPaytypes = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            OrderDto good1 = dtos.get(i);
            List<OrderPaytype> list =  new Gson().fromJson(good1.getPayway(), new TypeToken<List<OrderPaytype>>() {
            }.getType());
            orderPaytypes.addAll(list);
        }



        payAdapter = new TodaysaleAllPayAdapter(context, setGoods(orderPaytypes));
        outLv.setAdapter(payAdapter);

    }
    //合并相同商品
    public List<OrderPaytype> setGoods(List<OrderPaytype> dtos) {
        Log.i("data", dtos.size() + "----");
        OrderPaytype tempDto = null;
        Map<String, OrderPaytype> dtoMap = new HashMap<>();//合并后的数据
        for (OrderPaytype gd : dtos) {
            tempDto = dtoMap.get(gd.getPayName());
            if (tempDto != null) {
                tempDto.setSs(DoubleSave.doubleSaveTwo(Double.parseDouble(tempDto.getSs()) + Double.parseDouble((gd.getSs())))+"");
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.todaysallall_data)
    public void onClick() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                c.set(year, monthOfYear, dayOfMonth);
                data.setText(DateFormat.format("yyyy-MM-dd", c));
                getOrderDate(data.getText().toString());

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}