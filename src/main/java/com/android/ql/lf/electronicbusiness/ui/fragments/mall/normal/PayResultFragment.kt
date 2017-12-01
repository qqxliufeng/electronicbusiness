package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_pay_result_layout.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class PayResultFragment : BaseFragment(){

    override fun getLayoutId()= R.layout.fragment_pay_result_layout

    override fun initView(view: View?) {
        mBtBack.setOnClickListener {
            finish()
        }
    }
}