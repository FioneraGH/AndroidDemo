package com.fionera.demo.util;

import android.content.Context;
import android.os.Looper;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fionera
 */
public class CrashHandler
        implements UncaughtExceptionHandler {
    private Context context;
    private UncaughtExceptionHandler mDefaultHandler;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), r -> new Thread(r, "crash-handler-%d"));

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return new CrashHandler();
    }

    public void init(Context context) {
        this.context = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        executor.execute(() -> {
            if (context != null) {
                Looper.prepare();
                Looper.loop();
            }
        });
        return true;
    }
}