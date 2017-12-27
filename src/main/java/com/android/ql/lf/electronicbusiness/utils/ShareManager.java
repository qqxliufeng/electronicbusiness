package com.android.ql.lf.electronicbusiness.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.IOException;

/**
 * Created by lf on 2017/12/14 0014.
 *
 * @author lf on 2017/12/14 0014
 */

public class ShareManager {

    public static int SHARE_PIC_WIDTH = 150;
    public static int SHARE_PIC_HEIGHT = 150;

    public static void shareToWxWebPager(IWXAPI api, int shareSceneType, String webpageUrl, String title, String description, Bitmap sharePic) {
        if ((sharePic != null && !sharePic.isRecycled())) {
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
    }


    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static void shareToWBWebPager(WbShareHandler shareHandler, String title, String description, String actionUrl, Bitmap bitmap, String defaultText)throws Exception {
        if ((bitmap != null && !bitmap.isRecycled())) {
            shareHandler.registerApp();
            shareHandler.setProgressColor(0xff33b5e5);
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = title;
            mediaObject.description = description;
            String outPath = Constants.IMAGE_PATH + System.currentTimeMillis() + ".jpg";
            ImageFactory.compressAndGenImage(bitmap, outPath, 32);
            Bitmap thumbImage = BitmapFactory.decodeFile(outPath);
            mediaObject.setThumbImage(thumbImage);
            mediaObject.actionUrl = actionUrl;
            mediaObject.defaultText = defaultText;
            weiboMessage.mediaObject = mediaObject;
            shareHandler.shareMessage(weiboMessage, false);
        }
    }

    public static void shareToQQ(Context context, Tencent tencent, String title, String description, String actionUrl, String imageUrl, IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, actionUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "拇指斗价");
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        tencent.shareToQQ((Activity) context, bundle, listener);
    }

}
