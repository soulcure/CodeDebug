package com.example.code;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sdk.DevicesListener;
import com.example.sdk.FamiliesListener;
import com.example.sdk.SameLan;
import com.example.sdk.SdkManager;
import com.example.sdk.entity.Device;
import com.example.sdk.entity.Family;

import org.json.JSONObject;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "AIDL";

    TextView tv_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_info = findViewById(R.id.tv_info);

        JSONObject jsonObject = new JSONObject();

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
                SdkManager.instance(this).getFamilies(new FamiliesListener() {
                    @Override
                    public void onSuccess(List<Family> list) {
                        tv_info.setText(list.get(0).getFamilyName());
                        Log.e(TAG, "client getFamilies:" + list.get(0).getFamilyName());
                    }
                });
                break;
            case R.id.test_tag_inout:
                /*SdkManager.instance(this).getDevices("1", new DevicesListener() {
                    @Override
                    public void onSuccess(List<Device> list) {
                        tv_info.setText(list.get(0).getDeviceName());
                        Log.e(TAG, "client getDevices:" + list.toString());
                    }
                });*/

                String ip = "172.20.146.248";
                //String ip = "192.168.50.66";
                boolean test = SameLan.isInSameLAN(ip);
                Log.e("yao", "test:" + test);
                break;
            default:
                break;
        }
    }
}