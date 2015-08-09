package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.WifiAdmin;

import java.util.List;

public class WifiBlueNFCActivity extends Activity {

    private TextView networkResult;
    private WifiAdmin wifiAdmin;
    private Button btnWifiScan;
    private Button btnWifiStart;
    private Button btnWifiStop;
    private Button btnWifiCheck;

    private Button btnNFCChooseApp;
    private String packageName;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;


    // 扫描结果列表
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb = new StringBuffer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_blue_nfc);

        wifiAdmin = new WifiAdmin(WifiBlueNFCActivity.this);
        networkResult = (TextView) findViewById(R.id.tv_wifi_state);
        btnWifiScan = (Button) findViewById(R.id.btn_wifi_scan);
        btnWifiStart = (Button) findViewById(R.id.btn_wifi_start);
        btnWifiStop = (Button) findViewById(R.id.btn_wifi_stop);
        btnWifiCheck = (Button) findViewById(R.id.btn_wifi_check);
        btnNFCChooseApp = (Button) findViewById(R.id.btn_choose_app);
        btnWifiScan.setOnClickListener(new MyListener());
        btnWifiStart.setOnClickListener(new MyListener());
        btnWifiStop.setOnClickListener(new MyListener());
        btnWifiCheck.setOnClickListener(new MyListener());
        btnNFCChooseApp.setOnClickListener(new MyListener());

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Log.d("NFC", "获取NfcAdapter：" + nfcAdapter.toString());
        //PendingIntent
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("NFC", "接收到新的Intent");
        if (packageName != null) {
            //获取tag
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeAppNfcTag(tag);
        }
    }

    

    /**
     * 写操作，tag为之前拿到的标签抽象实例
     *
     * @param tag
     */
    public void writeAppNfcTag(Tag tag) {
        if (tag != null) {
            /**
             * 封装一个用于打开应用程序的NdefMessage并写入
             */
            NdefMessage ndefMessage = new NdefMessage(
                    new NdefRecord[]{NdefRecord.createApplicationRecord(packageName)});
            int size = ndefMessage.getByteArrayLength();
            try {
                Ndef ndef = Ndef.get(tag);
                if (ndef != null) {
                    //类似socket的通讯前建立连接
                    ndef.connect();
                    Log.d("NFC", "连接成功");
                    if (ndef.isWritable()) {
                        if (ndef.getMaxSize() >= size) {
                            ndef.writeNdefMessage(ndefMessage);
                            Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "NFC标签不可写", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Tag非NDEF格式", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                packageName = data.getStringExtra("package_name");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        } else {
            Toast.makeText(this, "Not support NFC", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_wifi_scan://扫描网络
                    getAllNetWorkList();
                    break;
                case R.id.btn_wifi_start://打开Wifi
                    wifiAdmin.openWifi();
                    break;
                case R.id.btn_wifi_stop://关闭Wifi
                    wifiAdmin.closeWifi();
                    break;
                case R.id.btn_wifi_check://Wifi状态
                    Toast.makeText(WifiBlueNFCActivity.this, "State:" + wifiAdmin.checkState() +
                            "\nInfo:" + wifiAdmin.getWifiInfo(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_choose_app:
                    startActivityForResult(
                            new Intent(WifiBlueNFCActivity.this, PackageInstalledActivity.class), 1,
                            null);
                    break;
            }
        }

    }


    public void getAllNetWorkList() {
        // 每次点击扫描之前清空上一次的扫描结果
        if (sb != null) {
            sb = new StringBuffer();
        }
        //开始扫描网络
        wifiAdmin.startScan();
        list = wifiAdmin.getWifiList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mScanResult = list.get(i);
                sb = sb.append(mScanResult.SSID).append("\n\n");
            }
            networkResult.setText(sb.toString());
        }
    }
}