package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.OrderDto;
import com.pospi.pai.pospiflat.R;

import java.util.List;


public class RestOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDto> orderDtos;

    public RestOrderAdapter(Context context, List<OrderDto> orderDtos) {
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
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.rest_order_item,null);
            holder=new ViewHolder();
            holder.table = convertView.findViewById(R.id.rest_order_table);
            holder.no=(TextView)convertView.findViewById(R.id.rest_order_no);
            holder.money=(TextView)convertView.findViewById(R.id.rest_order_money);
            holder.time=(TextView)convertView.findViewById(R.id.rest_order_time);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        OrderDto orderDto=orderDtos.get(position);
        holder.table.setText(orderDto.getTableNumber());
        holder.no.setText(String.valueOf(orderDto.getMaxNo()));
        holder.time.setText(orderDto.getDetailTime());
        holder.money.setText(String.valueOf(orderDto.getYs_money()));

        return convertView;
    }


    class ViewHolder{
        TextView table;
        TextView no;
        TextView time;
        TextView money;
    }

}
