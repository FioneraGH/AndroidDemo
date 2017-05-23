package com.fionera.multipic.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFragment
        extends Fragment {

    private PhotoViewAttacher photoViewAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_image_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view;
        ImageUtil.loadImage(getArguments().getString("url"), imageView);
        photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.setRotatable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (photoViewAttacher != null) {
            photoViewAttacher.cleanup();
            photoViewAttacher = null;
        }
    }
}
