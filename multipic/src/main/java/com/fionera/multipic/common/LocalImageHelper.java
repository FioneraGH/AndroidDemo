package com.fionera.multipic.common;

import android.database.Cursor;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.fionera.base.BaseApplication;
import com.fionera.base.util.ShowToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LocalImageHelper {
    private static int TOTAL_SIZE = 100;

    private static LocalImageHelper instance;
    private static boolean isRunning = false;

    private final List<LocalFile> checkedItems = new ArrayList<>();
    private final List<LocalFile> paths = new ArrayList<>();
    private final Map<String, List<LocalFile>> folders = new HashMap<>();

    private int currentSize;
    private int totalSize = TOTAL_SIZE;
    private String cameraImgPath;

    public List<LocalFile> getCheckedItems() {
        return checkedItems;
    }
    public List<LocalFile> getFolder(String folder) {
        return folders.get(folder);
    }
    public Map<String, List<LocalFile>> getFolderMap() {
        return folders;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public String getCameraImgPath() {
        return cameraImgPath;
    }

    public String setupCameraImgPath() {
        String folder = getFilePath() + "/";
        File saveDir = new File(folder);
        if (!saveDir.exists()) {
            LogUtils.d("CameraImgPath is created:" + saveDir.mkdirs());
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA).format(
                new Date());
        String picName = timeStamp + ".jpg";
        cameraImgPath = folder + picName;
        return cameraImgPath;
    }

    // 大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    // 小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    public static LocalImageHelper getInstance() {
        return instance;
    }

    private boolean isInitialized() {
        return paths.size() > 0;
    }

    public static void init() {
        if (instance == null) {
            instance = new LocalImageHelper();
        }
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                final long startTime = System.currentTimeMillis();
                isRunning = true;
                instance.initImage();
                isRunning = false;
                e.onNext(startTime);
            }
        }).filter(new Predicate<Long>() {
            @Override
            public boolean test(@NonNull Long aLong) throws Exception {
                return !isRunning && !instance.isInitialized();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long startTime) throws Exception {
                        ShowToast.show(String.format(Locale.CHINA, "image helper init speed %d ms",
                                System.currentTimeMillis() - startTime));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        ShowToast.show(throwable.getMessage());
                    }
                });
    }

    private synchronized void initImage() {
        // 获取大图的游标
        Cursor cursor = BaseApplication.getInstance().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 大图URI
                STORE_IMAGES, // 字段
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); // 根据时间升序
        if (cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); // 大图ID
            String path = cursor.getString(1); // 大图路径
            File file = new File(path);
            // 判断大图是否存在
            if (file.exists()) {
                // 小图URI
                String thumbUri = getThumbnail(id);
                // 获取大图URI
                String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(id)).build().toString();
                if (TextUtils.isEmpty(uri)) {
                    continue;
                }
                if (TextUtils.isEmpty(thumbUri)) {
                    thumbUri = uri;
                }
                // 获取目录名
                String folder = file.getParentFile().getName();
                LocalFile localFile = new LocalFile();
                localFile.setPath(path);
                localFile.setOriginalUri(uri);
                localFile.setThumbnailUri(thumbUri);
                int degree = cursor.getInt(2);
                if (degree != 0) {
                    degree = degree + 180;
                }
                localFile.setOrientation(360 - degree);
                paths.add(localFile);

                // 判断文件夹是否已经存在
                if (folders.containsKey(folder)) {
                    folders.get(folder).add(localFile);
                } else {
                    List<LocalFile> files = new ArrayList<>();
                    files.add(localFile);
                    folders.put(folder, files);
                }
            }
        }
        folders.put("所有图片", paths);
        cursor.close();
    }

    private String getThumbnail(int id) {
        // 获取大图的缩略图
        Cursor cursor = BaseApplication.getInstance().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int thumbId = cursor.getInt(0);
                String thumbnailUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(thumbId)).build().toString();
                cursor.close();
                return thumbnailUri;
            }
            cursor.close();
        }
        return null;
    }

    public void release(){
        currentSize = 0;
        checkedItems.clear();
        paths.clear();
        folders.clear();
        totalSize = TOTAL_SIZE;
    }

    private boolean resultOk;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean ok) {
        resultOk = ok;
    }

    private static String getFilePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED) && (cacheDir = BaseApplication.getInstance()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES)) != null) {
            return cacheDir.getAbsolutePath();
        } else {
            cacheDir = BaseApplication.getInstance().getFilesDir();
            return cacheDir.getAbsolutePath() + "/Pictures";
        }
    }

    public static class LocalFile
            implements Parcelable {

        private String originalUri;
        private String thumbnailUri;
        private int orientation;
        private String path;

        public String getOriginalUri() {
            return originalUri;
        }

        public void setOriginalUri(String originalUri) {
            this.originalUri = originalUri;
        }

        public String getThumbnailUri() {
            return thumbnailUri;
        }

        public void setThumbnailUri(String thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
        }

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.originalUri);
            dest.writeString(this.thumbnailUri);
            dest.writeInt(this.orientation);
            dest.writeString(this.path);
        }

        public LocalFile() {
        }

        LocalFile(Parcel in) {
            this.originalUri = in.readString();
            this.thumbnailUri = in.readString();
            this.orientation = in.readInt();
            this.path = in.readString();
        }

        public static final Creator<LocalFile> CREATOR = new Creator<LocalFile>() {
            @Override
            public LocalFile createFromParcel(Parcel source) {
                return new LocalFile(source);
            }

            @Override
            public LocalFile[] newArray(int size) {
                return new LocalFile[size];
            }
        };
    }
}
