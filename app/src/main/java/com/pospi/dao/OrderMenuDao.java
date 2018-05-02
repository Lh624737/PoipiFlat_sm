package com.pospi.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pospi.database.DB;
import com.pospi.dto.OrderDto;
import com.pospi.dto.OrderMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/6/16.
 */

public class OrderMenuDao {
    private DB db;
    private String tablename = "orderinfo";

    private Context context;
    public OrderMenuDao(Context context) {
        db = new DB(context);//实例化数据库
        this.context = context;
    }

    public void addMenu(OrderMenu orderMenu) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("insert into order_menus(sid ,name ,price ,number ,discount,totalPrice ,orderSid)values(?,?,?,?,?,?,?)",
                new Object[]{orderMenu.getSid() ,orderMenu.getName() ,orderMenu.getPrice() ,orderMenu.getNumber(),orderMenu.getDiscount(),orderMenu.getTotalPrice(),orderMenu.getOrderSid()});
        database.close();

    }
    //查询对应订单下的所有商品信息
    public List<OrderMenu> query(String orderSid) {
        List<OrderMenu> list = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.query("order_menus", null, "orderSid =?", new String[]{orderSid}, null, null, null);
        while (cursor.moveToNext()) {
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            orderMenu.setName(cursor.getString(cursor.getColumnIndex("name")));
            orderMenu.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            orderMenu.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            orderMenu.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));
            orderMenu.setTotalPrice(cursor.getDouble(cursor.getColumnIndex("totalPrice")));
            orderMenu.setOrderSid(cursor.getString(cursor.getColumnIndex("orderSid")));
            list.add(orderMenu);
        }
        cursor.close();
        database.close();
        return list;
    }


}
