package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MessageListAdapter(layoutId: Int, mArrayList: ArrayList<MessageBean>?) : BaseQuickAdapter<MessageBean, BaseViewHolder>(layoutId, mArrayList) {
    override fun convert(helper: BaseViewHolder?, item: MessageBean?) {
        helper!!.setText(R.id.mTvMessageItemTitle, item!!.message_title)
        helper.setText(R.id.mTvMessageItemTime, item.message_time)
        helper.setText(R.id.mTvMessageItemContent, item.message_content)
    }

}