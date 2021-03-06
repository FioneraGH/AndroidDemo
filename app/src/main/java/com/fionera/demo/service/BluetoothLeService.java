package com.fionera.demo.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.fionera.base.util.LogCat;
import com.fionera.demo.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author fionera
 */
public class BluetoothLeService
        extends Service {
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le" +
            ".ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le" +
            ".ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le" +
            ".ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le" +
            ".ACTION_DATA_AVAILABLE";

    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_NOTIFY = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private static BluetoothAdapter mBluetoothAdapter;

    private static Map<BluetoothDevice, BluetoothGatt> mBluetoothGatt = new HashMap<>();
    private static Map<BluetoothGatt, BluetoothGattCharacteristic> mNotifyCharacteristic = new
            HashMap<>();
    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;

    private List<Byte> list = new ArrayList<>();
    private List<Byte> storeList = new ArrayList<>();

    private static WeakReference<BluetoothLeService> weakReferenceContext;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            LogCat.d("Bluetooth State:status=" + status + " NewStatus=" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED, gatt.getDevice());
                LogCat.d("Bluetooth State:Connected to GATT server.");
                LogCat.d("Bluetooth State:Attempting to start service discovery:" + gatt
                        .discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                gatt.close();
                mBluetoothGatt.remove(gatt.getDevice());
                broadcastUpdate(ACTION_GATT_DISCONNECTED, gatt.getDevice());
                LogCat.d("Bluetooth State:Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogCat.d("Bluetooth State:onServicesDiscovered received");
                findService(gatt);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogCat.d("Bluetooth State:OnCharacteristicRead");
                broadcastUpdate(ACTION_DATA_AVAILABLE, gatt.getDevice(), characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            LogCat.d("Bluetooth State:OnCharacteristicChanged [from] " + gatt.getDevice().getAddress());
            broadcastUpdate(ACTION_DATA_AVAILABLE, gatt.getDevice(), characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            LogCat.d("Bluetooth State:OnCharacteristicWrite");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor bd, int status) {
            LogCat.d("Bluetooth State:onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor bd, int status) {
            LogCat.d("Bluetooth State:onDescriptorWrite");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int a, int b) {
            LogCat.d("Bluetooth State:onReadRemoteRssi");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int a) {
            LogCat.d("Bluetooth State:onReliableWriteCompleted");
        }

    };

    public static void writeValue(String address, final byte[] cmd) {
        if (mBluetoothAdapter == null || mBluetoothGatt.size() == 0) {
            LogCat.d("蓝牙异常断开，请尝试重新连接");
            return;
        }
        for (BluetoothDevice bluetoothDevice : mBluetoothGatt.keySet()) {
            if (bluetoothDevice.getAddress().equals(address)) {
                BluetoothGatt bluetoothGatt = mBluetoothGatt.get(bluetoothDevice);
                BluetoothGattCharacteristic bluetoothGattCharacteristic = mNotifyCharacteristic.get(
                        bluetoothGatt);
                if (bluetoothGattCharacteristic != null && bluetoothGatt != null) {
                    bluetoothGattCharacteristic.setValue(cmd);
                    bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
                }
                break;
            }
        }
    }

    public void findService(BluetoothGatt bluetoothGatt) {
        List<BluetoothGattService> gattServices = bluetoothGatt.getServices();
        LogCat.d("Bluetooth State:Service Count is:" + gattServices.size());
        for (BluetoothGattService gattService : gattServices) {
            LogCat.d("Bluetooth State:suuid:" + gattService.getUuid().toString());
            LogCat.d("Bluetooth State:suuid_service:" + UUID_SERVICE.toString());
            if (gattService.getUuid().toString().equalsIgnoreCase(UUID_SERVICE.toString())) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                        .getCharacteristics();
                LogCat.d("Bluetooth State:Characteristic Count is:" + gattCharacteristics.size());
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    LogCat.d("Bluetooth State:cuuid:" + gattCharacteristic.getUuid().toString());
                    LogCat.d("Bluetooth State:cuuid_notify:" + UUID_NOTIFY.toString());
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(
                            UUID_NOTIFY.toString())) {
                        mNotifyCharacteristic.put(bluetoothGatt, gattCharacteristic);
                        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
                        LocalBroadcastManager.getInstance(weakReferenceContext.get()).sendBroadcast(
                                new Intent(ACTION_GATT_SERVICES_DISCOVERED));
                        return;
                    }
                }
            }
        }
    }

    private void broadcastUpdate(String action, BluetoothDevice bluetoothDevice) {
        Intent intent = new Intent(action);
        intent.putExtra("BLUETOOTH_DEVICE", bluetoothDevice);
        LocalBroadcastManager.getInstance(weakReferenceContext.get()).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, BluetoothDevice bluetoothDevice,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        byte[] data = characteristic.getValue();

        if (data != null && data.length > 0) {
            for (byte aData : data) {
                storeList.add(aData);
            }
            LogCat.e("Bluetooth Receive Data:" + bytesToHexString(data));
            if (storeList.get(0) == 0x02 && storeList.get(storeList.size() - 1) == 0x03) {
                if (isLast03(storeList)) {
                    int i = 0;
                    while (i < storeList.size()) {
                        if (storeList.get(i) == 0x10) {
                            list.add(storeList.get(i+1));
                            i += 2;
                        } else {
                            list.add(storeList.get(i));
                            i++;
                        }
                    }
                    byte[] bData = new byte[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        bData[j] = list.get(j);
                    }
                    LogCat.e("Bluetooth Receive Final Data:" + bytesToHexString(bData));
                    intent.putExtra("BLUETOOTH_DEVICE", bluetoothDevice);
                    intent.putExtra(EXTRA_DATA, bData);
                    list.clear();

                    LocalBroadcastManager.getInstance(weakReferenceContext.get()).sendBroadcast(
                            intent);
                }
            }
        }
    }

    /**
     * 判断字节03是否为包结尾
     * @param list list
     * @return boolean
     */
    private boolean isLast03(List<Byte> list) {
        int i = list.size() - 2;
        int count = 0;
        while (list.get(i) == 0x10) {
            count++;
            i--;
        }
        // 成对10存在，表示校验和或者包括校验和在内之前数据为10，03位结尾
        // 奇数10存在，表示03为数据非结尾
        return count % 2 == 0;
    }

    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                LogCat.d("Bluetooth State:Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            LogCat.d("Bluetooth State:Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    public boolean connect(String address) {
        if (mBluetoothAdapter == null || address == null) {
            LogCat.d("Bluetooth State:BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        /*
          get device according to mac address
         */
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            LogCat.d("Bluetooth State:Device not found. Unable to connect.");
            return false;
        }

        mBluetoothGatt.put(device, device.connectGatt(this, false, mGattCallback));

        LogCat.d("Bluetooth State:Trying to create a new connection.");
        return true;
    }

    public static void disconnect(BluetoothDevice device) {
        if (mBluetoothAdapter == null || mBluetoothGatt.size() == 0) {
            LogCat.d("Bluetooth State:BluetoothAdapter not initialized");
            return;
        }
        BluetoothGatt bluetoothGatt = mBluetoothGatt.get(device);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    /**
     * 使用给定的BLE设备后,应用程序必须调用这个方法,以确保正确地释放资源。
     */
    @TargetApi(Build.VERSION_CODES.N)
    public void close() {
        if (mBluetoothAdapter == null || mBluetoothGatt.size() == 0) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBluetoothGatt.forEach((bluetoothDevice, bluetoothGatt) -> bluetoothGatt.close());
        } else {
            for (BluetoothDevice bluetoothDevice : mBluetoothGatt.keySet()) {
                BluetoothGatt bluetoothGatt = mBluetoothGatt.get(bluetoothDevice);
                if (bluetoothGatt != null) {
                    bluetoothGatt.close();
                }
            }
        }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (initialize()) {
            LogCat.e("BluetoothLeService Started");
            if (intent == null || intent.getStringExtra("DEVICE_ADDRESS") == null) {
                LogCat.e("Bluetooth Address:null");
                return super.onStartCommand(intent, flags, startId);
            }
            String deviceAddress = intent.getStringExtra("DEVICE_ADDRESS");
            LogCat.e("Bluetooth Address:" + deviceAddress + " [Result]:" + connect(deviceAddress));
            connect(deviceAddress);
            /*
              开启前台服务，保证蓝牙服务一直处于运行状态
             */
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "DEFAULT_CHANNEL");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel;
                notificationChannel = new NotificationChannel("DEFAULT_CHANNEL",
                        "DEFAULT", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.createNotificationChannel(notificationChannel);
                }
            }
            Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher).setTicker(
                    "蓝牙通信服务已启动").setContentTitle("\"信印\"蓝牙通信服务").setContentText("\"信印\"蓝牙通信服务")
                    .build();
            startForeground(1, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        weakReferenceContext = new WeakReference<>(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    private class LocalBinder
            extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }
}
