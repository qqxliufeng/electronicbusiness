package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IMallGoodsItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.IntegrationMallItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.DividerGridItemDecoration
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by liufeng on 2017/12/19.
 */
class IntegrationTypeSearchResultFragment : BaseRecyclerViewFragment<IMallGoodsItemBean>() {

    companion object {
        val TYPE_PARAM_FLAG = "type_param_flag"
        val STYPE_PARAM_FLAG = "stype_param_flag"
    }

    override fun createAdapter(): BaseQuickAdapter<IMallGoodsItemBean, BaseViewHolder> =
            IntegrationMallItemAdapter(R.layout.adapter_integration_mall_item_layout, mArrayList)

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 2)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_JPRODUCT_SEARCH,
                RequestParamsHelper.getJproductSearchParam(
                        arguments.getString(TYPE_PARAM_FLAG),
                        arguments.getString(STYPE_PARAM_FLAG, ""),
                        page = currentPage)
        )
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun getEmptyMessage() = "暂无商品哦~~~"


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result), IMallGoodsItemBean::class.java)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext,
                "兑换详情",
                true,
                false,
                bundleOf(Pair(IntegrationMallGoodsInfoFragment.GOODS_ID_FLAG, mArrayList[position].jproduct_id)),
                IntegrationMallGoodsInfoFragment::class.java)
    }

}