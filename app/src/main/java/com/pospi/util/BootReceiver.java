package com.pospi.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pospi.pai.pospiflat.login.SpalishActivity;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent StartIntent = new Intent(context, SpalishActivity.class); //接收到广播后，跳转到MainActivity
        StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(StartIntent);

    }
}