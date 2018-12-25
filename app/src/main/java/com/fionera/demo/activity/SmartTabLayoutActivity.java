package com.fionera.demo.activity;

import android.os.Bundle;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.fragment.ExtrasFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author fionera
 */
public class SmartTabLayoutActivity
        extends AppCompatActivity {

    private String[] title = {"你好", "我并不好", "我真的很不好", "我并不好", "我真的很不好"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_tab_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_test);
        toolbar.setOnMenuItemClickListener(item -> {
            ShowToast.show(item.getTitle());
            return true;
        });

        DrawerLayout drawer = findViewById(R.id.drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawerToggle.syncState();
        drawer.addDrawerListener(drawerToggle);

        ViewPager viewPager = findViewById(R.id.vp_tab_layout);
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

        SmartTabLayout tabLayout = findViewById(R.id.tl_tab_layout);
        tabLayout.setViewPager(viewPager);
    }
}
