package com.pospi.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;

import java.util.List;

/**
 * Created by Qiyan on 2016/5/17.
 * <p/>
 * 把list写成Json数据存储起来
 */
public class Sava_list_To_Json {

    public static void changeToJaon(Context context, List<GoodsDto> dtos) {
        Gson gson = new Gson();
        String json = gson.toJson(dtos);
//        Log.i("json", json);
        SharedPreferences.Editor editor = context.getSharedPreferences("goodsdto_json", context.MODE_PRIVATE).edit();
        editor.putString("json", json);
        editor.commit();

        SharedPreferences.Editor editor1 = context.getSharedPreferences("goodsdto_json1", context.MODE_PRIVATE).edit();
        editor1.clear();
        editor1.putString("goodsMsg", json);
        editor1.commit();


    }
    public static void addShoppings(Context context,String json){
        SharedPreferences.Editor editor1 = context.getSharedPreferences("goodsdto_json1", context.MODE_PRIVATE).edit();
        editor1.clear();
        editor1.putString("goodsMsg", json);
        editor1.commit();
    }
    public static void clearGoodsMsg(Context context){
        SharedPreferences.Editor editor1 = context.getSharedPreferences("goodsdto_json1", context.MODE_PRIVATE).edit();
        editor1.clear();
        editor1.commit();
    }
    public static String changeOrderDtoToJaon(List<OrderDto> dtos) {
        Gson gson = new Gson();
        String json = gson.toJson(dtos);
//        Log.i("json", json);
        return json;
    }

    public static String changeGoodDtoToJaon(List<GoodsDto> dtos) {
        Gson gson = new Gson();
        String json = gson.toJson(dtos);
//        Log.i("json", json);
        return json;
    }


    public static List<OrderDto> changeOrderDtoToList(String str) {
        Gson gson = new Gson();
        List<OrderDto> dtos = gson.fromJson(str, new TypeToken<List<OrderDto>>() {
        }.getType());
        return dtos;
    }


    public static List<GoodsDto> changeToList(String str) {
        Gson gson = new Gson();
        List<GoodsDto> dtos = gson.fromJson(str, new TypeToken<List<GoodsDto>>() {
        }.getType());
        return dtos;
    }
}
