package com.fionera.demo.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.fionera.demo.R
import com.fionera.demo.fragment.TabLayoutFragment
import com.fionera.demo.util.ShowToast
import kotlinx.android.synthetic.main.activity_smart_tab_layout.*
import org.jetbrains.anko.find

class SmartTabLayoutActivity : AppCompatActivity() {

    private val title = arrayOf("你好", "我并不好", "我真的很不好", "我并不好", "我真的很不好")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_tab_layout)

        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_recycle)
        toolbar.setOnMenuItemClickListener { item ->
            ShowToast.show(item.title)
            true
        }

        val drawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)
        drawerToggle.syncState()
        drawer.addDrawerListener(drawerToggle)

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

        tl_tab_layout!!.setViewPager(vp_tab_layout)
    }
}
