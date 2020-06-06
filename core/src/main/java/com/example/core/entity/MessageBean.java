package com.example.core.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBean {

    public static final String SMART_SCREEN = "ss-clientID-SmartScreen";
    public static final String APP_STORE = "ss-clientID-appstore_12345";


    /**
     * id : 1f932332-0600-4d63-9016-89f85d298128
     * source : {"id":"164aa4dd6688494ba3d36db11fbf671f","extra":{"im-local":"172.20.146.54:34823","address-local":"172.20.146.54","stream-local":"172.20.146.54","im-cloud":"164aa4dd6688494ba3d36db11fbf671f"}}
     * target : {"id":"1f975a3a72a84261a1c8cfea5fd6499e","extra":{"im-local":"192.168.50.53:33843","address-local":"192.168.50.53","stream-local":"192.168.50.53","im-cloud":"1f975a3a72a84261a1c8cfea5fd6499e"}}
     * client-source : ss-clientID-SmartScreen
     * client-target : ss-clientID-appstore_12345
     * type : TEXT
     * content : {"cmd":"","param":"","type":"SCREEN_SHOT"}
     * extra : {"force-sse":"true"}
     * reply : false
     */

    private String id;
    private String source;
    private String target;
    private String clientSource;
    private String clientTarget;
    private String type;
    private String content;
    private Map<String, String> extra;
    private boolean reply;


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", UUID.randomUUID().toString());
            object.put("source", source);
            object.put("target", target);
            object.put("client-source", clientSource);
            object.put("client-target", clientTarget);
            object.put("type", type);
            object.put("content", content);

            JSONObject jsonObj = new JSONObject(extra);//转化为json格式
            object.put("extra", jsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();

    }


    public static String getKeyCodeContent(int keyCode, String keyEvent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", keyCode);
            jsonObject.put("param", "");
            jsonObject.put("type", keyEvent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static final class Builder {

        private String source;
        private String target;
        private String clientSource;
        private String clientTarget;
        private String type;
        private String content;
        private Map<String, String> extra;
        private boolean reply;

        private Builder() {
            extra = new HashMap<>();
        }

        public static Builder newBuilder() {
            return new Builder();
        }


        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setTarget(String target) {
            this.target = target;
            return this;
        }

        public Builder setClientSource(String clientSource) {
            this.clientSource = clientSource;
            return this;
        }

        public Builder setClientTarget(String clientTarget) {
            this.clientTarget = clientTarget;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setForceSse(boolean b) {
            extra.put("force-sse", String.valueOf(b));
            return this;
        }

        public Builder setReply(boolean reply) {
            this.reply = reply;
            return this;
        }

        public MessageBean build() {
            MessageBean messageBean = new MessageBean();
            messageBean.id = UUID.randomUUID().toString();
            messageBean.clientSource = this.clientSource;
            messageBean.content = this.content;
            messageBean.source = this.source;
            messageBean.type = this.type;
            messageBean.target = this.target;
            messageBean.extra = this.extra;
            messageBean.clientTarget = this.clientTarget;
            messageBean.reply = this.reply;
            return messageBean;
        }
    }
}
