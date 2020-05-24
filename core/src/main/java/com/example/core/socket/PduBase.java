package com.example.core.socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 命令码      包体长度    包体参数
 * 2Bytes	    2Bytes	   NByte
 */
public class PduBase {

    String sourceId;
    String targetId;
    String content;

    PduBase(String pduBase) {

        try {

            JSONObject jsonObject = new JSONObject(pduBase);
            sourceId = jsonObject.getString("sourceId");
            targetId = jsonObject.getString("targetId");
            content = jsonObject.getString("content");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("sourceId", sourceId);
            jsonObject.put("targetId", targetId);
            jsonObject.put("content", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}


