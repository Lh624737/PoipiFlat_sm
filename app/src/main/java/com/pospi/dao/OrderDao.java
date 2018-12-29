package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lany.sp.SPHelper;
import com.pospi.database.DB;
import com.pospi.dto.OrderDto;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.util.GetData;
import com.pospi.util.UpLoadServer;
import com.pospi.util.UploadERP;
import com.pospi.util.constant.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/5/25.
 */
public class OrderDao {

    private DB DB;
    private String tablename = "orderinfo";

    private OrderDto dto;

    private Context context;

    public OrderDao(Context context) {
        DB = DB.getInstance(context);
        this.context = context;
    }

    /***
     * 给数据库里面添加数据
     */
    public void addOrder(OrderDto orderDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        Log.i("addOrder()", "orderDto.getMiYaNumber(): "+orderDto.getMiYaNumber());
       // String sid = orderDto.getOrderSid();
        db.execSQL("insert into orderinfo(" +
                "maxNo,orderType,payway,shop_id,time,ss_money,ys_money,zl_money,order_info," +
                "cashiername,detailTime,checkoutTime,hasReturnGoods,out_trade_no,payReturn,ifFinishOrder," +
                "serialNumber,tableNumber,eatingNumber,orderId,upLoadServer,upLoadERP,orderNo,miYaNumber,orderSid,tableId,vipNumber)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        orderDto.getMaxNo(), orderDto.getOrderType(), orderDto.getPayway(),
                        orderDto.getShop_id(), orderDto.getTime(), orderDto.getSs_money(),
                        orderDto.getYs_money(), orderDto.getZl_money(), orderDto.getOrder_info(),
                        orderDto.getCashiername(), orderDto.getDetailTime(), orderDto.getCheckoutTime(),
                        orderDto.getHasReturnGoods(), orderDto.getOut_trade_no(), orderDto.getPayReturn(),
                        orderDto.getIfFinishOrder(), orderDto.getSerialNumber(), orderDto.getTableNumber(),
                        orderDto.getStatus(), orderDto.getOrderId(), orderDto.getUpLoadServer(),
                        orderDto.getUpLoadERP(), orderDto.getOrderNo(),orderDto.getMiYaNumber(),
                        orderDto.getOrderSid(),orderDto.getTableId(),orderDto.getVipNumber()
                });
        db.close();
//        Log.i("addOrder",""+orderDto.getUpLoadERP());
    }

    public boolean updateOrderInfo(String maxNo) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hasReturnGoods", URL.hasReturnGoods_Yes);
        if ((db.update(tablename, values, "maxNo=?", new String[]{maxNo})) > 0) {
            db.close();
            return true;
        }
        if (db != null) {
            db.close();
        }
        return false;
    }

    /**
     * 更改挂单的状态
     */
    public boolean deleteRestOrderInfo(String maxNo) {
        SQLiteDatabase db = DB.getWritableDatabase();
        if (db.delete(tablename, "maxNo=? and ifFinishOrder=?", new String[]{maxNo,String.valueOf(URL.ZHUOTai)}) > 0) {
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 根据时间来查询当天的订单的信息
     *
     * @param time 当天的日期
     * @return 返回一个当天的所有信息的列表
     */
    public List<OrderDto> selectGoods(String time) {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);

        SQLiteDatabase db = DB.getReadableDatabase();

        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "time=? and shop_id=? and ifFinishOrder=?", new String[]{time, shopId, String.valueOf(URL.YES)}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public OrderDto selectOrderByNo(String maxNo) {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);

        SQLiteDatabase db = DB.getReadableDatabase();

        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "maxNo=? and shop_id=?", new String[]{String.valueOf(maxNo), shopId}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(maxNo);
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
        }
        cursor.close();
        db.close();
        return dto;
    }


    /**
     * 根据时间来查询当天的订单的信息
     *
     * @param time 当天的日期
     * @return 返回一个当天的所有信息的列表
     */
    public List<OrderDto> findMaxNO(String time) {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();

        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "time=? and shop_id=?", new String[]{time, shopId}, null, null, "maxNo DESC");
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    /**
     * 查询当前的所有的挂单的信息
     */
    public List<OrderDto> selectRestOrder() {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
//        String time = GetData.getYYMMDDTime();
        SQLiteDatabase db = DB.getReadableDatabase();

        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, " shop_id=? and ifFinishOrder=?", new String[]{ shopId, String.valueOf(URL.ZHUOTai)}, null, null, null);
//        Cursor cursor = db.query(tablename, null, "time=? and shop_id=? and ifFinishOrder=?", new String[]{time, shopId, String.valueOf(URL.ZHUOTai)}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setTableId(cursor.getString(cursor.getColumnIndex("tableId")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    /**
     * 查询当前的所有的挂单的信息
     */
    public List<OrderDto> selectTableOrder() {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shop_id=? and ifFinishOrder=?", new String[]{shopId, String.valueOf(URL.ZHUOTai)}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public OrderDto selectTableHasOrder(String tableId) {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.query(tablename, null, "shop_id=? and ifFinishOrder=? and tableNumber=?", new String[]{shopId, String.valueOf(URL.ZHUOTai), tableId}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
        }
        cursor.close();
        db.close();
        return dto;
    }


    public boolean updateServerInfo(OrderDto orderDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upLoadServer", UpLoadServer.hasUpLoad);
        if ((db.update(tablename, values, "orderId=?", new String[]{orderDto.getOrderId()})) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询当前的所有没有上传的前10订单
     */
    public List<OrderDto> findOrderNOUpLoad() {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shop_id=? and upLoadServer=?", new String[]{shopId, String.valueOf(UpLoadServer.noUpload)}, null, null, "id desc");
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
            if (dtosss.size() > 10) {
                break;
            }
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    /**
     * 查询所有没有上传本服务器的订单
     */
    public List<OrderDto> findNOUpLoad() {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shop_id=? and upLoadServer=?", new String[]{shopId, String.valueOf(UpLoadServer.noUpload)}, null, null, "id desc");
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            Log.i("payType1", "获取订单付款" + cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            //获取数据库订单信息中付款方式的sid
            dto.setOrderSid(cursor.getString(cursor.getColumnIndex("orderSid")));
            Log.i("sid", "OrderSid-------------->"+cursor.getString(cursor.getColumnIndex("orderSid")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
        return dtosss;
    }

    public boolean updateERPInfo(OrderDto orderDto) {
        SQLiteDatabase db = DB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upLoadERP", UploadERP.hasUpLoad);
        if ((db.update(tablename, values, "orderId=?", new String[]{orderDto.getOrderId()})) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询所有ERP没有上传的订单的信息
     */
    public List<OrderDto> findOrderERPNOUpLoad() {
//        Log.i("addOrder","findOrderERPNOUpLoad  开始查询");
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shop_id=? and upLoadERP=?", new String[]{shopId, String.valueOf(UploadERP.noUpload)}, null, null, "id desc");
        while (cursor.moveToNext()) {
//            Log.i("addOrder","findOrderERPNOUpLoad 遍历开始");
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
//            if (dtosss.size() > 10) {
//                break;
//            }
        }
        cursor.close();
        db.close();
//        Log.i("addOrder","size:"+dtosss.size());
        return dtosss;
    }

    /**
     * 查询当天的所有ERp没有上传的订单的信息
     */
    public List<OrderDto> findERPNOUpLoad() {
//        Log.i("addOrder","findOrderERPNOUpLoad  开始查询");
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        List<OrderDto> dtosss = new ArrayList<>();
        Cursor cursor = db.query(tablename, null, "shop_id=? and upLoadERP=? and time=?", new String[]{shopId, String.valueOf(UploadERP.noUpload), GetData.getYYMMDDTime()}, null, null, "id desc");
        while (cursor.moveToNext()) {
//            Log.i("addOrder","findOrderERPNOUpLoad 遍历开始");
            dto = new OrderDto();
            //遍历Cursor对象，取出数据并打印
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
            dtosss.add(dto);
        }
        cursor.close();
        db.close();
//        Log.i("addOrder","size:"+dtosss.size());
        return dtosss;
    }

    /**
     * 根据订单号查询某一条订单的信息
     */
    public OrderDto findOrderDetailMessage(String orderNO) {
        String shopId = SPHelper.getInstance().getString(Constant.STORE_ID);
        SQLiteDatabase db = DB.getReadableDatabase();
        Cursor cursor = db.query(tablename, null, "shop_id=? and maxNo=?", new String[]{shopId, String.valueOf(orderNO)}, null, null, null);
        while (cursor.moveToNext()) {
            dto = new OrderDto();
            dto.setMaxNo(cursor.getString(cursor.getColumnIndex("maxNo")));
            dto.setOrderType(cursor.getInt(cursor.getColumnIndex("orderType")));
            dto.setPayway(cursor.getString(cursor.getColumnIndex("payway")));
            dto.setSs_money(cursor.getString(cursor.getColumnIndex("ss_money")));
            dto.setYs_money(cursor.getString(cursor.getColumnIndex("ys_money")));
            dto.setZl_money(cursor.getString(cursor.getColumnIndex("zl_money")));
            dto.setOrder_info(cursor.getString(cursor.getColumnIndex("order_info")));
            dto.setCashiername(cursor.getString(cursor.getColumnIndex("cashiername")));
            dto.setIfFinishOrder(cursor.getInt(cursor.getColumnIndex("ifFinishOrder")));
            dto.setDetailTime(cursor.getString(cursor.getColumnIndex("detailTime")));
            dto.setCheckoutTime(cursor.getString(cursor.getColumnIndex("checkoutTime")));
            dto.setHasReturnGoods(cursor.getInt(cursor.getColumnIndex("hasReturnGoods")));
            dto.setOut_trade_no(cursor.getString(cursor.getColumnIndex("out_trade_no")));
            dto.setPayReturn(cursor.getString(cursor.getColumnIndex("payReturn")));
            dto.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            dto.setTableId(cursor.getString(cursor.getColumnIndex("tableId")));
            dto.setStatus(cursor.getInt(cursor.getColumnIndex("eatingNumber")));
            dto.setOrderId(cursor.getString(cursor.getColumnIndex("orderId")));
            dto.setUpLoadServer(cursor.getInt(cursor.getColumnIndex("upLoadServer")));
            dto.setUpLoadERP(cursor.getInt(cursor.getColumnIndex("upLoadERP")));
            dto.setOrderNo(cursor.getString(cursor.getColumnIndex("orderNo")));
            dto.setVipNumber(cursor.getString(cursor.getColumnIndex("vipNumber")));
            dto.setMiYaNumber(cursor.getString(cursor.getColumnIndex("miYaNumber")));
        }
        cursor.close();
        db.close();
        return dto;
    }
}
