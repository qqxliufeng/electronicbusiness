package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.MessageListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AnswerInfoFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

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
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            processList(json, MessageBean::class.java)
        } else if (requestID == 0x1) {
            if (json != null) {
                OrderPresent.notifyRefreshOrderNum()
            }
        }
    }

    override fun getEmptyMessage() = "暂无消息"

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val messageBean = mArrayList[position]
        if ("1" == messageBean.message_token) { //1 代表问答消息  0 代表系统消息
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "问答详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, messageBean.message_qid)), AnswerInfoFragment::class.java)
        } else {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "系统消息", true, false, bundleOf(Pair(MessageInfoFragment.MESSAGE_BEAN_FLAG, messageBean)), MessageInfoFragment::class.java)
        }
        messageBean.message_status = "1"
        mBaseAdapter.notifyItemChanged(position)
        mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_MYMSG_STATUS, RequestParamsHelper.getEdit_mymsg_status(messageBean.message_id))
    }

}