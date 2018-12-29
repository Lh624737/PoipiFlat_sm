package com.pospi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pospi.database.DB;
import com.pospi.dto.GoodsDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品
 * Created by Qiyan on 2016/5/13.
 */
public class GoodsDao {
    private DB goodsDB;
    private String tablename = "goods";

    private GoodsDto dto;
    private ContentValues values;

    public GoodsDao(Context context) {
        goodsDB = DB.getInstance(context);
    }

    /***
     * 给数据库里面添加数据
     */
    public void addGoods(List<GoodsDto> dtos) {
        SQLiteDatabase db = null;
        try {
            db = goodsDB.getWritableDatabase();
            clearFeedTable(db);//先删除表格里面的所有的数据
            db.beginTransaction();//开启事务，加快写入速度
            for (int i = 0; i < dtos.size(); i++) {
                GoodsDto goodsDto = dtos.get(i);
                values = new ContentValues();
                values.put("Sid", goodsDto.getSid());
                Log.i("Sid", "addGoods: "+goodsDto.getSid());
                values.put("UId", goodsDto.getUId());
                values.put("category_sid", goodsDto.getCategory_sid());
                values.put("colorCode", goodsDto.getColorCode());
                values.put("group_sid", goodsDto.getGroup_sid());
                values.put("image", goodsDto.getImage());
                values.put("name", goodsDto.getName());
                values.put("discount", goodsDto.getDiscount());
                values.put("price", goodsDto.getPrice());
                values.put("CostPrice", goodsDto.getCostPrice());
                values.put("unit", goodsDto.getUnit());
                values.put("dzc",goodsDto.getDzc());
                values.put("oldPrice",goodsDto.getOldPrice());
                Log.i("dzc", goodsDto.getDzc());
                values.put("mainPrinterSid", goodsDto.getMainPrinterSid());
                values.put("backPrinterSid", goodsDto.getBackPrinterSid());
                values.put("Code", goodsDto.getCode());
                values.put("createTime", goodsDto.getCreateTime());
                values.put("orderBy", goodsDto.getOrderBy());
                values.put("isHide", goodsDto.isIsHide()?1:0);
                values.put("IsDel", goodsDto.isIsDel()?1:0);
                values.put("Code_bm", goodsDto.getCode_bm());
                values.put("specification", goodsDto.getSpecification());
                values.put("valuationType", goodsDto.getValuationType());
                values.put("genre", goodsDto.getGenre());
                values.put("setFlag", goodsDto.isSetFlag()?1:0);
                values.put("setPids", goodsDto.getSetPids());
                values.put("setOldPrice", goodsDto.getSetOldPrice());
                values.put("colorCodeShow", goodsDto.getColorCodeShow());

                //新增项目
                values.put("hyj", goodsDto.getHyj());
                values.put("hyj1", goodsDto.getHyj1());
                values.put("hyj2", goodsDto.getHyj2());
                values.put("hyj3", goodsDto.getHyj3());
                values.put("hyjf", goodsDto.getUsejf());
                values.put("hyzk", goodsDto.getUsezk());
                values.put("minzkl",goodsDto.getMinzkl());
                values.put("plu", goodsDto.getPlu());
                values.put("bzts",goodsDto.getBzts());

                long result = db.insert(tablename, null, values);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        db.close();

    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     */
    public void clearFeedTable(SQLiteDatabase db) {
        String sql = "DELETE FROM " + tablename + ";";

        db.execSQL(sql);
//        Log.i("删除表格数据", "删除数据成功");

        revertSeq(db);
    }

    private void revertSeq(SQLiteDatabase db) {
        String sql = "update sqlite_sequence set seq=0 where name='" + tablename + "'";
        db.execSQL(sql);
    }

    public GoodsDto selectGoods(String code) {
        SQLiteDatabase db = goodsDB.getReadableDatabase();

        Cursor cursor = db.query(tablename, null, "Code=?", new String[]{code}, null, null, null);
        if (cursor.moveToFirst()) {
            dto = new GoodsDto();
            //遍历Cursor对象，取出数据并打印
            dto.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));
            dto.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto.setUnit(cursor.getString(cursor.getColumnIndex("unit")));

            dto.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));

            dto.setNum(1);
            dto.setDiscount(0);
            dto.setProType("");
            dto.setProDiscout(1);
            dto.setProPrice(dto.getPrice());
            dto.setSingnum(9999);
            //打印数据
            Log.d("查询数据", "名字" + dto.getName());
            Log.d("查询数据", "价格" + dto.getPrice());
        } else {
            dto = null;
        }
        cursor.close();
        db.close();
        return dto;
    }
    public GoodsDto selectGoodsbyId(String code) {
        SQLiteDatabase db = goodsDB.getReadableDatabase();

        Cursor cursor = db.query(tablename, null, "Sid=?", new String[]{code}, null, null, null);
        if (cursor.moveToFirst()) {
            dto = new GoodsDto();
            //遍历Cursor对象，取出数据并打印
            dto.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));
            dto.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto.setUnit(cursor.getString(cursor.getColumnIndex("unit")));

            dto.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));
            dto.setNum(1);
            dto.setDiscount(0);
            dto.setProType("");
            dto.setProDiscout(1);
            dto.setProPrice(dto.getPrice());
            dto.setSingnum(9999);
            //打印数据
            Log.d("查询数据", "名字" + dto.getName());
            Log.d("查询数据", "价格" + dto.getPrice());
        } else {
            dto = null;
        }
        cursor.close();
        db.close();
        return dto;
    }
    public GoodsDto selectDzcGoods(String plu) {
        SQLiteDatabase db = goodsDB.getReadableDatabase();

        Cursor cursor = db.query(tablename, null, "plu=?", new String[]{plu}, null, null, null);
        if (cursor.moveToFirst()) {
            dto = new GoodsDto();
            //遍历Cursor对象，取出数据并打印
            dto.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));
            dto.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto.setUnit(cursor.getString(cursor.getColumnIndex("unit")));

            dto.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));
            dto.setNum(1);
            dto.setDiscount(0);
            dto.setProType("");
            dto.setProDiscout(1);
            dto.setProPrice(dto.getPrice());
            dto.setSingnum(9999);
            //打印数据
            Log.d("查询数据", "名字" + dto.getName());
            Log.d("查询数据", "价格" + dto.getPrice());
        } else {
            dto = null;
        }
        cursor.close();
        db.close();
        return dto;
    }

    public List<GoodsDto> findSelectPointGoods(String category_sid) {
        List<GoodsDto> listgoodsdto = new ArrayList<>();
        SQLiteDatabase db = goodsDB.getReadableDatabase();
//        Cursor cursor = db.query(tablename, null, "category_sid=? and dzc<>?", new String[]{category_sid,"3"}, null, null, null);
        Cursor cursor = db.query(tablename, null, "category_sid=? ", new String[]{category_sid}, null, null, null);
        while (cursor.moveToNext()) {
            Log.i("cursor", cursor.getCount() + "");
            GoodsDto dto1 = new GoodsDto();
            dto1.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            Log.i("setSid", "setSid: "+cursor.getString(cursor.getColumnIndex("Sid")));
            dto1.setUId(cursor.getString(cursor.getColumnIndex("UId")));
            dto1.setCategory_sid(cursor.getString(cursor.getColumnIndex("category_sid")));
            dto1.setColorCode(cursor.getString(cursor.getColumnIndex("colorCode")));
            dto1.setGroup_sid(cursor.getString(cursor.getColumnIndex("group_sid")));
            dto1.setImage(cursor.getString(cursor.getColumnIndex("image")));
            dto1.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto1.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));
            dto1.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto1.setCostPrice(cursor.getDouble(cursor.getColumnIndex("CostPrice")));
            dto1.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            dto1.setMainPrinterSid(cursor.getString(cursor.getColumnIndex("mainPrinterSid")));
            dto1.setBackPrinterSid(cursor.getString(cursor.getColumnIndex("backPrinterSid")));
            dto1.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto1.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            dto1.setOrderBy(cursor.getInt(cursor.getColumnIndex("orderBy")));
            dto1.setIsHide(cursor.getInt(cursor.getColumnIndex("isHide")) == 1);
            dto1.setIsDel(cursor.getInt(cursor.getColumnIndex("IsDel")) == 1);
            dto1.setCode_bm(cursor.getString(cursor.getColumnIndex("Code_bm")));
            dto1.setSpecification(cursor.getString(cursor.getColumnIndex("specification")));
            dto1.setValuationType(cursor.getInt(cursor.getColumnIndex("valuationType")));
            dto1.setGenre(cursor.getInt(cursor.getColumnIndex("genre")));
            dto1.setSetFlag(cursor.getInt(cursor.getColumnIndex("setFlag")) == 1);
            dto1.setSetPids(cursor.getString(cursor.getColumnIndex("setPids")));
            dto1.setSetOldPrice(cursor.getString(cursor.getColumnIndex("setOldPrice")));
            dto1.setColorCodeShow(cursor.getString(cursor.getColumnIndex("colorCodeShow")));
            dto1.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));

            dto1.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto1.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto1.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto1.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto1.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto1.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto1.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto1.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto1.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));


            dto1.setDiscount(0);
            dto1.setProType("");
            dto1.setProDiscout(1);
            dto1.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto1.setSingnum(9999);
            Log.i("dzc", dto1.getDzc()+"------");
            listgoodsdto.add(dto1);
        }
        cursor.close();
        db.close();
        return listgoodsdto;
    }
    public List<GoodsDto> findGoodsByFilter(String filter,String catid) {

        List<GoodsDto> listgoodsdto = new ArrayList<>();
        SQLiteDatabase db = goodsDB.getReadableDatabase();
        Cursor cursor = null;
        if (!catid.isEmpty()) {
            cursor = db.query(tablename, null, "name LIKE ? and dzc<>? and category_sid = ?", new String[]{"%" + filter + "%", "3", catid}, null, null, null);
        } else {
            cursor = db.query(tablename, null, "name LIKE ? and dzc<>?", new String[]{ "%" + filter + "%" ,"3"}, null, null, null);
        }

        while (cursor.moveToNext()) {
            Log.i("cursor", cursor.getCount() + "");
            GoodsDto dto1 = new GoodsDto();
            dto1.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            Log.i("setSid", "setSid: "+cursor.getString(cursor.getColumnIndex("Sid")));
            dto1.setUId(cursor.getString(cursor.getColumnIndex("UId")));
            dto1.setCategory_sid(cursor.getString(cursor.getColumnIndex("category_sid")));
            dto1.setColorCode(cursor.getString(cursor.getColumnIndex("colorCode")));
            dto1.setGroup_sid(cursor.getString(cursor.getColumnIndex("group_sid")));
            dto1.setImage(cursor.getString(cursor.getColumnIndex("image")));
            dto1.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto1.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));
            dto1.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto1.setCostPrice(cursor.getDouble(cursor.getColumnIndex("CostPrice")));
            dto1.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            dto1.setMainPrinterSid(cursor.getString(cursor.getColumnIndex("mainPrinterSid")));
            dto1.setBackPrinterSid(cursor.getString(cursor.getColumnIndex("backPrinterSid")));
            dto1.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto1.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            dto1.setOrderBy(cursor.getInt(cursor.getColumnIndex("orderBy")));
            dto1.setIsHide(cursor.getInt(cursor.getColumnIndex("isHide")) == 1);
            dto1.setIsDel(cursor.getInt(cursor.getColumnIndex("IsDel")) == 1);
            dto1.setCode_bm(cursor.getString(cursor.getColumnIndex("Code_bm")));
            dto1.setSpecification(cursor.getString(cursor.getColumnIndex("specification")));
            dto1.setValuationType(cursor.getInt(cursor.getColumnIndex("valuationType")));
            dto1.setGenre(cursor.getInt(cursor.getColumnIndex("genre")));
            dto1.setSetFlag(cursor.getInt(cursor.getColumnIndex("setFlag")) == 1);
            dto1.setSetPids(cursor.getString(cursor.getColumnIndex("setPids")));
            dto1.setSetOldPrice(cursor.getString(cursor.getColumnIndex("setOldPrice")));
            dto1.setColorCodeShow(cursor.getString(cursor.getColumnIndex("colorCodeShow")));
            dto1.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));
            dto1.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto1.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto1.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto1.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto1.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto1.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto1.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto1.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto1.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));
            dto1.setDiscount(0);
            dto1.setProType("");
            dto1.setProDiscout(1);
            dto1.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto1.setSingnum(9999);
            Log.i("dzc", dto1.getDzc()+"------");
            listgoodsdto.add(dto1);
        }
        cursor.close();
        db.close();
        return listgoodsdto;
    }
    public List<GoodsDto> findGoodsByFilter(String filter) {

        List<GoodsDto> listgoodsdto = new ArrayList<>();
        SQLiteDatabase db = goodsDB.getReadableDatabase();
        Cursor  cursor = db.query(tablename, null, "name LIKE ? ", new String[]{ "%" + filter + "%" }, null, null, null,"0,100");
        while (cursor.moveToNext()) {
            Log.i("cursor", cursor.getCount() + "");
            GoodsDto dto1 = new GoodsDto();
            dto1.setSid(cursor.getString(cursor.getColumnIndex("Sid")));
            Log.i("setSid", "setSid: "+cursor.getString(cursor.getColumnIndex("Sid")));
            dto1.setUId(cursor.getString(cursor.getColumnIndex("UId")));
            dto1.setCategory_sid(cursor.getString(cursor.getColumnIndex("category_sid")));
            dto1.setColorCode(cursor.getString(cursor.getColumnIndex("colorCode")));
            dto1.setGroup_sid(cursor.getString(cursor.getColumnIndex("group_sid")));
            dto1.setImage(cursor.getString(cursor.getColumnIndex("image")));
            dto1.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto1.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));
            dto1.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            dto1.setCostPrice(cursor.getDouble(cursor.getColumnIndex("CostPrice")));
            dto1.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            dto1.setMainPrinterSid(cursor.getString(cursor.getColumnIndex("mainPrinterSid")));
            dto1.setBackPrinterSid(cursor.getString(cursor.getColumnIndex("backPrinterSid")));
            dto1.setCode(cursor.getString(cursor.getColumnIndex("Code")));
            dto1.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            dto1.setOrderBy(cursor.getInt(cursor.getColumnIndex("orderBy")));
            dto1.setIsHide(cursor.getInt(cursor.getColumnIndex("isHide")) == 1);
            dto1.setIsDel(cursor.getInt(cursor.getColumnIndex("IsDel")) == 1);
            dto1.setCode_bm(cursor.getString(cursor.getColumnIndex("Code_bm")));
            dto1.setSpecification(cursor.getString(cursor.getColumnIndex("specification")));
            dto1.setValuationType(cursor.getInt(cursor.getColumnIndex("valuationType")));
            dto1.setGenre(cursor.getInt(cursor.getColumnIndex("genre")));
            dto1.setSetFlag(cursor.getInt(cursor.getColumnIndex("setFlag")) == 1);
            dto1.setSetPids(cursor.getString(cursor.getColumnIndex("setPids")));
            dto1.setSetOldPrice(cursor.getString(cursor.getColumnIndex("setOldPrice")));
            dto1.setColorCodeShow(cursor.getString(cursor.getColumnIndex("colorCodeShow")));
            dto1.setDzc(cursor.getString(cursor.getColumnIndex("dzc")));
            dto1.setHyj(cursor.getDouble(cursor.getColumnIndex("hyj")));
            dto1.setHyj1(cursor.getDouble(cursor.getColumnIndex("hyj1")));
            dto1.setHyj2(cursor.getDouble(cursor.getColumnIndex("hyj2")));
            dto1.setHyj3(cursor.getDouble(cursor.getColumnIndex("hyj3")));
            dto1.setMinzkl(cursor.getDouble(cursor.getColumnIndex("minzkl")));
            dto1.setUsejf(cursor.getString(cursor.getColumnIndex("hyjf")));
            dto1.setUsezk(cursor.getString(cursor.getColumnIndex("hyzk")));
            dto1.setPlu(cursor.getString(cursor.getColumnIndex("plu")));
            dto1.setBzts(cursor.getString(cursor.getColumnIndex("bzts")));
            dto1.setDiscount(0);
            dto1.setProType("");
            dto1.setProDiscout(1);
            dto1.setOldPrice(cursor.getDouble(cursor.getColumnIndex("oldPrice")));
            dto1.setSingnum(9999);
            Log.i("dzc", dto1.getDzc()+"------");
            listgoodsdto.add(dto1);
        }
        cursor.close();
        db.close();
        return listgoodsdto;
    }
}
