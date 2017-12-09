package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.android.ql.lf.electronicbusiness.ui.adapters.MessageListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MessageListFragment : BaseRecyclerViewFragment<MessageBean>() {

    override fun createAdapter(): BaseQuickAdapter<MessageBean, BaseViewHolder> = MessageListAdapter(R.layout.adapter_message_list_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MSG, RequestParamsHelper.getMyMessageList(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MSG, RequestParamsHelper.getMyMessageList(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result), MessageBean::class.java)
    }

}