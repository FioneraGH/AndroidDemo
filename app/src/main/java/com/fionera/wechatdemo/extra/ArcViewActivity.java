package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.view.ArcMenu;

public class ArcViewActivity extends Activity {

    private ArcMenu arcMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_view);

        arcMenu =(ArcMenu)findViewById(R.id.arc_menu);
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {

            }
        });
    }
}
