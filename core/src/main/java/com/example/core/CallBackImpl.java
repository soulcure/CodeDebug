package com.example.core;

import android.os.RemoteException;
import android.text.TextUtils;

import com.example.sdk.ICallBackManager;
import com.example.sdk.ICallback;

public class CallBackImpl extends ICallBackManager.Stub {//Stub内部类，其实就是一个Binder类
    private static final String TAG = "AIDL";


    private SkyServer mServer;

    CallBackImpl(SkyServer server) {
        super();
        mServer = server;
    }


    @Override
    public void registerCallback(String key, ICallback cb) throws RemoteException {
        if (!TextUtils.isEmpty(key) && cb != null) {
            mServer.registerCallback(key, cb);
        }
    }

    @Override
    public void unregisterCallback(String key, ICallback cb) throws RemoteException {
        if (!TextUtils.isEmpty(key) && cb != null) {
            mServer.registerCallback(key, cb);
        }
    }


}

