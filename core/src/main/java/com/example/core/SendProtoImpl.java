package com.example.core;

import android.os.RemoteException;

import com.example.sdk.ISendProto;
import com.example.sdk.entity.PduBase;

public class SendProtoImpl extends ISendProto.Stub {//Stub内部类，其实就是一个Binder类
    private static final String TAG = "AIDL";


    private SkyServer mServer;

    SendProtoImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public void sendProto(String appId, PduBase pdu) throws RemoteException {
        mServer.sendProto(appId, pdu);
    }
}

