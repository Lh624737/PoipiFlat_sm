package com.pospi.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Qiyan on 2016/5/16.
 * 用于得到不同的模式的时间
 */
public class GetData {
    public static String getDataTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String getHHmmssTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }
    public static String getHHmmssTimet() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getYYMMDDTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
    public static String getDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }
    public static String getDate2(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }
    public static String getHHmm(){
        SimpleDateFormat sdf=new SimpleDateFormat("HHmm");
        return sdf.format(new Date());
    }
    public static String getYYMMDDNoSpellingTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date());
    }
    public static String getYYMMDD(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date());
    }
    //通过SimpleDateFormat获取24小时制时间
    /**
     * 随机生成一个长度为length的字符串
     * @param length 字符串的长度
     * @return
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = 65;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }
    public static String getStringRandom() {
        int length =26;
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = 65;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        String time = getYYMMDD();
        Log.i("number", time + val);
        return time+val;
    }
}
