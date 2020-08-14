package com.example.core.sse;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.core.SkyServer;
import com.example.core.callback.MsgCallBack;
import com.example.core.utils.AppUtils;
import com.skyworthiot.iotssemsg.IotSSEMsgLib;

import java.io.File;
import java.io.IOException;


public class SSEPushModel {
    private static final String TAG = "SSE";

    private Context mContext;
    private SkyServer mSkyServer;
    private MsgCallBack msgCallBack;


    private IotSSEMsgLib mIotSSEMsgLib;
    private String mDeviceId = "";
    private ConnectHandler mConnectHandler;
    private int connectDelay = 1;   //SSE重连延时时间,单位秒

    private static final String APP_SALT = "e53fc2d3c4ca4177b280fcc1fbf69aa4";

    private static final int HANDLER_SSE_RECONNECT = 1;

    public SSEPushModel(@NonNull SkyServer server, @NonNull MsgCallBack callback) {
        mContext = server;
        mSkyServer = server;
        msgCallBack = callback;

        mConnectHandler = new ConnectHandler(mContext.getMainLooper());

        mIotSSEMsgLib = new IotSSEMsgLib(mContext, new IotSSEMsgLib.IOTSSEMsgListener() {
            @Override
            public String appSalt() {
                return APP_SALT;
            }

            @Override
            public void onSSELibError(IotSSEMsgLib.SSEErrorEnum sseErrorEnum, String s) {
                Log.e("sse", "sseErrorEnum = " + sseErrorEnum + " msg = " + s);
                //网络连接失败，重连SSE
                switch (sseErrorEnum) {
                    case ConnectHostError:
                    case SSEDisconnectError:
                    case ConnectBosHostError:
                    case RegisterSSError:
                    case RegisterIotDeviceError:
                        reConnect();
                        break;
                }
            }

            @Override
            public void onSSEStarted() {
                Log.d("sse", "SSE Connect success...");
                connectDelay = 1;
                mConnectHandler.removeMessages(HANDLER_SSE_RECONNECT);
            }

            @Override
            public void onSendResult(IotSSEMsgLib.SSESendResultEnum sendResult, String destId, String msgId, String msgName, String message) {
                msgCallBack.onSendResult(0, message);
            }

            @Override
            public void onSendFileToCloud(IotSSEMsgLib.SendFileResultEnum sendFileResult, String fileKey, long currentSize, long totalSize, int respCode, String respMsg, String toDestId) {
                Log.d(TAG, "onSendFileToCloud sendFileResult=" + sendFileResult + "  fileKey=" + fileKey + " currentSize=" + currentSize + " totalSize=" + totalSize + " respCode" + respCode + " respMsg" + respMsg + " toDestId" + toDestId);
            }

            @Override
            public void onReceivedFileFromCloud(IotSSEMsgLib.ReceivedFileResultEnum receFileResult, String fileKey, long currentSize, long totalSize, String url) {
                msgCallBack.onReceiveFile(fileKey, url);
            }

            @Override
            public void onReceivedSSEMessage(String id, String event, String data) {
                Log.d(TAG, "onReceivedSSEMessage id=" + id + " event=" + event + " data=" + data);
                msgCallBack.onReceiveString(data);
            }
        });
    }


    public boolean isSSEConnected() {
        return mIotSSEMsgLib.isSSEConnected();
    }

    public boolean isSSEStarted() {
        return mIotSSEMsgLib.isSSEConnected();
    }


    public boolean connectSSE(String deviceId) {
        Log.d("SmartScreenImpl", "SmartScreenImpl deviceId:" + deviceId);
        return initPushSEE(deviceId);
    }


    private void sseReConnect() {
        if (TextUtils.isEmpty(mDeviceId)) {
            Log.e("sse", "sse mDeviceId is null and core exit");
            System.exit(0);
        } else {
            if (AppUtils.isNetworkConnected(mContext)) {
                mIotSSEMsgLib.reConnectSSEAsSmartScreen(mDeviceId);
                connectDelay = connectDelay + 5;
                Log.e(TAG, "sse 开始重连，不成功下次重连延时秒---" + connectDelay);
            } else {
                Log.e(TAG, "sse 网络不可用...等待网络可用");
            }
        }
    }

    public void disconnect() {
        mIotSSEMsgLib.close();
    }


    public void reConnect() {
        mConnectHandler.sendEmptyMessageDelayed(HANDLER_SSE_RECONNECT, connectDelay * 1000);
    }


    public void sendSSEMessage(String toDeviceId, String msgId, String msgName, String message) throws IOException {
        if (mIotSSEMsgLib.isSSEConnected()) {
            mIotSSEMsgLib.sendMessage(toDeviceId, msgId, msgName, message);
        } else {
            initPushSEE(mDeviceId);
            throw new IOException();
        }
    }

    public void uploadFile(String target, File file, String uid) throws IOException {
        if (mIotSSEMsgLib.isSSEConnected()) {
            mIotSSEMsgLib.syncFileToCloud(target, file, uid);
        } else {
            initPushSEE(mDeviceId);
            throw new IOException();
        }
    }

    public void downloadFile(String fileKey) {
        mIotSSEMsgLib.syncFileFromCloud(fileKey);
    }

    /**
     * SSE初始化
     */
    public boolean initPushSEE(String deviceId) {
        if (!TextUtils.isEmpty(deviceId)) {
            connectDelay = 1;
            mDeviceId = deviceId;
            mIotSSEMsgLib.connectSSEAsSmartScreen(mDeviceId);

            Log.d(TAG, "SSE推送初始化--参数：DeviceID:" + mDeviceId);
            return true;
        } else {
            Log.e(TAG, "设备ID或userId为空----参数：DeviceID:" + mDeviceId);
            return false;
        }

    }


    private class ConnectHandler extends Handler {

        public ConnectHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_SSE_RECONNECT) {
                mSkyServer.post(new Runnable() {
                    @Override
                    public void run() {
                        sseReConnect();
                    }
                });
            }


        }
    }
}
