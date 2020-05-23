package com.example.core;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.sdk.IBinderPool;

import static com.example.sdk.BinderPool.BIND_CALLBACK;
import static com.example.sdk.BinderPool.BIND_DEVICE;
import static com.example.sdk.BinderPool.BIND_FAMILY;

public class BinderPoolImpl extends IBinderPool.Stub {

    private   SkyServer mServer;

    BinderPoolImpl(SkyServer server) {
        super();
        mServer = server;
    }

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        Binder binder = null;
        switch (binderCode) {
            case BIND_DEVICE:
                binder = new DeviceImpl(mServer);//PayImpl继承了IPay.Stub, IPay.Stub继承了Binder
                break;
            case BIND_CALLBACK:
                binder = new CallBackImpl(mServer);
                break;

        }
        return binder;
    }
}