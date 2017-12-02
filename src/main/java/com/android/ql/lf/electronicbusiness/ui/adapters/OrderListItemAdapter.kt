package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderListItemAdapter(layoutId:Int,list: ArrayList<MyOrderBean>):BaseQuickAdapter<MyOrderBean,BaseViewHolder>(layoutId,list) {
    override fun convert(helper: BaseViewHolder?, item: MyOrderBean?) {
        helper!!.addOnClickListener(R.id.mBtOrderListItemExpress)
        helper.addOnClickListener(R.id.mBtOrderListItemComment)
    }
}