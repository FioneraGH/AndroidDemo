package com.fionera.wechatdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fionera.wechatdemo.ChatActivity;
import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.adapter.RecentSessionAdapter;
import com.fionera.wechatdemo.util.PageTransformer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by fionera on 15-10-3.
 */
public class HomePageFragment extends BaseFragment {

    @ViewInject(R.id.rv_recent_session)
    private RecyclerView recyclerView;

    private RecentSessionAdapter recentSessionAdapter;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void findViewInThisFunction(View rootView) {

        setTitleBarText("最近");

        recentSessionAdapter = new RecentSessionAdapter(mContext);
        recyclerView.setAdapter(recentSessionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        recentSessionAdapter.setRvItemTouchListener(
                (v, pos) -> startActivity(new Intent(mContext, ChatActivity.class)));
    }
}
