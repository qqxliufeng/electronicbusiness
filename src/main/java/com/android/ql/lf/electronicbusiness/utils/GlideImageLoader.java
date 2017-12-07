package com.android.ql.lf.electronicbusiness.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by lf on 2017/12/7 0007.
 *
 * @author lf on 2017/12/7 0007
 */

public class GlideImageLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideManager.loadImage(context, (String) path, imageView);
    }
}
