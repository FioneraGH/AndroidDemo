package com.fionera.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.fionera.demo.R;
import com.fionera.demo.adapter.ChatMsgViewAdapter;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.demo.util.DBHelper;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.ArcMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity
        extends Activity
        implements OnClickListener, AbsListView.OnScrollListener {

    private Context mContext = this;

    private ListView listView;
    private View footer;
    private EditText mEditTextContent;
    private DBHelper dbHelper = new DBHelper(this, "ChatEntity");

    private static final int lineSize = 20;    //每次显示数
    private int currentPage = 1; //默认在第一页
    private int allRecorders = 0;  //全部记录数
    private int pageSize = 1;  //默认共一页

    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgBean> mDataArrays = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.CloseDb();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.lv_chat_content);
        findViewById(R.id.iv_toggle_menu).setOnClickListener(this);
        findViewById(R.id.tv_send_msg).setOnClickListener(this);
        findViewById(R.id.tv_extra_function).setOnClickListener(this);
        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        arcMenu.setOnMenuItemClickListener((view, pos) -> {

            ShowToast.show(pos + "");
        });

        mEditTextContent = (EditText) findViewById(R.id.et_send_message);
    }

    private void initData() {

        footer = getLayoutInflater().inflate(R.layout.layout_load_more, null);

        listView.addHeaderView(footer);
        listView.setOnScrollListener(this);
        showAllData();
    }

    /**
     * 读取显示数据
     */
    public void showAllData() {

        // 获取总记录数
        allRecorders = dbHelper.getCount();
        // 如果数据少于20，则不需要出现加载提示
        if (allRecorders < lineSize) {
            listView.removeHeaderView(footer);
        }
        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;
        mDataArrays = dbHelper.getSomeItems(currentPage, lineSize);
        mAdapter = new ChatMsgViewAdapter(ChatActivity.this, mDataArrays, lineSize);
        listView.setAdapter(mAdapter);
        listView.setSelection(mDataArrays.size() - 1);//直接定位到最底部
        dbHelper.CloseDb();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_msg:
                send();
                break;
            case R.id.tv_extra_function:
                startActivity(new Intent(mContext, CaptureActivity.class));
                break;
        }
    }

    int firstItem = -1;

    @Override
    public void onScroll(AbsListView absView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        firstItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
        if (firstItem == 0 && currentPage < pageSize && scorllState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE) {
            currentPage++;
            new Handler().postDelayed(this::appendDate, 1000);
        }
    }

    /**
     * 增加数据
     */
    private void appendDate() {

        ArrayList<ChatMsgBean> addItems = dbHelper.getSomeItems(currentPage, lineSize);
        mAdapter.setCount(mAdapter.getCount() + addItems.size());

        if (allRecorders == mAdapter.getCount()) {
            listView.removeHeaderView(footer);
        }
        mDataArrays.addAll(0, addItems);

        mAdapter.notifyDataSetChanged();
        listView.setSelection(addItems.size());
        dbHelper.CloseDb();
    }


    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgBean entry = new ChatMsgBean();
            entry.setDate(getDate());
            entry.setName("world");
            entry.setMsgType(false);
            entry.setText(contString);
            mDataArrays.add(entry);
            mAdapter.notifyDataSetChanged();
            listView.setSelection(listView.getCount() - 1);
            dbHelper.insertChatEntity(entry);
            new Handler().postDelayed(this::autoReply, 1000);
            mEditTextContent.setText("");
        }
    }

    private void autoReply() {
        ChatMsgBean entry = new ChatMsgBean();
        entry.setDate(getDate());
        entry.setName("hello");
        entry.setMsgType(true);
        entry.setText("请稍候联系！ " + mEditTextContent.getText().toString());
        mDataArrays.add(entry);
        mAdapter.notifyDataSetChanged();
        mEditTextContent.setText("");
        listView.setSelection(listView.getCount() - 1);
        dbHelper.insertChatEntity(entry);
    }

    public static String getDate() {

        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        return (year + "-" + month + "-" + day + " " + hour + ":" + mins);
    }

}