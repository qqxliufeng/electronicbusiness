package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyFocusBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerOfFocusAdapter(layoutId: Int, list: ArrayList<MyFocusBean>) : BaseQuickAdapter<MyFocusBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: MyFocusBean?) {
        helper!!.setText(R.id.mTvMyAskFocusItemTitle, item!!.quiz_title)
        helper.setText(R.id.mTvMyAskFocusItemCount, "回答数：${item.answer_num}")
    }
}