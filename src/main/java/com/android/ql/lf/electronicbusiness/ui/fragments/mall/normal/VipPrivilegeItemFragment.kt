package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.a.VipPrivilegeItemInfoFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.VipPrivilegeItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.IntegrationMallItemFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemFragment : AbstractLazyLoadFragment<String>() {

    companion object {

        val ITEM_ID_FLAG = "item_id_flag"

        fun newInstance(bundle: Bundle): VipPrivilegeItemFragment {
            val vipPrivilegeItemFragment = VipPrivilegeItemFragment()
            vipPrivilegeItemFragment.arguments = bundle
            return vipPrivilegeItemFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            VipPrivilegeItemAdapter(R.layout.adapter_vip_privilege_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun initView(view: View?) {
        isFirstRefresh = false
        super.initView(view)
    }

    override fun lazyLoad() {
        onRefresh()
    }

    override fun onRefresh() {
        if (!isVisible || !isPrepared || isLoad) {
            return
        }
        mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true
        }
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT_TYPE_SEARCH,
                RequestParamsHelper.getProductTypeSearchParams(arguments.getString(ITEM_ID_FLAG, ""), "", "3", currentPage))
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, VipPrivilegeItemInfoFragment::class.java)
    }

}