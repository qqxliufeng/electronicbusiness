package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_pay_result_layout.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class PayResultFragment : BaseFragment() {

    companion object {
        fun newInstance(code: Int): PayResultFragment {
            val fragment = PayResultFragment()
            val bundle = Bundle()
            bundle.putInt("code", code)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_pay_result_layout

    override fun initView(view: View?) {
        mBtBack.setOnClickListener {
            finish()
        }
        when (arguments.getInt("code")) {
            0 -> {
                mLlPayResultOrderInfoContainer.visibility = View.VISIBLE
                mTvPayResultTitle.text = "支付成功"
                mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_success,0,0,0)
            }
            else -> {
                mLlPayResultOrderInfoContainer.visibility = View.GONE
                mTvPayResultTitle.text = "支付失败"
                mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_fail,0,0,0)
            }
        }
    }
}