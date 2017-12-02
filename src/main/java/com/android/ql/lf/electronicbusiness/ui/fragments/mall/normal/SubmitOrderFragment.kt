package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AddressSelectFragment
import kotlinx.android.synthetic.main.fragment_submit_order_layout.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class SubmitOrderFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_submit_order_layout

    override fun initView(view: View?) {
        mCbALiPay.setOnCheckedChangeListener { _, isChecked ->
            mCbWX.isChecked = !isChecked
        }
        mCbWX.setOnCheckedChangeListener { _, isChecked ->
            mCbALiPay.isChecked = !isChecked
        }
        mRlSubmitOrderWXContainer.setOnClickListener {
            mCbWX.isChecked = true
        }
        mRlSubmitOrderAliPayContainer.setOnClickListener {
            mCbALiPay.isChecked = true
        }
        mTvSubmitOrder.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付完成", true, false, PayResultFragment::class.java)
        }
        mLlSubmitOrderAddress.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext,"选择地址",true,false, AddressSelectFragment::class.java)
        }
    }
}