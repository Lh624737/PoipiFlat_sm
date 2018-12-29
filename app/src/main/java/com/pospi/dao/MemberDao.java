package com.pospi.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pospi.database.DB;
import com.pospi.dto.MemberDto;

import java.util.List;

/**
 * 会员
 * Created by Qiyan on 2016/6/8.
 */
public class MemberDao {
    private String tableName = "memberinfo";

    private DB memberDb;
    private Context context;
    private MemberDto dto;

    public MemberDao(Context context) {
        this.context = context;
        memberDb = DB.getInstance(context);
    }

    //添加会员的信息
    public void addMember(List<MemberDto> memberDtos) {
        SQLiteDatabase db = memberDb.getWritableDatabase();
        clearFeedTable(db);//先删除表格里面的所有的数据
        for (int i = 0; i < memberDtos.size(); i++) {
            MemberDto memberDto = memberDtos.get(i);
            db.execSQL("insert into memberinfo(name,number,tel,address,score,getcardtime,bornyear,bornmonth,bornday)values(?,?,?,?,?,?,?,?,?)",
                    new Object[]{memberDto.getName(), memberDto.getNumber(), memberDto.getTel(), memberDto.getAddress(), memberDto.getScore(), memberDto.getGetcardtime(), memberDto.getBornyear(), memberDto.getBornmonth(), memberDto.getBornday()});
        }
        db.close();
    }

    /**
     * 在添加数据之前先删除表格里面所有的数据
     *
     * @param db
     */
    public void clearFeedTable(SQLiteDatabase db) {

        String sql = "DELETE FROM " + tableName + ";";

        db.execSQL(sql);
//        Log.i("删除表格数据", "删除数据成功");

        revertSeq(db);
    }

    private void revertSeq(SQLiteDatabase db) {
        String sql = "update sqlite_sequence set seq=0 where name='" + tableName + "'";
        db.execSQL(sql);
    }

    public MemberDto findMemberByTel(String tel) {
        SQLiteDatabase db = memberDb.getReadableDatabase();
        Log.d("find", "diaoyony");
        Cursor cursor = db.query(tableName, null, "tel=?", new String[]{tel}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new MemberDto();
            //遍历Cursor对象，取出数据并打印
            dto.setTel(tel);
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            dto.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            dto.setScore(cursor.getDouble(cursor.getColumnIndex("score")));
            dto.setGetcardtime(cursor.getString(cursor.getColumnIndex("getcardtime")));
            dto.setBornyear(cursor.getInt(cursor.getColumnIndex("bornyear")));
            dto.setBornmonth(cursor.getInt(cursor.getColumnIndex("bornmonth")));
            dto.setBornday(cursor.getInt(cursor.getColumnIndex("bornday")));
            Log.d("find", dto.getName());
            Log.d("find", "sdwe");
        }
        cursor.close();
        db.close();
        return dto;
    }

    public MemberDto findMemberByNumber(String number) {
        SQLiteDatabase db = memberDb.getReadableDatabase();
        Log.d("find", "diaoyony");
        Cursor cursor = db.query(tableName, null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            dto = new MemberDto();
            //遍历Cursor对象，取出数据并打印
            dto.setTel(cursor.getString(cursor.getColumnIndex("tel")));
            dto.setName(cursor.getString(cursor.getColumnIndex("name")));
            dto.setNumber(number);
            dto.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            dto.setScore(cursor.getDouble(cursor.getColumnIndex("score")));
            dto.setGetcardtime(cursor.getString(cursor.getColumnIndex("getcardtime")));
            dto.setBornyear(cursor.getInt(cursor.getColumnIndex("bornyear")));
            dto.setBornmonth(cursor.getInt(cursor.getColumnIndex("bornmonth")));
            dto.setBornday(cursor.getInt(cursor.getColumnIndex("bornday")));
            Log.d("find", dto.getName());
            Log.d("find", "sdwe");
        }
        cursor.close();
        db.close();
        return dto;
    }

}
