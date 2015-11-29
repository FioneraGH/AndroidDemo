package com.fionera.wechatdemo;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fionera.wechatdemo.application.DemoApplication;
import com.fionera.wechatdemo.fragment.RadioGroupFragment;
import com.fionera.wechatdemo.fragment.TabLayoutFragment;
import com.fionera.wechatdemo.view.ChangableTabView;
import com.fionera.wechatdemo.view.FloatView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final String CLICK_TO_CHANGE = "com.fionera.broadcast.CLICK_TO_CHANGE";
    public static final String CLICK_TO_CHANGE_FROM_OTHER = "com.fionera.broadcast" + "" +
            ".CLICK_TO_CHANGE_FROM_OTHER";

    private WindowManager wm;
    private WindowManager.LayoutParams wmLayoutParams;
    private FloatView floatView;

    private ViewPager viewPager;
    private List<Fragment> views = new ArrayList<>();
    private String[] titles = new String[]{"One", "Two", "Three", "Four"};

    private LinearLayout linearLayout;
    private List<ChangableTabView> tabs = new ArrayList<>();

    private ClickReceiver clickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverFlowAlwaysShow();
        //        createFloatView();

        viewPager = (ViewPager) findViewById(R.id.vp_main_page);
        linearLayout = (LinearLayout) findViewById(R.id.ll_main_page);

        int count = 0;
        for (String title : titles) {
            if (3 == count) {
                RadioGroupFragment radioGroupFragment = new RadioGroupFragment();
                views.add(radioGroupFragment);
            } else {
                TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                Bundle bundle = new Bundle();
                bundle.putString(TabLayoutFragment.TITLE, title);
                tabLayoutFragment.setArguments(bundle);
                views.add(tabLayoutFragment);
            }
            count ++;
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
        } catch (Exception e) {

        }
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
        if (item.getItemId() == R.id.action_friend) {

        }
        if (item.getItemId() == R.id.action_scan) {
            Toast.makeText(MainActivity.this, "空", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_view) {

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
