package com.example.sdk;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import static com.example.sdk.BinderPool.BIND_DEVICE;
import static com.example.sdk.BinderPool.BIND_FAMILY;


public class SdkManager {

    private static final String TAG = "AIDL";

    private static volatile SdkManager instance;
    private BinderPool binderPool;

    /**
     * SDK初始化结果监听器
     */
    public interface InitListener {
        void success();

        void fail();
    }


    public static SdkManager instance(Context context) {
        if (null == instance) {
            synchronized (BinderPool.class) {
                if (null == instance) {
                    instance = new SdkManager(context);
                }
            }
        }
        return instance;
    }

    private SdkManager(Context context) {
        binderPool = new BinderPool(context);

    }


    /**
     * sdk初始化
     */
    public void init() {
        this.init(null);
    }


    /**
     * sdk初始化
     */
    public void init(InitListener listener) {
        binderPool.bindPoolService(listener);
    }


    public void getDevices() {
        IBinder binder = binderPool.queryBinder(BIND_DEVICE);//获取Binder后使用

        IDeviceManager iDeviceManager = IDeviceManager.Stub.asInterface(binder);

        try {
            iDeviceManager.getDevices("1");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void getFamilies() {
        IBinder binder = binderPool.queryBinder(BIND_FAMILY);//获取Binder后使用

        //将服务端的Binder对象转换成客户端所需的AIDL类型的的对象
        IFamilyManager iFamilyManager = IFamilyManager.Stub.asInterface(binder);//ISecurity.Stub.asInterface
        try {
            iFamilyManager.getFamilyList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * sdk销毁
     */
    public void destroy() {
        binderPool.destroy();
    }

}
