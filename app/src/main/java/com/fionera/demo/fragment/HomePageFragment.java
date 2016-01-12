package com.fionera.demo.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fionera.demo.activity.ChatActivity;
import com.fionera.demo.R;
import com.fionera.demo.adapter.RecentSessionAdapter;

import org.xutils.view.annotation.ViewInject;

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
