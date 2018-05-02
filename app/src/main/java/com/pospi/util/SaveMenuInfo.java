package com.pospi.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pospi.dto.MenuDto;

import java.util.List;

/**
 * Created by Qiyan on 2016/6/15.
 */
public class SaveMenuInfo {

    public static void saveAsJson(Context context, List<MenuDto> dtos) {
        Gson gson = new Gson();
        String json = gson.toJson(dtos);
//        Log.i("json", json);
        SharedPreferences.Editor editor = context.getSharedPreferences("MenuDto_json", context.MODE_PRIVATE).edit();
        editor.putString("json", json);
        editor.apply();

    }

    /**
     * 把订单的信息转换为List
     *
     * @param str
     * @return
     */
    public static List<MenuDto> changeToList(String str) {
        Gson gson = new Gson();
        List<MenuDto> dtos = gson.fromJson(str, new TypeToken<List<MenuDto>>() {
        }.getType());
        return dtos;
    }
}
