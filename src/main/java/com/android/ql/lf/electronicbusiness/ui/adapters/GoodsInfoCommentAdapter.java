package com.android.ql.lf.electronicbusiness.ui.adapters;

import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean;
import com.android.ql.lf.electronicbusiness.utils.GlideManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lf on 2017/12/8 0008.
 *
 * @author lf on 2017/12/8 0008
 */

public class GoodsInfoCommentAdapter extends BaseQuickAdapter<CommentForGoodsBean, BaseViewHolder> {

    public GoodsInfoCommentAdapter(int layoutResId, @Nullable List<CommentForGoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentForGoodsBean item) {
        ImageView iv_user_pic = helper.getView(R.id.mIvGoodsInfoCommentItemFace);
        GlideManager.loadCircleImage(iv_user_pic.getContext(), item.getMember_pic(), iv_user_pic);
        helper.setText(R.id.mTvGoodsInfoCommentItemName, item.getMember_name());
        helper.setText(R.id.mTvGoodsInfoCommentItemTime, item.getComment_time());
        helper.setText(R.id.mTvGoodsInfoCommentItemContent, item.getComment_content());
        LinearLayout ll_pic_container = helper.getView(R.id.mLlGoodsInfoCommentItemPicContainer);
        ll_pic_container.removeAllViews();
        ArrayList<String> commentPic = item.getComment_pic();
        int size = Math.min(commentPic.size(), 3);
        for (int i = 0; i < size; i++) {
            ImageView image = new ImageView(ll_pic_container.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0f, ll_pic_container.getContext().getResources().getDisplayMetrics()));
            params.weight = 1.0f;
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, ll_pic_container.getContext().getResources().getDisplayMetrics());
            params.leftMargin = margin;
            params.rightMargin = margin;
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideManager.loadImage(ll_pic_container.getContext(), commentPic.get(i), image);
            ll_pic_container.addView(image);
        }
    }
}