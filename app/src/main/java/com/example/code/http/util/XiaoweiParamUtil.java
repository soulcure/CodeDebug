package com.example.code.http.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @ClassName XiaoweiParamUtil
 * @Description TODO (write something)
 * @User wuhaiyuan
 * @Date 2020-03-17
 * @Version TODO (write something)
 */
public class XiaoweiParamUtil {

    private static final String TAG = "XiaoweiParamUtil";

    public static final String APP_KEY = "e3a7185c0c88a5111e3b02829795c6da";
    public static final String APP_SECRET = "14017d1cafc8deac603d62ae798f8cdb";


    public static HashMap<String, String> getQueryMap(HashMap<String, String> queryMap){
        HashMap<String, String> pairs = new HashMap<>();
        if (null != queryMap) {
            pairs.putAll(queryMap);
        }

        pairs.put("clientid", "1111");
        pairs.put("appkey", APP_KEY);
        pairs.put("appsecret", APP_SECRET);
        pairs.put("nonce", getRandomStr(16));
        pairs.put("timestamp", System.currentTimeMillis() / 1000 + "");

//        Collection<String> keyset = pairs.keySet();
//        List<String> list = new ArrayList<String>(keyset);
        Collection<String> values = pairs.values();
        List<String> list = new ArrayList<String>(values);

        //对key键值按字典升序排序
        Collections.sort(list);

        StringBuilder sortedPairStr = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
//            String key = list.get(i);
//            String value = pairs.get(key);
//            sortedPairStr.append(key + value);
            sortedPairStr.append(list.get(i));
        }
//        sortedPairStr.append(Constants.APP_SALT_TVPAI);
        Log.d(TAG,"sortUrlParameters after sort===== : " + sortedPairStr.toString());
        String signStr = sortedPairStr.toString();
//        String sign = MD5Util.getMD5String(signStr).toLowerCase(Locale.getDefault());
        String sinature = shaEncrypt(signStr);
        pairs.put("signature", sinature);
        return pairs;
    }



    private static String getRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        int randomNum;
        char randomChar;
        Random random = new Random();
        // StringBuffer类型的可以append增加字符
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < length; i++) {
            // 可生成[0,n)之间的整数，获得随机位置
            randomNum = random.nextInt(base.length());
            // 获得随机位置对应的字符
            randomChar = base.charAt(randomNum);
            // 组成一个随机字符串
            str.append(randomChar);
        }
        return str.toString();
    }

//    private static String getSignature() {
//        Collections.sort(urlParamSortValues);
//        String tempSignature = "";
//        for (String s :
//                urlParamSortValues) {
//            Log.d(TAG, "urlParamSortValues: " + s + "\n");
//            tempSignature += s;
//        }
//        Log.d(TAG, "getSignature: " + tempSignature);
//        return shaEncrypt(tempSignature);
//    }

    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-256、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        Log.d(TAG, "shaEncrypt: " + strDes);
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

}
