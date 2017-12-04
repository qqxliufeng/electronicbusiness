package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.VipGoodsBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.VipPrivilegeItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemFragment : AbstractLazyLoadFragment<VipGoodsBean>() {

    companion object {

        val ITEM_ID_FLAG = "item_id_flag"

        fun newInstance(bundle: Bundle): VipPrivilegeItemFragment {
            val vipPrivilegeItemFragment = VipPrivilegeItemFragment()
            vipPrivilegeItemFragment.arguments = bundle
            return vipPrivilegeItemFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<VipGoodsBean, BaseViewHolder> =
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
                RequestParamsHelper.ACT_PRODUCT,
                RequestParamsHelper.getProductParams("3", "", currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        processList(json, VipGoodsBean::class.java)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val bundle = Bundle()
        bundle.putString(VipPrivilegeItemInfoFragment.GOODS_ID_FLAG, mArrayList[position].product_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, VipPrivilegeItemInfoFragment::class.java)
    }

}