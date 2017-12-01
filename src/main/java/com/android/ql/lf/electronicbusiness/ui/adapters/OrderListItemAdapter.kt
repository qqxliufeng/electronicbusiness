package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderListItemAdapter(layoutId:Int,list: ArrayList<String>):BaseQuickAdapter<String,BaseViewHolder>(layoutId,list) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.addOnClickListener(R.id.mBtOrderListItemExpress)
        helper.addOnClickListener(R.id.mBtOrderListItemComment)
    }
}