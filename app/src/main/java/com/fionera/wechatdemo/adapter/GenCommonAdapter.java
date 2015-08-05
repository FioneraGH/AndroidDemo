package com.fionera.wechatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
