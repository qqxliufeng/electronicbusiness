package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerOfAnswerAdapter(layoutId: Int, list: ArrayList<AnswerBean>) : BaseQuickAdapter<AnswerBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: AnswerBean?) {
        helper!!.addOnClickListener(R.id.mTvAnswerItemExpand)
        val tvExpand = helper.getView<TextView>(R.id.mTvAnswerItemExpand)
        val tvAnswer = helper.getView<TextView>(R.id.mTvAnswerItemAnswerContent)
        helper.addOnClickListener(R.id.mTvAnswerItemExpand)
        helper.setText(R.id.mTvAnswerItemAnswerTitle, "问题：${item!!.answer_title}")
        helper.setText(R.id.mTvAnswerItemAnswerContent, "回答：${item.answer_content}")
        tvExpand.text = if (item!!.isExpand) "收起" else "展开"
        tvAnswer.maxLines = item.maxLines
    }
}