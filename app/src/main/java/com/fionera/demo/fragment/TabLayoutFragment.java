package com.fionera.demo.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fionera on 15-10-3.
 */
public class TabLayoutFragment
        extends Fragment {

    public final static String TITLE = "TITLE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView view = new TextView(getActivity());
        if (getArguments() != null) {

            view.setText(getArguments().getString(TITLE));
            view.setTextSize(30);
        } else {
            view.setText(TITLE);
            view.setTextSize(30);
        }
        view.setBackground(new ColorDrawable(0xffffffff));
        return view;
    }
}