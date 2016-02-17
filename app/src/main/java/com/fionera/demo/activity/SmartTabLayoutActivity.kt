package com.fionera.demo.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.fionera.demo.R
import com.fionera.demo.fragment.TabLayoutFragment
import com.fionera.demo.util.ShowToast
import kotlinx.android.synthetic.main.activity_smart_tab_layout.*

class SmartTabLayoutActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null

    private val title = arrayOf("你好", "我并不好", "我真的很不好", "你", "你好", "我并不好", "我真的很不好", "你")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_tab_layout)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.menu_recycle)
        toolbar.setOnMenuItemClickListener { item ->

            ShowToast.show(item.title)
            true
        }
        val mDrawerLayout = findViewById(R.id.drawer) as DrawerLayout
        val mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, 0, 0)
        mDrawerToggle.syncState()
        mDrawerLayout.setDrawerListener(mDrawerToggle)

        vp_tab_layout!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {

                return TabLayoutFragment()
            }

            override fun getCount(): Int {
                return title.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return title[position % title.size]
            }
        }

        tl_tab_layout!!.setViewPager(viewPager)
    }

}
