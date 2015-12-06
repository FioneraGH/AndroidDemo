package com.fionera.wechatdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.fionera.wechatdemo.R;

/**
 * Created by fionera on 15-10-3.
 */
public class RadioGroupFragment extends Fragment implements View.OnClickListener{

    private RadioGroup rgAllButton;
    private Button btnClearCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radiogroup, container, false);

        rgAllButton = (RadioGroup) view.findViewById(R.id.rg_home);
        btnClearCheck = (Button) view.findViewById(R.id.btn_home_clear);
        btnClearCheck.setTag("clear");
        btnClearCheck.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getTag().equals("clear")){
            rgAllButton.clearCheck();
        }
    }
}
