package com.fionera.demo.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fionera.base.util.LogCat;
import com.fionera.demo.DemoApplication;
import com.fionera.demo.service.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙工具类
 * 检测连接蓝牙的信息
 * @author fionera
 */
public class BlueToothScanUtil {
    private Context context;
    private static BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private boolean isScanning;
    private String macAddress;

    private Handler handler = new Handler(Looper.getMainLooper());

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogCat.d("Search Bluetooth Device:" + result.getDevice().getAddress());
            addDeviceToList(result.getDevice());
        }
    };

    private Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (isScanning) {
                stopSearch(true);
                isScanning = false;
            }
        }
    };

    public BlueToothScanUtil(Context context) {
        this.context = context;
        BluetoothManager manager = (BluetoothManager) DemoApplication.getInstance().getSystemService(
                Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public static boolean checkBLEAvailable() {
        return DemoApplication.getInstance().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE) && bluetoothAdapter != null;
    }

    public static boolean isBlueToothOpened() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private void addDeviceToList(BluetoothDevice device) {
        if (!devices.contains(device)) {
            if ("HMSoft".equals(device.getName())) {
                devices.add(device);
            }
            checkDevicesList(devices);
        }
    }

    private void checkDevicesList(final List<BluetoothDevice> devices) {
        if (devices.size() > 0) {
            BluetoothDevice connectedDevice = devices.get(devices.size() - 1);
            if (TextUtils.equals(connectedDevice.getAddress(), macAddress)) {
                if (isScanning) {
                    stopSearch(false);
                    isScanning = false;
                }
                connectDevice(connectedDevice);
                handler.removeCallbacks(dismissRunnable);
            }
        }
    }

    private void connectDevice(BluetoothDevice connectedDevice) {
        Intent intent = new Intent(context, BluetoothLeService.class);
        intent.putExtra("DEVICE_ADDRESS", connectedDevice.getAddress());
        context.startService(intent);
    }

    public void startSearchBlueDevice() {
        LogCat.d("Search Bluetooth Device Start:" + macAddress);
        if(bluetoothAdapter == null || isScanning){
            return;
        }
        isScanning = true;
        devices.clear();
        bluetoothAdapter.getBluetoothLeScanner().startScan(leScanCallback);
        handler.postDelayed(dismissRunnable, 8000);
    }

    public void stopSearch(boolean timeout) {
        LogCat.d("Search Bluetooth Device End:" + macAddress);
        if(bluetoothAdapter == null || !isScanning){
            return;
        }
        bluetoothAdapter.getBluetoothLeScanner().stopScan(leScanCallback);
        isScanning = false;
        if (stopSearchListener != null) {
            stopSearchListener.onStopped(timeout);
        }
        /*
          make sure remove runnable
         */
        handler.removeCallbacksAndMessages(null);
    }

    private StopSearchListener stopSearchListener;

    public void setStopSearchListener(StopSearchListener stopSearchListener) {
        this.stopSearchListener = stopSearchListener;
    }

    public interface StopSearchListener{
        void onStopped(boolean timeout);
    }
}
