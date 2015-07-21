package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.view.RefreshableView;

public class PullToRefreshActivity extends Activity {

    private RefreshableView refreshableView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private static int CURRENTID = 0;
    private String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        listView = (ListView) findViewById(R.id.list_view_refresh);
        listView.setAdapter(adapter);

        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {

            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, CURRENTID);
    }
}