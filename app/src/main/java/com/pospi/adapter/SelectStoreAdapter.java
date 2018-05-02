package com.pospi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.StoreMsgDto;
import com.pospi.pai.pospiflat.R;

import java.util.List;

/**
 * Created by Qiyan on 2016/5/11.
 */
public class SelectStoreAdapter extends BaseAdapter {

    private Context context;
    private List<StoreMsgDto> storeName;


    public SelectStoreAdapter(Context context, List<StoreMsgDto> storeName) {
        this.context = context;
        this.storeName = storeName;
    }

    @Override
    public int getCount() {
        return storeName.size();
    }

    @Override
    public Object getItem(int position) {
        return storeName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.select_store_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
        Log.i("storeName", storeName.get(position).getName());
        tv.setText(storeName.get(position).getName());
        return convertView;
    }
}
