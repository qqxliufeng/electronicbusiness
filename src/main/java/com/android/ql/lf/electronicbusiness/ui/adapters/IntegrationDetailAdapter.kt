package com.android.ql.lf.electronicbusiness.ui.adapters

import android.graphics.Color
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IntegrationBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class IntegrationDetailAdapter(layoutId: Int, list: ArrayList<IntegrationBean>) : BaseQuickAdapter<IntegrationBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: IntegrationBean?) {
        helper!!.setText(R.id.mTvIntegrationItemTitle, item!!.integral_title)
        helper.setText(R.id.mTvIntegrationItemTime, item.integral_time)
        helper.setText(R.id.mTvIntegrationItemCount, "${ if ("0" == item.integral_sym) "-" else "+"}${item.integral_price}")
        helper.setTextColor(R.id.mTvIntegrationItemCount, if ("0" == item.integral_sym) Color.RED else Color.GRAY)
    }
}