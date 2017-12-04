package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.SpecificationBean;
import com.android.ql.lf.electronicbusiness.utils.GlideManager;

import java.util.ArrayList;

/**
 * Created by liufeng on 2017/12/4.
 */

public class BottomGoodsParamDialog extends BottomSheetDialog {

    private TextView tv_price;
    private TextView tv_release_count;
    private TextView tv_goods_name;

    private TextView tv_goods_num;

    private LinearLayout llContainer;
    private ImageView iv_goods_pic;

    public BottomGoodsParamDialog(@NonNull Context context) {
        super(context);
        View contentView = View.inflate(context, R.layout.layout_personal_cut_item_goods_info_bootom_params_layout, null);
        setContentView(contentView);
        getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, context.getResources().getDisplayMetrics());
        contentView.getLayoutParams().height = height;
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(((View) contentView.getParent()));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(height);
        tv_price = contentView.findViewById(R.id.mTvBottomParamPrice);
        tv_release_count = contentView.findViewById(R.id.mTvBottomParamReleaseCount);
        tv_goods_name = contentView.findViewById(R.id.mTvBottomParamName);
        tv_goods_num = contentView.findViewById(R.id.mTvBottomParamCount);
        llContainer = contentView.findViewById(R.id.mLlBottomParamRuleContainer);
        iv_goods_pic = contentView.findViewById(R.id.mIvGoodsPic);
        contentView.findViewById(R.id.mTvBottomParamClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        contentView.findViewById(R.id.mTvBottomParamConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        contentView.findViewById(R.id.mTvBottomParamDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contentView.findViewById(R.id.mTvBottomParamAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void bindDataToView(String price, String releaseCount, String goodsName, String defaultPicPath,ArrayList<SpecificationBean> items) {
        tv_price.setText(price);
        tv_release_count.setText(releaseCount);
        tv_goods_name.setText(goodsName);
        GlideManager.loadRoundImage(getContext(), defaultPicPath, iv_goods_pic, 15);
        if (items != null && !items.isEmpty()) {
            for (final SpecificationBean item : items) {
                MyFlexboxLayout myFlexboxLayout = new MyFlexboxLayout(getContext());
                myFlexboxLayout.setTitle(item.getName());
                myFlexboxLayout.addItems(item.getItem());
                myFlexboxLayout.setOnItemClickListener(new MyFlexboxLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index) {
                        GlideManager.loadRoundImage(getContext(), item.getPic().get(index), iv_goods_pic, 15);
                    }
                });
                llContainer.addView(myFlexboxLayout);
            }
        }
    }
}
