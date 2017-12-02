package com.android.ql.lf.electronicbusiness.utils;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.android.ql.lf.electronicbusiness.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created by lf on 2017/11/24 0024.
 *
 * @author lf on 2017/11/24 0024
 */

public class MyImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(Uri.fromFile(new File(path))).error(R.drawable.img_glide_load_default).placeholder(R.drawable.img_glide_load_default).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        displayImage(activity, path, imageView, width, height);
    }

    @Override
    public void clearMemoryCache() {

    }
}
