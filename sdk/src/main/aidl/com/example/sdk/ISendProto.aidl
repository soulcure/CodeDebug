// IFamilyManager.aidl
package com.example.sdk;

import com.example.sdk.entity.PduBase;

// Declare any non-default types here with import statements

interface ISendProto {
    void sendProto(in String appId,in PduBase pdu);
}
