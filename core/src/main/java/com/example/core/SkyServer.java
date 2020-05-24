package com.example.core;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sdk.ICallback;

import java.util.HashMap;
import java.util.Map;


public class SkyServer extends Service {
    public static final String TAG = "AIDL";

    private Map<String, RemoteCallbackList<ICallback>> mCallBack = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl(this);
    }


    public void registerCallback(String key, ICallback cb) throws RemoteException {
        if (TextUtils.isEmpty(key) || cb == null) {
            return;
        }

        if (mCallBack.containsKey(key)) {
            RemoteCallbackList<ICallback> callbacks = mCallBack.get(key);
            if (callbacks != null) {
                callbacks.register(cb);
            }
        } else {
            RemoteCallbackList<ICallback> callbacks = new RemoteCallbackList<>();
            callbacks.register(cb);
            mCallBack.put(key, callbacks);
        }

    }

    public void unregisterCallback(String key, ICallback cb) throws RemoteException {
        if (TextUtils.isEmpty(key) || cb == null) {
            return;
        }

        if (mCallBack.containsKey(key)) {
            RemoteCallbackList<ICallback> callbacks = mCallBack.get(key);
            if (callbacks != null) {
                callbacks.unregister(cb);
            }

            mCallBack.remove(key);
        }

    }


    public void postDevices(String key) {
        Log.e(TAG, "this is return devices");
        callback(key, "this is return devices");
    }


    public void postFamilies(String key) {
        Log.e(TAG, "this is return families");
        callback(key, "this is return families");
    }

    private void callback(String key, String info) {
        if (mCallBack.containsKey(key)) {
            RemoteCallbackList<ICallback> callbacks = mCallBack.get(key);
            if (callbacks != null) {
                final int n = callbacks.beginBroadcast();
                for (int i = 0; i < n; i++) {
                    try {
                        callbacks.getBroadcastItem(i).onResult(info);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                callbacks.finishBroadcast();
            }
        }
    }


}