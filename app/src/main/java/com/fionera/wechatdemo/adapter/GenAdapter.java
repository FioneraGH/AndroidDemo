package com.fionera.wechatdemo.adapter;

import android.content.Context;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgEntry;
import com.fionera.wechatdemo.util.GenViewHolder;

import java.util.List;

/**
 * Created by fionera on 15-8-5.
 */

/**
 * 示例适配器
 */
public class GenAdapter extends GenCommonAdapter<ChatMsgEntry> {


    public GenAdapter(Context context, List<ChatMsgEntry> data) {

        super(context, data);
    }

    @Override
    public void convert(GenViewHolder genViewHolder, ChatMsgEntry chatMsgEntry) {
        genViewHolder.setText(R.id.tv_sendtime,chatMsgEntry.getDate()).
                setText(R.id.tv_username,chatMsgEntry.getName()).
                setText(R.id.tv_chatcontent, chatMsgEntry.getText());
    }
}
