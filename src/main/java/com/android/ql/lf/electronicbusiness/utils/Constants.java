package com.android.ql.lf.electronicbusiness.utils;

import android.os.Environment;

import com.hyphenate.helpdesk.easeui.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class Constants {
    public static final String BASE_IP = "http://tshop.sdqlweb.com/";
    public static final String BASE_IP_POSTFIX = "interface.php/v1/{postfix1}/{postfix2}";

    private static final String APP_ID = "shop123";
    private static final String APP_SEC = "37b082a279e3b7a9403a16b4bb15073b";

    public static final String WX_APP_ID = "wx6d1db840e54fcafc";
    public static final String WB_APP_ID = "3237027949";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE = "";

    public static final String QQ_APP_ID = "1106535595";


    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_PATH = BASE_PATH + "/mzdj/";
    public static final String IMAGE_PATH = APP_PATH + "/image/";


    private static final String APP_TOKEN = APP_ID + APP_SEC;

    public static String md5Token() {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bs = digest.digest(APP_TOKEN.getBytes());
            String hexString = "";
            for (byte b : bs) {
                int temp = b & 255;
                if (temp < 16 && temp >= 0) {
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成签名
     */
    public static String genAppSign(List<WXModel> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).key);
            sb.append('=');
            sb.append(list.get(i).value);
            sb.append('&');
        }
        sb.append("key=");
        sb.append("1437d150bbb198f577583ddbe667b52f");
        String appSign = md5(sb.toString()).toUpperCase();
        return appSign;
    }

    public static String md5(String src) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bs = digest.digest(src.getBytes());
            String hexString = "";
            for (byte b : bs) {
                int temp = b & 255;
                if (temp < 16 && temp >= 0) {
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static class WXModel {
        String key;
        String value;

        public WXModel(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}
