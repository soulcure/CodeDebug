package com.example.core.socket;

import com.example.sdk.entity.PduBase;

public abstract class ReceiveListener {
    public abstract void OnRec(PduBase msg);
}
