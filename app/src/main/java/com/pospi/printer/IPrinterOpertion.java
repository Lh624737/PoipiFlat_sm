package com.pospi.printer;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import com.android.print.sdk.PrinterInstance;


/**
 * 打印机操作的接口
 */
public interface IPrinterOpertion {
    /**
     * 打开连接
     * @param data
     */
	 void open(Intent data);

    /**
     * 关闭连接
     */
	 void close();

    /**
     * 选择连接的设备
     */
	 void chooseDevice();

    /**
     * 得到打印机的实例
     * @return 返回一个打印机的实例
     */
	 PrinterInstance getPrinter();


	/**
	 * 首先进行判断  根据不同的连接打印机的方式去调用不同的方法
	 * 当连接的方式是usb的时候
	 * @param manager
	 */
	 void usbAutoConn(UsbManager manager);

	/**
	 * 当连接的方式是wifi或者是蓝牙连接的时候
	 * wifi和USB连接都
	 * @param context
	 * @param mHandler
	 */
	 void btAutoConn(Context context, Handler mHandler);


}
