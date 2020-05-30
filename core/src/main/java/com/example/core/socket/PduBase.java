package com.example.core.socket;

import org.json.JSONException;
import org.json.JSONObject;

public class PduBase {
/*
    {
	"id": "9bde2e7a-1dfd-4851-ad68-a6ca528b09b3",
	"source": {
		"id": "99e64124f3c04b1d822056c8f5f24603",
		"extra": {
			"im-local": "192.168.1.131:46494",
			"address-local": "192.168.1.131",
			"stream-local": "192.168.1.131",
			"im-cloud": "99e64124f3c04b1d822056c8f5f24603"
		}
	},
	"target": {
		"id": "e8eeaa28bd2d49008a62f76e2f44fc62",
		"extra": {
			"im-local": "172.20.146.28:42856",
			"address-local": "172.20.146.28",
			"stream-local": "172.20.146.28",
			"im-cloud": "e8eeaa28bd2d49008a62f76e2f44fc62"
		}
	},
	"client-source": "ss-clientID-interconnected",
	"client-target": "ss-clientID-appstore_12345",
	"type": "TEXT",
	"content": {
		"cmd": "24",
		"param": "",
		"type": "KEY_EVENT"
	},
	"extra": {
		"force-sse": "true"
	},
	"reply": false
}
*/


    //消息id
    String id;

    //消息类型
    String type;

    //消息内容
    ContentBean content;

    //消息扩展参数
    ExtraParam extra;

    //是否答复
    boolean reply;

    Session source;
    Session target;


    String clientSource;
    String clientTarget;


    public static class ContentBean {
        private String cmd;
        private String param;
        private String type;
    }


    public static class Session {
        private String id;
        private ExtraBean extra;

        public static class ExtraBean {

            private String imLocal;
            private String addressLocal;
            private String streamLocal;
            private String imCloud;

        }
    }

    public static class ExtraParam {
        private String forceSse;
    }

    public PduBase() {

    }

    PduBase(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            id = jsonObject.optString("id", "");
            type = jsonObject.optString("type", "");

            JSONObject contentObj = jsonObject.optJSONObject("content");
            if (contentObj != null) {
                content = new ContentBean();
                content.cmd = contentObj.optString("cmd", "");
                content.param = contentObj.optString("param", "");
                content.type = contentObj.optString("type", "");
            }


            JSONObject ext = jsonObject.optJSONObject("extra");
            if (ext != null) {
                extra = new ExtraParam();
                extra.forceSse = ext.optString("extra", "");
            }

            reply = jsonObject.optBoolean("reply", false);

            JSONObject sourceObj = jsonObject.optJSONObject("source");
            if (sourceObj != null) {
                source.id = contentObj.optString("id", "");
                JSONObject extraObj = jsonObject.optJSONObject("extra");
                if (extraObj != null) {
                    source.extra = new Session.ExtraBean();
                    source.extra.imLocal = extraObj.optString("im-local", "");
                    source.extra.addressLocal = extraObj.optString("address-local", "");
                    source.extra.streamLocal = extraObj.optString("stream-local", "");
                    source.extra.imCloud = extraObj.optString("im-cloud", "");
                }
            }


            JSONObject targetObj = jsonObject.optJSONObject("target");
            if (targetObj != null) {
                target.id = contentObj.optString("id", "");
                JSONObject extraObj = jsonObject.optJSONObject("extra");
                if (extraObj != null) {
                    target.extra = new Session.ExtraBean();
                    target.extra.imLocal = extraObj.optString("im-local", "");
                    target.extra.addressLocal = extraObj.optString("address-local", "");
                    target.extra.streamLocal = extraObj.optString("stream-local", "");
                    target.extra.imCloud = extraObj.optString("im-cloud", "");
                }
            }


            clientSource = jsonObject.optString("client-source", "");
            clientTarget = jsonObject.optString("client-target", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("source", source);

            if (content != null) {
                JSONObject contentObject = new JSONObject();
                contentObject.put("cmd", content.cmd);
                contentObject.put("param", content.param);
                contentObject.put("type", content.type);

                jsonObject.put("content", contentObject);
            }


            if (extra != null) {
                JSONObject extraObject = new JSONObject();
                extraObject.put("force-sse", extra.forceSse);

                jsonObject.put("extra", extraObject);
            }

            jsonObject.put("reply", reply);


            jsonObject.put("client-source", clientSource);
            jsonObject.put("client-target", clientTarget);

            if (source != null) {
                JSONObject sourceObject = new JSONObject();
                sourceObject.put("id", source.id);

                if (source.extra != null) {
                    JSONObject extraObject = new JSONObject();
                    extraObject.put("im-local", source.extra.imLocal);
                    extraObject.put("address-local", source.extra.addressLocal);
                    extraObject.put("stream-local", source.extra.streamLocal);
                    extraObject.put("im-cloud", source.extra.imCloud);

                    sourceObject.put("extra", extraObject);

                    jsonObject.put("source", sourceObject);
                }
            }


            if (target != null) {
                JSONObject targetObject = new JSONObject();
                targetObject.put("id", target.id);

                if (target.extra != null) {
                    JSONObject extraObject = new JSONObject();
                    extraObject.put("im-local", target.extra.imLocal);
                    extraObject.put("address-local", target.extra.addressLocal);
                    extraObject.put("stream-local", target.extra.streamLocal);
                    extraObject.put("im-cloud", target.extra.imCloud);

                    targetObject.put("extra", extraObject);

                    jsonObject.put("target", targetObject);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}


