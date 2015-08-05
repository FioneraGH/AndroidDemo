package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgEntry;
import com.fionera.wechatdemo.util.GenViewHolder;

import java.util.List;

/**
 * Created by fionera on 15-8-5.
 */
public class GenAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<ChatMsgEntry> data;
    private Context context;

    public GenAdapter(Context context, List<ChatMsgEntry> data) {

        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GenViewHolder genViewHolder = GenViewHolder.getViewHolder(context,
                convertView, R.layout.chat_msg_left_item, position, parent);
        ChatMsgEntry entry = data.get(position);
        ((TextView)genViewHolder.getView(R.id.tv_sendtime)).setText(entry.getDate());
        ((TextView)genViewHolder.getView(R.id.tv_username)).setText(entry.getName());
        ((TextView)genViewHolder.getView(R.id.tv_chatcontent)).setText(entry.getText());

        // 返回处理过的convertView
        return genViewHolder.getConvertView();
    }
}
