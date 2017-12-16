package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.ql.lf.electronicbusiness.utils.ExtensionUtilsKt;
import com.android.ql.lf.electronicbusiness.utils.GlideManager;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/13 0013.
 *
 * @author lf on 2017/12/13 0013
 */

public class ImageContainerLinearLayout extends LinearLayout {

    private ArrayList<String> paths;

    public ImageContainerLinearLayout(Context context) {
        this(context, null);
    }

    public ImageContainerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageContainerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setImages(ArrayList<String> images) {
        paths = images;
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (paths != null && !paths.isEmpty()) {
            removeAllViews();
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, getContext().getResources().getDisplayMetrics());
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getContext().getResources().getDisplayMetrics());
            int imageWidth = (getMeasuredWidth() - padding * 4) / 3;
            int imageHeight = imageWidth;
            for (String path : paths) {
                ImageView image = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                params.leftMargin = margin;
                params.rightMargin = margin;
                image.setLayoutParams(params);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideManager.loadImage(getContext(), path, image);
                addView(image);
            }
        }
    }
}
