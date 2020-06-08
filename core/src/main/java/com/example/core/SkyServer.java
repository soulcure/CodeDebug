package com.example.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sdk.entity.PduBase;
import com.example.core.http.HttpConnector;
import com.example.core.httpserver.SimpleServer;
import com.example.core.socket.ReceiveListener;
import com.example.core.socket.TcpClient;
import com.example.core.utils.AppUtils;
import com.example.core.utils.DeviceUtils;
import com.example.sdk.ICallback;
import com.example.sdk.entity.Device;
import com.example.sdk.entity.Family;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SkyServer extends Service {
    public static final String TAG = "AIDL";

    private RemoteCallbackList<ICallback> mCallBack = new RemoteCallbackList<>();
    private ConcurrentMap<String, String> mapKey = new ConcurrentHashMap<>();  //线程安全

    private Session sourceSession, targetSession;
    private TcpClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();

        sourceSession = new Session();
        targetSession = new Session();

        mClient = new TcpClient(this);

        SimpleServer simpleServer = new SimpleServer(this, 8088);//开启服务器
        simpleServer.startServer();

        createTcp();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl(this);
    }


    private void createTcp() {
        final String sid = AppUtils.getStringSharedPreferences(this, "sid", null);
        if (TextUtils.isEmpty(sid)) {
            Log.e("TAG", "sid is null ,not create tcp connect");
            return;
        }

        String ip = "192.168.1.1";   //ip
        int port = 5566;             //port

        Log.d(TAG, "createTcp ip:" + ip + "   port:" + port);

        if (!mClient.isConnect()) {
            InetSocketAddress isa = new InetSocketAddress(ip, port);
            mClient.setRemoteAddress(isa);
            TcpClient.IClientListener callback = new TcpClient.IClientListener() {
                @Override
                public void connectSuccess() {
                    tcpLogin(sid);
                }
            };
            mClient.connect(callback);
        }

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

//        targetSession.setId(sid);
//        targetSession.setExtraItem(Session.IM_CLOUD, sid);
//
//        targetSession.setExtraItem(Session.ADDRESS_LOCAL, targetIp);
//        targetSession.setExtraItem(Session.ADDRESS_LOCAL, targetIp);
//
//        targetSession.setExtraItem(Session.IM_LOCAL, targetIp + ":" + targetPort);

        ReceiveListener callback = null;
        if (!TextUtils.isEmpty(msgId)) {
            callback = new ReceiveListener() {
                @Override
                public void OnRec(PduBase msg) {

                }
            };
        }
        mClient.sendProto(pdu, callback);

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

        mClient.sendProto(msg, null);
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

        mClient.sendProto(msg, new ReceiveListener() {
            @Override
            public void OnRec(PduBase msg) {
                String target = msg.getSource();
                if (!TextUtils.isEmpty(target)) {
                    try {
                        JSONObject jsonObject = new JSONObject(target);

                        String id = jsonObject.optString("id", "");
                        if (TextUtils.isEmpty(id)) {
                            Log.e(TAG, "error");
                        } else {
                            targetSession.setId(id);
                            JSONObject extraObj = jsonObject.optJSONObject("extra");
                            if (extraObj != null) {
                                String imLocal = extraObj.optString("im-local", "");
                                String addressLocal = extraObj.optString("address-local", "");
                                String streamLocal = extraObj.optString("stream-local", "");
                                String imCloud = extraObj.optString("im-cloud", "");

                                targetSession.setExtraItem("im-local", imLocal);
                                targetSession.setExtraItem("address-local", addressLocal);
                                targetSession.setExtraItem("stream-local", streamLocal);
                                targetSession.setExtraItem("im-cloud", imCloud);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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


}