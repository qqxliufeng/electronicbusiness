package com.android.ql.lf.electronicbusiness.utils;

import android.os.Environment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class Constants {
    public static final String BASE_IP = "http://tshop.sdqlweb.com/";
    public static final String BASE_IP_POSTFIX = "interface.php/v1/{postfix1}/{postfix2}";

    private static final String APP_ID = "shop123";
    private static final String APP_SEC = "37b082a279e3b7a9403a16b4bb15073b";

    public static final String WX_APP_ID = "wx6d1db840e54fcafc";

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
}
