package com.fionera.multipic.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fionera.multipic.common.Constants;
import com.fionera.multipic.common.LocalImageHelper;

import java.util.List;

public class LocalViewPagerAdapter
        extends FragmentStatePagerAdapter {
    private List<LocalImageHelper.LocalFile> files;

    public LocalViewPagerAdapter(FragmentManager fragmentManager,
                                 List<LocalImageHelper.LocalFile> files) {
        super(fragmentManager);
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.ZOOM, true);
        bundle.putString("url", files.get(position).getOriginalUri());
        fragment.setArguments(bundle);
        return fragment;
    }
}