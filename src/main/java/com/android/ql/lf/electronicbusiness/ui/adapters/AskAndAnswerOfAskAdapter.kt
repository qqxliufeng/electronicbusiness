package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class AskAndAnswerOfAskAdapter(layoutId: Int, list: ArrayList<IndexAskInfoBean>) : BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: IndexAskInfoBean?) {
        helper!!.setText(R.id.mTvMyAskItemTitle, item!!.quiz_title)
        helper.setText(R.id.mTvMyAskItemContent, item.quiz_content)
        helper.addOnClickListener(R.id.mTvAnswerItemExpand)
        val tvAnswer = helper.getView<TextView>(R.id.mTvMyAskItemContent)
        val tvExpand = helper.getView<TextView>(R.id.mTvAnswerItemExpand)
        tvExpand.text = if (item.isExpand) "收起" else "展开"
        tvAnswer.maxLines = item.maxLines
    }
}