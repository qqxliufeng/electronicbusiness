package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IntegrationExChangeRecordBean
import com.android.ql.lf.electronicbusiness.ui.adapters.IntegrationExchangeListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class IntegrationExchangeListFragment :BaseRecyclerViewFragment<IntegrationExChangeRecordBean>(){

    override fun createAdapter(): BaseQuickAdapter<IntegrationExChangeRecordBean, BaseViewHolder> =
            IntegrationExchangeListItemAdapter(R.layout.adapter_integration_exchange_list_item_layout,mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,RequestParamsHelper.MEMBER_MODEL,RequestParamsHelper.ACT_RECORD,RequestParamsHelper.getRecordParam(currentPage))
    }


    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0,RequestParamsHelper.MEMBER_MODEL,RequestParamsHelper.ACT_RECORD,RequestParamsHelper.getRecordParam(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result),IntegrationExChangeRecordBean::class.java)
    }


}