package com.fionera.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.google.android.material.behavior.SwipeDismissBehavior;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author fionera
 */
public class MatrixActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        ImageView imageView1 = findViewById(R.id.ivTest1);
        ImageView imageView2 = findViewById(R.id.ivTest2);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageView1.setImageBitmap(bitmap);

        Bitmap newBitmap = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Color.BLACK);
        // 设置变换抗锯齿
        paint.setAntiAlias(true);
        Matrix matrix = new Matrix();
        // 通过矩阵设置图形变换
        matrix.setValues(new float[]{0.5f, 0, 0, 0, 0.8f, 0, 0, 0, 1});
        // 设置旋转角度
        matrix.setRotate(30);
        // 设置平移
        matrix.postTranslate(10, 0);
        // 绘制Bitmap
        canvas.drawBitmap(bitmap, matrix, paint);
        imageView2.setImageBitmap(newBitmap);

        final SwipeDismissBehavior<ImageView> swipe = new SwipeDismissBehavior<>();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                ShowToast.show("已移除");
            }

            @Override
            public void onDragStateChanged(int state) {
            }
        });
    }
}
