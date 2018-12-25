package com.fionera.multipic.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fionera.base.activity.TitleBarActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageConst;
import com.fionera.multipic.common.ImageUtil;
import com.fionera.multipic.common.LocalImageHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LocalAlbum
        extends TitleBarActivity {
    private final static int ASK_FOR_CAMERA = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album);

        setTitleBarText("选择相册");
        setTitleBarTitleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToCapturePic();
            }
        });

        if (getIntent().getBooleanExtra("onlyCamera", false)){
            tryToCapturePic();
            return;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.local_album_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        FolderAdapter folderAdapter = new FolderAdapter(mContext, LocalImageHelper.getInstance().getFolderMap());
        recyclerView.setAdapter(folderAdapter);
    }

    @SuppressLint("ObsoleteSdkInt")
    private void tryToCapturePic() {
        /*
          统计审核照片数量，判断是否继续添加照片
         */
        if (LocalImageHelper.getInstance().getCurrentSize() + LocalImageHelper.getInstance()
                .getCheckedItems().size() >= LocalImageHelper.getInstance()
                .getTotalSize()) {
            ShowToast.show(
                    "最多选择" + LocalImageHelper.getInstance().getTotalSize() + "张图片");
            return;
        }

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    ASK_FOR_CAMERA);
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String cameraPath = LocalImageHelper.getInstance().setupCameraImgPath();
        File file = new File(cameraPath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, "com.fionera.demo.FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, ImageConst.REQUEST_CODE_GET_IMAGE_BY_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageConst.REQUEST_CODE_GET_IMAGE_BY_CAMERA) {
            String cameraPath = LocalImageHelper.getInstance().getCameraImgPath();
            if (TextUtils.isEmpty(cameraPath)) {
                ShowToast.show("图片获取失败");
                return;
            }
            File file = new File(cameraPath);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                LocalImageHelper.LocalFile localFile = new LocalImageHelper.LocalFile();
                localFile.setPath(cameraPath);
                localFile.setThumbnailUri(uri.toString());
                localFile.setOriginalUri(uri.toString());
                localFile.setOrientation(getBitmapDegree(cameraPath));
                LocalImageHelper.getInstance().getCheckedItems().add(localFile);
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
            } else {
                ShowToast.show("图片获取失败");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ASK_FOR_CAMERA == requestCode && Manifest.permission.CAMERA.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tryToCapturePic();
            } else {
                ShowToast.show("该功能需要相机权限支持");
            }
        }
    }

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private class FolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        Map<String, List<LocalImageHelper.LocalFile>> folders;
        List<String> folderNames;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders) {
            this.context = context;
            this.folders = folders;
            folderNames = new ArrayList<>();

            for (Map.Entry<String, List<LocalImageHelper.LocalFile>> set : folders.entrySet()) {
                String key = set.getKey();
                folderNames.add(key);
            }

            Collections.sort(folderNames, new Comparator<String>() {
                @Override
                public int compare(String arg0, String arg1) {
                    Integer num1 = LocalImageHelper.getInstance().getFolder(arg0).size();
                    Integer num2 = LocalImageHelper.getInstance().getFolder(arg1).size();
                    return num2.compareTo(num1);
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.rv_local_album_foler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            String name = folderNames.get(position);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            holder.textView.setText(String.format(Locale.CHINA, "%s(%d)", name, files.size()));
            if (files.size() > 0) {
                holder.imageView.setVisibility(View.VISIBLE);
                ImageUtil.loadImage(files.get(0).getThumbnailUri(), holder.imageView);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LocalAlbumDetail.class);
                    intent.putExtra("local_folder_name",
                            folderNames.get(holder.getAdapterPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return folders.size();
        }
    }

    private class ViewHolder
            extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_local_album_folder_preview);
            textView = itemView.findViewById(R.id.iv_local_album_folder_name);
        }
    }
}
