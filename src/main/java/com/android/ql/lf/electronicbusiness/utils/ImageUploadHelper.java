package com.android.ql.lf.electronicbusiness.utils;

import com.android.ql.lf.electronicbusiness.data.UserInfo;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lf on 2017/11/24 0024.
 *
 * @author lf on 2017/11/24 0024
 */

public class ImageUploadHelper {

    private OnImageUploadListener onImageUploadListener;

    public static MultipartBody.Builder createMultipartBody() {
        return new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", Constants.md5Token())
                .addFormDataPart("uid", UserInfo.getInstance().getMemberId());
    }

    public ImageUploadHelper(OnImageUploadListener onImageUploadListener) {
        this.onImageUploadListener = onImageUploadListener;
    }

    public void upload(final ArrayList<ImageItem> list) {
        final File dir = new File(Constants.IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final ArrayList<String> compressList = new ArrayList<>();
        Observable.from(list).map(new Func1<ImageItem, String>() {
            @Override
            public String call(ImageItem imageItem) {
                String path = dir + File.separator + System.currentTimeMillis() + ".jpg";
                try {
                    ImageFactory.compressAndGenImage(imageItem.path, path, 100, false);
                } catch (IOException e) {
                    return null;
                }
                return path;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (onImageUploadListener != null) {
                            onImageUploadListener.onActionStart();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s != null) {
                            compressList.add(s);
                            if (compressList.size() == list.size()) {
                                if (onImageUploadListener != null) {
                                    onImageUploadListener.onActionEnd(compressList);
                                }
                            }
                        } else {
                            if (onImageUploadListener != null) {
                                onImageUploadListener.onActionFailed();
                            }
                        }
                    }
                });
    }

    public interface OnImageUploadListener {

        public void onActionStart();

        public void onActionEnd(ArrayList<String> paths);

        public void onActionFailed();
    }

}
