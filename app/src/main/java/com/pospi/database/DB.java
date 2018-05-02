package com.pospi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Qiyan on 2016/5/25.
 * <p/>
 * 创建一个用于存储订单信息的数据库
 */
public class DB extends SQLiteOpenHelper {
    private String tableOrder = "orderinfo";
    private String tableGoods = "goods";
    private String tableMember = "memberinfo";
    private String tableTable = "Tableinfo";
    private String tableArea = "areaInfo";
    private String tableDinner = "dinnerInfo";
    private String tablePayWay = "PayWayInfo";
    private String print = "printInfo";
    private String tableLog = "table_log";
    /**
     * 创建Log表格
     */
    public final String CREATE_Log_Table = "create table if not exists " + tableLog + "("
            + "id integer primary key autoincrement,"
            + "name text,time text,message text)";
    /**
     * 创建商品表格
     */
    public final String CREATE_Goods_Table = "create table if not exists " + tableGoods + "("
            + "id integer primary key autoincrement,Sid text,"
            + "UId text,category_sid text,colorCode text,group_sid text,"
            + "image text,name text,unit text,mainPrinterSid text,"
            + "backPrinterSid text,Code text,createTime text,orderBy integer,"
            + "isHide integer,IsDel integer,Code_bm text,specification text,"
            + "valuationType integer,genre integer,setFlag integer,setPids text,"
            + "setOldPrice text,colorCodeShow text,"
            + "price double,discount double,CostPrice double)";

    private String tableModifiedGroup = "modifiedgroup_info";
    private String tableModified = "modified_info";
    /**
     * 创建做法组表格
     */
    public final String CREATE_ModifiedGroup_Table = "create table if not exists " + tableModifiedGroup + "("
            + "id integer primary key autoincrement,Sid text,"
            + "Uid text,Name text,CreateTime text,option text,"
            + "orderBy text,option_bool double)";

    /**
     * 创建做法表格
     */
    public final String CREATE_Modified_Table = "create table if not exists " + tableModified + "("
            + "id integer primary key autoincrement,Sid text,"
            + "Uid text,Name text,GroupSid text,Price double,"
            + "CostPrice double,image text,unit text,colorCode text,"
            + "CreateTime text,orderBy text,colorCodeShow text)";

    /**
     * 创建订单表格,版本5,將maxNo interger改爲maxNo text
     */
    public final String CREATE_Order_Table = "create table if not exists " + tableOrder + "("
            + "id integer primary key autoincrement,maxNo text,orderType integer,payway text,"
            + "shop_id text,time text,ss_money text,ys_money text,zl_money text,"
            + "order_info text,cashiername text,detailTime text,checkoutTime text,"
            + "hasReturnGoods integer,out_trade_no text,payReturn text,ifFinishOrder integer,"
            + "serialNumber integer,tableNumber text,eatingNumber integer,orderId text,upLoadServer integer,upLoadERP integer"
            + ",orderNo text,miYaNumber text,orderSid text,tableId text)";

    /**
     * 创建会员表格
     */
    public final String CREATE_Member_TABLE = "create table if not exists " + tableMember + "("
            + "id integer primary key autoincrement,name text,number text,tel text,"
            + "address text,score float,getcardtime text,bornyear integer,bornmonth integer,"
            + "bornday integer)";


    public final String CREATE_Table_TABLE = "create table if not exists " + tableTable + "("
            + "sid text,uid text,width double,"
            + "x double,y double,height double,name text,sectionId text,tableType integer," +
            "status integer,shopId text,tableId text)";

    /**
     * 创建区域表格
     */
    public final String CREATE_Table_AREA = "create table if not exists " + tableArea + "("
            + "id integer primary key autoincrement,areaName text,areaNum integer,shopId text)";


    /**
     * private String colorCodeShow;
     * private String Sid;
     * private String UId;
     * private String Name;
     * private String colorCode;
     * private String orderBy;
     * private String payType1;
     * private int status;
     * private String AliPayUrl;
     * 创建支付方式的表格
     */
    public final String CREATE_Table_PayWay = "create table if not exists " + tablePayWay + "("
            + "colorCodeShow text,Sid text," +
            "UId text,Name text,colorCode text,orderBy text,payType1 text," +
            "status integer,AliPayUrl text,shopId text,payWayId text)";
   /* //创建订单表
    public final String CREATE_Order = "create table order ("
            + "id integer primary key autoincrement, "
            + "sid text, "
            + "id text, "
            + "lineNum integer,"
            + "serverNo text,"
            + "serverName text,"
            + "menus_sid text,"
            + "menus_name text,"
            + "m_price text,"
            + "price text,"
            + "saleprice text,"
            + "number integer,"
            + "discount double,"
            + "order_sid text,)";*/
    //创建订单详情表
    public final String CREATE_Order_Menus = "create table order_menus ("
                        + "id integer primary key autoincrement, "  
                        + "sid text, "
                        + "name text,"
                        + "price text,"
                        + "number integer,"
                        + "discount double,"
                        + "orderSid text,"
                        + "totalPrice double)";
    //创建订单支付方式表
    public final String CREATE_Order_PayType = "create table order_paytype("
                        + "id integer primary key autoincrement, "
                        + "sid text, "
                        + "name text,"
                        + "orderSid text,"
                        + "ys text,"
                        + "ss text,"
                        + "zl text,"
                        + "payCode integer)";
    /**
     * private int port;
     * private String ip;
     * private String name;
     * private String printId;
     *
     * @param context
     */

    public final String Create_Print = "create table if not exists " + print + "("
            + "port integer,ip text,name text,printId text,shopId text)";


    public DB(Context context) {
        super(context, "orderinfo.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("db", "onCreate");
        db.execSQL(CREATE_Goods_Table);
        db.execSQL(CREATE_Order_Table);
        db.execSQL(CREATE_Member_TABLE);
        db.execSQL(CREATE_Table_TABLE);
        db.execSQL(CREATE_Table_AREA);
        db.execSQL(CREATE_Table_PayWay);
        db.execSQL(Create_Print);
        db.execSQL(CREATE_Modified_Table);
        db.execSQL(CREATE_ModifiedGroup_Table);
        db.execSQL(CREATE_Order_Menus);
        db.execSQL(CREATE_Order_PayType);
    }

    private String CREATE_TEMP_BOOK = "alter table " + tableOrder + " rename to _temp_book";
    private String INSERT_DATA = "insert into " + tableOrder + "  select *,'' from _temp_book";
    private String DROP_BOOK = "drop table _temp_book";

    private String DROP_GOODS_BOOK = "drop table " + tableGoods;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (newVersion) {
//            case 2:
//                Log.i("db", "onUpgrade");
//                db.execSQL(CREATE_TEMP_BOOK);
//                db.execSQL(CREATE_Order_Table);
//                db.execSQL(INSERT_DATA);
//                db.execSQL(DROP_BOOK);
//                break;
//            case 3:
//                db.execSQL(CREATE_TEMP_BOOK);
//                db.execSQL(CREATE_Order_Table);
//                db.execSQL(INSERT_DATA);
//                db.execSQL(DROP_BOOK);
////                db.execSQL(CREATE_Log_Table);
//                break;
//            case 4:
//                db.execSQL(DROP_GOODS_BOOK);
//                db.execSQL(CREATE_Goods_Table);
//
//                db.execSQL(CREATE_Modified_Table);
//                db.execSQL(CREATE_ModifiedGroup_Table);
//                break;
//            case 5:
//                db.execSQL("alter table "+tableOrder+" add orderSid text");
//                db.execSQL(CREATE_Order_Menus);
//                db.execSQL(CREATE_Order_PayType);
//                break;
//        }
    }

}
