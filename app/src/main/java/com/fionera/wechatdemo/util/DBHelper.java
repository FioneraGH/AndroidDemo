package com.fionera.wechatdemo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fionera.wechatdemo.ChatMsgEntry;

/**
 * Created by fionera on 15-7-23.
 */
public class DBHelper extends SQLiteOpenHelper {

    private String DatabaseName;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        this(context, "Default");
    }

    public DBHelper(Context context, String name) {
        this(context, name, null);

    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        this(context, name, factory, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.DatabaseName = name;
    }

    public void CloseDb(){
        if (db != null){
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 创建数据表Tbl_ChatEntity用于存放聊天信息
        db.execSQL("create table Tbl_ChatEntity (" +
                "id INTEGER primary key autoincrement ," +
                "name char(10)," +
                "content char(100)," +
                "date char(30)," +
                "flag INTEGER );");
    }

    public Cursor queryAllChatEntity() {
        Cursor cursor;
        db = getReadableDatabase();
        String[] columns = {"id", "name", "content", "date", "flag"};
        cursor = db.query("Tbl_ChatEntity", columns, null, null, null, null, "id");

        return cursor;
    }

    public void insertChatEntity(ChatMsgEntry entry){
        SQLiteDatabase db = getWritableDatabase();
        String name = entry.getName();
        String content = entry.getText();
        String date = entry.getDate();
        boolean flag = entry.getMsgType();

        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("content",content);
        values.put("date",date);
        values.put("flag",flag?new Integer(1):new Integer(0));
        db.insert("Tbl_ChatEntity", null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
