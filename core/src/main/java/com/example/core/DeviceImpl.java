package com.example.core;

import android.os.RemoteException;
import android.util.Log;

import com.example.core.http.HttpConnector;
import com.example.sdk.IDeviceManager;
import com.example.sdk.entity.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceImpl extends IDeviceManager.Stub {//Stub内部类，其实就是一个Binder类

    private static final String TAG = "AIDL";

    private SkyServer mServer;

    DeviceImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void getDevices(final List<Device> devices, String familyId) throws RemoteException {
        Log.e(TAG, "test 3333");

        String test = HttpConnector.doGet(null, "https://www.baidu.com/", null);

        Log.e(TAG, "test 4444");

        List<Device> list = new ArrayList<>();

        Device device1 = new Device();
        device1.setDeviceId("id-1");
        device1.setDeviceName("设备test01");

        Device device2 = new Device();
        device2.setDeviceId("id-2");
        device2.setDeviceName("设备2");

        list.add(device1);
        list.add(device2);

        devices.addAll(list);


        Log.e(TAG, "test 3.5");

    }
}


