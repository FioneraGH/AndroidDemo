package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ListView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.adapter.GenAdapter;
import com.fionera.wechatdemo.bean.ChatMsgEntry;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapterActivity extends Activity {


    private GenAdapter genAdapter;
    private ListView listView;
    private List<ChatMsgEntry> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_adapter);
        data = new ArrayList<ChatMsgEntry>();

        for (int i = 0; i < 10; i++) {
            ChatMsgEntry entry = new ChatMsgEntry();
            entry.setDate("0000-00-00");
            entry.setName("hello");
            entry.setMsgType(false);
            entry.setText("world " + i);
            data.add(entry);
        }
        listView = (ListView) findViewById(R.id.list_view_generic);
        genAdapter = new GenAdapter(GenericAdapterActivity.this,data);
        listView.setAdapter(genAdapter);
    }

}
