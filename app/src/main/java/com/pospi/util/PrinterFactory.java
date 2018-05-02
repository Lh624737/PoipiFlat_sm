package com.pospi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.RemoteException;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.pospi.dto.ValueCardDto;
import com.pospi.gpprinter.GPprint;
import com.pospi.pai.pospiflat.pay.PayActivity;
import com.pospi.pai.pospiflat.util.EPrint;
import com.pospi.pai.pospiflat.util.SuMiPrint;
import com.pospi.paxprinter.PaxPrint;
import com.pospi.paxprinter.PrnTest;
import com.pospi.util.constant.URL;

import me.xiaopan.android.preference.PreferencesUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liuh on 2017/6/13.
 * 打印工厂类
 */

public class PrinterFactory {
    //佳博打印
    public static MyPrinter jbPrinter(GpService mGpService, Context context, String maxNO, String payway) {
        return new GPprint(mGpService, context, maxNO, payway);
    }
    //d800打印
    public static MyPrinter dPrinter(Context context, String maxNO, String payway) {
        return new PaxPrint(context, maxNO, payway);
    }
    //商米打印
    public static SuMiPrint getSuMiPrinter(Context context, String MaxNo, String payWay) {
        return new SuMiPrint(context ,MaxNo ,payWay);
    }
    //e500
    public static EPrint getEPrinter(Context context, String MaxNo, String payWay) {
        return new EPrint(context);
    }
}
