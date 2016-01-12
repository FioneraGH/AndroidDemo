package com.fionera.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fionera.demo.activity.ChatActivity;
import com.fionera.demo.activity.DanmuActivity;
import com.fionera.demo.activity.FlowLayoutActivity;
import com.fionera.demo.activity.PropertyAnimActivity;
import com.fionera.demo.activity.SmartTabLayoutActivity;
import com.fionera.demo.fragment.BitmapUtilFragment;
import com.fionera.demo.fragment.ContactFragment;
import com.fionera.demo.fragment.HomePageFragment;
import com.fionera.demo.fragment.ExtrasFragment;
import com.fionera.demo.view.ChangableTabView;
import com.fionera.demo.view.FloatView;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final String CLICK_TO_CHANGE = "com.fionera.broadcast.CLICK_TO_CHANGE";
    public static final String CLICK_TO_CHANGE_FROM_OTHER = "com.fionera.broadcast" + "" +
            ".CLICK_TO_CHANGE_FROM_OTHER";

    private Context mContext = this;
    private WindowManager wm;
    private WindowManager.LayoutParams wmLayoutParams;
    private FloatView floatView;

    private ViewPager viewPager;
    private List<Fragment> views = new ArrayList<>();

    private List<ChangableTabView> tabs = new ArrayList<>();

    private ClickReceiver clickReceiver;


    @Event({R.id.rl_flow_layout, R.id.rl_tab_layout, R.id.rl_property_anim, R.id.rl_dan_mu})
    private void onMenuClick(View v) {

        switch (v.getId()) {
            case R.id.rl_flow_layout:
                startActivity(new Intent(mContext, FlowLayoutActivity.class));
                break;
            case R.id.rl_tab_layout:
                startActivity(new Intent(mContext, SmartTabLayoutActivity.class));
                break;
            case R.id.rl_property_anim:
                startActivity(new Intent(mContext, PropertyAnimActivity.class));
                break;
            case R.id.rl_dan_mu:
                startActivity(new Intent(mContext, DanmuActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNeverHasMenuKey();
        //        createFloatView();

        x.view().inject(this);

        viewPager = (ViewPager) findViewById(R.id.vp_main_page);

        HomePageFragment homePageFragment = new HomePageFragment();
        views.add(homePageFragment);
        ContactFragment contactFragment = new ContactFragment();
        views.add(contactFragment);
        BitmapUtilFragment bitmapUtilFragment = new BitmapUtilFragment();
        views.add(bitmapUtilFragment);
        ExtrasFragment extrasFragment = new ExtrasFragment();
        views.add(extrasFragment);

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CLICK_TO_CHANGE);
        intentFilter.addAction(CLICK_TO_CHANGE_FROM_OTHER);
        clickReceiver = new ClickReceiver();
        registerReceiver(clickReceiver, intentFilter);

    }

    private void createFloatView() {

        floatView = new FloatView(getApplicationContext());
        floatView.setImageResource(R.mipmap.ic_launcher);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wmLayoutParams = DemoApplication.getWmLayoutParams();
        /**
         * 设置类型为TYPE_PHONE，拥有最高顶层的权限
         */
        wmLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        /**
         * 设置图片背景透明
         */
        wmLayoutParams.format = PixelFormat.RGBA_8888;
        /**
         * 初始位置在左上角
         */
        wmLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        /**
         * 设定原点为(0,0)
         */
        wmLayoutParams.x = 0;
        wmLayoutParams.y = 0;
        /**
         * 设定长宽
         */
        wmLayoutParams.width = 256;
        wmLayoutParams.height = 256;

        wm.addView(floatView, wmLayoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (clickReceiver != null) {
            unregisterReceiver(clickReceiver);
        }

        try {
            wm.removeView(floatView);
        } catch (Exception ignored) {

        }
    }

    /**
     * 设置在有键盘的手机可见
     */
    private void setNeverHasMenuKey() {

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

        Intent intent = new Intent(CLICK_TO_CHANGE);
        switch (v.getId()) {
            case R.id.ctv_one:
                intent.putExtra("which_click", 0);
                break;
            case R.id.ctv_two:
                intent.putExtra("which_click", 1);
                break;
            case R.id.ctv_three:
                intent.putExtra("which_click", 2);
                break;
            case R.id.ctv_four:
                intent.putExtra("which_click", 3);
                break;
        }

        sendBroadcast(intent);

    }

    private class ClickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", intent.getAction());
            if (intent.getAction().equals(CLICK_TO_CHANGE)) {

                viewPager.setCurrentItem(intent.getIntExtra("which_click", 0), false);
            } else if (intent.getAction().equals(CLICK_TO_CHANGE_FROM_OTHER)) {

                tabs.get(intent.getIntExtra("which_click", 0)).performClick();
            }
        }
    }
}
