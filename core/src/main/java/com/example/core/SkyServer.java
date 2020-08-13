package com.example.core;

import android.app.Service;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sdk.entity.PduBase;
import com.example.core.http.HttpConnector;
import com.example.core.httpserver.SimpleServer;
import com.example.core.utils.AppUtils;
import com.example.core.utils.DeviceUtils;
import com.example.sdk.ICallback;
import com.example.sdk.entity.Device;
import com.example.sdk.entity.Family;
import com.skyworth.dpclientsdk.ConnectState;
import com.skyworth.dpclientsdk.StreamSinkCallback;
import com.skyworth.dpclientsdk.StreamSourceCallback;
import com.skyworth.dpclientsdk.TcpClient;
import com.skyworth.dpclientsdk.TcpServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SkyServer extends Service {
    public static final String TAG = "AIDL";

    private static final int LOCAL_PORT = 34000;

    private static final int HANDLER_THREAD_INIT_CONFIG_START = 1;
    private static final int HANDLER_THREAD_AUTO_LOGIN = 2;

    private ProcessHandler mProcessHandler;  //子线程 handler

    private RemoteCallbackList<ICallback> mCallBack = new RemoteCallbackList<>();
    private ConcurrentMap<String, String> mapKey = new ConcurrentHashMap<>();  //线程安全

    private Session sourceSession, targetSession;
    private TcpClient tcpClient;
    private TcpServer tcpServer;

    private StreamSinkCallback streamSinkCallback = new StreamSinkCallback() {
        @Override
        public void onConnectState(ConnectState connectState) {

        }

        @Override
        public void onData(String s) {

        }

        @Override
        public void onData(byte[] bytes) {

        }

        @Override
        public void onAudioFrame(MediaCodec.BufferInfo bufferInfo, ByteBuffer byteBuffer) {

        }

        @Override
        public void onVideoFrame(MediaCodec.BufferInfo bufferInfo, ByteBuffer byteBuffer) {

        }

        @Override
        public void ping(String s) {

        }

        @Override
        public void pong(String s) {

        }
    };

    private StreamSourceCallback streamSourceCallback = new StreamSourceCallback() {
        @Override
        public void onConnectState(ConnectState connectState) {

        }

        @Override
        public void onData(String s) {

        }

        @Override
        public void onData(byte[] bytes) {

        }

        @Override
        public void ping(String s) {

        }

        @Override
        public void pong(String s) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initHandler();

        tcpServer = new TcpServer(LOCAL_PORT, TcpServer.BUFFER_SIZE_LOW, streamSinkCallback);


        sourceSession = new Session();
        targetSession = new Session();


        SimpleServer simpleServer = new SimpleServer(this, 8088);//开启服务器
        simpleServer.startServer();

        createTcp("192.168.50.106");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl(this);
    }


    private void createTcp(String ip) {
        if (TextUtils.isEmpty(ip)) {
            Log.e(TAG, "createTcp error --- ip address is null");
            return;
        }
        if (tcpClient != null) {
            tcpClient.close();
            tcpClient = null;
        }
        tcpClient = new TcpClient(ip, LOCAL_PORT, streamSourceCallback);
        tcpClient.open();
    }

    /**
     * 发送登录IM服务器请求
     */
    private void tcpLogin(String sid) {
        Log.d(TAG, "socket connect success");

        AppUtils.setStringSharedPreferences(this, "sid", sid);

        sourceSession.setId(sid);
        sourceSession.setExtraItem(Session.IM_CLOUD, sid);

        String localIPAddress = DeviceUtils.getLocalIPAddress(this);
        sourceSession.setExtraItem(Session.ADDRESS_LOCAL, localIPAddress);
        sourceSession.setExtraItem(Session.STREAM_LOCAL, localIPAddress);

    }


    private void pingTarget(String msgId, String appId, String sid) {
        if (!TextUtils.isEmpty(msgId)) {
            mapKey.put(msgId, appId);
        }

        String targetIp = "";
        int targetPort = 0;

        targetSession.setId(sid);
        targetSession.setExtraItem(Session.IM_CLOUD, sid);

        targetSession.setExtraItem(Session.ADDRESS_LOCAL, targetIp);
        targetSession.setExtraItem(Session.ADDRESS_LOCAL, targetIp);

        targetSession.setExtraItem(Session.IM_LOCAL, targetIp + ":" + targetPort);
    }


    public void sendProto(String appId, PduBase pdu) {

        String msgId = pdu.getId();

        if (!TextUtils.isEmpty(msgId)) {
            mapKey.put(msgId, appId);
        }

        String targetIp = "";
        int targetPort = 0;

    }


    public void registerCallback(String key, ICallback cb) throws RemoteException {
        if (TextUtils.isEmpty(key) || cb == null) {
            return;
        }

        mCallBack.register(cb, key);

    }

    public void unregisterCallback(String key, ICallback cb) throws RemoteException {
        if (TextUtils.isEmpty(key) || cb == null) {
            return;
        }

        mCallBack.unregister(cb);

    }


    public void sendKeyCmd(int keyCode, String keyEvent) {

        PduBase.Builder builder = PduBase.Builder.newBuilder();
        builder.setSource(sourceSession.toString());
        builder.setTarget(targetSession.toString());
        builder.setClientSource(PduBase.SMART_SCREEN);
        builder.setClientSource(PduBase.APP_STORE);
        builder.setType("TEXT");
        builder.setReply(false);
        builder.setForceSse(true);
        builder.setContent(PduBase.getKeyCodeContent(keyCode, keyEvent));
        PduBase msg = builder.build();
    }


    public void connectDevice(String targetSid) {

        Session target = new Session();
        target.setId(targetSid);

        PduBase.Builder builder = PduBase.Builder.newBuilder();
        builder.setSource(sourceSession.toString());
        builder.setTarget(target.toString());
        builder.setClientSource(PduBase.SMART_SCREEN);
        //builder.setClientSource(MessageBean.APP_STORE);
        builder.setType("TEXT");
        builder.setReply(false);
        builder.setForceSse(true);
        //builder.setContent(MessageBean.getKeyCodeContent(keyCode, keyEvent));
        PduBase msg = builder.build();

    }


    public List<Device> getDevices(String key) {
        List<Device> list = new ArrayList<>();

        String test = HttpConnector.doGet(null, "https://www.baidu.com/", null);


        Device device1 = new Device();
        device1.setDeviceId("id-1");
        device1.setDeviceName("设备test01");

        Device device2 = new Device();
        device2.setDeviceId("id-2");
        device2.setDeviceName("设备2");

        list.add(device1);
        list.add(device2);

        return list;
    }


    public List<Family> getFamilies(String key) {
        Log.e(TAG, "this is return families");

        try {
            Thread.sleep(2 * 1000);  //fot http request test
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Family> families = new ArrayList<>();
        Family family1 = new Family();
        family1.setFamilyId("001");
        family1.setFamilyName("家庭001");

        Family family2 = new Family();
        family2.setFamilyId("002");
        family2.setFamilyName("家庭002");

        families.add(family1);
        families.add(family2);

        return families;
    }

    private void callback(String msgId, String info) {
        final int n = mCallBack.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                ICallback callback = mCallBack.getBroadcastItem(i);
                String key = (String) mCallBack.getBroadcastCookie(i);

                if (mapKey.containsKey(msgId)) {
                    String appId = mapKey.get(msgId);
                    if (appId != null && appId.equals(key)) {
                        callback.onResult(info);
                        mapKey.remove(msgId);
                        break;
                    }
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBack.finishBroadcast();
    }


    public void post(Runnable r) {
        mProcessHandler.post(r);
    }

    public void postDelayed(Runnable r, long delay) {
        mProcessHandler.postDelayed(r, delay);
    }

    /**
     * 线程初始化
     */
    private void initHandler() {
        if (mProcessHandler == null) {
            HandlerThread handlerThread = new HandlerThread("SkyServer-Thread");
            handlerThread.start();
            mProcessHandler = new ProcessHandler(handlerThread.getLooper());
        }
    }


    /**
     * 子线程handler,looper
     *
     * @author Administrator
     */
    private static class ProcessHandler extends Handler {

        public ProcessHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_THREAD_INIT_CONFIG_START:
                    break;
                case HANDLER_THREAD_AUTO_LOGIN:
                    break;
                default:
                    break;
            }

        }

    }
}