package com.fionera.multipic.ui;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fionera.base.activity.TitleBarActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.multipic.R;
import com.fionera.multipic.common.ImageConst;
import com.fionera.multipic.common.ImageUtil;
import com.fionera.multipic.common.LocalImageHelper;

import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LocalAlbum
        extends TitleBarActivity {
    private ListView listView;
    private List<String> folderNames;

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

        listView = (ListView) findViewById(R.id.local_album_list);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LocalImageHelper.getInstance().initImage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDestroy) {
                            listView.setAdapter(new FolderAdapter(mContext, LocalImageHelper.getInstance().getFolderMap()));
                        }
                    }
                });
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, LocalAlbumDetail.class);
                intent.putExtra("local_folder_name", folderNames.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });
    }

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

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        File file = new File(cameraPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent,
                ImageConst.REQUEST_CODE_GET_IMAGE_BY_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageConst.REQUEST_CODE_GET_IMAGE_BY_CAMERA:
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {
                        ShowToast.show("图片获取失败");
                    }
                    break;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private class FolderAdapter extends BaseAdapter {
        Map<String, List<LocalImageHelper.LocalFile>> folders;
        Context context;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders) {
            this.folders = folders;
            this.context = context;
            folderNames = new ArrayList<>();

            for (Object set : folders.entrySet()) {
                Map.Entry entry = (Map.Entry) set;
                String key = (String) entry.getKey();
                folderNames.add(key);
            }

            Collections.sort(folderNames, new Comparator<String>() {
                public int compare(String arg0, String arg1) {
                    Integer num1 = LocalImageHelper.getInstance().getFolder(arg0).size();
                    Integer num2 = LocalImageHelper.getInstance().getFolder(arg1).size();
                    return num2.compareTo(num1);
                }
            });
        }

        @Override
        public int getCount() {
            return folders.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.ll_local_album_foler_item, viewGroup,false);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_local_album_folder_preview);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.iv_local_album_folder_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = folderNames.get(i);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            viewHolder.textView.setText(name + "(" + files.size() + ")");
            if (files.size() > 0) {
                ImageUtil.loadImage(files.get(0).getThumbnailUri(), viewHolder.imageView);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
