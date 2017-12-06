package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.PersonalCutGoodsItemBean
import com.android.ql.lf.electronicbusiness.data.TeamCutGoodsItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.PersonalCutItemAdapter
import com.android.ql.lf.electronicbusiness.ui.adapters.TeamCutItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class TeamCutItemFragment : AbstractLazyLoadFragment<TeamCutGoodsItemBean>() {

    companion object {
        fun newInstance(cid: String): TeamCutItemFragment {
            val bundle = Bundle()
            bundle.putString("cid", cid)
            val teamCutFragment = TeamCutItemFragment()
            teamCutFragment.arguments = bundle
            return teamCutFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<TeamCutGoodsItemBean, BaseViewHolder> =
            TeamCutItemAdapter(R.layout.adapter_team_cut_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun loadData() {
        if (TextUtils.isEmpty(arguments.getString("cid"))) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT,
                    RequestParamsHelper.getProductParams("2", "", currentPage))
        } else {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT_TYPE_SEARCH,
                    RequestParamsHelper.getProductTypeSearchParams(arguments.getString("cid"), "", "2", currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result),TeamCutGoodsItemBean::class.java)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, TeamCutItemInfoFragment::class.java)
    }
}