package com.fionera.demo.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.fionera.demo.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class RxAndroidActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);

        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Observable	completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Oh,no!	Something	wrong	happened!");
            }

            @Override
            public void onNext(String item) {
                System.out.println(item);
            }
        });
        publishSubject.onNext(helloWorld());
    }

    private String helloWorld(){
        return "Hello World~~~";
    }
}
