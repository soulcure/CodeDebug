package com.example.core;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sdk.ICallback;


public class SkyServer extends Service {
    public static final String TAG = "AIDL";

    private final RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl(this);
    }


    public RemoteCallbackList<ICallback> getCallbacks() {
        return mCallbacks;
    }


    public void postDevices() {
        Log.e(TAG, "this is return devices");
        callback("this is return devices");
    }


    public void postFamilies() {
        Log.e(TAG, "this is return families");
        callback("this is return families");
    }

    private void callback(String info) {
        final int n = mCallbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                mCallbacks.getBroadcastItem(i).onResult(info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }


}