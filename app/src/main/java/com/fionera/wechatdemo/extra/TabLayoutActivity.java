package com.fionera.wechatdemo.extra;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.fragment.TabLayoutFragment;

public class TabLayoutActivity extends FragmentActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String [] title = new String[]{"hello","world"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        tabLayout = (TabLayout) findViewById(R.id.tl_tab_layout);
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

        tabLayout.setupWithViewPager(viewPager);
    }

}
