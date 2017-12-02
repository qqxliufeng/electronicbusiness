package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AddNewAskListAdapter(layoutId: Int, list: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.setText(R.id.mTvAddNewAskItemTitle, item)
    }
}