package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fionera.demo.R;
import com.fionera.demo.fragment.ExtrasFragment;
import com.fionera.demo.util.ShowToast;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class SmartTabLayoutActivity
        extends AppCompatActivity {

    private String[] title = {"你好", "我并不好", "我真的很不好", "我并不好", "我真的很不好"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_test);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowToast.show(item.getTitle());
                return true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawerToggle.syncState();
        drawer.addDrawerListener(drawerToggle);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_tab_layout);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new ExtrasFragment();
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

        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tl_tab_layout);
        tabLayout.setViewPager(viewPager);
    }
}
