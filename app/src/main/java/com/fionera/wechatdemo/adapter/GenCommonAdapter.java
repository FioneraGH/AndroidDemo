package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fionera.wechatdemo.R;

import java.util.List;

/**
 * Created by fionera on 15-8-5.
 */
public abstract class GenCommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    protected List<T> data;
    protected Context context;

    public GenCommonAdapter(Context context, List<T> data) {

        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GenViewHolder genViewHolder = GenViewHolder.getViewHolder(context,
                convertView, R.layout.lv_chat_msg_left_item, position, parent);
        convert(genViewHolder,data.get(position));
        return genViewHolder.getConvertView();
    }

    // 抽象方法 ，让实现类去实现数据绑定
    public abstract void convert(GenViewHolder genViewHolder,T t);
}
