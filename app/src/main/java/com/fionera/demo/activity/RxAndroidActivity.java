package com.fionera.demo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author fionera
 */
public class RxAndroidActivity
        extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private List<AppInfo> outList = new ArrayList<>();
    private RxAdapter rxAdapter;
    private PublishSubject<Integer> downloadProgress = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);

        findViewById(R.id.btn_rx_demo).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(RxAndroidActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                    .PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            downloadProgress.distinct().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new Observer<Integer>() {
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            ShowToast.show("Download complete");
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            ShowToast.show("Download subscribe");
                        }

                        @Override
                        public void onNext(Integer integer) {
                            progressBar.setProgress(integer);
                        }
                    });
            downloadObservable().subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe();
        });

        swipeRefreshLayout = findViewById(R.id.srl_rx_demo);
        progressBar = findViewById(R.id.pb_rx_demo);
        RecyclerView recyclerView = findViewById(R.id.rv_rx_demo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rxAdapter = new RxAdapter();
        recyclerView.setAdapter(rxAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> loadList(outList));
        refreshList();
    }

    private void refreshList() {
        getApps().toSortedList().subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<AppInfo>>() {
            @Override
            public void onError(Throwable e) {
                ShowToast.show("Something error");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<AppInfo> appInfos) {
                rxAdapter.addApplications(appInfos);
                swipeRefreshLayout.setRefreshing(false);
                ShowToast.show("Here is the list");
            }
        });
    }

    private void loadList(List<AppInfo> outList) {
        rxAdapter.clearApplications();
        List<AppInfo> reverseList = new ArrayList<>(Arrays.asList(new AppInfo[outList.size()]));
        Collections.copy(reverseList, outList);
        Collections.reverse(reverseList);

        Observable<Long> clock = Observable.interval(1, TimeUnit.SECONDS);

        Observable.zip(Observable.merge(Observable.fromIterable(outList).take(3),
                Observable.fromIterable(reverseList).take(3)), clock,
                (appInfo, aLong) -> new AppInfo(appInfo.icon, appInfo.name + aLong)).map(appInfo -> (appInfo.name.startsWith("V")) ? new AppInfo(appInfo.icon,
                        "new " + appInfo.name) : appInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<AppInfo>() {

            @Override
            public void onError(Throwable e) {
                ShowToast.show("Something error");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                ShowToast.show("Here is the refresh list");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AppInfo appInfo) {
                rxAdapter.addApplication(appInfo);
            }
        });
    }

    private Observable<Boolean> downloadObservable() {
        return Observable.create(subscriber -> {
            boolean result = downloadFile(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/GfanMobile.apk");
            if (result) {
                subscriber.onNext(true);
                subscriber.onComplete();
            } else {
                subscriber.onError(new Throwable("Download error"));
            }
        });
    }

    private boolean downloadFile(String dest) {
        boolean result = false;
        InputStream in = null;
        OutputStream out = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(
                    "http://down.gfan.com/gfan/product/a/gfanmobile/beta/GfanMobile_2015092316.apk");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
            int fileLength = connection.getContentLength();
            in = connection.getInputStream();
            out = new FileOutputStream(dest);
            byte[] data = new byte[4096];
            long total = 0L;
            int count;
            while ((count = in.read(data)) != -1) {
                total += count;
                if (fileLength > 0) {
                    int percent = (int) (total * 100 / fileLength);
                    downloadProgress.onNext(percent);
                }
                out.write(data, 0, count);
            }
            downloadProgress.onComplete();
            result = true;
        } catch (Exception e) {
            downloadProgress.onError(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                downloadProgress.onError(e);
            }
            if (connection != null) {
                connection.disconnect();
                downloadProgress.onComplete();
            }
        }
        return result;
    }

    private Observable<AppInfo> getApps() {
        return Observable.create(subscriber -> {
            List<AppInfoRich> apps = new ArrayList<>();
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_DEFAULT);

            List<ResolveInfo> infos = getPackageManager().queryIntentActivities(mainIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(info));
            }

            for (AppInfoRich appInfo : apps) {
                Drawable icon = appInfo.icon;
                String name = appInfo.name;
                AppInfo info = new AppInfo(icon, name);
                subscriber.onNext(info);
                outList.add(info);
            }

            subscriber.onComplete();
        });
    }

    private class RxAdapter
            extends RecyclerView.Adapter<RxHolder> {

        private List<AppInfo> appInfos = new ArrayList<>();

        @NonNull
        @Override
        public RxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RxAndroidActivity.this).inflate(
                    android.R.layout.simple_list_item_1, parent, false);
            return new RxHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RxHolder holder, int position) {
            ((TextView) holder.itemView).setText(appInfos.get(position).name);
        }

        @Override
        public int getItemCount() {
            return appInfos.size();
        }

        void addApplication(AppInfo appInfo) {
            this.appInfos.add(appInfo);
            notifyItemInserted(this.appInfos.size());
        }

        void addApplications(List<AppInfo> appInfos) {
            this.appInfos.addAll(appInfos);
            notifyDataSetChanged();
        }

        void clearApplications() {
            this.appInfos.clear();
            notifyDataSetChanged();
        }
    }

    class RxHolder
            extends RecyclerView.ViewHolder {
        RxHolder(View itemView) {
            super(itemView);
        }
    }

    private class AppInfo
            implements Comparable<Object> {
        Drawable icon;
        String name;

        AppInfo(Drawable icon, String name) {
            this.icon = icon;
            this.name = name;
        }

        @Override
        public int compareTo(@NonNull Object another) {
            AppInfo f = (AppInfo) another;
            return name.compareTo(f.name);
        }
    }

    private class AppInfoRich {
        ResolveInfo info;
        Drawable icon;
        String name;

        AppInfoRich(ResolveInfo info) {
            this.info = info;
            this.icon = info.loadIcon(getPackageManager());
            this.name = info.loadLabel(getPackageManager()).toString();
        }
    }
}
