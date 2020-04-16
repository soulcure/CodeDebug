
package com.example.code.http.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class CipherUtil
{
    public static String CHARSET = "UTF-8";
    private static final String AES_OPTIONS = "AES/ECB/PKCS5Padding";

    /**
    *
    * @param cipherText
    * @param key
    * @return
    */
    public static String decryptAES(String cipherText, String key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(AES_OPTIONS);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(CHARSET), "AES"));
            return new String(cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT)));
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param plain 
     * @param key 
     * @return
     */
    public static String encryptAES(String plain, String key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(AES_OPTIONS);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(CHARSET), "AES"));
            return Base64.encodeToString(cipher.doFinal(plain.getBytes(CHARSET)), Base64.DEFAULT);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5(String plain)
    {
        byte[] hash;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(plain.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash)
        {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
