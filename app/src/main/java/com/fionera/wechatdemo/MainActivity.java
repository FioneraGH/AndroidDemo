package com.fionera.wechatdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    private Button mBtnSend;
    private Button mBtnBack;
    private Button mBtnExtra;
    private ImageView mIvHead;
    private TextView mTvHead;
    private ListView mListView;
    private EditText mEditTextContent;

    // content adapter
    private ChatMsgViewAdapter mAdapter;

    // content body
    private List<ChatMsgEntry> mDataArrays = new ArrayList<ChatMsgEntry>();

    // init data
    private String[] msgArray = new String[]{"0", "1",
            "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21",
            "22", "23"};
    private String[] dataArray = new String[]{"2015-07-01 18:00",
            "2015-07-01 18:10", "2015-07-01 18:11", "2015-07-01 18:20",
            "2015-07-01 18:30", "2015-07-01 18:35", "2015-07-01 18:40",
            "2015-07-01 18:50", "2015-07-01 18:00", "2015-07-01 18:10",
            "2015-07-01 18:11", "2015-07-01 18:20", "2015-07-01 18:30",
            "2015-07-01 18:35", "2015-07-01 18:40", "2015-07-01 18:50",
            "2015-07-01 18:00", "2015-07-01 18:10", "2015-07-01 18:11",
            "2015-07-01 18:20", "2015-07-01 18:30", "2015-07-01 18:35",
            "2015-07-01 18:40", "2015-07-01 18:50"};
    private static int COUNT = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
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
        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntry entry = new ChatMsgEntry();
            entry.setDate(dataArray[i]);
            if (i % 2 == 0) {
                entry.setName("hello");
                entry.setMsgType(true);
            } else {
                entry.setName("world");
                entry.setMsgType(false);
            }
            entry.setText(msgArray[i]);
            mDataArrays.add(entry);
        }
        mAdapter = new ChatMsgViewAdapter(MainActivity.this, mDataArrays);
        mListView.setAdapter(mAdapter);
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
                Toast.makeText(MainActivity.this, "Extra Function", Toast.LENGTH_SHORT).show();
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
        }
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

}