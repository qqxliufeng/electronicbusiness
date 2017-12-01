package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.adapters.MessageListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MessageListFragment : BaseRecyclerViewFragment<MessageBean>() {

    override fun createAdapter(): BaseQuickAdapter<MessageBean, BaseViewHolder> = MessageListAdapter(R.layout.adapter_message_list_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        setLoadEnable(false)
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MSG, RequestParamsHelper.getMyMessageList())
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        val jsonObject = checkResultCode(result.toString())
        if (jsonObject != null) {
            mArrayList.addAll(ListParseHelper<MessageBean>().fromJson(jsonObject.toString(), MessageBean::class.java))
            mBaseAdapter.notifyDataSetChanged()
        } else {
            toast("暂无消息")
        }
    }

}