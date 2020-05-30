package com.example.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.core.http.HttpConnector;
import com.example.core.httpserver.SimpleServer;
import com.example.core.socket.PduBase;
import com.example.core.socket.TcpClient;
import com.example.sdk.ICallback;
import com.example.sdk.entity.Device;
import com.example.sdk.entity.Family;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;


public class SkyServer extends Service {
    public static final String TAG = "AIDL";

    private RemoteCallbackList<ICallback> mCallBack = new RemoteCallbackList<>();

    private TcpClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();

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
        String ip = "192.168.1.1";
        int port = 5566;

        Log.d(TAG, "createTcp ip:" + ip + "   port:" + port);

        if (!mClient.isConnect()) {
            InetSocketAddress isa = new InetSocketAddress(ip, port);
            mClient.setRemoteAddress(isa);
            TcpClient.IClientListener callback = new TcpClient.IClientListener() {
                @Override
                public void connectSuccess() {
                    tcpLogin();
                }
            };
            mClient.connect(callback);
        }

    }

    /**
     * 发送登录IM服务器请求
     */
    private void tcpLogin() {
        Log.d(TAG, "socket connect success");
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


    public void sendProto(String key, String dstSid, int keyCode, String keyEvent) {
        PduBase msg = new PduBase();
        mClient.sendProto(msg, null);
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

    private void callback(String info) {
        final int n = mCallBack.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                ICallback callback = mCallBack.getBroadcastItem(i);
                String key = (String) mCallBack.getBroadcastCookie(i);

                callback.onResult(info);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBack.finishBroadcast();
    }


}