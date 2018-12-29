package com.pospi.util;

import android.content.Context;

import com.gprinter.aidl.GpService;
import com.pospi.dto.OrderDto;
import com.pospi.gpprinter.GPprint;
import com.pospi.pai.yunpos.util.EPrint;
import com.pospi.pai.yunpos.util.SuMiPrint;
import com.pospi.paxprinter.PaxPrint;

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
    public static SuMiPrint getSuMiPrinter(Context context, OrderDto orderDto) {
        return new SuMiPrint(context , orderDto);
    }
    //e500
    public static EPrint getEPrinter(Context context, String MaxNo, String payWay) {
        return new EPrint(context);
    }
    //e500
    public static SGprint getSGrinter(Context context, OrderDto orderDto) {
        return new SGprint(context,orderDto);
    }
}
