package com.fionera.multipic.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageUtil;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment
        extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_image_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PhotoView imageView = (PhotoView) view;
        Bundle bundle = getArguments();
        if (bundle != null) {
            ImageUtil.loadImage(bundle.getString("url"), imageView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
