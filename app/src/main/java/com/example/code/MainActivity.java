package com.example.code;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sdk.SdkManager;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "AIDL";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_tag_in).setOnClickListener(this);
        findViewById(R.id.test_tag_out).setOnClickListener(this);
        findViewById(R.id.test_tag_inout).setOnClickListener(this);

        SdkManager.instance(this).init(new SdkManager.InitListener() {
            @Override
            public void success() {
                Log.e(TAG, "sdk init success");
            }

            @Override
            public void fail() {
                Log.e(TAG, "sdk init fail");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_tag_in:
            case R.id.test_tag_out:
                SdkManager.instance(this).getFamilies();
                break;
            case R.id.test_tag_inout:
                SdkManager.instance(this).getDevices();
                break;
            default:
                break;
        }
    }
}