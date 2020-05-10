package com.example.core;

import android.os.RemoteException;
import android.util.Log;

import com.example.sdk.ICallback;
import com.example.sdk.IDeviceManager;

public class DeviceImpl extends IDeviceManager.Stub {//Stub内部类，其实就是一个Binder类

    private static final String TAG = "AIDL";
    SkyServer mServer;

    DeviceImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void getDevices(String familyId) throws RemoteException {
        Log.e(TAG, "getDevices");
        mServer.postDevices();
    }

}


