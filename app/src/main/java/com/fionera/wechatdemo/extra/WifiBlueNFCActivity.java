package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fionera.wechatdemo.R;
import com.fionera.wechatdemo.util.WifiAdmin;

import java.util.List;

public class WifiBlueNFCActivity extends Activity {

    private TextView networkResult;
    private Button scan;
    private Button start;
    private Button stop;
    private Button check;
    private WifiAdmin wifiAdmin;

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
        scan = (Button) findViewById(R.id.btn_wifi_scan);
        start = (Button) findViewById(R.id.btn_wifi_start);
        stop = (Button) findViewById(R.id.btn_wifi_stop);
        check = (Button) findViewById(R.id.btn_wifi_check);
        scan.setOnClickListener(new MyListener());
        start.setOnClickListener(new MyListener());
        stop.setOnClickListener(new MyListener());
        check.setOnClickListener(new MyListener());
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
                    Toast.makeText(WifiBlueNFCActivity.this, "State:" + wifiAdmin.checkState() + "\nInfo:" + wifiAdmin.getWifiInfo(), Toast.LENGTH_SHORT).show();
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