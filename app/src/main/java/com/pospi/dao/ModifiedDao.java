package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pospi.database.DB;
import com.pospi.dto.ModifiedDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员
 * Created by Qiyan on 2016/6/8.
 */
public class ModifiedDao {
    private String tableName = "modified_info";

    private DB db;
    private Context context;
    private ModifiedDto dto;
    private ContentValues values;

    public ModifiedDao(Context context) {
        this.context = context;
        db = DB.getInstance(context);
    }

    /**
     * 给数据库里面添加数据
     */
    public void addModified(List<ModifiedDto> dtos) {
        SQLiteDatabase database = null;
        try {
            database = db.getWritableDatabase();
            clearFeedTable(database);//先删除表格里面的所有的数据
            for (int i = 0; i < dtos.size(); i++) {
                ModifiedDto dto = dtos.get(i);
                values = new ContentValues();
                values.put("Sid", dto.getSid());
                values.put("Uid", dto.getUid());
                values.put("Name", dto.getName());
                values.put("GroupSid", dto.getGroupSid());
                values.put("Price", dto.getPrice());
                values.put("unit", dto.getUid());
                values.put("image", dto.getImage());
                values.put("colorCode", dto.getColorCode());
                values.put("CreateTime", dto.getCreateTime());
                values.put("orderBy", dto.getOrderBy());
                values.put("colorCodeShow", dto.getColorCodeShow());
                long result = database.insert(tableName, null, values);
                Log.i("GroupSid", "GroupSid=" + dto.getGroupSid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.close();
    }

    /**
     * 根据groupsid查找对应的modified集合
     *
     * @return
     */
    public List<ModifiedDto> findModifiedByGroupSid(String groupSid) {
        List<ModifiedDto> list_modified = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(tableName, null, "GroupSid=?", new String[]{groupSid}, null, null, null);
        while (cursor.moveToNext()) {
            ModifiedDto dto1 = new ModifiedDto();
            dto1.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            dto1.setUid(cursor.getString(cursor.getColumnIndex("Uid")));
            dto1.setName(cursor.getString(cursor.getColumnIndex("Name")));
            dto1.setGroupSid(cursor.getString(cursor.getColumnIndex("GroupSid")));
            dto1.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
            dto1.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            dto1.setImage(cursor.getString(cursor.getColumnIndex("image")));
            dto1.setColorCode(cursor.getString(cursor.getColumnIndex("colorCode")));
            dto1.setCreateTime(cursor.getString(cursor.getColumnIndex("CreateTime")));
            dto1.setOrderBy(cursor.getString(cursor.getColumnIndex("orderBy")));
            dto1.setColorCodeShow(cursor.getString(cursor.getColumnIndex("colorCodeShow")));
            list_modified.add(dto1);
            Log.i("name", dto1.getName());
        }
        cursor.close();
        database.close();
        return list_modified;
    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     *
     * @param database
     */
    public void clearFeedTable(SQLiteDatabase database) {
        String sql = "DELETE FROM " + tableName + ";";
        database.execSQL(sql);
        database.execSQL("update sqlite_sequence set seq=0 where name='" + tableName + "'");
    }


}
