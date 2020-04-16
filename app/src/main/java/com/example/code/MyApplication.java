package com.example.code;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.InitListener;


public class MyApplication extends Application {


    private static final String TAG = "login";

    @Override
    public void onCreate() {
        super.onCreate();

        //闪验SDK配置debug开关 （必须放在初始化之前，开启后可打印闪验SDK更加详细日志信息）
        OneKeyLoginManager.getInstance().setDebug(true);

        //闪验SDK初始化（建议放在Application的onCreate方法中执行）
        initShanyanSDK(getApplicationContext());

    }

    private void initShanyanSDK(Context context) {
        OneKeyLoginManager.getInstance().init(context, BuildConfig.OneKey_AppID, new InitListener() {
            @Override
            public void getInitStatus(int code, String result) {
                //闪验SDK初始化结果回调
                Log.e(TAG, "init： code==" + code + "   result==" + result);
                if (code == 1022) {//初始化成功

                } else {
                    //todo
                }
            }
        });
    }


}
