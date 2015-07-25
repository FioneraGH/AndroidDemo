package com.fionera.wechatdemo.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fionera.wechatdemo.R;

import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    private List<ChatMsgEntry> data;
    private int count;
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntry> data,int count) {
        this.data = data;
        this.count = count;
        mInflater = LayoutInflater.from(context);
    }

    // 获取ListView的项个数
    public int getCount() {
        // 如果数量不足一页（20行） 则返回真是数量，否则返回一页的数量
        if (count > 20)
            return count;
        return data.size();
    }
    public void setCount(int count) {
        this.count = count;
    }

    // 获取项
    public Object getItem(int position) {
        return data.get(position);
    }

    // 获取项的ID
    public long getItemId(int position) {
        return position;
    }

    // 获取View
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntry entry = (ChatMsgEntry) getItem(position);
        boolean isComMsg = entry.getMsgType();


        ViewHolder viewHolder = null;
        if (convertView == null) {

            if (isComMsg) {
                convertView = mInflater.inflate(R.layout.chat_msg_left_item,
                        null);
            } else {
                convertView = mInflater.inflate(R.layout.chat_msg_right_item,
                        null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView
                    .findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isComMsg == viewHolder.isComMsg) {

        } else {
            System.out.println("new instance");
            if (isComMsg) {
                convertView = mInflater.inflate(R.layout.chat_msg_left_item,
                        null);
            } else {
                convertView = mInflater.inflate(R.layout.chat_msg_right_item,
                        null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView
                    .findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;
            convertView.setTag(viewHolder);
        }


        viewHolder.tvSendTime.setText(entry.getDate());
        viewHolder.tvUserName.setText(entry.getName());
        viewHolder.tvContent.setText(entry.getText());

        return convertView;
    }


    class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;

        public boolean isComMsg;
    }

}
