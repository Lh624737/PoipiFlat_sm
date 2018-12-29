package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.OrderDto;
import com.pospi.pai.yunpos.R;
import com.pospi.util.constant.PayWay;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Qiyan on 2016/5/26.
 */
public class Cashier_Today_sale_Adapter extends BaseAdapter {
    private Context context;
    private List<OrderDto> orderDtos;
    private double card_discount;

    public Cashier_Today_sale_Adapter(Context context, List<OrderDto> orderDtos) {
        this.context = context;
        this.orderDtos = orderDtos;
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
    }

    @Override
    public int getCount() {
        return orderDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cashier_today_lv_item, null);
            holder.way = (TextView) convertView.findViewById(R.id.cashier_way);
            holder.should = (TextView) convertView.findViewById(R.id.cashier_ys);
            holder.shishou = (TextView) convertView.findViewById(R.id.cashier_ss);
            holder.zl = (TextView) convertView.findViewById(R.id.cashier_zl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.way.setText(orderDtos.get(position).getCheckoutTime());
        if (orderDtos.get(position).getPayway().equals(PayWay.CZK)) {
            holder.should.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getYs_money()) * card_discount)));
            holder.shishou.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getSs_money()) * card_discount)));
        } else {
            holder.should.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getYs_money()))));
            holder.shishou.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getSs_money()))));
        }
//        holder.should.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getYs_money()))));
//        holder.shishou.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getSs_money()))));
//        holder.zl.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getZl_money()))));
        if (orderDtos.get(position).getZl_money().equals("")) {
            holder.zl.setText(String.valueOf("0.00"));
        } else {
            holder.zl.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getZl_money()))));
        }
        return convertView;
    }

    public double translateDouble(double b) {
        BigDecimal b2 = new BigDecimal(b);
        double f3 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f3;
    }

    class ViewHolder {
        TextView way;
        TextView should;
        TextView shishou;
        TextView zl;
    }
}
