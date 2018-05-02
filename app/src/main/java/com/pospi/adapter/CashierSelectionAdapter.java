package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.CashierMsgDto;
import com.pospi.pai.pospiflat.R;

import java.util.List;

;

/**
 * Created by Qiyan on 2016/4/13.
 */
public class CashierSelectionAdapter extends BaseAdapter {
    private Context context;
    private List<CashierMsgDto> cashierMsgDtos;

    public CashierSelectionAdapter(Context context, List<CashierMsgDto> cashierMsgDtos) {
        this.context = context;
        this.cashierMsgDtos = cashierMsgDtos;
    }

    @Override
    public int getCount() {
        return cashierMsgDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return cashierMsgDtos.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.cashierselection_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.cashierselection_item_tv_name);
            holder.tv_num = (TextView) convertView.findViewById(R.id.cashierselection_item_tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.tv_num.setText(cashierMsgDtos.get(position).getNumber());
        holder.tv_name.setText("("+cashierMsgDtos.get(position).getName()+")");
        return convertView;
    }


    class ViewHolder {
        TextView tv_name;
        TextView tv_num;
    }
}
