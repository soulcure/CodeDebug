package com.example.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.example.sdk.entity.Device;

import java.util.ArrayList;
import java.util.List;

import static com.example.sdk.BinderPool.BIND_DEVICE;
import static com.example.sdk.BinderPool.BIND_FAMILY;


public class SdkManager {

    private static final String TAG = "AIDL";


    private static final int HANDLER_AIDL = 1;


    private static volatile SdkManager instance;
    private BinderPool binderPool;

    private Handler mHandler;
    private ProcessHandler mProcessHandler;

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
        initHandler();
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


    public void getDevices(final String familyId, final Devices listener) {
        mProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                binderPool.setListener(listener);

                IBinder binder = binderPool.queryBinder(BIND_DEVICE);//获取Binder后使用

                IDeviceManager iDeviceManager = IDeviceManager.Stub.asInterface(binder);

                try {
                    Log.e(TAG, "test 2222");

                    final List<Device> devices = new ArrayList<>();
                    iDeviceManager.getDevices(devices, familyId);

                    Log.e(TAG, "test 5555");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(devices);
                            Log.e(TAG, "test 7777");
                        }
                    });

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    /**
     * sdk销毁
     */
    public void destroy() {
        binderPool.destroy();
    }


    /**
     * 线程初始化
     */
    private void initHandler() {
        mHandler = new Handler();

        if (mProcessHandler == null) {
            HandlerThread handlerThread = new HandlerThread(
                    "handler looper Thread");
            handlerThread.start();
            mProcessHandler = new ProcessHandler(handlerThread.getLooper());
        }
    }


    /**
     * 子线程handler,looper
     *
     * @author Administrator
     */
    private class ProcessHandler extends Handler {

        public ProcessHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_AIDL:

                    break;
                default:
                    break;
            }

        }

    }

}
