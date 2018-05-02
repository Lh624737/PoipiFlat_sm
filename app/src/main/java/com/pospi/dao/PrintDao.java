package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pospi.database.DB;
import com.pospi.dto.PrintDto;
import com.pospi.util.GetData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/7/25.
 */
public class PrintDao {

    private com.pospi.database.DB DB;
    private String tablename = "printInfo";

    private PrintDto dto;

    private Context context;
    private String shopId;
    private ContentValues values;

    public PrintDao(Context context) {
        DB = new DB(context);//实例化数据库
        this.context = context;
        shopId = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getString("Id", "");

    }

    /**
     * * private int port;
     * private String ip;
     * private String name;
     * private String printId;
     *
     * @param printDto
     * @return
     */
    public boolean addPrintInfo(PrintDto printDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        values = new ContentValues();
        values.put("printId", GetData.getStringRandom(32));
        values.put("name", printDto.getName());
        values.put("ip", printDto.getIp());
        values.put("port", printDto.getPort());
        values.put("shopId", shopId);
        if (db.insert(tablename, null, values) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    public List<PrintDto> findAllPrinter() {
        SQLiteDatabase db = DB.getReadableDatabase();
        List<PrintDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shopId=?", new String[]{shopId}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new PrintDto();
            //遍历Cursor对象，取出数据并打印
            dto.setPort(cursor.getInt(cursor.getColumnIndex("port")));
            dto.setIp(cursor.getString(cursor.getColumnIndex("ip")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setPrintId(cursor.getString(cursor.getColumnIndex("printId")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public boolean deletePrintDto(String id) {
        SQLiteDatabase db = DB.getWritableDatabase();
        if (db.delete(tablename, "printId=?", new String[]{id}) > 0) {
            return true;
        }
        db.close();
        return false;
    }

    public boolean updataPrintInfo(PrintDto printDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
//        Log.i("name", printDto.getName() + "之后");
        values = new ContentValues();
        values.put("name", printDto.getName());
        values.put("ip", printDto.getIp());
        values.put("port", printDto.getPort());
        if (db.update(tablename, values, "printId=?", new String[]{printDto.getPrintId()}) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }
}
