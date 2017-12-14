package com.android.ql.lf.electronicbusiness.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.ql.lf.electronicbusiness.interfaces.MyWbAuthListener;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by lf on 2017/12/14 0014.
 *
 * @author lf on 2017/12/14 0014
 */

public class ShareManager {

    public static int SHARE_PIC_WIDTH = 150;
    public static int SHARE_PIC_HEIGHT = 150;

    public static void shareToWxWebPager(IWXAPI api, int shareSceneType, String webpageUrl, String title, String description, Bitmap sharePic) {
        WXWebpageObject webPager = new WXWebpageObject();
        webPager.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webPager);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(sharePic, SHARE_PIC_WIDTH, SHARE_PIC_HEIGHT, true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareSceneType;
        api.sendReq(req);
    }


    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    public static void shareToWBWebPager(Context context) {
        WbShareHandler shareHandler = new WbShareHandler((Activity) context);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        shareHandler.shareMessage(weiboMessage, false);
    }


    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private static TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "text";
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

}
