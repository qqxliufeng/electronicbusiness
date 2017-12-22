package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.backgroundResource

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MessageListAdapter(layoutId: Int, mArrayList: ArrayList<MessageBean>?) : BaseQuickAdapter<MessageBean, BaseViewHolder>(layoutId, mArrayList) {
    override fun convert(helper: BaseViewHolder?, item: MessageBean?) {
        helper!!.setText(R.id.mTvMessageItemTitle, item!!.message_title)
        helper.setText(R.id.mTvMessageItemTime, item.message_time)
        helper.setText(R.id.mTvMessageItemContent, item.message_content)
        val tv_system = helper.getView<TextView>(R.id.mSystem)
        val notify = helper.getView<View>(R.id.mViewNotify)
        notify.visibility = if (item.message_status == "0") View.VISIBLE else View.GONE
        helper.setText(R.id.mSystem, when (item.message_token) {
            "1" -> {
                tv_system.backgroundResource = R.drawable.shape_tv_system_bg1
                "问答"
            }
            else -> {
                tv_system.backgroundResource = R.drawable.shape_tv_system_bg
                "系统"
            }
        })
    }

}