package com.pospi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pospi.dto.Tablebeen;
import com.pospi.pai.pospiflat.R;

import java.util.List;

/**
 * Created by acer on 2018/4/13.
 * 餐桌
 */

public class TableAdapter extends BaseAdapter {
    private Context context;
    private List<Tablebeen> tablebeens;

    public TableAdapter(Context context, List<Tablebeen> tablebeens) {
        this.context = context;
        this.tablebeens = tablebeens;
    }

    @Override
    public int getCount() {
        return tablebeens.size();
    }

    @Override
    public Object getItem(int i) {
        return tablebeens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.table_item, null);
            holder.ll = (LinearLayout) view.findViewById(R.id.table_ll);
            holder.no = (TextView) view.findViewById(R.id.item_status);
            holder.name = (TextView) view.findViewById(R.id.item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//            GoodsDto goodsBean = goodsBeens.get(position);
//            holder.name.setText(goodsBean.getName().trim());
        Tablebeen tb = tablebeens.get(i);
        holder.name.setText(tb.getNumber()+"号桌");
        if (tb.getStatus() == 0) {
            holder.no.setText("空桌");
            holder.no.setTextColor(Color.GREEN);
        } else {
            holder.no.setText("就餐中");
            holder.no.setTextColor(Color.RED);
            holder.ll.setBackgroundColor(Color.GRAY);
            holder.ll.setClickable(false);
        }
        return view;
    }

    class ViewHolder {
        TextView no;
        TextView name;
        LinearLayout ll;

    }

}
