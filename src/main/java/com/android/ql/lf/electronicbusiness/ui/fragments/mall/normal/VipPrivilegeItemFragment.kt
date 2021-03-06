package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.VipGoodsBean
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.VipPrivilegeItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import rx.Subscription

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

    private lateinit var currentItem: VipGoodsBean

    override fun createAdapter(): BaseQuickAdapter<VipGoodsBean, BaseViewHolder> =
            VipPrivilegeItemAdapter(R.layout.adapter_vip_privilege_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun loadData() {
        if (TextUtils.isEmpty(arguments.getString(ITEM_ID_FLAG))) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT,
                    RequestParamsHelper.getProductParams(OrderPresent.GoodsType.VIP_GOODS, "", currentPage))
        } else {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT_TYPE_SEARCH,
                    RequestParamsHelper.getProductTypeSearchParams(arguments.getString(ITEM_ID_FLAG), "", OrderPresent.GoodsType.VIP_GOODS, "", currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        isLoad = true
        val json = checkResultCode(result)
        processList(json, VipGoodsBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentItem = mArrayList[position]
        enterGoodsInfo()
    }

    private fun enterGoodsInfo() {
        val bundle = Bundle()
        bundle.putString(VipPrivilegeItemInfoFragment.GOODS_ID_FLAG, currentItem.product_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, VipPrivilegeItemInfoFragment::class.java)
    }
}