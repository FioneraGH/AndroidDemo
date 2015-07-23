package com.fionera.wechatdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.wechatdemo.util.DBHelper;
import com.fionera.wechatdemo.view.RefreshableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private Button mBtnSend;
    private Button mBtnBack;
    private Button mBtnExtra;
    private ImageView mIvHead;
    private TextView mTvHead;
    private RefreshableView refreshableChatEntity;
    private ListView mListView;
    private EditText mEditTextContent;
    private DBHelper dbHelper = new DBHelper(this, "ChatEntity");

    // content adapter
    private ChatMsgViewAdapter mAdapter;

    // content body
    private List<ChatMsgEntry> mDataArrays = new ArrayList<ChatMsgEntry>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        registerRefreshListener();
    }


    private void initView() {
        refreshableChatEntity = (RefreshableView) findViewById(R.id.refreshable_chat_entity);
        mListView = (ListView) findViewById(R.id.list_view_chat);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnExtra = (Button) findViewById(R.id.btn_extra);
        mBtnExtra.setOnClickListener(this);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mIvHead.setOnClickListener(this);
        mTvHead = (TextView) findViewById(R.id.tv_head);
        mTvHead.setOnClickListener(this);

        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    private void initData() {

        Cursor cursor = dbHelper.queryAllChatEntity();
        while (cursor.moveToNext()) {

            ChatMsgEntry entry = new ChatMsgEntry();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String content = cursor.getString(2);
            String date = cursor.getString(3);
            int flag = cursor.getInt(4);
            System.out.println(name + content + date);
            entry.setName(name);
            entry.setText(content);
            entry.setDate(date);
            entry.setMsgType(flag == 1 ? true : false);
            mDataArrays.add(entry);
        }
        cursor.close();
        dbHelper.close();

        mAdapter = new ChatMsgViewAdapter(MainActivity.this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    private void registerRefreshListener() {

        refreshableChatEntity = (RefreshableView) findViewById(R.id.refreshable_chat_entity);
        refreshableChatEntity.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {

            @Override
            public void onRefresh(String data) {
                try {
                    Thread.sleep(3000);
                    System.out.println(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshableChatEntity.finishRefreshing();
            }
        }, 1);
        // 让ListView默认滚到最后
        mListView.setSelection(mListView.getCount() - 1);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_extra:
                Intent extraIntent = new Intent(MainActivity.this, ExtraActivity.class);
                startActivity(extraIntent);
                break;
            case R.id.iv_head:
                Intent blueIntent = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(blueIntent);
                break;
            case R.id.tv_head:
                Intent handlerIntent = new Intent(MainActivity.this, HandlerActivity.class);
                startActivity(handlerIntent);
                break;
        }
    }


    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntry entry = new ChatMsgEntry();
            entry.setDate(getDate());
            entry.setName("world");
            entry.setMsgType(false);
            entry.setText(contString);
            mDataArrays.add(entry);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
            dbHelper.insertChatEntity(entry);
            autoReply();
        }
    }

    private void autoReply(){
        ChatMsgEntry entry = new ChatMsgEntry();
        entry.setDate(getDate());
        entry.setName("hello");
        entry.setMsgType(true);
        entry.setText("请稍候联系！");
        mDataArrays.add(entry);
        mAdapter.notifyDataSetChanged();
        mEditTextContent.setText("");
        mListView.setSelection(mListView.getCount() - 1);
        dbHelper.insertChatEntity(entry);
    }

    private void back() {

        (new AlertDialog.Builder(MainActivity.this))
                .setTitle("Wow")
                .setMessage("Are you sure to exit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                MainActivity.this.finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);
        return sbBuffer.toString();

    }

}