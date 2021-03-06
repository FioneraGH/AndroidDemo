package com.fionera.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.fionera.base.util.DisplayUtils;
import com.fionera.demo.DemoApplication;
import com.fionera.demo.R;
import com.fionera.demo.view.game2048.NumberContainer;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author fionera
 */
public class GameActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g2048);

        final Button button = findViewById(R.id.btn_g2048_score);
        LinearLayout linearLayout = findViewById(R.id.ll_image_container);
        NumberContainer numberContainer = findViewById(R.id.nc_g2048_game_board);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                DemoApplication.screenWidth - 2 * DisplayUtils.dp2px(60), DisplayUtils.dp2px(200));

        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.ic_chat_head);
        imageView1.setScaleType(ScaleType.CENTER);
        imageView1.setBackgroundColor(Color.YELLOW);
        linearLayout.addView(imageView1, params);

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.ic_chat_head);
        imageView2.setScaleType(ScaleType.CENTER);
        imageView2.setBackgroundColor(Color.RED);
        linearLayout.addView(imageView2, params);

        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.ic_chat_head);
        imageView3.setScaleType(ScaleType.CENTER);
        imageView3.setBackgroundColor(Color.GRAY);
        linearLayout.addView(imageView3, params);

        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.ic_chat_head);
        imageView4.setScaleType(ScaleType.CENTER);
        imageView4.setBackgroundColor(Color.BLUE);
        linearLayout.addView(imageView4, params);

        ImageView imageView5 = new ImageView(this);
        imageView5.setImageResource(R.drawable.ic_chat_head);
        imageView5.setScaleType(ScaleType.CENTER);
        imageView5.setBackgroundColor(Color.GREEN);
        linearLayout.addView(imageView5, params);

        numberContainer.setOnScoreChangeListener(score -> {
            if (-1 == score) {
                button.setText("游戏结束");
            } else {
                button.setText(score);
            }
        });
    }
}
