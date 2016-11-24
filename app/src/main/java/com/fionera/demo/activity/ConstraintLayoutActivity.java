package com.fionera.demo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fionera.demo.R;
import com.fionera.demo.service.BluetoothLeService;
import com.fionera.demo.util.DesUtil;
import com.fionera.demo.util.ShowToast;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConstraintLayoutActivity
        extends AppCompatActivity {

    private static final byte[] KEY = {(byte) 0xB4, 0x31, 0x5B, (byte) 0x86, (byte) 0x9D, 0x7D,
            (byte) 0xFA, (byte) 0xA2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_constrant_title);
        toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        TextView textView = (TextView) findViewById(R.id.tv_constraint_tips);
        textView.setText(stringFromJNI() + " " + addNumberUsingJNI(1, 10));


        findViewById(R.id.btn_contrant_connect_1).setOnClickListener(view -> {
            Intent intent = new Intent(ConstraintLayoutActivity.this, BluetoothLeService.class);
            intent.putExtra("DEVICE_ADDRESS", "88:4A:EA:30:34:FD");
            ConstraintLayoutActivity.this.startService(intent);
        });
        findViewById(R.id.btn_contrant_connect_2).setOnClickListener(view -> {
            Intent intent = new Intent(ConstraintLayoutActivity.this, BluetoothLeService.class);
            intent.putExtra("DEVICE_ADDRESS", "20:91:48:A2:D9:BD");
            ConstraintLayoutActivity.this.startService(intent);
//            BlueToothScanUtil blueToothScanUtil = new BlueToothScanUtil(this);
//            blueToothScanUtil.setMacAddress("20:91:48:A2:D9:BD");
//            blueToothScanUtil.startSearchBlueDevice();
        });
        findViewById(R.id.btn_contrant_send_1).setOnClickListener(view -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            byte[] byteDate = hexStr2Bytes(date);
            byte[] cmd = new byte[8];
            cmd[0] = 0x0e;
            System.arraycopy(byteDate, 0, cmd, 1, 7);
            writeValue("88:4A:EA:30:34:FD",cmd ,new ArrayList<>());
        });
        findViewById(R.id.btn_contrant_send_2).setOnClickListener(view -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            byte[] byteDate = hexStr2Bytes(date);
            byte[] cmd = new byte[8];
            cmd[0] = 0x0e;
            System.arraycopy(byteDate, 0, cmd, 1, 7);
            writeValue("20:91:48:A2:D9:BD",cmd ,new ArrayList<>());
        });
        findViewById(R.id.btn_contrant_stamp_1).setOnClickListener(view -> {
            byte[] cmd = {0x07};
            List<Byte> packet = new ArrayList<>();
            packet.add((byte) 0x01);
            packet.add((byte) 0x00);
            writeValue("88:4A:EA:30:34:FD", cmd, packet);
        });
        findViewById(R.id.btn_contrant_stamp_2).setOnClickListener(view -> {
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
            LogUtil.e("Need Send Data：" + bytesToHexString(list.get(i)));
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
        packet.add(0, packetHead); //添加包头
        packet.add(1, length[1]); //添加长度字低位
        packet.add(2, length[0]); //添加长度字高位
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
            LogUtil.e("Send Data Before Encrypt:" + bytesToHexString(c));
            LogUtil.e("Send Data After Encrypt:" + bytesToHexString(checkDes));
            byte[] result = DesUtil.decrypt(checkDes, KEY);
            LogUtil.e("Send Data After Decrypt:" + bytesToHexString(result));
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
        System.loadLibrary("native-lib");
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
    }
}
