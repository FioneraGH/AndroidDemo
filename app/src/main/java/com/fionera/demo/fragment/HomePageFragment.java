package com.fionera.demo.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.fionera.demo.DemoApplication;
import com.fionera.demo.activity.ChatActivity;
import com.fionera.demo.R;
import com.fionera.demo.adapter.RecentSessionAdapter;
import com.fionera.demo.util.DisplayUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fionera on 15-10-3.
 */
public class HomePageFragment
        extends BaseFragment {

    @ViewInject(R.id.rv_recent_session)
    private RecyclerView recyclerView;
    @ViewInject(R.id.fab_home_add)
    FloatingActionButton floatingActionButton;

    private List<String> sessionList;
    private RecentSessionAdapter recentSessionAdapter;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("最近");

        sessionList = new ArrayList<>();
        recentSessionAdapter = new RecentSessionAdapter(mContext, sessionList);
        recyclerView.setAdapter(recentSessionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        recentSessionAdapter.setRvItemTouchListener(
                (v, pos) -> startActivity(new Intent(mContext, ChatActivity.class)));

        floatingActionButton.setOnClickListener(v -> addSession());
        floatingActionButton.setTranslationX(DemoApplication.screenWidth / 3);
        floatingActionButton.animate().withLayer().translationX(0).setDuration(700)
                .setInterpolator(new OvershootInterpolator(1.0f)).start();
    }

    private void addSession() {
        floatingActionButton.animate().withLayer().scaleX(0.0f).scaleY(0.0f).setDuration(300)
                .start();
        float elevation = ViewCompat.getElevation(floatingActionButton);
        ViewCompat.setElevation(floatingActionButton, 0);
        sessionList.add("");
        recyclerView.getAdapter().notifyItemInserted(sessionList.size() - 1);
        recyclerView.postDelayed(() -> {
            floatingActionButton.animate().withLayer().scaleX(1.0f).scaleY(1.0f).setDuration(300)
                    .start();
            ViewCompat.setElevation(floatingActionButton, elevation);
        }, 3000);
    }
}
