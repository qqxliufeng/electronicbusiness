package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.PersonalCutItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/12/20 0020.
 * @author lf on 2017/12/20 0020
 */
class AllPersonalCutItemFragment : BaseRecyclerViewFragment<GoodsItemBean>() {

    override fun createAdapter(): BaseQuickAdapter<GoodsItemBean, BaseViewHolder> =
            PersonalCutItemAdapter(R.layout.adapter_personal_cut_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT,
                RequestParamsHelper.getProductParams("1", "", currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT,
                RequestParamsHelper.getProductParams("1", "", currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result), GoodsItemBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        enterGoodsInfo(mArrayList[position])
    }

    private fun enterGoodsInfo(currentItem: GoodsItemBean) {
        val bundle = Bundle()
        bundle.putString(CutGoodsInfoFragment.GOODS_ID_FLAG, currentItem.product_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, CutGoodsInfoFragment::class.java)
    }


}