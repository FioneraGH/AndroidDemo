package com.fionera.wechatdemo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fionera.wechatdemo.ChatMsgEntry;

import java.util.ArrayList;

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

    public void CloseDb() {
        if (db != null) {
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

        // 不能关闭数据库
        return cursor;
    }

    /**
     * 返回数据库聊天记录总数
     * @return 总数
     */
    public int getCount() {
        int count = 0;
        Cursor cursor = this.queryAllChatEntity();
        while (cursor.moveToNext()) {

            count++;
        }
        cursor.close();
        this.CloseDb();

        return count;
    }

    /**
     * 分页查询聊天记录内容
     * @param currentPage 当前页
     * @param pageSize    每页显示的记录
     * @return 当前页的记录
     */
    public ArrayList getAllItems(int currentPage, int pageSize) {
        int firstResult = (currentPage - 1) * pageSize;
        int maxResult = currentPage * pageSize;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[] columns = {"id", "name", "content", "date", "flag"};
        cursor = db.query("Tbl_ChatEntity", columns, null, null, null, null, "id",firstResult+","+pageSize);

        ArrayList items = new ArrayList();
        while (cursor.moveToNext()) {
            String item = cursor.getString(2);
            items.add(item);

        }
        //不要关闭数据库
        return items;
    }

    public void insertChatEntity(ChatMsgEntry entry) {
        SQLiteDatabase db = getWritableDatabase();
        String name = entry.getName();
        String content = entry.getText();
        String date = entry.getDate();
        boolean flag = entry.getMsgType();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("content", content);
        values.put("date", date);
        values.put("flag", flag ? new Integer(1) : new Integer(0));
        db.insert("Tbl_ChatEntity", null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
