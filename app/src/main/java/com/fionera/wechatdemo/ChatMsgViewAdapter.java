package com.fionera.wechatdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<ChatMsgEntry> data;
	private Context context;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntry> data) {
		this.context = context;
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	// 获取ListView的项个数
	public int getCount() {
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

		if(isComMsg == viewHolder.isComMsg){

			System.out.println(position + " " + entry.getMsgType() + " " + viewHolder.isComMsg + " ");
		}else{
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
