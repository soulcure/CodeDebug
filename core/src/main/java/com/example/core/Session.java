package com.example.core;


import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Session {
    /**
     * 局域网IM通道标识
     */
    public static final String IM_LOCAL = "im-local";

    /**
     * 局域网Stream通道标识
     */
    public static final String STREAM_LOCAL = "stream-local";


    public static final String ADDRESS_LOCAL = "address-local";

    /**
     * 云端IM通道标识
     */
    public static final String IM_CLOUD = "im-cloud";

    /**
     * 云端Stream通道标识
     */
    public static final String STREAM_CLOUD = "stream-cloud";


    private String id;
    private Map<String, String> extra;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtraItem(String key, String value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
    }


    @NonNull
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);

            if (extra != null) {
                JSONObject extraObject = new JSONObject(extra);
                jsonObject.put("extra", extraObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
