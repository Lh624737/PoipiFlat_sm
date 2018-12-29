package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pospi.database.DB;
import com.pospi.dto.Tabledto;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.GetData;
import com.pospi.util.constant.tableinfo.TableStatusConstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/6/29.
 */
public class TableDao {

    private DB DB;
    private String tablename = "Tableinfo";

    private Tabledto dto;

    private Context context;
    private String shopId;
    private ContentValues values;
    private String Uid;

    public TableDao(Context context) {
        DB = DB.getInstance(context);
        this.context = context;
        shopId = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getString("Id", "");


    }

    public boolean addTableInfo(Tabledto tabledto) {
        findData();
        SQLiteDatabase db = DB.getWritableDatabase();
        values = new ContentValues();
        values.put("sid", GetData.getStringRandom(32));
        values.put("uid", Uid);
        values.put("width", tabledto.getWidth());
        values.put("x", tabledto.getX());
        values.put("y", tabledto.getY());
        values.put("height", tabledto.getHeight());
        values.put("name", tabledto.getName());
        values.put("sectionId", tabledto.getSectionId());
        values.put("tableType", tabledto.getTableType());
        values.put("status", tabledto.getStatus());
        values.put("shopId", shopId);
        if (db.insert(tablename, null, values) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public void findData() {
        int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        Uid = new CashierLogin_pareseJson().parese(
                context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getUid();
    }

    /**
     * 从网络上下载已经存在的桌台的信息
     */
    public void downLoadTableinfo(List<Tabledto> tabledtos) {
        SQLiteDatabase db = null;
        try {
            db = DB.getWritableDatabase();
            clearFeedTable(db);//首先会清空数据库
            for (int i = 0; i < tabledtos.size(); i++) {
                Tabledto tabledto = tabledtos.get(i);
                values = new ContentValues();
                values.put("sid", tabledto.getSid());
                values.put("uid", tabledto.getUid());
                values.put("width", tabledto.getWidth());
                values.put("x", tabledto.getX());
                values.put("y", tabledto.getY());
                values.put("height", tabledto.getHeight());
                values.put("name", tabledto.getName());
                values.put("sectionId", tabledto.getSectionId());
                values.put("tableType", tabledto.getTableType());
                values.put("status", TableStatusConstance.Status_Free);
                values.put("shopId", shopId);
                db.insert(tablename, null, values);
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     */
    public void clearFeedTable(SQLiteDatabase db) {
        String sql = "DELETE FROM " + tablename + ";";
        db.execSQL(sql);
    }

    public List<Tabledto> findTableInfo() {
        findData();
        SQLiteDatabase db = DB.getReadableDatabase();
        List<Tabledto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shopId=? and uid=?", new String[]{shopId, Uid}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new Tabledto();
            //遍历Cursor对象，取出数据并打印
            dto.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            dto.setWidth(cursor.getDouble(cursor.getColumnIndex("width")));
            dto.setX(cursor.getInt(cursor.getColumnIndex("x")));
            dto.setY(cursor.getDouble(cursor.getColumnIndex("y")));
            dto.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setSectionId(cursor.getString(cursor.getColumnIndex("sectionId")));
            dto.setTableType(cursor.getInt(cursor.getColumnIndex("tableType")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            dto.setShopId(cursor.getString(cursor.getColumnIndex("shopId")));
            Log.i("findTableInfo", "findTableInfo");
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public List<Tabledto> findFreeTableInfo() {
        findData();
        SQLiteDatabase db = DB.getReadableDatabase();
        List<Tabledto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shopId=? and uid=? and status=?", new String[]{shopId, Uid, String.valueOf(TableStatusConstance.Status_Free)}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new Tabledto();
            //遍历Cursor对象，取出数据并打印
            dto.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            dto.setWidth(cursor.getDouble(cursor.getColumnIndex("width")));
            dto.setX(cursor.getInt(cursor.getColumnIndex("x")));
            dto.setY(cursor.getDouble(cursor.getColumnIndex("y")));
            dto.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setSectionId(cursor.getString(cursor.getColumnIndex("sectionId")));
            dto.setTableType(cursor.getInt(cursor.getColumnIndex("tableType")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            dto.setShopId(cursor.getString(cursor.getColumnIndex("shopId")));
            Log.i("findTableInfo", "findTableInfo");
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public boolean updateTableInfo(Tabledto tabledto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sid", tabledto.getSid());
        values.put("width", tabledto.getWidth());
        values.put("x", tabledto.getX());
        values.put("y", tabledto.getY());
        values.put("height", tabledto.getHeight());
        values.put("name", tabledto.getName());
        values.put("tableType", tabledto.getTableType());
        values.put("status", tabledto.getStatus());
        if ((db.update(tablename, values, "shopId=? and areaName=? and tableName=?", new String[]{shopId})) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public boolean deleteTable(String tableNumber) {
        SQLiteDatabase db = DB.getWritableDatabase();

        if (db.delete(tablename, "shopId=? and sid=?", new String[]{shopId, tableNumber}) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }
}