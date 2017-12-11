package com.android.ql.lf.electronicbusiness.ui.adapters;

import android.support.annotation.Nullable;

import com.android.ql.lf.electronicbusiness.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by liufeng on 2017/12/11.
 */

public class SearchAdapter extends BaseQuickAdapter<String,BaseViewHolder>{


    public SearchAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.mTvSearchTitle,item);
    }
}
