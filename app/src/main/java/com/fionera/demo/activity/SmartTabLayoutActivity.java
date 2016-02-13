package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fionera.demo.R;
import com.fionera.demo.fragment.TabLayoutFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class SmartTabLayoutActivity
        extends AppCompatActivity {

    private ViewPager viewPager;
    private SmartTabLayout tabLayout;

    private String[] title = new String[]{"你好", "我并不好", "我真的很不好", "你", "你好", "我并不好", "我真的很不好", "你"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.menu_recycle);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                                                        toolbar, 0, 0);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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
