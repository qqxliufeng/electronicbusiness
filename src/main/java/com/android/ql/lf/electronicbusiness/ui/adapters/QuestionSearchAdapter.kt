package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/12/18 0018.
 * @author lf on 2017/12/18 0018
 */
class QuestionSearchAdapter(layoutId: Int, list: ArrayList<IndexAskInfoBean>) : BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: IndexAskInfoBean?) {
        helper!!.setText(R.id.mTvQuestionSearchItem, item!!.quiz_title)
        helper.setText(R.id.mTvQuestionSearchItemCount, "${item.quiz_click}个回答")
    }
}