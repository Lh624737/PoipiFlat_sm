package com.pospi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pospi.util.App;

/**
 * Created by huangqi on 2016/11/16.
 */
public class ErpReceiver extends BroadcastReceiver {
    private final static String TAG = "ERPService";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i(TAG, "onReceive: ");
        if (App.isUpLoad()) {
            SharedPreferences preferences = context.getSharedPreferences("ERP", Context.MODE_PRIVATE);
            String url = preferences.getString("url", "");//地址
            if (!TextUtils.isEmpty(url)) {
                Intent i = new Intent(context, ERPService.class);
                context.startService(i);
            }
        }
    }
}