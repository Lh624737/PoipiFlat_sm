package com.pospi.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static String getYYMMDDhhmmss(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
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

    public static String getAnyTime(int t) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -t);
        String time = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return time;
    }
    //获取当月一号
    public static String getFirstDate() {
        //当月一号
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1=Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar1.getTime());
    }

    //获取时间差是否大于指定长度
    public static boolean compareTime(String nowTime, String targetTime,String t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long nt = sdf.parse(nowTime).getTime();
            long tt = sdf.parse(targetTime).getTime();
            double time = (double) (nt - tt)/1000/60;

            if (time > Double.parseDouble(t)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
