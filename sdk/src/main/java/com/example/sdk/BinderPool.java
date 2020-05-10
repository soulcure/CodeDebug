package com.example.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class BinderPool {
    private static final String TAG = "AIDL";

    public static final int BIND_DEVICE = 1;
    public static final int BIND_FAMILY = 2;
    public static final int BIND_CALLBACK = 3;


    private enum BIND_STATUS {
        IDLE, BINDING, BINDED
    }


    private BIND_STATUS binded = BIND_STATUS.IDLE;

    private Context mContext;
    private IBinderPool mBinderPool;
    private ICallBackManager iCallBackManager;
    private List<SdkManager.InitListener> mInitListenerList;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            Log.e(TAG, "mBinderPool = " + mBinderPool);
            try {
                IBinder binder = mBinderPool.queryBinder(BIND_CALLBACK);//获取Binder后使用
                iCallBackManager = ICallBackManager.Stub.asInterface(binder);
                iCallBackManager.registerCallback(mProxyCallback);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            binded = BIND_STATUS.BINDED;
            for (SdkManager.InitListener item : mInitListenerList) {
                item.success();
            }
            mInitListenerList.clear();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected = " + name);
            mBinderPool = null;
            binded = BIND_STATUS.IDLE;
            for (SdkManager.InitListener item : mInitListenerList) {
                item.fail();
            }
            mInitListenerList.clear();
        }
    };


    Devices listener;

    public Devices getListener() {
        return listener;
    }

    public void setListener(Devices listener) {
        this.listener = listener;
    }

    private ICallback mProxyCallback = new ICallback.Stub() {
        @Override
        public void onResult(String info) throws RemoteException {
            Log.e(TAG, "bind Pool onResult=" + info);
            if (listener != null) {
                ArrayList<String> list = new ArrayList<>();
                list.add("1");
                list.add("2");
                list.add("3");
                listener.onSuccess(list);
            }
        }
    };


    public BinderPool(Context context) {
        mContext = context.getApplicationContext();
        mInitListenerList = new ArrayList<>();
    }

    public synchronized void bindPoolService(SdkManager.InitListener listener) {
        Log.e(TAG, "bindPoolService");

        if (listener != null) {
            mInitListenerList.add(listener);
        }

        if (binded == BIND_STATUS.IDLE) {
            binded = BIND_STATUS.BINDING;

            String PROXY_SERVICE = "com.example.core.SkyServer";
            String PROXY_SERVICE_PACKAGE = "com.example.code";

            Intent intent = new Intent(PROXY_SERVICE);
            intent.setClassName(PROXY_SERVICE_PACKAGE, PROXY_SERVICE);

            mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
            Log.e(TAG, "bind Pool Service！");

        } else if (binded == BIND_STATUS.BINDING) {
            //do nothing
        } else if (binded == BIND_STATUS.BINDED) {
            for (SdkManager.InitListener item : mInitListenerList) {
                item.success();
            }
            mInitListenerList.clear();
        }
    }


    //获取Binder
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (null != mBinderPool) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }


    /**
     * sdk销毁
     */
    public void destroy() {
        if (mContext != null) {
            if (binded == BIND_STATUS.BINDED) {
                binded = BIND_STATUS.IDLE;
                mContext.unbindService(conn);
            }

            if (iCallBackManager != null) {
                try {
                    iCallBackManager.unregisterCallback(mProxyCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
