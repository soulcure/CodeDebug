package com.example.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PduBase implements Parcelable {

    public static final String SMART_SCREEN = "ss-clientID-SmartScreen";   //iot-channel通道 clientID
    public static final String APP_STORE = "ss-clientID-appstore_12345";   //应用圈 clientID

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

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
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

            object.put("reply", reply);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();

    }

    private PduBase() {

    }


    public PduBase(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            id = jsonObject.optString("id", "");
            source = jsonObject.optString("source", "");
            target = jsonObject.optString("target", "");
            clientSource = jsonObject.optString("client-source", "");
            clientTarget = jsonObject.optString("client-target", "");
            type = jsonObject.optString("type", "");

            content = jsonObject.optString("content", "");
            reply = jsonObject.optBoolean("reply", false);

            JSONObject ext = jsonObject.optJSONObject("extra");
            if (ext != null) {
                String key = "force-sse";
                String value = ext.optString(key, "");
                if (!TextUtils.isEmpty(value)) {
                    extra.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        public PduBase build() {
            PduBase pduBase = new PduBase();
            pduBase.id = UUID.randomUUID().toString();
            pduBase.clientSource = this.clientSource;
            pduBase.content = this.content;
            pduBase.source = this.source;
            pduBase.type = this.type;
            pduBase.target = this.target;
            pduBase.extra = this.extra;
            pduBase.clientTarget = this.clientTarget;
            pduBase.reply = this.reply;
            return pduBase;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.source);
        dest.writeString(this.target);
        dest.writeString(this.clientSource);
        dest.writeString(this.clientTarget);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeInt(this.extra.size());
        for (Map.Entry<String, String> entry : this.extra.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeByte(this.reply ? (byte) 1 : (byte) 0);
    }

    protected PduBase(Parcel in) {
        this.id = in.readString();
        this.source = in.readString();
        this.target = in.readString();
        this.clientSource = in.readString();
        this.clientTarget = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        int extraSize = in.readInt();
        this.extra = new HashMap<String, String>(extraSize);
        for (int i = 0; i < extraSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.extra.put(key, value);
        }
        this.reply = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PduBase> CREATOR = new Parcelable.Creator<PduBase>() {
        @Override
        public PduBase createFromParcel(Parcel source) {
            return new PduBase(source);
        }

        @Override
        public PduBase[] newArray(int size) {
            return new PduBase[size];
        }
    };
}
