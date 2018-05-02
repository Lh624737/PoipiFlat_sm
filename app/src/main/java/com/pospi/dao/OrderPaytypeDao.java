package com.pospi.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pospi.database.DB;
import com.pospi.dto.OrderPaytype;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/6/16.
 */

public class OrderPaytypeDao {
    private DB db;
    private String tablename = "orderinfo";

    private Context context;
    public OrderPaytypeDao(Context context) {
        db = new DB(context);//实例化数据库
        this.context = context;
    }

    public void addPaytype(OrderPaytype paytype) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("insert into order_paytype(sid ,name ,payCode ,orderSid,ys ,ss ,zl)values(?,?,?,?,?,?,?)",
                new Object[]{paytype.getSid() ,paytype.getPayName(),paytype.getPayCode() ,paytype.getOrderSid(),paytype.getYs(),paytype.getSs() ,paytype.getZl()});
        database.close();

    }
    //查询对应订单下的所有支付方式
    public List<OrderPaytype> query(String orderSid) {
        List<OrderPaytype> list = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.query("order_paytype", null, "orderSid =?", new String[]{orderSid}, null, null, null);
        while (cursor.moveToNext()) {
            OrderPaytype paytype = new OrderPaytype();
            paytype.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            paytype.setPayName(cursor.getString(cursor.getColumnIndex("name")));
            paytype.setPayCode(cursor.getInt(cursor.getColumnIndex("payCode")));
            paytype.setOrderSid(cursor.getString(cursor.getColumnIndex("orderSid")));
            paytype.setYs(cursor.getString(cursor.getColumnIndex("ys")));
            paytype.setSs(cursor.getString(cursor.getColumnIndex("ss")));
            paytype.setZl(cursor.getString(cursor.getColumnIndex("zl")));
            list.add(paytype);
        }
        cursor.close();
        database.close();
        return list;
    }
}
