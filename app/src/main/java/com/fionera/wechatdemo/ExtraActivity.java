package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fionera.wechatdemo.extra.EditInListActivity;
import com.fionera.wechatdemo.extra.ExpandableListViewActivity;
import com.fionera.wechatdemo.extra.GenericAdapterActivity;
import com.fionera.wechatdemo.extra.JSONActivity;
import com.fionera.wechatdemo.extra.MatrixActivity;
import com.fionera.wechatdemo.extra.PullToLoadActivity;
import com.fionera.wechatdemo.extra.PullToRefreshActivity;
import com.fionera.wechatdemo.extra.RecycleActivity;
import com.fionera.wechatdemo.extra.SplitPageActivity;
import com.fionera.wechatdemo.extra.VolleyActivity;
import com.fionera.wechatdemo.extra.WifiBlueNFCActivity;

public class ExtraActivity extends Activity {

    private Button matrix;
    private Button fresh;
    private Button split;
    private Button load;
    private Button json;
    private Button recycle;
    private Button generic;
    private Button wbn;
    private Button volley;
    private Button expand;
    private Button etinlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        // 设定Matrix跳转
        matrix = (Button) findViewById(R.id.button1);
        matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, MatrixActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定下拉刷新跳转
        fresh = (Button) findViewById(R.id.button2);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, PullToRefreshActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定分页加载跳转
        split = (Button) findViewById(R.id.button3);
        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, SplitPageActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定下拉加载跳转
        load = (Button) findViewById(R.id.button4);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, PullToLoadActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定JSON测试跳转
        json = (Button) findViewById(R.id.button5);
        json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, JSONActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定RecyclerView测试跳转
        recycle = (Button) findViewById(R.id.button6);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, RecycleActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定Generic测试跳转
        generic = (Button) findViewById(R.id.button7);
        generic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, GenericAdapterActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定WifiBlueNFC测试跳转
        wbn = (Button) findViewById(R.id.button8);
        wbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, WifiBlueNFCActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定Volley测试跳转
        volley = (Button) findViewById(R.id.button9);
        volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, VolleyActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定ExpandableListView测试跳转
        expand = (Button) findViewById(R.id.button10);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, ExpandableListViewActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });
        // 设定EditTextInListView测试跳转
        etinlv = (Button) findViewById(R.id.button11);
        etinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this, EditInListActivity.class);
                ExtraActivity.this.startActivity(intent);
                ExtraActivity.this.finish();
            }
        });

    }
}
