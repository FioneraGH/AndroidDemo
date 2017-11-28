package com.fionera.multipic.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.base.activity.BaseActivity;
import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageUtil;
import com.fionera.multipic.common.LocalImageHelper;
import com.fionera.multipic.widget.HackyViewPager;
import com.fionera.multipic.widget.LocalViewPagerAdapter;

import java.util.List;
import java.util.Locale;

/**
 * 相册详情页
 */
public class LocalAlbumDetail
        extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener {

    private RecyclerView recyclerView;

    private TextView tvFinish;
    private TextView headerFinish;

    private CheckBox checkBox;
    private FrameLayout pagerContainer;
    private HackyViewPager viewPager;
    private TextView mCountView;

    private List<LocalImageHelper.LocalFile> currentFolder;
    private List<LocalImageHelper.LocalFile> checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);

        findViewById(R.id.iv_local_album_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ic_local_album_detail_header_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideViewPager();
            }
        });

        tvFinish = findViewById(R.id.tv_local_album_detail_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
            }
        });
        headerFinish = findViewById(R.id.tv_local_album_detail_header_finish);
        headerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
            }
        });

        recyclerView = findViewById(R.id.local_album_detail);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

        pagerContainer = findViewById(R.id.fl_local_album_detail_pager_view);
        mCountView = findViewById(R.id.tv_local_album_detail_header_count);
        checkBox = findViewById(R.id.cb_local_album_detail_check);
        checkBox.setOnCheckedChangeListener(this);
        viewPager = findViewById(R.id.vp_local_album_detail);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (viewPager.getAdapter() != null) {
                    String text = String.format(Locale.CHINA, "%d/%d", position + 1,
                            viewPager.getAdapter().getCount());
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
        String folder = "Default";
        /*
        none check
         */
        if (getIntent().hasExtra("local_folder_name")) {
            folder = getIntent().getStringExtra("local_folder_name");
        }
        ((TextView) findViewById(R.id.tv_local_album_detail_title)).setText(folder);
        currentFolder = LocalImageHelper.getInstance().getFolder(
                folder);
        checkedItems = LocalImageHelper.getInstance().getCheckedItems();
        if (currentFolder != null) {
            ImagePreviewAdapter adapter = new ImagePreviewAdapter(currentFolder);
            recyclerView.setAdapter(adapter);
            updateCheckedInfo();
        }

        LocalImageHelper.getInstance().setResultOk(false);
    }

    private void updateCheckedInfo() {
        String checkedInfo = String.format(Locale.CHINA, "完成(%d/%d)",
                checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize(),
                LocalImageHelper.getInstance().getTotalSize());
        tvFinish.setText(checkedInfo);
        headerFinish.setText(checkedInfo);
    }

    private void showViewPager(int index) {
        findViewById(R.id.ll_local_album_detail_title_bar).setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pagerContainer.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new LocalViewPagerAdapter(getSupportFragmentManager(), currentFolder));
        viewPager.setCurrentItem(index);
        mCountView.setText(String.format(Locale.CHINA, "%d/%d", index + 1, currentFolder.size()));
        if (index == 0) {
            checkBox.setTag(currentFolder.get(index));
            checkBox.setChecked(checkedItems.contains(currentFolder.get(index)));
        }
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1,
                pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(300);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(300);
        animationSet.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(animationSet);
    }

    private void hideViewPager() {
        findViewById(R.id.ll_local_album_detail_title_bar).setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        pagerContainer.setVisibility(View.GONE);
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9,
                pagerContainer.getWidth() / 2, pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        animationSet.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(animationSet);
        recyclerView.getAdapter().notifyDataSetChanged();
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
        if (compoundButton.getTag() instanceof LocalImageHelper.LocalFile) {
            if (checked) {
                if (checkedItems.size() + LocalImageHelper.getInstance()
                        .getCurrentSize() >= LocalImageHelper.getInstance().getTotalSize()) {
                    Toast.makeText(this,
                            "最多选择" + LocalImageHelper.getInstance().getTotalSize() + "张图片",
                            Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    return;
                }

                if (!checkedItems.contains(compoundButton.getTag())) {
                    checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
                }
            } else {
                if (checkedItems.contains(compoundButton.getTag())) {
                    checkedItems.remove(compoundButton.getTag());
                }
            }
            updateCheckedInfo();
        }
    }

    private class ImagePreviewAdapter
            extends RecyclerView.Adapter<ViewHolder> {
        List<LocalImageHelper.LocalFile> files;

        ImagePreviewAdapter(List<LocalImageHelper.LocalFile> files) {
            this.files = files;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.rv_local_album_detail_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            LocalImageHelper.LocalFile localFile = files.get(position);
            ImageUtil.loadImage(localFile.getThumbnailUri(), holder.imageView);
            holder.checkBox.setTag(localFile);
            holder.checkBox.setChecked(checkedItems.contains(localFile));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showViewPager(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return files.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(LocalAlbumDetail.this);
        }
    }
}