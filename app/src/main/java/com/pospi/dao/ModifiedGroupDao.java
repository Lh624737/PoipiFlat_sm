package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pospi.database.DB;
import com.pospi.dto.ModifiedGroupDto;

import java.util.List;

/**
 * 会员
 * Created by Qiyan on 2016/6/8.
 */
public class ModifiedGroupDao {
    private String tableName = "modifiedgroup_info";

    private DB db;
    private Context context;
    private ModifiedGroupDto dto;
    private ContentValues values;

    public ModifiedGroupDao(Context context) {
        this.context = context;
        db = new DB(context);
    }

    /***
     * 给数据库里面添加数据
     */
    public void addModifiedGroup(List<ModifiedGroupDto> dtos) {
        SQLiteDatabase database = null;
        try {
            database = db.getWritableDatabase();
            clearFeedTable(database);//先删除表格里面的所有的数据
            for (int i = 0; i < dtos.size(); i++) {
                ModifiedGroupDto dto = dtos.get(i);
                values = new ContentValues();
                values.put("Sid", dto.getSid());
                values.put("Uid", dto.getUid());
                values.put("Name", dto.getName());
                values.put("CreateTime", dto.getCreateTime());
                values.put("option", dto.getOption());
                values.put("orderBy", dto.getOrderBy());
                values.put("option_bool", dto.isOption_bool() ? 1 : 0);
                database.insert(tableName, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
    }

    /**
     * 根据groupsid查找对应的modifiedgroup.Name
     *
     * @return
     */
    public String findModifiedGroupByGroupSid(String groupSid) {
        SQLiteDatabase database = db.getReadableDatabase();
        String name = "";
        Cursor cursor = database.query(tableName, null, "Sid=?", new String[]{groupSid}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new ModifiedGroupDto();
            //遍历Cursor对象，取出数据并打印
            name = cursor.getString(cursor.getColumnIndex("Name"));
        }
        cursor.close();
        database.close();
        return name;
    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     *
     * @param database
     */
    private void clearFeedTable(SQLiteDatabase database) {
        String sql = "DELETE FROM " + tableName + ";";
        database.execSQL(sql);
    }


}
