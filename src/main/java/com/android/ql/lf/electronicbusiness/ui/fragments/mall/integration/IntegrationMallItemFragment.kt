package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IMallGoodsItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.IntegrationMallItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.utils.DividerGridItemDecoration
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class IntegrationMallItemFragment : AbstractLazyLoadFragment<IMallGoodsItemBean>() {

    companion object {
        val ACT_TYPE_FLAG = "act_type_flag"
        val TYPE_PARAM_FLAG = "type_param_flag"
        val STYPE_PARAM_FLAG = "stype_param_flag"

        fun newInstance(bundle: Bundle): IntegrationMallItemFragment {
            val integrationMallItemFragment = IntegrationMallItemFragment()
            integrationMallItemFragment.arguments = bundle
            return integrationMallItemFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<IMallGoodsItemBean, BaseViewHolder> =
            IntegrationMallItemAdapter(R.layout.adapter_integration_mall_item_layout, mArrayList)

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 2)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
    }

    override fun loadData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                arguments.getString(ACT_TYPE_FLAG),
                if (TextUtils.isEmpty(arguments.getString(TYPE_PARAM_FLAG))) {
                    RequestParamsHelper.getJproductParam(currentPage)
                } else {
                    RequestParamsHelper.getJproductSearchParam(
                            arguments.getString(TYPE_PARAM_FLAG),
                            arguments.getString(STYPE_PARAM_FLAG,"") ,
                            page = currentPage)
                }
        )
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        isLoad = true
        processList(json, IMallGoodsItemBean::class.java)
    }

    override fun onLoadMore() {
        if (!isVisible || !isPrepared) {
            return
        }
        super.onLoadMore()
        loadData()
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val bundle = Bundle()
        bundle.putString(IntegrationMallGoodsInfoFragment.GOODS_ID_FLAG, mArrayList[position].jproduct_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "兑换详情", true, false, bundle, IntegrationMallGoodsInfoFragment::class.java)
    }

}