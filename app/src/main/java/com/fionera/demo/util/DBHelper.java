package com.fionera.demo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fionera.demo.bean.ChatMsgBean;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DBHelper
 * Created by fionera on 15-7-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    private String databaseName;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        this(context, "Default");
    }

    public DBHelper(Context context, String name) {
        this(context, name, null);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        this(context, name, factory, 1);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
        this.databaseName = name;
    }

    public void closeDb() {
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         创建数据表Tbl_ChatEntity用于存放聊天信息
          */
        db.execSQL("create table Tbl_" + databaseName + "(" +
                "id INTEGER primary key autoincrement ," +
                "name char(10)," +
                "content varchar(100)," +
                "date char(30)," +
                "flag INTEGER );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 返回数据库聊天记录总数
     *
     * @return 总数
     */
    public int getCount() {
        db = getReadableDatabase();

        String[] columns = {"id", "name", "content", "date", "flag"};
        Cursor cursor = db.query("Tbl_ChatEntity", columns, null, null, null, null, "id");
        int count = cursor.getCount();
        cursor.close();

        closeDb();

        return count;
    }

    /**
     * 分页查询聊天记录内容
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示的记录
     * @return 当前页的记录
     */
    public List<ChatMsgBean> getSomeItems(int currentPage, int pageSize) {
        db = getReadableDatabase();

        int firstResult = (currentPage - 1) * pageSize;
        String[] columns = {"id", "name", "content", "date", "flag"};
        Cursor cursor = db.query("Tbl_ChatEntity", columns, null, null, null, null, "id desc",
                firstResult + "," + pageSize);

        List<ChatMsgBean> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChatMsgBean entry = new ChatMsgBean();

            String name = cursor.getString(1);
            String content = cursor.getString(2);
            String date = cursor.getString(3);
            int flag = cursor.getInt(4);

            entry.setName(name);
            entry.setText(content);
            entry.setDate(date);
            entry.setMsgType(flag == 1);

            items.add(entry);
        }
        cursor.close();

        Collections.reverse(items);

        //不要关闭数据库
        return items;
    }

    public void insertChatEntity(ChatMsgBean entry) {
        SQLiteDatabase db = getWritableDatabase();

        String name = entry.getName();
        String content = entry.getText();
        String date = entry.getDate();
        boolean flag = entry.getMsgType();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("content", content);
        values.put("date", date);
        values.put("flag", flag ? Integer.valueOf(1) : Integer.valueOf(0));

        db.insert("Tbl_ChatEntity", null, values);
        db.close();
    }
}
