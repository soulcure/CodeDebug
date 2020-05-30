package com.example.sdk;

import com.example.sdk.entity.Device;

import java.util.List;

public interface DevicesListener {
    void onSuccess(List<Device> list);
}