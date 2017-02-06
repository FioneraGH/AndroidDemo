package com.fionera.multipic.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.multipic.R;
import com.fionera.multipic.common.LocalImageHelper;

import org.xutils.x;

import java.util.List;


/**
 * 图片集详情展示页
 */
public class AlbumViewPager extends ViewPager
		implements MatrixImageView.OnMovingListener {

	private boolean mChildIsBeingDragged = false;

	private MatrixImageView.OnSingleTapListener onSingleTapListener;

    public AlbumViewPager(Context context) {
        this(context, null);
    }

	public AlbumViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return !mChildIsBeingDragged && super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void startDrag() {
		mChildIsBeingDragged=true;
	}

	@Override
	public void stopDrag() {
		mChildIsBeingDragged=false;
	}

	public void setOnSingleTapListener(MatrixImageView.OnSingleTapListener onSingleTapListener) {
		this.onSingleTapListener = onSingleTapListener;
	}

	public class LocalViewPagerAdapter extends PagerAdapter {
		private List<LocalImageHelper.LocalFile> paths;

        public LocalViewPagerAdapter(List<LocalImageHelper.LocalFile> paths) {
            this.paths = paths;
        }

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup viewGroup, int position) {
            View imageLayout = inflate(getContext(), R.layout.item_album_pager, null);
            viewGroup.addView(imageLayout);
            MatrixImageView imageView = (MatrixImageView) imageLayout.findViewById(R.id.image);
            imageView.setOnMovingListener(AlbumViewPager.this);
            imageView.setOnSingleTapListener(onSingleTapListener);
            LocalImageHelper.LocalFile path = paths.get(position);
            x.image().bind(imageView, "file://" + path.getOriginalUri());
            return imageLayout;
        }

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int arg1, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}