package com.fionera.demo.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.fionera.base.fragment.BaseFragment;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;
import com.fionera.demo.activity.ChatActivity;
import com.fionera.demo.adapter.RecentSessionAdapter;
import com.fionera.demo.util.TadaAnimator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * HomePageFragment
 *
 * @author fionera
 * @date 15-10-3
 */

public class HomePageFragment
        extends BaseFragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private List<String> sessionList;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initViews(View rootView) {
        setTitleBarText("最近");

        recyclerView = rootView.findViewById(R.id.rv_recent_session);
        floatingActionButton = rootView.findViewById(R.id.fab_home_add);

        Toolbar toolbar = (Toolbar) titleBar.getChildAt(0);
        toolbar.setNavigationIcon(R.drawable.ic_text_close);
        toolbar.setNavigationOnClickListener(v -> ShowToast.show("Boom"));
        toolbar.inflateMenu(R.menu.menu_single);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_single) {
                ShowToast.show("Shakalaka");
                return true;
            }
            return false;
        });

        sessionList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            sessionList.add("");
        }
        RecentSessionAdapter recentSessionAdapter = new RecentSessionAdapter(mContext, sessionList);
        recyclerView.setAdapter(recentSessionAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        recentSessionAdapter.setRvItemTouchListener((v, pos) -> {
            if (pos == sessionList.size() - 1) {
                startActivity(new Intent(mContext, ChatActivity.class));
            }
            ShowToast.show(pos);
        });

        floatingActionButton.setOnClickListener(v -> addSession());
        floatingActionButton.setTranslationX(DemoApplication.screenWidth >> 2);
        floatingActionButton.animate().withLayer().translationX(0).setDuration(700).setInterpolator(
                new OvershootInterpolator(1.0f)).start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(recyclerView, "backgroundColor",
                new ArgbEvaluator(), Color.BLUE, Color.RED).setDuration(2000);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private void addSession() {
        TadaAnimator.nope(floatingActionButton, 1).start();
        sessionList.add(1, "");
        recyclerView.getAdapter().notifyItemInserted(1);
    }
}
