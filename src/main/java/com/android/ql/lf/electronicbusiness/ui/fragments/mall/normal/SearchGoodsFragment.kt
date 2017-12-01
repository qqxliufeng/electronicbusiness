package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.adapters.SearchGoodsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.DividerGridItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import android.view.View
import kotlinx.android.synthetic.main.fragment_search_goods_layout.*


/**
 * Created by lf on 2017/11/14 0014.
 * @author lf on 2017/11/14 0014
 */
class SearchGoodsFragment : BaseRecyclerViewFragment<String>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_search_goods_layout

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            SearchGoodsAdapter(R.layout.adapter_search_goods_item_layout, mArrayList)

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val manager = GridLayoutManager(mContext, 2)
        mManager = manager
        return manager
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
    }

    override fun initView(view: View?) {
        super.initView(view)
        mIvSearchGoodsBack.setOnClickListener {
            finish()
        }
    }

    override fun onRefresh() {
        (0..5).forEach {
            mArrayList.add("")
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        setRefreshEnable(false)
        onRequestEnd(-1)
    }


}