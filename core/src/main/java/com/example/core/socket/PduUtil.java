package com.example.core.socket;

import android.util.Log;


import com.example.core.BuildConfig;
import com.example.core.utils.HexUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public abstract class PduUtil {

    private static final String TAG = "TcpClient";

    public abstract void OnRec(String pduBase);

    public abstract void OnCallback(PduBase pduBase);

    public int ParsePdu(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        byte[] packByte = buffer.array();
        String content = new String(packByte);
        buffer.clear();

        OnRec(content);

        return packByte.length;

    }

}