package com.android.ql.lf.electronicbusiness.utils;

import android.content.Context;
import android.widget.ImageView;

import com.android.ql.lf.electronicbusiness.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lf on 2017/11/22 0022.
 *
 * @author lf on 2017/11/22 0022
 */

public class GlideManager {

    public static void loadImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(Constants.BASE_IP + path).error(R.drawable.img_glide_load_default).placeholder(R.drawable.img_glide_load_default).into(imageView);
    }

    public static void loadFaceCircleImage(Context context, String path, ImageView imageView) {
        String tempPath;
        if (path != null) {
            if (path.startsWith("http://") || path.startsWith("http://")) {
                tempPath = path;
            } else {
                tempPath = Constants.BASE_IP + path;
            }
            Glide.with(context)
                    .load(tempPath)
                    .error(R.drawable.pic_headportrait)
                    .placeholder(R.drawable.pic_headportrait)
                    .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                    .into(imageView);
        }
    }

    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        String tempPath;
        if (path != null) {
            if (path.startsWith("http://") || path.startsWith("http://")) {
                tempPath = path;
            } else {
                tempPath = Constants.BASE_IP + path+"asdfasdfasdfa";
            }
            Glide.with(context)
                    .load("https://gss3.bdstatic.com/7Po3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=eb3a512554df8db1bc2e7b623118ba69/7af40ad162d9f2d3e0767452a3ec8a136327cc2c.jpg")
                    .error(R.drawable.img_glide_circle_load_default)
                    .placeholder(R.drawable.img_glide_circle_load_default)
                    .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                    .into(imageView);
        }
    }

    public static void downImage(Context context,String src){
//        Glide.with(context).load(src).downloadOnly(500,500)
    }

}
