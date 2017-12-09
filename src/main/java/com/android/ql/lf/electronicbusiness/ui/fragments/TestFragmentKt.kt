package com.android.ql.lf.electronicbusiness.ui.fragments

import android.app.ProgressDialog
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import kotlinx.android.synthetic.main.fragment_test_layout.*

/**
 * Created by Administrator on 2017/10/17 0017.
 */
class TestFragmentKt : BaseNetWorkingFragment() {

    companion object {
        fun newInstance() = TestFragmentKt()
    }

    override fun getLayoutId(): Int = R.layout.fragment_test_layout

    override fun initView(view: View?) {
        id_bt.setOnClickListener {
//            mPresent.getDataByGet(0x0)
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
    }

    override fun <T> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        progressDialog?.dismiss()
    }
}