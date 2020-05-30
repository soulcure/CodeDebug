package com.example.core;

import android.os.RemoteException;
import android.text.TextUtils;

import com.example.sdk.ICallBackManager;
import com.example.sdk.ICallback;
import com.example.sdk.IKeyCmd;

public class KeyCmdImpl extends IKeyCmd.Stub {//Stub内部类，其实就是一个Binder类
    private static final String TAG = "AIDL";


    private SkyServer mServer;

    KeyCmdImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void sendKeyEvent(String key, String dstSid, int keyCode, String keyEvent) throws RemoteException {
        mServer.sendProto(key, dstSid, keyCode, keyEvent);
    }

}

