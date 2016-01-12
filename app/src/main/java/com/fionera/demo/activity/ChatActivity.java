package com.fionera.demo.activity;

import android.app.Activity;
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

import com.fionera.demo.R;
import com.fionera.demo.adapter.ChatMsgViewAdapter;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.demo.util.DBHelper;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.ArcMenu;
import com.fionera.demo.view.SlidingMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends Activity implements OnClickListener, AbsListView
        .OnScrollListener {

    private ImageView ivToggleMenu;
    private Button mBtnExtra;
    private Button mBtnSend;
    private ArcMenu arcMenu;
    private ListView listView;
    private View header;
    private Button btn;
    private ProgressBar pg;
    private EditText mEditTextContent;
    private DBHelper dbHelper = new DBHelper(this, "ChatEntity");

    private RelativeLayout rlLeftMenu;
    private SlidingMenu slidingMenu;

    private RelativeLayout flowlayout;
    private RelativeLayout tabsViewPager;
    private RelativeLayout propertyAnim;
    private RelativeLayout danMu;

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


    private void initView() {

        listView = (ListView) findViewById(R.id.list_view_chat);
        ivToggleMenu = (ImageView) findViewById(R.id.iv_toggle_menu);
        ivToggleMenu.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnExtra = (Button) findViewById(R.id.btn_extra);
        mBtnExtra.setOnClickListener(this);
        arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        arcMenu.setOnMenuItemClickListener((view, pos) -> {

            ShowToast.show(pos + "");
        });

        mEditTextContent = (EditText) findViewById(R.id.et_send_message);
        rlLeftMenu = (RelativeLayout) findViewById(R.id.left_menu);
        slidingMenu = (SlidingMenu) findViewById(R.id.sm_chat);

        flowlayout = (RelativeLayout) rlLeftMenu.findViewById(R.id.rl_flow_layout);
        flowlayout.setOnClickListener(this);
        tabsViewPager = (RelativeLayout) rlLeftMenu.findViewById(R.id.rl_tab_layout);
        tabsViewPager.setOnClickListener(this);
        propertyAnim = (RelativeLayout) rlLeftMenu.findViewById(R.id.rl_property_anim);
        propertyAnim.setOnClickListener(this);
        danMu = (RelativeLayout) rlLeftMenu.findViewById(R.id.rl_dan_mu);
        danMu.setOnClickListener(this);
    }

    private void initData() {

        header = getLayoutInflater().inflate(R.layout.lv_more_info_footer, null);
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
        mDataArrays = dbHelper.getSomeItems(currentPage, lineSize);
        mAdapter = new ChatMsgViewAdapter(ChatActivity.this, mDataArrays, lineSize);
        listView.setAdapter(mAdapter);
        listView.setSelection(mDataArrays.size() - 1);//直接定位到最底部
        dbHelper.CloseDb();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toggle_menu:
                slidingMenu.toggleMenu();
                break;
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_extra:
                break;
            case R.id.rl_flow_layout:
                Intent flowLayoutIntent = new Intent(ChatActivity.this, FlowLayoutActivity.class);
                startActivity(flowLayoutIntent);
                break;
            case R.id.rl_tab_layout:
                Intent tabLayoutIntent = new Intent(ChatActivity.this,
                        SmartTabLayoutActivity.class);
                startActivity(tabLayoutIntent);
                break;
            case R.id.rl_property_anim:
                Intent propertyAnimIntent = new Intent(ChatActivity.this,
                        PropertyAnimActivity.class);
                startActivity(propertyAnimIntent);
                break;
            case R.id.rl_dan_mu:
                Intent danmuIntent = new Intent(ChatActivity.this, DanmuActivity.class);
                startActivity(danmuIntent);
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

        ArrayList addItems = dbHelper.getSomeItems(currentPage, lineSize);
        mAdapter.setCount(mAdapter.getCount() + addItems.size());
        //判断，如果到了最末尾则去掉进度圈
        if (allRecorders == mAdapter.getCount()) {
            listView.removeHeaderView(header);
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
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        return (year + "-" + month + "-" + day + " " + hour + ":" + mins);
    }

}