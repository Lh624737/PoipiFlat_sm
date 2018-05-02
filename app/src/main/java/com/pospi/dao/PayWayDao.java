package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pospi.database.DB;
import com.pospi.dto.PayWayDto;
import com.pospi.util.GetData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/7/22.
 */
public class PayWayDao {

    private DB goodsDB;
    private String tablename = "PayWayInfo";

    private PayWayDto dto;
    private String shopId;
    private ContentValues values;

    public PayWayDao(Context context) {
        goodsDB = new DB(context);//实例化数据库
        shopId = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getString("Id", "");
    }

    /***
     * 给数据库里面添加数据
     * + "id integer primary key autoincrement,colorCodeShow text,Sid text," +
     * "UId text,Name text,colorCode text,orderBy text,payType1 text," +
     * "status integer,AliPayUrl text,shopId text)";
     */
    public void addPayWay(List<PayWayDto> dtos) {
        SQLiteDatabase db = null;
        try {
            db = goodsDB.getWritableDatabase();
            clearFeedTable(db);//先删除表格里面的所有的数据
            for (int i = 0; i < dtos.size(); i++) {
                PayWayDto dto = dtos.get(i);
                values = new ContentValues();
                values.put("colorCodeShow", dto.getColorCodeShow());
                values.put("Sid", dto.getSid());
                values.put("UId", dto.getUId());
                values.put("Name", dto.getName());
                values.put("colorCode", dto.getColorCode());
                values.put("orderBy", dto.getOrderBy());
                values.put("payType1", dto.getPayType1());
                Log.i("payType1", "存储的付款方式："+dto.getPayType1());
                values.put("status", dto.getStatus());
                values.put("AliPayUrl", dto.getAliPayUrl());
                values.put("shopId", shopId);
                values.put("payWayId", GetData.getStringRandom(32));
                db.insert(tablename, null, values);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     */

    public void clearFeedTable(SQLiteDatabase db) {
        String sql = "DELETE FROM " + tablename + ";";
        db.execSQL(sql);
    }

    /**
     * "id integer primary key autoincrement,colorCodeShow text,Sid text," +
     * "UId text,Name text,colorCode text,orderBy text,payType1 text," +
     * "status integer,AliPayUrl text,shopId text)";
     *
     * @return
     */
    public List<PayWayDto> findAllPayWay() {
        List<PayWayDto> listgoodsdto = new ArrayList<>();
        SQLiteDatabase db = goodsDB.getReadableDatabase();
        Cursor cursor = db.query(tablename, null, "shopId=?", new String[]{shopId}, null, null, null);
        while (cursor.moveToNext()) {
            PayWayDto dto1 = new PayWayDto();
            dto1.setAliPayUrl(cursor.getString(cursor.getColumnIndex("AliPayUrl")));
            dto1.setColorCode(cursor.getString(cursor.getColumnIndex("colorCode")));
            dto1.setColorCodeShow(cursor.getString(cursor.getColumnIndex("colorCodeShow")));
            dto1.setName(cursor.getString(cursor.getColumnIndex("Name")));
            dto1.setOrderBy(cursor.getString(cursor.getColumnIndex("orderBy")));
            dto1.setPayType1(Integer.parseInt(cursor.getString(cursor.getColumnIndex("payType1"))));
            Log.i("payType1", "获取到的付款方式--" + Integer.parseInt(cursor.getString(cursor.getColumnIndex("payType1"))));
            dto1.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            dto1.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            dto1.setUId(cursor.getString(cursor.getColumnIndex("UId")));
            dto1.setPayWayId(cursor.getString(cursor.getColumnIndex("payWayId")));
            listgoodsdto.add(dto1);
//            Log.i("name", dto1.getName());
        }
        cursor.close();
        db.close();
        return listgoodsdto;
    }

    public String findPaySid(String payType) {
        SQLiteDatabase db = goodsDB.getReadableDatabase();
        String Sid = "";
        Cursor cursor = db.query(tablename, null, "payType1=?", new String[]{payType}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new PayWayDto();
            //遍历Cursor对象，取出数据并打印
            Sid = cursor.getString(cursor.getColumnIndex("Sid"));
        }
        cursor.close();
        db.close();
        return Sid;
    }

    public String findPayName(String payType) {
        SQLiteDatabase db = goodsDB.getReadableDatabase();
        String name = "";
        Cursor cursor = db.query(tablename, null, "payType1=?", new String[]{payType}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new PayWayDto();
            //遍历Cursor对象，取出数据并打印
            name = cursor.getString(cursor.getColumnIndex("Name"));
        }
        cursor.close();
        db.close();
        return name;
    }
}
