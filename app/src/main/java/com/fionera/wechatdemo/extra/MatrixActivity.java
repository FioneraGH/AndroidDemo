package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        paint.setAntiAlias(true);
        Matrix matrix = new Matrix();
        matrix.setValues(new float[]{
                0.5f, 0, 0,
                0, 0.8f, 0,
                0, 0, 1
        });
        matrix.setRotate(30);
        matrix.postTranslate(10,0);
        canvas.drawBitmap(bitmap, matrix, paint);

        imageView2.setImageBitmap(newBitmap);


    }
}
