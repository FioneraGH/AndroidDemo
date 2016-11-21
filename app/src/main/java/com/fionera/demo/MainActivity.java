package com.fionera.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.fionera.demo.fragment.BitmapUtilFragment;
import com.fionera.demo.fragment.ContactFragment;
import com.fionera.demo.fragment.ExtrasFragment;
import com.fionera.demo.fragment.HomePageFragment;
import com.fionera.demo.fragment.LoginFragment;
import com.fionera.demo.service.BluetoothLeService;
import com.fionera.demo.util.ShowToast;
import com.fionera.demo.view.ChangableTabView;
import com.fionera.demo.view.FloatView;

import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, LoginFragment.OnFragmentInteractionListener {

    public static final String CLICK_TO_CHANGE = "com.fionera.broadcast.CLICK_TO_CHANGE";

    private WindowManager wm;
    private WindowManager.LayoutParams wmLayoutParams;
    private FloatView floatView;

    private ViewPager viewPager;
    private List<Fragment> views = new ArrayList<>();

    private List<ChangableTabView> tabs = new ArrayList<>();

    private ClickReceiver clickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNeverHasMenuKey();
        //        createFloatView();

        x.view().inject(this);

        viewPager = (ViewPager) findViewById(R.id.vp_main_page);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.ptas_main_page);

        pagerTabStrip
                .setTabIndicatorColor(ContextCompat.getColor(MainActivity.this, R.color.blue2));

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

        initViewPager();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CLICK_TO_CHANGE);
        clickReceiver = new ClickReceiver();
        DemoApplication.getLocalBroadcastManager().registerReceiver(clickReceiver, intentFilter);
    }

    private int currIndex = 0;

    private void initViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title_text = "测试 ";
                for (int i = 0; i < position; i++) {
                    title_text += new DecimalFormat(" #0").format(Math.pow(position, position));
                }
                return title_text;
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

                Animation animation = new TranslateAnimation(100 * currIndex, 100 * position, 0, 0);
                currIndex = position;
                animation.setFillAfter(true);
                animation.setDuration(300);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
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
        wmLayoutParams.gravity = Gravity.START | Gravity.TOP;
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
            DemoApplication.getLocalBroadcastManager().unregisterReceiver(clickReceiver);
            clickReceiver = null;
        }

        try {
            wm.removeView(floatView);
        } catch (Exception ignored) {

        }

        stopService(new Intent(MainActivity.this, BluetoothLeService.class));
    }

    /**
     * 设置菜单项在有键盘的手机可见
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
     * 利用反射在菜单打开时显示ActionBar菜单项Icon
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
                    Method m = menu.getClass()
                            .getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
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

        DemoApplication.getLocalBroadcastManager().sendBroadcast(intent);

    }

    @Override
    public void onFragmentInteraction(String result) {

        ShowToast.show(result);
    }

    private class ClickReceiver
            extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", intent.getAction());
            if (intent.getAction().equals(CLICK_TO_CHANGE)) {
                viewPager.setCurrentItem(intent.getIntExtra("which_click", 0), false);
            }
        }
    }
}
