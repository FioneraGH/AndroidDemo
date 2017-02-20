package com.fionera.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.fragment.ContactFragment;
import com.fionera.demo.fragment.ExtrasFragment;
import com.fionera.demo.fragment.HomePageFragment;
import com.fionera.demo.fragment.LoginFragment;
import com.fionera.demo.fragment.RichTextFragment;
import com.fionera.demo.service.BluetoothLeService;
import com.fionera.demo.util.PageTransformer;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends BaseActivity
        implements LoginFragment.OnFragmentInteractionListener {

    @ViewInject(R.id.vp_main_page)
    private ViewPager viewPager;
    @ViewInject(R.id.ptas_main_page)
    private PagerTabStrip pagerTabStrip;
    @ViewInject(R.id.bnv_main_page)
    private BottomNavigationView bottomNavigationView;

    private List<Fragment> views = new ArrayList<>();
    private MenuItem preMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNeverHasMenuKey();

        x.view().inject(this);

        viewPager.setPageTransformer(true, new PageTransformer());
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(mContext, R.color.blue2));

        HomePageFragment homePageFragment = new HomePageFragment();
        views.add(homePageFragment);
        ContactFragment contactFragment = new ContactFragment();
        views.add(contactFragment);
        RichTextFragment richTextFragment = new RichTextFragment();
        views.add(richTextFragment);
        ExtrasFragment extrasFragment = new ExtrasFragment();
        views.add(extrasFragment);

        initViewPager();
    }

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
            }

            @Override
            public void onPageSelected(int position) {
                if (preMenuItem != null) {
                    preMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                preMenuItem = bottomNavigationView.getMenu().getItem(position);
                if (preMenuItem != null) {
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.menu_list:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.menu_rich:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.menu_extra:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(mContext, BluetoothLeService.class));
    }

    /**
     * 设置菜单项在有键盘的手机可见
     */
    private void setNeverHasMenuKey() {
        try {
            ViewConfiguration vc = ViewConfiguration.get(mContext);

            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(vc, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射在菜单打开时显示ActionBar菜单项Icon
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
    public void onFragmentInteraction(String result) {
        ShowToast.show(result);
    }
}
