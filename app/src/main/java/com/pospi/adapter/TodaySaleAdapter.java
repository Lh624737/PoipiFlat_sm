package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.GoodsDto;
import com.pospi.pai.yunpos.R;
import com.pospi.util.DoubleSave;

import java.util.List;

/**
 * Created by Qiyan on 2016/5/26.
 */
public class TodaySaleAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsDto> goodsDtos;
    private double card_discount;


    public TodaySaleAdapter(Context context, List<GoodsDto> goodsDtos) {
        this.context = context;
        this.goodsDtos = goodsDtos;
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);

    }

    @Override
    public int getCount() {
        return goodsDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.today_sale_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.today_sale_item_name);
            viewHolder.num = (TextView) convertView.findViewById(R.id.today_sale_item_num);
            viewHolder.sale = (TextView) convertView.findViewById(R.id.today_sale_item_sale);
            viewHolder.discount = (TextView) convertView.findViewById(R.id.today_sale_item_discount);
            viewHolder.shouru = (TextView) convertView.findViewById(R.id.today_sale_item_shouru);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        double price = goodsDtos.get(position).getPrice();

        viewHolder.name.setText(goodsDtos.get(position).getName());
        double num = goodsDtos.get(position).getNum();
        viewHolder.num.setText(String.valueOf(num));
        if(goodsDtos.get(position).isSetFlag()){
            viewHolder.sale.setText(String.valueOf(DoubleSave.doubleSaveTwo(price * num*card_discount)));
            viewHolder.discount.setText(String.valueOf(DoubleSave.doubleSaveTwo(price * num* (1- card_discount))));
            viewHolder.shouru.setText(String.valueOf(DoubleSave.doubleSaveTwo(price * num* card_discount)));

        }else {
            viewHolder.sale.setText(String.valueOf(DoubleSave.doubleSaveTwo(price * num)));
            viewHolder.discount.setText(String.valueOf(DoubleSave.doubleSaveTwo(goodsDtos.get(position).getDiscount())));
            viewHolder.shouru.setText(String.valueOf(DoubleSave.doubleSaveTwo(price * num - goodsDtos.get(position).getDiscount())));

        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView num;
        TextView sale;
        TextView discount;
        TextView shouru;
    }
}
