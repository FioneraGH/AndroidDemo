package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.view.SlidingMenu;

public class HorizontalActivity extends Activity {


    private Button button;
    private SlidingMenu slidingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);

        button = (Button) findViewById(R.id.btn_toogle_menu);
        slidingMenu = (SlidingMenu) findViewById(R.id.sm_horizontal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.ToogleMenu();
            }
        });
    }

}
