package com.fionera.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.LinearLayout;

import com.fionera.wechatdemo.fragment.TabLayoutFragment;
import com.fionera.wechatdemo.view.ChangableTabView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {


    private ViewPager viewPager;
    private List<Fragment> views = new ArrayList<>();
    private String[] titles = new String[]{"One", "Two", "Three", "Four"};

    private LinearLayout linearLayout;
    private List<ChangableTabView> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverFlowAlwaysShow();

        viewPager = (ViewPager) findViewById(R.id.vp_main_page);
        linearLayout = (LinearLayout) findViewById(R.id.ll_main_page);

        for (String title : titles) {
            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabLayoutFragment.TITLE, title);
            tabLayoutFragment.setArguments(bundle);
            views.add(tabLayoutFragment);
        }

        tabs.add((ChangableTabView) findViewById(R.id.ctv_one));
        tabs.add((ChangableTabView) findViewById(R.id.ctv_two));
        tabs.add((ChangableTabView) findViewById(R.id.ctv_three));
        tabs.add((ChangableTabView) findViewById(R.id.ctv_four));
        for (ChangableTabView view : tabs) {
            view.setOnClickListener(this);
            if (view.getId() == R.id.ctv_one) {
                view.setTabAlpha(1.0f);
            }
        }

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                if (positionOffset > 0) {

                    tabs.get(position).setTabAlpha(1 - positionOffset);
                    tabs.get(position + 1).setTabAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_chat) {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * 设置在有键盘的手机可见
     */
    private void setOverFlowAlwaysShow() {

        try {
            ViewConfiguration vc = ViewConfiguration.get(MainActivity.this);

            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(vc, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射获取菜单打开时根据方法名修改参数值
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        for (ChangableTabView view : tabs) {
            view.setTabAlpha(0.0f);
        }
        ((ChangableTabView) v).setTabAlpha(1.0f);

        switch (v.getId()) {
            case R.id.ctv_one:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.ctv_two:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.ctv_three:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.ctv_four:
                viewPager.setCurrentItem(3, false);
                break;
        }

    }
}
