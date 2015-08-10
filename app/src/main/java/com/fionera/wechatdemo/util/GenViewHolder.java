package com.fionera.wechatdemo.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fionera on 15-8-5.
 */
public class GenViewHolder {

    private SparseArray<View> viewsSparseArray;
    private View convertView;
    private int position;

    /**
     * 获取ViewHolder
     *
     * @param context
     * @param convertView
     * @param id
     * @param position
     * @param parent
     * @return
     */
    public static GenViewHolder getViewHolder(Context context, View convertView, int id, int position, ViewGroup parent) {

        if (convertView == null) {
            return new GenViewHolder(context, id, position, parent);
        } else {
            GenViewHolder genViewHolder = (GenViewHolder) convertView.getTag();
            genViewHolder.position = position;
            return genViewHolder;
        }
    }

    public GenViewHolder(Context context, int id, int position, ViewGroup parent) {

        this.position = position;
        this.viewsSparseArray = new SparseArray<View>();

        this.convertView = LayoutInflater.from(context).inflate(id, parent, false);
        this.convertView.setTag(this);
    }

    /**
     * 通过viewId 获取view
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = viewsSparseArray.get(viewId);

        if (view == null) {
            view = convertView.findViewById(viewId);
            viewsSparseArray.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    // 设置TextView的通用setText方法，并支持链式编程
    public GenViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    // 设置ImageView的通用setImage方法，并支持链式编程
    public GenViewHolder setImage(int viewId, int resId) {
        ImageView tv = getView(viewId);
        tv.setImageResource(resId);
        return this;
    }
}
