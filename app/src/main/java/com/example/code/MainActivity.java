package com.example.code;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sdk.Devices;
import com.example.sdk.SdkManager;
import com.example.sdk.entity.Device;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "AIDL";

    TextView tv_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_info = findViewById(R.id.tv_info);

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
                break;
            case R.id.test_tag_inout:
                Log.e(TAG, "test 1111");

                SdkManager.instance(this).getDevices("1", new Devices() {
                    @Override
                    public void onSuccess(List<Device> list) {
                        tv_info.setText(list.get(0).getDeviceName());
                        Log.e(TAG, "test result");
                        Log.e(TAG, "client getDevices:" + list.toString());
                    }
                });
                Log.e(TAG, "test 2222");

                break;
            default:
                break;
        }
    }
}