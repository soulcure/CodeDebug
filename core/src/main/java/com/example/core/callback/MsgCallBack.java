package com.example.core.callback;

public interface MsgCallBack {

    void onSendResult(int code, String msg);

    void onReceiveString(String msg);

    void onReceiveFile(String fileId, String url);


}
