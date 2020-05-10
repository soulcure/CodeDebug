// ICallBackManager.aidl
package com.example.sdk;
import com.example.sdk.ICallBack;

interface ICallBackManager {
    void registerCallback(in ICallback cb);
    void unregisterCallback(in ICallback cb);
}
