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
        devices.addAll(mServer.getDevices("key"));
        Log.e(TAG, "test 3.5");

    }
}


