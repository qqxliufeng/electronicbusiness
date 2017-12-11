package com.android.ql.lf.electronicbusiness.utils;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.SelectImageItemBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by lf on 2017/12/11 0011.
 *
 * @author lf on 2017/12/11 0011
 */

public class SelectImageManager {

    /**
     * 选择图片适配类
     */
    public static class SelectImagesAdapter extends BaseQuickAdapter<SelectImageItemBean, BaseViewHolder> {

        public SelectImagesAdapter(int layoutResId, @Nullable List<SelectImageItemBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SelectImageItemBean item) {
            ImageView iv_pic = helper.getView(R.id.mIvCommentImageItem);
            if (TextUtils.isEmpty(item.getPath())) {
                Glide.with(iv_pic.getContext()).load(item.getResId()).into(iv_pic);
            } else {
                Glide.with(iv_pic.getContext()).load(item.getPath()).into(iv_pic);
            }
        }
    }

    /**
     * 选择图片加载类
     */
    public static class SelectImageLoader implements ImageLoader {
        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Glide.with(activity).load(Uri.fromFile(new File(path))).error(R.drawable.img_glide_load_default).placeholder(R.drawable.img_glide_load_default).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Glide.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {

        }
    }

}
