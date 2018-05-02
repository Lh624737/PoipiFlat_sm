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
import android.widget.TextView;

import com.pospi.dao.OrderDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.pai.pospiflat.R;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.MyChartItem;
import com.pospi.util.MyChartView;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.PayWay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodayTimeStaticFragment extends Fragment {


    @Bind(R.id.today_sale_data)
    TextView data;
    @Bind(R.id.static_num)
    TextView num;
    @Bind(R.id.static_sale)
    TextView sale;
    @Bind(R.id.static_discount)
    TextView discount;
    @Bind(R.id.static_in)
    TextView in;
    @Bind(R.id.chart1)
    MyChartView chart1;

    private Context context;
    private List<OrderDto> nine;
    private List<OrderDto> Ten;
    private List<OrderDto> Eleven;
    private List<OrderDto> Twelve;
    private List<OrderDto> Thirteen;
    private List<OrderDto> Fourteen;
    private List<OrderDto> Fifteen;
    private List<OrderDto> Sixteen;
    private List<OrderDto> Seventeen;
    private List<OrderDto> Eighteen;
    private List<OrderDto> Nineteen;
    private List<OrderDto> Twenty;
    private List<OrderDto> Twenty_one;
    private List<OrderDto> Twenty_two;
    private List<OrderDto> Twenty_three;
    private List<OrderDto> Twenty_four;

    private View layout;
    private double card_discount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_today_time_static, container, false);

        ButterKnife.bind(this, layout);
        context = getActivity().getApplicationContext();
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

        data.setText(GetData.getYYMMDDTime());//进入界面的时候就会把日期定位在当天
        loadChart(layout, data.getText().toString());
        return layout;
    }

    private void loadChart(View view, String date) {
        List<OrderDto> dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(date);
        double saleMoney = 0;
        List<GoodsDto> goodsDtoList = new ArrayList<>();
        double discountTotal = 0;
        for (int i = 0; i < dtos.size(); i++) {
            goodsDtoList = Sava_list_To_Json.changeToList(dtos.get(i).getOrder_info());
            if (dtos.get(i).getPayway().equals(PayWay.CZK)) {
                saleMoney += Double.parseDouble(dtos.get(i).getSs_money()) * card_discount;
                discountTotal += Double.parseDouble(dtos.get(i).getSs_money()) * (1 - card_discount);
            } else {
                saleMoney += Double.parseDouble(dtos.get(i).getSs_money());
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
        sale.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleMoney)));
        discount.setText(String.valueOf(DoubleSave.doubleSaveTwo(discountTotal)));
        in.setText(String.valueOf(DoubleSave.doubleSaveTwo(saleMoney)));

        nine = new ArrayList<>();
        Ten = new ArrayList<>();
        Eleven = new ArrayList<>();
        Twelve = new ArrayList<>();
        Thirteen = new ArrayList<>();
        Fourteen = new ArrayList<>();
        Fifteen = new ArrayList<>();
        Sixteen = new ArrayList<>();
        Seventeen = new ArrayList<>();
        Eighteen = new ArrayList<>();
        Nineteen = new ArrayList<>();
        Twenty = new ArrayList<>();
        Twenty_one = new ArrayList<>();
        Twenty_two = new ArrayList<>();
        Twenty_three = new ArrayList<>();
        Twenty_four = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            saleMoney += Double.parseDouble(dtos.get(i).getYs_money());
            String str = dtos.get(i).getDetailTime().substring(0, 2);
            int hour = Integer.parseInt(str);
            switch (hour) {
                case 9:
                    nine.add(dtos.get(i));
                    break;
                case 10:
                    Ten.add(dtos.get(i));
                    break;
                case 11:
                    Eleven.add(dtos.get(i));
                    break;
                case 12:
                    Twelve.add(dtos.get(i));
                    break;
                case 13:
                    Thirteen.add(dtos.get(i));
                    break;
                case 14:
                    Fourteen.add(dtos.get(i));
                    break;
                case 15:
                    Fifteen.add(dtos.get(i));
                    break;
                case 16:
                    Sixteen.add(dtos.get(i));
                    break;
                case 17:
                    Seventeen.add(dtos.get(i));
                    break;
                case 18:
                    Eighteen.add(dtos.get(i));
                    break;
                case 19:
                    Nineteen.add(dtos.get(i));
                    break;
                case 20:
                    Twenty.add(dtos.get(i));
                    break;
                case 21:
                    Twenty_one.add(dtos.get(i));
                    break;
                case 22:
                    Twenty_two.add(dtos.get(i));
                    break;
                case 23:
                    Twenty_three.add(dtos.get(i));
                    break;
                case 24:
                    Twenty_four.add(dtos.get(i));
                    break;
            }
        }
        ArrayList<MyChartItem> list = new ArrayList<>();
        list.add(new MyChartItem("9", nine.size()));
        list.add(new MyChartItem("10", Ten.size()));
        list.add(new MyChartItem("11", Eleven.size()));
        list.add(new MyChartItem("12", Twelve.size()));
        list.add(new MyChartItem("13", Thirteen.size()));
        list.add(new MyChartItem("14", Fourteen.size()));
        list.add(new MyChartItem("15", Fifteen.size()));
        list.add(new MyChartItem("16", Sixteen.size()));
        list.add(new MyChartItem("17", Seventeen.size()));
        list.add(new MyChartItem("18", Eighteen.size()));
        list.add(new MyChartItem("19", Nineteen.size()));
        list.add(new MyChartItem("20", Twenty.size()));
        list.add(new MyChartItem("21", Twenty_one.size()));
        list.add(new MyChartItem("22", Twenty_two.size()));
        list.add(new MyChartItem("23", Twenty_three.size()));
        list.add(new MyChartItem("24", Twenty_four.size()));
        MyChartView tu = (MyChartView) view.findViewById(R.id.chart1);
        tu.SetTuView(list, "订单数：单");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public double translateDouble(double b) {
        BigDecimal b2 = new BigDecimal(b);
        double f3 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f3;
    }

    @OnClick(R.id.today_sale_data)
    public void onClick() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                c.set(year, monthOfYear, dayOfMonth);
                data.setText(DateFormat.format("yyyy-MM-dd", c));
                loadChart(layout, data.getText().toString());

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
