package com.example.core;

import android.os.RemoteException;

import com.example.sdk.IConnectDevice;

public class ConnectDeviceImpl extends IConnectDevice.Stub {//Stub内部类，其实就是一个Binder类
    private static final String TAG = "AIDL";


    private SkyServer mServer;

    ConnectDeviceImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void connectDevice(String targetSid) throws RemoteException {

    }
}

