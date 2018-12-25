package com.fionera.multipic.widget;

import android.os.Bundle;

import com.fionera.multipic.common.LocalImageHelper;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
        bundle.putString("url", files.get(position).getOriginalUri());
        fragment.setArguments(bundle);
        return fragment;
    }
}