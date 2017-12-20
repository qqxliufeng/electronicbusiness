package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CutGoodsInfoBean
import com.android.ql.lf.electronicbusiness.data.MyCutPriceBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.CutPriceListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.CutGoodsInfoFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class CutPriceListFragment : BaseRecyclerViewFragment<MyCutPriceBean>() {

    override fun createAdapter(): BaseQuickAdapter<MyCutPriceBean, BaseViewHolder> =
            CutPriceListItemAdapter(R.layout.adapter_cut_price_list_item_layout, mArrayList)


    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration: DividerItemDecoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return decoration
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MBARGAIN, RequestParamsHelper.getMbargainParam(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MBARGAIN, RequestParamsHelper.getMbargainParam(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result), MyCutPriceBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext,"商品详情",true,false, bundleOf(Pair(CutGoodsInfoFragment.GOODS_ID_FLAG,mArrayList[position].product_id)),CutGoodsInfoFragment::class.java)
    }

}