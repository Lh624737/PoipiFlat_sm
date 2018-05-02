package com.pospi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.RemoteException;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.pospi.dto.ValueCardDto;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.constant.URL;

import me.xiaopan.android.preference.PreferencesUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by acer on 2017/6/14.
 * 打印机
 */

public class Printer {
    private MyPrinter myPrinter;
    private GpService mGpService = null;
    public void print(Context context, final String shishou , String payway , String maxNo, final ValueCardDto valueCardDto , final String sid ) {
        final String goods = context.getSharedPreferences("goodsdto_json", MODE_PRIVATE).getString("json", null);
        switch (Build.MODEL) {
            case URL.MODEL_D800:
                byte status = PrnTest.prnStatus();
                if (status == 0x00) {
                    myPrinter = PrinterFactory.dPrinter(context, maxNo, payway);
                } else if (status == 0x02) {
                    Toast.makeText(context, "打印机缺纸", Toast.LENGTH_SHORT).show();
                    return;
                } else if (status == 0x08) {
                    Toast.makeText(context, "打印机发热", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(context, "打印机故障", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case URL.MODEL_DT92:
                mGpService = App.getmGpService();
                if (mGpService == null) {
                    Toast.makeText(context, "未连接到打印设备", Toast.LENGTH_SHORT).show();
                    Utils.connection();
                    mGpService = App.getmGpService();
                    try {
                        mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
                    } catch (RemoteException e) {
                        String gPrint_log = PreferencesUtils.getString(App.getContext(), "GPrintLog");
                        gPrint_log += GetData.getDataTime() + " 连接打印机端口时异常 " + e.getMessage() + "; ";
                        PreferencesUtils.putString(App.getContext(), "GPrintLog", gPrint_log);
                        e.printStackTrace();
                        return;
                    }
                }
                myPrinter = PrinterFactory.jbPrinter(mGpService, context, maxNo, payway);
                break;
            case URL.MODEL_T1:
                myPrinter = PrinterFactory.getSuMiPrinter(context, maxNo, payway);
                break;
            case URL.MODEL_E500:
                myPrinter = PrinterFactory.getEPrinter(context, "", "");
                break;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("receipt_num", MODE_PRIVATE);
        int receipt_num = sharedPreferences.getInt("receipt_num", 1);

        for (int i = 0; i < receipt_num; i++) {
            if (i > 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myPrinter.starPrint(goods, shishou, null, true, valueCardDto ,sid ,"");
                }
            }).start();
        }
    }
}
