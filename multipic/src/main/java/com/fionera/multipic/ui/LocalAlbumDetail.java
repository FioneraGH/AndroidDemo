package com.fionera.multipic.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.base.activity.BaseActivity;
import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageUtil;
import com.fionera.multipic.common.LocalImageHelper;
import com.fionera.multipic.widget.AlbumViewPager;

import org.xutils.x;

import java.util.List;

/**
 * 相册详情页
 */
public class LocalAlbumDetail
        extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener {

    private GridView gridView;

    private TextView tvTitle;
    private TextView tvFinish;

    private TextView headerFinish;
    private CheckBox checkBox;
    private View pagerContainer;
    private AlbumViewPager viewpager;
    private TextView mCountView;

    private List<LocalImageHelper.LocalFile> currentFolder = null;
    private List<LocalImageHelper.LocalFile> checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);

        if (!LocalImageHelper.getInstance().isInitialized()) {
            finish();
            return;
        }

        ImageView ivBack = (ImageView) findViewById(R.id.iv_local_album_detail_back);
        ImageView ivHeaderBack = (ImageView) findViewById(R.id.ic_local_album_detail_header_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideViewPager();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_local_album_detail_title);
        tvFinish = (TextView) findViewById(R.id.tv_local_album_detail_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
            }
        });
        headerFinish = (TextView) findViewById(R.id.tv_local_album_detail_header_finish);
        headerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
            }
        });

        gridView = (GridView) findViewById(R.id.gv_local_album_detail);

        pagerContainer = findViewById(R.id.fl_local_album_detail_pager_view);
        mCountView = (TextView) findViewById(R.id.tv_local_album_detail_header_count);
        checkBox = (CheckBox) findViewById(R.id.cb_local_album_detail_check);
        checkBox.setOnCheckedChangeListener(this);
        viewpager = (AlbumViewPager) findViewById(R.id.vp_local_album_detail);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (viewpager.getAdapter() != null) {
                    String text = (position + 1) + "/" + viewpager.getAdapter().getCount();
                    mCountView.setText(text);
                    checkBox.setTag(currentFolder.get(position));
                    checkBox.setChecked(checkedItems.contains(currentFolder.get(position)));
                } else {
                    mCountView.setText("0/0");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final String folder = getIntent().getExtras().getString("local_folder_name");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //防止停留在本界面时切换到桌面，导致应用被回收，图片数组被清空，在此处做一个初始化处理
                LocalImageHelper.getInstance().initImage();
                //获取该文件夹下地所有文件
                final List<LocalImageHelper.LocalFile> folders = LocalImageHelper.getInstance()
                        .getFolder(folder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (folders != null) {
                            currentFolder = folders;
                            MyAdapter adapter = new MyAdapter(
                                    LocalAlbumDetail.this, folders);
                            tvTitle.setText(folder);
                            gridView.setAdapter(adapter);
                            if (checkedItems.size() + LocalImageHelper.getInstance()
                                    .getCurrentSize() > 0) {
                                if (LocalImageHelper.getInstance().getTotalSize() == 100) {
                                    tvFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper
                                            .getInstance().getCurrentSize()) + ")");
                                    headerFinish.setText(
                                            "完成(" + (checkedItems.size() + LocalImageHelper
                                                    .getInstance().getCurrentSize()) + ")");
                                } else {
                                    tvFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper
                                            .getInstance()
                                            .getCurrentSize()) + "/" + LocalImageHelper
                                            .getInstance().getTotalSize() + ")");
                                    headerFinish.setText(
                                            "完成(" + (checkedItems.size() + LocalImageHelper
                                                    .getInstance()
                                                    .getCurrentSize()) + "/" + LocalImageHelper
                                                    .getInstance().getTotalSize() + ")");
                                }
                            } else {
                                tvFinish.setText("完成");
                                headerFinish.setText("完成");
                            }
                        }
                    }
                });
            }
        }).start();
        checkedItems = LocalImageHelper.getInstance().getCheckedItems();
        LocalImageHelper.getInstance().setResultOk(false);
    }

    private void showViewPager(int index) {
        pagerContainer.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
        findViewById(R.id.ll_local_album_detail_title_bar).setVisibility(View.GONE);
        viewpager.setAdapter(viewpager.new LocalViewPagerAdapter(currentFolder));
        viewpager.setCurrentItem(index);
        mCountView.setText((index + 1) + "/" + currentFolder.size());
        if (index == 0) {
            checkBox.setTag(currentFolder.get(index));
            checkBox.setChecked(checkedItems.contains(currentFolder.get(index)));
        }
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1,
                pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(300);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
    }

    private void hideViewPager() {
        pagerContainer.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        findViewById(R.id.ll_local_album_detail_title_bar).setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9,
                pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
        ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (pagerContainer.isShown()) {
            hideViewPager();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (!checked) {
            if (checkedItems.contains(compoundButton.getTag())) {
                checkedItems.remove(compoundButton.getTag());
            }
        } else {
            if (!checkedItems.contains(compoundButton.getTag())) {
                if (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize() >= LocalImageHelper.getInstance().getTotalSize()) {
                    Toast.makeText(this,
                            "最多选择" + LocalImageHelper.getInstance().getTotalSize() + "张图片",
                            Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
            }
        }
        if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
            if (LocalImageHelper.getInstance().getTotalSize() == 100) {
                tvFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize()) + ")");
                headerFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize()) + ")");
            } else {
                tvFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize()) + "/" + LocalImageHelper.getInstance()
                        .getTotalSize() + ")");
                headerFinish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize()) + "/" + LocalImageHelper.getInstance()
                        .getTotalSize() + ")");
            }
            /*
              set enable when it can be used
             */
            tvFinish.setEnabled(true);
            headerFinish.setEnabled(true);
        } else {
            tvFinish.setText("完成");
            tvFinish.setEnabled(false);
            headerFinish.setText("完成");
            headerFinish.setEnabled(false);
        }
    }

    private class MyAdapter
            extends BaseAdapter {
        List<LocalImageHelper.LocalFile> paths;

        MyAdapter(Context context, List<LocalImageHelper.LocalFile> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public LocalImageHelper.LocalFile getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.gv_local_album_detail_item, viewGroup, false);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setOnCheckedChangeListener(LocalAlbumDetail.this);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            LocalImageHelper.LocalFile localFile = paths.get(i);
            ImageUtil.loadImage(localFile.getThumbnailUri(), viewHolder.imageView);
            viewHolder.checkBox.setTag(localFile);
            viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showViewPager(i);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }
}