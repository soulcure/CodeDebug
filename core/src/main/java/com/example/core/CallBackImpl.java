package com.example.core;

import android.os.RemoteException;

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
    public void registerCallback(ICallback cb) throws RemoteException {
        if (cb != null) {
            mServer.getCallbacks().register(cb);
        }
    }

    @Override
    public void unregisterCallback(ICallback cb) throws RemoteException {
        if (cb != null) {
            mServer.getCallbacks().unregister(cb);
        }
    }



}

