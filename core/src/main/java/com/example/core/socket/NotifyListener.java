package com.example.core.socket;

public abstract class NotifyListener {
    public int commandId;

    public NotifyListener(int comId) {
        commandId = comId;
    }

    public int getCommandId() {
        return commandId;
    }

    public abstract void OnRec(byte[] body);
}
