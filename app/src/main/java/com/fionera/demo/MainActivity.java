package com.fionera.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.LogCat;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.fragment.ConstraintSetFragment;
import com.fionera.demo.fragment.ExtrasFragment;
import com.fionera.demo.fragment.HomePageFragment;
import com.fionera.demo.fragment.LoginFragment;
import com.fionera.demo.fragment.RichTextFragment;
import com.fionera.demo.service.BluetoothLeService;
import com.fionera.demo.util.PageTransformer;
import com.fionera.multipic.common.LocalImageHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

/**
 * @author fionera
 */
public class MainActivity
        extends BaseActivity
        implements LoginFragment.OnFragmentInteractionListener {
    private static int ASK_FOR_READ_EXTERNAL = 3000;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private List<Fragment> views = new ArrayList<>();
    private MenuItem preMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.vp_main_page);
        PagerTabStrip pagerTabStrip = findViewById(R.id.ptas_main_page);
        bottomNavigationView = findViewById(R.id.bnv_main_page);

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ASK_FOR_READ_EXTERNAL);
        } else {
            LocalImageHelper.init();
        }

        viewPager.setPageTransformer(true, new PageTransformer());
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(mContext, R.color.blue2));

        HomePageFragment homePageFragment = new HomePageFragment();
        views.add(homePageFragment);
        ConstraintSetFragment constraintSetFragment = new ConstraintSetFragment();
        views.add(constraintSetFragment);
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
                StringBuilder titleText = new StringBuilder("测试 ");
                for (int i = 0; i < position; i++) {
                    titleText.append(
                            new DecimalFormat(" #0").format(Math.pow(position, position)));
                }
                return titleText.toString();
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

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.menu_list:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.menu_rich:
                    viewPager.setCurrentItem(2);
                    switchMainComponent();
                    break;
                case R.id.menu_extra:
                    viewPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    private void switchMainComponent() {
        ComponentName anotherComponentName = new ComponentName(this,
                "com.fionera.demo.AnotherMainActivity");
        ComponentName originComponentName = new ComponentName(this,
                "com.fionera.demo.MainActivity");
        PackageManager packageManager = getPackageManager();
        if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED == packageManager
                .getComponentEnabledSetting(anotherComponentName)) {
            packageManager.setComponentEnabledSetting(anotherComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            packageManager.setComponentEnabledSetting(originComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } else {
            packageManager.setComponentEnabledSetting(originComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            packageManager.setComponentEnabledSetting(anotherComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        killLauncher(packageManager);
    }

    @SuppressLint("MissingPermission")
    private void killLauncher(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                LogCat.d("Need kill Package:" + res.activityInfo.packageName);
                if (am != null) {
                    am.killBackgroundProcesses(res.activityInfo.packageName);
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalImageHelper.getInstance().release();
        stopService(new Intent(mContext, BluetoothLeService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ASK_FOR_READ_EXTERNAL == requestCode && Manifest.permission.READ_EXTERNAL_STORAGE
                .equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ShowToast.show("授权成功");
                LocalImageHelper.init();
            } else {
                ShowToast.show("该应用需要外部存储权限");
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(String result) {
        ShowToast.show(result);
    }
}
