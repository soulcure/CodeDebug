package com.example.code.http.util;


import android.os.Build;
import android.util.Log;


import com.example.code.BuildConfig;
import com.example.code.http.common.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by WHY on 2017/2/13.
 */
public class ParamsUtil {

    private static final String TAG = ParamsUtil.class.getSimpleName();

    public static String tv_source = "iqiyi";

    public static HashMap<String, Object> getQueryMap(HashMap<String, Object> queryMap){
        HashMap<String, Object> pairs = new HashMap<>();
        if (null != queryMap) {
            pairs.putAll(queryMap);
        }
        pairs.put("appkey", Constants.APP_KEY_TVPAI);
        pairs.put("time", System.currentTimeMillis() / 1000);
        pairs.put("vuid", getUniquePsuedoID());
        pairs.put("version_code", BuildConfig.VERSION_CODE);
        pairs.put("tv_source", tv_source);

        Collection<String> keyset= pairs.keySet();
        List<String> list = new ArrayList<String>(keyset);

        //对key键值按字典升序排序
        Collections.sort(list);

        StringBuilder sortedPairStr = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            Object value = pairs.get(key);
            sortedPairStr.append(key + value);
        }
        sortedPairStr.append(Constants.APP_SALT_TVPAI);
        Log.d(TAG,"sortUrlParameters after sort===== : " + sortedPairStr.toString());
        String signStr = sortedPairStr.toString();
        String sign = MD5Util.getMD5String(signStr).toLowerCase(Locale.getDefault());
        pairs.put("sign", sign);
        return pairs;
    }

    public static String getUniquePsuedoID() {
        String serial = "serial";

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            //API>=9 使用serial号
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    public static String getSignByQueryAndBodyParams(Map<String,Object> queryAndBodyParamsMap){

        Log.w(TAG,"传入的原始query和body参数：" + queryAndBodyParamsMap.toString());
        String sign = "";
        //按字母顺序拼接参数
        List<Map.Entry<String, Object>> list = MapSortUtil.sortMap(queryAndBodyParamsMap);
        for (int i = 0; i < list.size(); i++) {
            if(i < list.size() -1) {
                sign += (list.get(i).getKey() + "=" + list.get(i).getValue() + "&");
            }else {
                sign += (list.get(i).getKey() + "=" + list.get(i).getValue() );
            }
        }
        sign += Constants.VIDEO_CALL_SIGN_SECRET;//加盐
        Log.w(TAG,"重排序拼接后的加盐参数：" + sign);
        String md5String = MD5Util.getMD5String(sign);
        Log.w(TAG,"最终获取到的md5参数：" + md5String);
        return md5String;
    }

    public static JSONArray getStringJsonArray(List<String> list) {

        JSONArray jsonArray = new JSONArray();
        if (list!=null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String parameter = list.get(i);
                if (parameter != null) {
                    jsonArray.put(parameter);
                } else {
//                    jsonArray.put("");
                }
                Log.d(TAG, "label=" + list.get(i));

            }
        } else {
//            jsonArray.put("");
        }
//        if (jsonArray.length() > 0)
        String str = jsonArray.toString();
        Log.d(TAG, "str==" + str);
        return jsonArray;
    }
}
