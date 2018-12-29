package com.pospi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pospi.dto.Tablebeen;
import com.pospi.pai.yunpos.R;

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
            holder.ll =  view.findViewById(R.id.table_ll);
            holder.name = view.findViewById(R.id.item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//            GoodsDto goodsBean = goodsBeens.get(position);
//            holder.name.setText(goodsBean.getName().trim());
        Tablebeen tb = tablebeens.get(i);
        holder.name.setText(tb.getNumber()+"号桌");
        if (tb.getStatus() == 0) {
            holder.name.setText(tb.getNumber() + "号桌");
        } else {
            holder.ll.setClickable(false);
            holder.ll.setBackground(context.getResources().getDrawable(R.drawable.bg_table));
            holder.name.setText("就餐中");
        }
        return view;
    }

    class ViewHolder {
        TextView no;
        TextView name;
        RelativeLayout ll;

    }

}
