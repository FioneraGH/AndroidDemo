package com.fionera.wechatdemo.extra;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.fragment.TabLayoutFragment;
import com.fionera.wechatdemo.util.ShowToast;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class SmartTabLayoutActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SmartTabLayout tabLayout;

    private String[] title = new String[]{"你好", "我并不好", "我真的很不好", "你", "你好", "我并不好", "我真的很不好", "你"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_tab_layout);

        tabLayout = (SmartTabLayout) findViewById(R.id.tl_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.vp_tab_layout);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return new TabLayoutFragment();
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position % title.length];
            }
        });

        tabLayout.setViewPager(viewPager);
    }

}
