package com.android.ql.lf.electronicbusiness.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.interfaces.INetDataPresenter;
import com.android.ql.lf.electronicbusiness.present.GetDataFromNetPresent;
import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/10/16 0016.
 * test normal function
 * @author lf
 */

public class TestActivity extends AppCompatActivity implements INetDataPresenter{

    @Inject
    GetDataFromNetPresent present;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(EBApplication.getInstance().getAppComponent()).build().inject(this);
//        present.setNetDataPresenter(this);
        setContentView(R.layout.activity_test_layout);
        ImageView imageView = this.findViewById(R.id.mTestImage);
//        Glide.with(this).load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg").into(imageView);
        Glide.with(this).load(R.drawable.img_icon_test_pic_05).into(imageView);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width = metrics.widthPixels;
//
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setShowCamera(true);
//        imagePicker.setCrop(true);
//        imagePicker.setImageLoader(new MyImageLoader());
//        imagePicker.setFocusWidth(width - 50 * 2);
//        imagePicker.setMultiMode(false);
//        imagePicker.setFocusHeight(imagePicker.getFocusWidth());
    }

    @Override
    public void onRequestStart(int requestID) {
    }

    @Override
    public void onRequestFail(int requestID, @NotNull Throwable e) {

    }

    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
        Log.e("TAG",result.toString());
    }

    @Override
    public void onRequestEnd(int requestID) {
    }

    static class MyImageLoader implements ImageLoader {
        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//            GlideApp.with(activity).load(Uri.fromFile(new File(path))).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//            GlideApp.with(activity)
//                    .load(Uri.fromFile(new File(path)))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
        }
    }


    public void onClickImage(View view) {
//        present.getDataByGet(0x0);
    }

    @Override
    protected void onDestroy() {
        ImagePicker.getInstance().clear();
        ImagePicker.getInstance().setImageLoader(null);
        super.onDestroy();
    }
}
