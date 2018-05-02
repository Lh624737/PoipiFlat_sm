package com.pospi.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.OrderPaytype;
import com.pospi.pai.pospiflat.R;
import com.pospi.util.constant.PayWay;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by acer on 2017/6/19.
 */

public class DetailPayAdapter extends BaseAdapter {
    private Context context;
    private List<OrderPaytype> orderDtos;
    private double card_discount;

    public DetailPayAdapter(Context context, List<OrderPaytype> orderDtos) {
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
        DetailPayAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new DetailPayAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.todaysaleall_outlvitem, null);
            holder.way = (TextView) convertView.findViewById(R.id.today_sale_all_item_way);
            holder.ys = (TextView) convertView.findViewById(R.id.today_sale_all_item_ys);
            holder.ss = (TextView) convertView.findViewById(R.id.today_sale_all_item_ss);
            holder.zl = (TextView) convertView.findViewById(R.id.today_sale_all_item_zl);
            convertView.setTag(holder);
        } else {
            holder = (DetailPayAdapter.ViewHolder) convertView.getTag();
        }

        holder.way.setText(orderDtos.get(position).getPayName());
        if (orderDtos.get(position).getPayCode() == PayWay.MIANZHI_CARD) {
            holder.ss.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getSs()) * card_discount)));
            holder.ys.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getYs()) * card_discount)));
        } else {
            holder.ss.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getSs()))));
            holder.ys.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getYs()))));
        }
        if (orderDtos.get(position).getZl().equals("")) {
            holder.zl.setText(String.valueOf("0.00"));
        } else {
            holder.zl.setText(String.valueOf(translateDouble(Double.parseDouble(orderDtos.get(position).getZl()))));
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
        TextView ys;
        TextView ss;
        TextView zl;
    }
}