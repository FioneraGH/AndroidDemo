package com.fionera.wechatdemo.adapter;

import android.content.Context;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.bean.ChatMsgBean;

import java.util.List;

/**
 * Created by fionera on 15-8-5.
 */

/**
 * 示例适配器
 */
public class GenAdapter extends GenCommonAdapter<ChatMsgBean> {


    public GenAdapter(Context context, List<ChatMsgBean> data) {

        super(context, data);
    }

    @Override
    public void convert(GenViewHolder genViewHolder, ChatMsgBean chatMsgBean) {
        genViewHolder.setText(R.id.tv_sendtime, chatMsgBean.getDate()).
                setText(R.id.tv_username, chatMsgBean.getName()).
                setText(R.id.tv_chatcontent, chatMsgBean.getText());
    }
}
