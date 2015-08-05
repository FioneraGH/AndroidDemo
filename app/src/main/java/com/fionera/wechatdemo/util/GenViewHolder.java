package com.fionera.wechatdemo.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fionera on 15-8-5.
 */
public class GenViewHolder {

    private SparseArray<View> viewsSparseArray;
    private int position;
    private View convertView;

    public static GenViewHolder getViewHolder(Context context, View convertView, int id, int position, ViewGroup parent) {

        if(convertView == null){
            return new GenViewHolder(context,id,position,parent);
        }else{
            GenViewHolder genViewHolder = (GenViewHolder) convertView.getTag();
            genViewHolder.position = position;
            return  genViewHolder;
        }
    }

    public GenViewHolder(Context context, int id, int position, ViewGroup parent) {

        this.position = position;
        this.viewsSparseArray = new SparseArray<View>();

        this.convertView = LayoutInflater.from(context).inflate(id,parent,false);
        this.convertView.setTag(this);
    }

    /**
     * 通过viewId 获取view
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = viewsSparseArray.get(viewId);

        if(view == null){
            view = convertView.findViewById(viewId);
            viewsSparseArray.put(viewId,view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return convertView;
    }
}
