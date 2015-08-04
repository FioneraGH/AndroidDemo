package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

import com.fionera.wechatdemo.R;

public class MatrixActivity extends Activity {

    private ImageView imageView1;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        imageView1 = (ImageView) findViewById(R.id.ivTest1);
        imageView2 = (ImageView) findViewById(R.id.ivTest2);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageView1.setImageBitmap(bitmap);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Color.BLACK);
        // 设置变换抗锯齿
        paint.setAntiAlias(true);
        Matrix matrix = new Matrix();
        // 通过矩阵设置图形变换
        matrix.setValues(new float[]{
                0.5f, 0, 0,
                0, 0.8f, 0,
                0, 0, 1
        });
        // 设置旋转角度
        matrix.setRotate(30);
        // 设置平移
        matrix.postTranslate(10, 0);
        // 绘制Bitmap
        canvas.drawBitmap(bitmap, matrix, paint);

        imageView2.setImageBitmap(newBitmap);


    }
}
