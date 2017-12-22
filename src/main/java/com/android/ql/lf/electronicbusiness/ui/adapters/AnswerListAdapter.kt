package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AnswerListAdapter(layoutId: Int, list: ArrayList<IndexAskInfoBean>) : BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: IndexAskInfoBean?) {
        helper!!.setText(R.id.mTvAskListItemTitle,item!!.quiz_title)
        helper.setText(R.id.mTvAskListItemCount,"回答：${item.quiz_num}")
        helper.setText(R.id.mTvAskListItemFocus,"关注：${item.quiz_click}")
        helper.addOnClickListener(R.id.mTvAskListItemDelete)
    }
}