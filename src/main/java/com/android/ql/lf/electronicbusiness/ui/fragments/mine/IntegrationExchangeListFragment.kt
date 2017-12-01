package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.adapters.IntegrationExchangeListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class IntegrationExchangeListFragment :BaseRecyclerViewFragment<String>(){

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            IntegrationExchangeListItemAdapter(R.layout.adapter_integration_exchange_list_item_layout,mArrayList)

    override fun onRefresh() {
        (0 until 10).forEach {
            mArrayList.add("")
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        onRequestEnd(-1)
        setRefreshEnable(false)
        setLoadEnable(false)
    }

}