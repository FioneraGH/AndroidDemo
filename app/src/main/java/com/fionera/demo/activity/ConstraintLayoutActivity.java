package com.fionera.demo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.LogCat;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.popupwindow.ProvinceJsonPopup;
import com.fionera.demo.service.BluetoothLeService;
import com.fionera.demo.util.BlueToothScanUtil;
import com.fionera.demo.util.DesUtil;
import com.fionera.demo.util.NavigationBarUtil;
import com.fionera.multipic.common.ImageConst;
import com.fionera.multipic.common.ImageUtil;
import com.fionera.multipic.common.LocalImageHelper;
import com.fionera.multipic.ui.LocalAlbum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

/**
 * @author fionera
 */
public class ConstraintLayoutActivity
        extends BaseActivity {

    private static final byte[] KEY = {(byte) 0xB4, 0x31, 0x5B, (byte) 0x86, (byte) 0x9D, 0x7D,
            (byte) 0xFA, (byte) 0xA2};

    private ProvinceJsonPopup provincePopup;

    private ImageView ivConstraintPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

        Toolbar toolbar = findViewById(R.id.tb_constrant_title);
        toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        ivConstraintPr = findViewById(R.id.iv_constraint_pr);

        provincePopup = new ProvinceJsonPopup(mContext);

        final TextView textView = findViewById(R.id.tv_constraint_tips);
        textView.setText(
                String.format(Locale.CHINA, "%s %d", stringFromJNI(), addNumberUsingJNI(1, 10)));
        textView.setOnClickListener(v -> {
            String[] addresses = textView.getText().toString().split(":");
            if (addresses.length == 3) {
                provincePopup.setValue(addresses[0] + ":" + addresses[1] + ":" + addresses[2]);
            }
            provincePopup.setGetValueCallback(
                    (province, city, district, provinceId, cityId, districtId) -> {
                        textView.setText(
                                String.format(Locale.CHINA, "%s:%s:%s", province, city, district));
                        ShowToast.show(provinceId + ":" + cityId + ":" + districtId);
                    });
            provincePopup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0,
                    NavigationBarUtil.getNavigationBarHeight(mContext));
            final WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.3f;
            getWindow().setAttributes(lp);
            provincePopup.setOnDismissListener(() -> {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            });
        });

        findViewById(R.id.btn_contrant_connect_1).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, BluetoothLeService.class);
            intent.putExtra("DEVICE_ADDRESS", "50:F1:4A:5D:64:F9");
            startService(intent);
        });
        findViewById(R.id.btn_contrant_connect_2).setOnClickListener(v -> {
            BlueToothScanUtil blueToothScanUtil = new BlueToothScanUtil(mContext);
            blueToothScanUtil.setMacAddress("50:F1:4A:5D:64:F9");
            blueToothScanUtil.startSearchBlueDevice();
        });
        findViewById(R.id.btn_contrant_send_1).setOnClickListener(v -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            byte[] byteDate = hexStr2Bytes(date);
            byte[] cmd = new byte[8];
            cmd[0] = 0x0e;
            System.arraycopy(byteDate, 0, cmd, 1, 7);
            writeValue("88:4A:EA:30:34:FD",cmd ,new ArrayList<Byte>());
        });
        findViewById(R.id.btn_contrant_send_2).setOnClickListener(v -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            byte[] byteDate = hexStr2Bytes(date);
            byte[] cmd = new byte[8];
            cmd[0] = 0x0e;
            System.arraycopy(byteDate, 0, cmd, 1, 7);
            writeValue("20:91:48:A2:D9:BD",cmd ,new ArrayList<Byte>());
        });
        findViewById(R.id.btn_contrant_stamp_1).setOnClickListener(v -> {
            byte[] cmd = {0x07};
            List<Byte> packet = new ArrayList<>();
            packet.add((byte) 0x01);
            packet.add((byte) 0x00);
            writeValue("88:4A:EA:30:34:FD", cmd, packet);
        });
        findViewById(R.id.btn_contrant_stamp_2).setOnClickListener(v -> {
            byte[] cmd = {0x07};
            List<Byte> packet = new ArrayList<>();
            packet.add((byte) 0x01);
            packet.add((byte) 0x00);
            writeValue("20:91:48:A2:D9:BD", cmd, packet);
        });
    }

    private static byte[] hexStr2Bytes(String src) {
        int m, n;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return ret;
    }

    private void writeValue(String address, byte[] cmd, List<Byte> extraData) {
        List<Byte> packet = new ArrayList<>();
        for (byte aCmd : cmd) {
            packet.add(aCmd);
        }
        List<byte[]> list = separate(makeDataPacket(packet, extraData));

        for (int i = 0; i < list.size(); i++) {
            LogCat.e("Need Send Data：" + bytesToHexString(list.get(i)));
            BluetoothLeService.writeValue(address,list.get(i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<byte[]> separate(List<Byte> list) {
        List<byte[]> packets = new ArrayList<>();
        int front = list.size() / 20;
        int last = list.size() % 20;
        for (int j = 0; j < front; j++) {
            byte[] packet = new byte[20];
            for (int i = 0; i < 20; i++) {
                packet[i] = list.get(0);
                list.remove(0);
            }
            packets.add(packet);
        }
        byte[] end = new byte[last];
        for (int i = 0; i < last; i++) {
            end[i] = list.get(i);
        }
        packets.add(end);
        return packets;
    }

    private List<Byte> makeDataPacket(List<Byte> packet, List<Byte> extraData) {
        byte[] length = intToByteArray(packet.size() + 2 + extraData.size());
        byte packetHead = 0x02;
        // 添加包头
        packet.add(0, packetHead);
        // 添加长度字低位
        packet.add(1, length[1]);
        // 添加长度字高位
        packet.add(2, length[0]);
        for (int i = 0; i < extraData.size(); i++) {
            packet.add(i + 4, extraData.get(i));
        }

        int checkByte = 0;
        for (int i = 1; i < packet.size(); i++) {
            checkByte += packet.get(i);
        }

        byte[] c = {(byte) checkByte, (byte) 0xAD, 0x61, (byte) 0xA5, (byte) 0xF7, 0x19, 0x77,
                0x14};
        byte[] checkDes = null;
        try {
            checkDes = DesUtil.encrypt(c, KEY);
            LogCat.e("Send Data Before Encrypt:" + bytesToHexString(c));
            LogCat.e("Send Data After Encrypt:" + bytesToHexString(checkDes));
            byte[] result = DesUtil.decrypt(checkDes, KEY);
            LogCat.e("Send Data After Decrypt:" + bytesToHexString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDes != null && checkDes.length > 7) {
            packet.add(packet.size(), checkDes[7]);
        }
        byte packetEnd = 0x03;
        packet.add(packet.size(), packetEnd);
        for (int i = 1; i < packet.size() - 1; i++) {
            if (packet.get(i) == 0x02 || packet.get(i) == 0x03 || packet.get(i) == 0x10) {
                packet.add(i, (byte) 0x10);
                ++i;
            }
        }

        return packet;
    }

    private static byte[] intToByteArray(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public native String stringFromJNI();

    public native int addNumberUsingJNI(int a, int b);

    static {
        System.loadLibrary("sec-lib");
    }

    public void turnOnNight(View view) {
        Intent localIntent = new Intent();
        localIntent.setClassName("com.android.systemui",
                "com.android.systemui.tuner.TunerActivity");
        localIntent.putExtra("show_night_mode", true);
        PackageManager packageManager = getPackageManager();
        if (packageManager != null && packageManager.queryIntentActivities(localIntent,
                PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            startActivity(localIntent);
        } else {
            ShowToast.show("cannot open");
        }

        LocalImageHelper.getInstance().getCheckedItems().clear();
        LocalImageHelper.getInstance().setCurrentSize(1);
        LocalImageHelper.getInstance().setCurrentSize(0);
        startActivityForResult(new Intent(mContext, LocalAlbum.class),
                ImageConst.REQUEST_CODE_GET_IMAGE_BY_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode != ImageConst.REQUEST_CODE_GET_IMAGE_BY_CROP) {
            return;
        }
        if (requestCode == ImageConst.REQUEST_CODE_GET_IMAGE_BY_CROP) {
            if (LocalImageHelper.getInstance().isResultOk()) {
                LocalImageHelper.getInstance().setResultOk(false);
                List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance()
                        .getCheckedItems();
                ShowToast.show(files.size());
                if (files.size() > 0) {
                    ImageUtil.loadImage(files.get(0).getOriginalUri(), ivConstraintPr);
                }
            }
        }
    }
}
