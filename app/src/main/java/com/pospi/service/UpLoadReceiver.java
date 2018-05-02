package com.pospi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pospi.util.App;
import com.pospi.util.Utils;

/**
 * Created by huangqi on 2016/11/16.
 */
public class UpLoadReceiver extends BroadcastReceiver {
    private final static String TAG = "UpLoadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i(TAG, "onReceive: ");
        if (!Utils.isServiceWork(context, "com.pospi.service.UpLoadService")) {//判断服务的状态
            if (App.isUpLoad()) {
                Intent i = new Intent(context, UpLoadService.class);
                context.startService(i);
            }
        }
    }

}