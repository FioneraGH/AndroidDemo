package com.fionera.demo.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;
import com.fionera.demo.activity.ChatActivity;
import com.fionera.demo.adapter.RecentSessionAdapter;
import com.fionera.demo.util.RvItemTouchListener;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.util.TadaAnimator;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * HomePageFragment
 * Created by fionera on 15-10-3.
 */

public class HomePageFragment
        extends BaseFragment {

    @ViewInject(R.id.rv_recent_session)
    private RecyclerView recyclerView;
    @ViewInject(R.id.fab_home_add)
    FloatingActionButton floatingActionButton;

    private List<String> sessionList;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("最近");

        Toolbar toolbar = (Toolbar) titleBar.getChildAt(0);
        toolbar.setNavigationIcon(R.drawable.ic_text_close);
        toolbar.setNavigationOnClickListener(v -> ShowToast.show("Boom"));
        ActionBar actionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sessionList = new ArrayList<>();
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        sessionList.add("");
        RecentSessionAdapter recentSessionAdapter = new RecentSessionAdapter(mContext, sessionList);
        recyclerView.setAdapter(recentSessionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        recentSessionAdapter.setRvItemTouchListener((v, pos) -> {
            if (pos == sessionList.size() - 1) {
                startActivity(new Intent(mContext, ChatActivity.class));
            }
            ShowToast.show("" + pos);
        });

        floatingActionButton.setOnClickListener(view -> addSession());
        floatingActionButton.setTranslationX(DemoApplication.screenWidth / 3);
        floatingActionButton.animate().withLayer().translationX(0).setDuration(700).setInterpolator(
                new OvershootInterpolator(1.0f)).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(recyclerView, "backgroundColor",
                new ArgbEvaluator(), Color.BLUE, Color.RED).setDuration(2000);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private void addSession() {
        TadaAnimator.nope(floatingActionButton).start();
        recyclerView.getAdapter().notifyItemInserted(1);
        recyclerView.postDelayed(() -> floatingActionButton.animate().withLayer().scaleX(1.0f)
                .scaleY(1.0f).setDuration(300).start(), 1000);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_single, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_single){
            ShowToast.show("Shakalaka");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
