package com.fionera.wechatdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.PageTransformer;
import com.fionera.wechatdemo.view.ClipViewPager;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by fionera on 15-10-3.
 */
@ContentView(R.layout.fragment_home_page)
public class HomePageFragment extends Fragment {

    @ViewInject(R.id.lv_home_root)
    private LinearLayout llHomeRoot;
    @ViewInject(R.id.vp_home_circle)
    private ClipViewPager vpHomeCircle;

    private int[] imgRes = new int[]{R.drawable.iv_test, R.drawable.iv_test, R.drawable.iv_test,
            R.drawable.iv_test, R.drawable.iv_test, R.drawable.iv_test, R.drawable.iv_test};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        /**
         * no use
         */
        llHomeRoot.setOnTouchListener((v, event) -> vpHomeCircle.dispatchTouchEvent(event));
        //        vpHomeCircle.setPageMargin(20);
        vpHomeCircle.setPageTransformer(true, new PageTransformer());
        vpHomeCircle.setOffscreenPageLimit(imgRes.length);
        vpHomeCircle.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View pageView = inflater.inflate(R.layout.vp_post_pager_item, null);
                ImageView imageView = (ImageView) pageView.findViewById(R.id.iv_post_preview);
                imageView.setImageResource(imgRes[position]);
                vpHomeCircle.addView(pageView);
                return pageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                vpHomeCircle.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return imgRes.length;
            }
        });
        vpHomeCircle.setCurrentItem(3, false);
        return view;
    }
}
