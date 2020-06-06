package com.example.core.socket;

import com.example.core.entity.MessageBean;

public abstract class ReceiveListener {
    public abstract void OnRec(MessageBean msg);
}
