package com.fionera.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.bean.ChatMsgBean;

import java.util.List;

/**
 * @author fionera
 */
public class ChatMsgViewAdapter extends BaseAdapter {

    private final int IS_COMING_MSG = 0;
    private final int IS_NOT_COMING_MSG = 1;

    private List<ChatMsgBean> data;
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgBean> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    // 获取ListView的项个数
    @Override
    public int getCount() {
        return data.size();
    }

    // 获取项
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    // 获取项的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 判定左右回收类型
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getMsgType()) {
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgBean entry = (ChatMsgBean) getItem(position);


        ViewHolder viewHolder;
        if (convertView == null) {

            if (getItemViewType(position) == IS_COMING_MSG) {
                convertView = mInflater.inflate(R.layout.lv_chat_msg_left_item, parent, false);
            } else if (getItemViewType(position) == IS_NOT_COMING_MSG) {
                convertView = mInflater.inflate(R.layout.lv_chat_msg_right_item, parent, false);
            }
            viewHolder = new ViewHolder();
            if (convertView != null) {
                viewHolder.tvSendTime = convertView.findViewById(R.id.tv_sendtime);
                viewHolder.tvUserName = convertView.findViewById(R.id.tv_username);
                viewHolder.tvContent = convertView.findViewById(R.id.tv_chatcontent);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSendTime.setText(entry.getDate());
        viewHolder.tvUserName.setText(entry.getName());
        viewHolder.tvContent.setText(entry.getText());

        return convertView;
    }

    class ViewHolder {
        TextView tvSendTime;
        TextView tvUserName;
        TextView tvContent;
    }
}
