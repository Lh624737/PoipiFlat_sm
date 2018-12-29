package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.CashierCashDto;
import com.pospi.pai.yunpos.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Qiyan on 2016/5/26.
 */
public class TodaySaleAllAdapter extends BaseAdapter {

    private Context context;
    private List<CashierCashDto> orderDtos;

    public TodaySaleAllAdapter(Context context, List<CashierCashDto> orderDtos) {
        this.context = context;
        this.orderDtos = orderDtos;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.todaysaleall_item, null);
            viewHolder.cashier = (TextView) convertView.findViewById(R.id.today_sale_all_item_cashier);
            viewHolder.num = (TextView) convertView.findViewById(R.id.today_sale_all_item_num);
            viewHolder.sale = (TextView) convertView.findViewById(R.id.today_sale_all_item_sale);
            viewHolder.discount = (TextView) convertView.findViewById(R.id.today_sale_all_item_discount);
            viewHolder.shouru = (TextView) convertView.findViewById(R.id.today_sale_all_item_shouru);
            viewHolder.gap = (TextView) convertView.findViewById(R.id.today_sale_all_item_gap);
            viewHolder.upload = (TextView) convertView.findViewById(R.id.today_sale_all_item_upload);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //得到收银员的名字
        viewHolder.cashier.setText(orderDtos.get(position).getCashierName());
        viewHolder.num.setText(String.valueOf(orderDtos.get(position).getNum()));// 查询写入每个收银员进行的交易的笔数
        viewHolder.sale.setText(String.valueOf(translateDouble(orderDtos.get(position).getSale())));
        viewHolder.discount.setText(String.valueOf(orderDtos.get(position).getDiscount()));//需要写入折扣额
        viewHolder.shouru.setText(String.valueOf(translateDouble(orderDtos.get(position).getIn())));
        viewHolder.upload.setText(String.valueOf(orderDtos.get(position).getPayment()));// 折扣的金额
        viewHolder.gap.setText(String.valueOf(translateDouble(orderDtos.get(position).getGap())));

        return convertView;
    }

    /**
     * 只保留两位小数
     * @param b
     * @return
     */
    public double translateDouble(double b){
        BigDecimal b2 = new BigDecimal(b);
        double f3 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f3;
    }

    class ViewHolder {
        TextView cashier;
        TextView num;
        TextView sale;
        TextView discount;
        TextView shouru;
        TextView gap;
        TextView upload;
    }
}
