package com.fionera.wechatdemo.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fionera on 15-10-3.
 */
public class TabLayoutFragment extends Fragment {

    public final static String TITLE = "TITLE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        TextView view = new TextView(getActivity());
        if (getArguments() != null) {

            view.setText(getArguments().getString(TITLE));
        }
        return view;
    }
}
