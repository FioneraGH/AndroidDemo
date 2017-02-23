package com.fionera.multipic.common;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.fionera.base.util.ShowToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocalImageHelper {
    private int TOTAL_SIZE = 100;
    private static LocalImageHelper instance;
    private final Context context;
    private final List<LocalFile> checkedItems = new ArrayList<>();

    private int currentSize;
    private int totalSize = TOTAL_SIZE;

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getCameraImgPath() {
        return cameraImgPath;
    }

    public String setCameraImgPath() {
        String folder= getCachePath(context)
                + "/PostPicture/";
        File saveDir = new File(folder);
        if (!saveDir.exists()) {
            LogUtils.d("CameraImgPath is created:" + saveDir.mkdirs());
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA).format(
                new Date());
        String picName = timeStamp + ".jpg";
        cameraImgPath = folder + picName;
        return cameraImgPath;
    }

    //拍照时指定保存图片的路径
    private String cameraImgPath;
    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    private final List<LocalFile> paths = new ArrayList<>();

    private final Map<String, List<LocalFile>> folders = new HashMap<>();

    private LocalImageHelper(Context context) {
        this.context = context;
    }

    public Map<String, List<LocalFile>> getFolderMap() {
        return folders;
    }

    public static LocalImageHelper getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new LocalImageHelper(context);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                instance.initImage();
            }
        }).start();
    }

    public void release(){
        currentSize = 0;
        checkedItems.clear();
        totalSize = TOTAL_SIZE;
    }

    public boolean isInitialized() {
        return paths.size() > 0;
    }

    public List<LocalFile> getCheckedItems() {
        return checkedItems;
    }

    private boolean resultOk;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean ok) {
        resultOk = ok;
    }

    private boolean isRunning = false;

    public synchronized void initImage() {
        final long startTime = System.currentTimeMillis();
        if (isRunning) {
            return;
        }
        isRunning = true;
        if (isInitialized()) {
            return;
        }
        //获取大图的游标
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                STORE_IMAGES,   // 字段
                null,         // No where clause
                null,         // No where clause
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);//大图ID
            String path = cursor.getString(1);//大图路径
            File file = new File(path);
            //判断大图是否存在
            if (file.exists()) {
                //小图URI
                String thumbUri = getThumbnail(id);
                //获取大图URI
                String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(id)).build().toString();
                if (TextUtils.isEmpty(uri)) {
                    continue;
                }
                if (TextUtils.isEmpty(thumbUri)) {
                    thumbUri = uri;
                }
                //获取目录名
                String folder = file.getParentFile().getName();
                LocalFile localFile = new LocalFile();
                localFile.setPath(path);
                localFile.setOriginalUri(uri);
                localFile.setThumbnailUri(thumbUri);
                int degree = cursor.getInt(2);
                if (degree != 0) {
                    degree = degree + 180;
                }
                localFile.setOrientation(360-degree);

                paths.add(localFile);
                //判断文件夹是否已经存在
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
        isRunning = false;
        /*
        here to compute content fetch time
         */
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ShowToast.show(String.format(Locale.CHINA, "image helper init speed %d ms",
                        System.currentTimeMillis() - startTime));
            }
        });
    }

    private String getThumbnail(int id) {
        //获取大图的缩略图
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int thumbId = cursor.getInt(0);
                String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(thumbId)).build().toString();
                cursor.close();
                return uri;
            }
            cursor.close();
        }
        return null;
    }

    public List<LocalFile> getFolder(String folder) {
        return folders.get(folder);
    }

    public void clear() {
        checkedItems.clear();
        currentSize = (0);
        String folder = getCachePath(context) + "/PostPicture/";
        File saveDir = new File(folder);
        if (saveDir.exists()) {
            deleteFile(saveDir);
        }
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File theFile : files) {
                    deleteFile(theFile);
                }
            }
        }
    }

    private static String getCachePath(Context context) {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        return cacheDir.getAbsolutePath();
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
