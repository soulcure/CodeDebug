// IFamilyManager.aidl
package com.example.sdk;

// Declare any non-default types here with import statements

interface IKeyCmd {
    void sendKeyEvent(in String key,in String dstSid,in int keyCode,in String keyEvent);
}
