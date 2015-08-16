package com.fionera.wechatdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fionera.wechatdemo.adapter.ChatMsgViewAdapter;
import com.fionera.wechatdemo.bean.ChatMsgEntry;
import com.fionera.wechatdemo.extra.ArcViewActivity;
import com.fionera.wechatdemo.util.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener, AbsListView
        .OnScrollListener {

    private Button mBtnSend;
    private Button mBtnBack;
    private Button mBtnExtra;
    private ImageView mIvHead;
    private TextView mTvHead;
    private ListView listView;
    private View header;
    private Button btn;
    private ProgressBar pg;
    private EditText mEditTextContent;
    private DBHelper dbHelper = new DBHelper(this, "ChatEntity");
    private Handler handler = new Handler();

    private RelativeLayout rlLeftMenu;
    private RelativeLayout arcview;

    // to adjust the content
    private int currentPage = 1; //默认在第一页
    private static final int lineSize = 20;    //每次显示数
    private int allRecorders = 0;  //全部记录数
    private int pageSize = 1;  //默认共一页

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
    }


    private void initView() {
        listView = (ListView) findViewById(R.id.list_view_chat);
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
        rlLeftMenu = (RelativeLayout) findViewById(R.id.left_menu);
        arcview = (RelativeLayout) rlLeftMenu.findViewById(R.id.rl_arc_view);

    }

    private void initData() {

        header = getLayoutInflater().inflate(R.layout.more_info_foot, null);
        btn = (Button) header.findViewById(R.id.bt_load);
        btn.setVisibility(View.GONE);
        pg = (ProgressBar) header.findViewById(R.id.pg);
        pg.setVisibility(View.VISIBLE);

        listView.addHeaderView(header);
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
            listView.removeHeaderView(header);
        }
        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;
        mDataArrays = dbHelper.getAllItems(currentPage, lineSize);
        mAdapter = new ChatMsgViewAdapter(MainActivity.this, mDataArrays, lineSize);
        listView.setAdapter(mAdapter);
        listView.setSelection(mDataArrays.size() - 1);//直接定位到最底部
        dbHelper.CloseDb();
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
            case R.id.tv_head:
                Intent handlerIntent = new Intent(MainActivity.this, HandlerActivity.class);
                startActivity(handlerIntent);
                break;
            case R.id.iv_head:
                Intent capIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(capIntent);
                break;
            case R.id.rl_arc_view:
                Intent arcViewIntent = new Intent(MainActivity.this, ArcViewActivity.class);
                startActivity(arcViewIntent);
                break;
        }
    }

    int firstItem = -1;

    @Override
    public void onScroll(AbsListView absView, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        firstItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
        if (firstItem == 0 && currentPage < pageSize && scorllState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE) {// 不再滚动
            currentPage++;
            // 增加数据
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    appendDate();
                }

            }, 2000);

        }
    }

    /**
     * 增加数据
     */
    private void appendDate() {
        final ArrayList addItems = dbHelper.getAllItems(currentPage, lineSize);
        mAdapter.setCount(mAdapter.getCount() + addItems.size());
        //判断，如果到了最末尾则去掉进度圈
        if (allRecorders == mAdapter.getCount()) {
            listView.removeHeaderView(header);
        }
        mDataArrays.addAll(0, addItems);

        mAdapter.notifyDataSetChanged();
        listView.setSelection(addItems.size() - 1);
        dbHelper.CloseDb();
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
            listView.setSelection(listView.getCount() - 1);
            dbHelper.insertChatEntity(entry);
            autoReply();
            mEditTextContent.setText("");
        }
    }

    private void autoReply() {
        ChatMsgEntry entry = new ChatMsgEntry();
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

    public static String getDate() {
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