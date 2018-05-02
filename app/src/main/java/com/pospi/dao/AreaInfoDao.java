package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pospi.database.DB;
import com.pospi.dto.AreaInfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/6/30.
 */
public class AreaInfoDao {

    private com.pospi.database.DB DB;
    private String tablename = "areaInfo";

    private AreaInfoDto dto;

    private Context context;
    private String shopId;

    public AreaInfoDao(Context context) {
        DB = new DB(context);//实例化数据库
        this.context = context;
        shopId = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getString("Id", "");
    }

    /**
     * 添加数据
     *
     * @param areaInfoDto
     * @return
     */
    public boolean addTableInfo(AreaInfoDto areaInfoDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("areaNum", areaInfoDto.getAreaNum());
        values.put("areaName", areaInfoDto.getAreaName());
        values.put("shopId", shopId);

        if (db.insert(tablename, null, values) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public List<AreaInfoDto> findTableInfo() {
        SQLiteDatabase db = DB.getReadableDatabase();
        List<AreaInfoDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shopId=?", new String[]{shopId}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new AreaInfoDto();
            //遍历Cursor对象，取出数据并打印
            dto.setAreaName(cursor.getString(cursor.getColumnIndex("areaName")));
            dto.setAreaNum(cursor.getInt(cursor.getColumnIndex("areaNum")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public boolean updateTableInfo(int areaNum, AreaInfoDto areaInfoDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("areaNum", areaInfoDto.getAreaNum());
        values.put("areaName", areaInfoDto.getAreaName());
        if ((db.update(tablename, values, "areaNum=?", new String[]{String.valueOf(areaNum)})) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public boolean deleteTable(int areaNum) {
        SQLiteDatabase db = DB.getWritableDatabase();
        if (db.delete(tablename, "areaNum=?", new String[]{String.valueOf(areaNum)}) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

}

