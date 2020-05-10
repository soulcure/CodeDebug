package com.example.core;

import android.os.RemoteException;
import android.util.Log;

import com.example.sdk.IFamilyManager;

public class FamilyImpl extends IFamilyManager.Stub {//Stub内部类，其实就是一个Binder类
    private static final String TAG = "AIDL";
    private SkyServer mServer;

    FamilyImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void getFamilyList() throws RemoteException {
        Log.e(TAG, "getFamilyList");
        mServer.postFamilies();
    }
}

