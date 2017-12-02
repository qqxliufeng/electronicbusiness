package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextPaint
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_vip_info_layout.*

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class VipInfoFragment : BaseNetWorkingFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_vip_info_layout

    override fun initView(view: View?) {
        mVipOldMoney.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mVipOldMoney.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        mCbWC.isChecked = true
        mCbWC.setOnCheckedChangeListener { _, isChecked ->
            mCbALiPay.isChecked = !isChecked
        }
        mCbALiPay.setOnCheckedChangeListener { _, isChecked ->
            mCbWC.isChecked = !isChecked
        }
    }
}