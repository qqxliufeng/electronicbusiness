package com.android.ql.lf.electronicbusiness.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by liufeng on 2017/12/11.
 */

public class SearchAdapter extends BaseQuickAdapter<GoodsItemBean, BaseViewHolder> {


    private String searchContent = "";

    public SearchAdapter(int layoutResId, @Nullable List<GoodsItemBean> data) {
        super(layoutResId, data);
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsItemBean item) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(item.getProduct_name());
        if (item.getProduct_name().contains(searchContent)) {
            int index = item.getProduct_name().indexOf(searchContent);
            stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.black_tv_color)), index, index + searchContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.mTvSearchTitle, stringBuilder);
        }else {
            helper.setText(R.id.mTvSearchTitle, item.getProduct_name());
        }
    }
}
