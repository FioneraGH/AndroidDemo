package com.fionera.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.util.ShowToast;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class RxAndroidActivity
        extends AppCompatActivity {

    @ViewInject(R.id.srl_rx_demo)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.pb_rx_demo)
    private ProgressBar progressBar;
    @ViewInject(R.id.rv_rx_demo)
    private RecyclerView recyclerView;

    private List<AppInfo> outList = new ArrayList<>();
    private RxAdapter rxAdapter;
    private PublishSubject<Integer> downloadProgress = PublishSubject.create();

    @Event(R.id.btn_rx_demo)
    private void onClick(View v) {
        if (ContextCompat.checkSelfPermission(RxAndroidActivity.this,
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                                              new String[]{Manifest.permission
                                                      .WRITE_EXTERNAL_STORAGE},
                                              0);
        }
        downloadProgress.distinct().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        ShowToast.show("Download complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        progressBar.setProgress(integer);
                    }
                });
        downloadObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);
        x.view().inject(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rxAdapter = new RxAdapter();
        recyclerView.setAdapter(rxAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> loadList(outList));
        refreshList();
    }

    private void refreshList() {
        getApps().toSortedList().take(10).subscribe(new Subscriber<List<AppInfo>>() {
            @Override
            public void onCompleted() {
                ShowToast.show("Here is the list");
            }

            @Override
            public void onError(Throwable e) {
                ShowToast.show("Something error");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<AppInfo> appInfos) {
                rxAdapter.addApplications(appInfos);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadList(List<AppInfo> outList) {
        rxAdapter.clearApplications();
        List<AppInfo> reverseList = new ArrayList<>(Arrays.asList(new AppInfo[outList.size()]));
        Collections.copy(reverseList, outList);
        Collections.reverse(reverseList);
        for (AppInfo info : reverseList) {
            info.name = "r " + info.name;
        }

        Observable<Long> clock = Observable.interval(1, TimeUnit.SECONDS);

        Observable.zip(Observable.merge(Observable.from(outList).take(3),
                                        Observable.from(reverseList).take(3)), clock,
                       (appInfo, aLong) -> new AppInfo(appInfo.icon,
                                                       appInfo.name + String.valueOf(aLong)))
                .map(appInfo -> (appInfo.name.startsWith("V")) ? new AppInfo(appInfo.icon,
                                                                             "new " + appInfo
                                                                                     .name) :
                        appInfo)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AppInfo>() {

            @Override
            public void onCompleted() {
                ShowToast.show("Here is the refresh list");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                ShowToast.show("Something error");
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(AppInfo appInfo) {
                rxAdapter.addApplication(appInfo);
            }
        });
    }

    private Observable<Boolean> downloadObservable() {
        return Observable.create(subscriber -> {
            boolean result = downloadFile(
                    "http://down.gfan.com/gfan/product/a/gfanmobile/beta/GfanMobile_2015092316.apk",
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/GfanMobile.apk");
            if (result) {
                subscriber.onNext(true);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable("Download error"));
            }
        });
    }

    private boolean downloadFile(String src, String dest) {
        boolean result = false;
        InputStream in = null;
        OutputStream out = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(src);
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
            downloadProgress.onCompleted();
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
                downloadProgress.onCompleted();
            }
        }
        return result;
    }

    private Observable<AppInfo> getApps() {
        return Observable.create((Observable.OnSubscribe<AppInfo>) subscriber -> {
            List<AppInfoRich> apps = new ArrayList<>();
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_DEFAULT);

            List<ResolveInfo> infos = getPackageManager()
                    .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo info : infos) {
                apps.add(new AppInfoRich(info));
            }

            for (AppInfoRich appInfo : apps) {
                Drawable icon = appInfo.icon;
                String name = appInfo.name;
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                AppInfo info = new AppInfo(icon, name);
                subscriber.onNext(info);
                outList.add(info);
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });
    }

    class RxAdapter
            extends RecyclerView.Adapter<RxHolder> {

        private List<AppInfo> appInfos = new ArrayList<>();

        @Override
        public RxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RxAndroidActivity.this)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RxHolder(view);
        }

        @Override
        public void onBindViewHolder(RxHolder holder, int position) {
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

        public RxHolder(View itemView) {
            super(itemView);
        }
    }

    class AppInfo
            implements Comparable<Object> {
        Drawable icon;
        String name;

        public AppInfo(Drawable icon, String name) {
            this.icon = icon;
            this.name = name;
        }

        @Override
        public int compareTo(@NonNull Object another) {
            AppInfo f = (AppInfo) another;
            return name.compareTo(f.name);
        }
    }

    class AppInfoRich {
        ResolveInfo info;
        Drawable icon;
        String name;

        public AppInfoRich(ResolveInfo info) {
            this.info = info;
            this.icon = info.loadIcon(getPackageManager());
            this.name = info.loadLabel(getPackageManager()).toString();
        }
    }
}
