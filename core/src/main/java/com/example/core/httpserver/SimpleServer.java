package com.example.core.httpserver;

import android.content.Context;
import android.util.Log;

import com.example.core.utils.DeviceUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class SimpleServer extends NanoHTTPD {

    public static final String TAG = "TcpClient";
    private static final Map<String, String> router = new HashMap<>();
    private Context mContext;

    public SimpleServer(Context context, int port) {
        super(port);
        this.mContext = context;
        registerRouter();
        String ip = DeviceUtils.getLocalIpAddress(true);
        Log.d(TAG, "NanoHTTPD listener to " + ip + ":" + port);
    }


    public void startServer() {
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public Response response404(IHTTPSession session, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }

    private void registerRouter() {
        router.put("/info", "/info");
        router.put("/index", "/index");
        router.put("/shutdown", "/shutdown");
        router.put("/reboot", "/reboot");
        router.put("/setWifiAp", "/setWifiAp");
        router.put("/setWifiApPrimary", "/setWifiApPrimary");
        router.put("/shell", "/shell");
        router.put("/cmd", "/cmd");
        router.put("/transfer", "/transfer");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Log.d(TAG, "NanoHTTPD serve uri:" + uri);

        if (!router.containsKey(uri)) {
            return response404(session, uri);
        }
        try {

            if (uri.equals("/info")) {   //begin for api
                return getCurInfo();
            } else if (uri.equals("/index")) {
                return newFixedLengthResponse("========/index=======");
            }


        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", uri);
    }

    private Response getCurInfo() {
        Log.d(TAG, "NanoHTTPD getCurInfo start");
        String json = "{}";

        Response res = newFixedLengthResponse(Response.Status.OK, "application/json", json);
        res.addHeader("Access-Control-Allow-Origin", "*");

        Log.d(TAG, "NanoHTTPD getCurInfo end");
        return res;
    }


}
