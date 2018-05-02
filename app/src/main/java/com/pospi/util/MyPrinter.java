package com.pospi.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.gprinter.aidl.GpService;
import com.pospi.dto.ValueCardDto;

/**
 * Created by acer on 2017/6/13.
 * 打印接口
 */

public interface MyPrinter {
    void starPrint(String goods, String payMoney, Bitmap qrCode, boolean isSale,ValueCardDto valueCardDto ,String sid,String tableId);
}
