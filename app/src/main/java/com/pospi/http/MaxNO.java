package com.pospi.http;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.lany.sp.SPHelper;
import com.pospi.dao.OrderDao;
import com.pospi.dto.OrderDto;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.util.App;
import com.pospi.util.GetData;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Qiyan on 2016/5/20.
 * <p/>
 * 得到当前可以用的最大的订单号
 */
public class MaxNO {


    public static String getMaxNo(Context context) {
        String maxNo;
        List<OrderDto> orderDtos = new OrderDao(context).findMaxNO(GetData.getYYMMDDTime());

        if (orderDtos.size() == 0) {
            maxNo = getPhoneMac()+GetData.getYYMMDDNoSpellingTime() + "0001";

        } else {
            String m = orderDtos.get(0).getMaxNo();
            //流水号
            String number = m.substring(m.length() - 4, m.length());
            maxNo = getPhoneMac()+GetData.getYYMMDDNoSpellingTime() +String.format("%04d", Integer.parseInt(number)+1);
        }

        return maxNo;
    }

    //获取登录手机的Mac。但是该方法必须要连接上了wifi才可以
    public static String getPhoneMac() {
        return SPHelper.getInstance().getString(Constant.SYJH);
//        String macAddress = "000000000000";
//        try {
//            //首先得到系统的服务
//            WifiManager wifiManager = (WifiManager) App.getContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
//            //当info 不是为空的时候
//            if (info != null) {
//                //当info.getMacAddress不为空的时候
//                if (!TextUtils.isEmpty(info.getMacAddress())) {
//                    macAddress = info.getMacAddress().replace(":", "");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.i("getPhoneMac: ", "getPhoneMac: " + macAddress);
//        return macAddress;
    }

    public static String getDriverId(){
        SharedPreferences sp = App.getContext().getSharedPreferences("token", MODE_PRIVATE);
        String id= sp.getString("driverId", "");
        if (id.equals("")) {
            return "";
        }
        return id.substring(id.length() - 2, id.length());
    }
}