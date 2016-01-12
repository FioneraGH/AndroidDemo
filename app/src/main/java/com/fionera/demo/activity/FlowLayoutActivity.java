package com.fionera.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.fionera.demo.R;
import com.fionera.demo.view.FlowLayout;

public class FlowLayoutActivity extends Activity {


    private String[] tags = new String[]{
            "zkckhs", "zxche", "zcyuieks", "vkxvjjeh", "asdiuqwyruiyuf", "asd", "xcuy",
            "zkckhs", "zxche", "zcyuieks", "vkxvjjeh", "asdiuqwyruiyuf", "asd", "xcuy",
            "zkckhs", "zxche", "zcyuieks", "vkxvjjeh", "asdiuqwyruiyuf", "asd", "xcuy",
            "zkckhs", "zxche", "zcyuieks", "vkxvjjeh", "asdiuqwyruiyuf", "asd", "xcuy",
    };
    private FlowLayout flHotTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        flHotTag = (FlowLayout) findViewById(R.id.fl_hot_tag);

        initData();
    }

    private void initData() {
        for (int i = 0; i < tags.length; i++) {
            Button button = new Button(this);
            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);

            button.setText(tags[i]);
            flHotTag.addView(button,mlp);
        }
    }


}
