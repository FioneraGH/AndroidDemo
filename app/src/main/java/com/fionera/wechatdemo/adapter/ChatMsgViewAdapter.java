package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgBean;

import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    private final int IS_COMING_MSG = 0;
    private final int IS_NOT_COMING_MSG = 1;

    private List<ChatMsgBean> data;
    private int count;
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgBean> data, int count) {
        this.data = data;
        this.count = count;
        mInflater = LayoutInflater.from(context);
    }

    public void setCount(int count) {
        this.count = count;
    }

    // 获取ListView的项个数
    public int getCount() {
        // 如果数量不足一页（20行） 则返回真是数量，否则返回一页的数量
        if (count > 20) return count;
        return data.size();
    }

    // 获取项
    public Object getItem(int position) {
        return data.get(position);
    }

    // 获取项的ID
    public long getItemId(int position) {
        return position;
    }

    // 判定左右回收类型
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getMsgType() == true) {
            return IS_COMING_MSG;
        }
        return IS_NOT_COMING_MSG;
    }

    // 类型总数
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // 获取View
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgBean entry = (ChatMsgBean) getItem(position);


        ViewHolder viewHolder;
        if (convertView == null) {

            if (getItemViewType(position) == IS_COMING_MSG) {
                convertView = mInflater.inflate(R.layout.lv_chat_msg_left_item, null);
            } else if (getItemViewType(position) == IS_NOT_COMING_MSG) {
                convertView = mInflater.inflate(R.layout.lv_chat_msg_right_item, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
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
    }
}
