// ICallBackManager.aidl
package com.example.sdk;
import com.example.sdk.ICallBack;

interface ICallBackManager {
    void registerCallback(in String appId,in ICallback cb);
    void unregisterCallback(in String appId,in ICallback cb);
}
