package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal;

import android.os.Bundle;
import android.view.View;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment;
import com.android.ql.lf.electronicbusiness.ui.adapters.SearchAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by liufeng on 2017/12/11.
 */

public class SearchFragment extends BaseRecyclerViewFragment<String>{

    public static final String K_TYPE_FLAG = "k_type_flag";

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BaseQuickAdapter<String, BaseViewHolder> createAdapter(){
        return new SearchAdapter(R.layout.adapter_search_item_layout,mArrayList);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    mArrayList.add("item"+i);
                }
                mBaseAdapter.notifyDataSetChanged();
            }
        },5000);
        mBaseAdapter.setEmptyView(R.layout.search_empty_view_layout);
        mBaseAdapter.notifyDataSetChanged();
    }



}
