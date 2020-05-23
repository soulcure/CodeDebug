// IDeviceManager.aidl
package com.example.sdk;
import com.example.sdk.entity.Device;

// Declare any non-default types here with import statements

interface IDeviceManager {
    void getDevices(out List<Device> devices,in String familyId);
}
