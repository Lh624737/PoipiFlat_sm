package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pospi.dto.OrderDto;
import com.pospi.dto.Tabledto;
import com.pospi.pai.yunpos.R;
import com.pospi.util.TurnSize;
import com.pospi.util.constant.tableinfo.TableStatusConstance;

import java.util.List;

/**
 * Created by Qiyan on 2016/7/20.
 */
public class TableGridAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDto> orderDtos;
    private List<Tabledto> tabledtos;

    public TableGridAdapter(Context context, List<OrderDto> orderDtos, List<Tabledto> tabledtos) {
        this.context = context;
        this.orderDtos = orderDtos;
        this.tabledtos = tabledtos;
    }

    @Override
    public int getCount() {
        return tabledtos.size();
    }

    @Override
    public Object getItem(int position) {
        return tabledtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_grid_item, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(TurnSize.dip2px(context, 150), TurnSize.dip2px(context, 150)));
            holder = new ViewHolder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.hasOrderTableLayout);
            holder.rl = (RelativeLayout) convertView.findViewById(R.id.noOrderLayout);
            holder.noName = (TextView) convertView.findViewById(R.id.noOrderTableName);
            holder.hasName = (TextView) convertView.findViewById(R.id.hasOrderTableName);
            holder.maxNO = (TextView) convertView.findViewById(R.id.hasOrderTableMaxNo);
            holder.time = (TextView) convertView.findViewById(R.id.hasOrderTableTime);
            holder.price = (TextView) convertView.findViewById(R.id.hasOrderTablePrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (tabledtos.get(position).getStatus() == TableStatusConstance.Status_Free) {
            holder.rl.setVisibility(View.VISIBLE);
            holder.ll.setVisibility(View.GONE);
            holder.rl.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
            holder.noName.setText(tabledtos.get(position).getName());
        } else {
            holder.ll.setVisibility(View.VISIBLE);
            holder.ll.setBackgroundColor(context.getResources().getColor(R.color.blue_theme));
            holder.rl.setVisibility(View.GONE);
            holder.hasName.setText(tabledtos.get(position).getName());
            for (int i = 0; i < orderDtos.size(); i++) {
                if (orderDtos.get(i).getTableNumber().equals(tabledtos.get(position).getSid())) {
                    holder.price.setText(orderDtos.get(i).getYs_money());
                    holder.time.setText(orderDtos.get(i).getDetailTime());
                    holder.maxNO.setText(String.valueOf(orderDtos.get(i).getMaxNo()));
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        RelativeLayout rl;
        LinearLayout ll;
        TextView noName;
        TextView hasName;
        TextView maxNO;
        TextView time;
        TextView price;
    }
}
