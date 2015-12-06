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

public class TabLayoutActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FloatingActionButton floatingActionButton;

    private String [] title = new String[]{"hello","world"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        tabLayout = (TabLayout) findViewById(R.id.tl_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.vp_tab_layout);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_tab_layout);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "该方法待添加！", Snackbar.LENGTH_SHORT).setAction("撤销",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowToast.show("别点！！");
                            }
                        }).show();
            }
        });


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
