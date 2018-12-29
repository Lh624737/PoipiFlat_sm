package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.GoodsDto;
import com.pospi.pai.yunpos.R;

import java.util.List;

/**
 * Created by Qiyan on 2016/6/6.
 */
public class RefundsDetailAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDto> dtos;
    private String cahierName;

    public RefundsDetailAdapter(Context context, List<GoodsDto> dtos, String cashierName) {
        this.context = context;
        this.dtos = dtos;
        this.cahierName = cashierName;
    }

    @Override
    public int getCount() {
        return dtos.size();
    }

    @Override
    public Object getItem(int position) {
        return dtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.refunds_detail, null);
            holder.serial = (TextView) convertView.findViewById(R.id.detail_item_serial);
            holder.name = (TextView) convertView.findViewById(R.id.detail_item_name);
            holder.price = (TextView) convertView.findViewById(R.id.detail_item_price);
            holder.dealMoney = (TextView) convertView.findViewById(R.id.detail_item_deal_price);
            holder.num = (TextView) convertView.findViewById(R.id.detail_item_num);
            holder.money = (TextView) convertView.findViewById(R.id.detail_item_money);
            holder.discount = (TextView) convertView.findViewById(R.id.detail_item_discount);
            holder.cashier = (TextView) convertView.findViewById(R.id.detail_item_cashier);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.serial.setText(String.valueOf(position + 1));
        holder.name.setText(dtos.get(position).getName());
        holder.price.setText(String.valueOf(dtos.get(position).getPrice()));
        holder.dealMoney.setText(String.valueOf(dtos.get(position).getPrice()));
        holder.num.setText(String.valueOf(dtos.get(position).getNum()));
        holder.money.setText(String.valueOf(dtos.get(position).getPrice() * dtos.get(position).getNum()-dtos.get(position).getDiscount()));
        holder.discount.setText(String.valueOf(dtos.get(position).getDiscount()));
        holder.cashier.setText(cahierName);

        return convertView;
    }

    class Holder {
        TextView serial;
        TextView name;
        TextView price;
        TextView dealMoney;
        TextView num;
        TextView money;
        TextView discount;
        TextView cashier;
    }
}
