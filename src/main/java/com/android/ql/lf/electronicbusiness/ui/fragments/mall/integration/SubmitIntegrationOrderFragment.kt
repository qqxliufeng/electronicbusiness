package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PayResultFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AddressSelectFragment
import com.android.ql.lf.electronicbusiness.utils.RxBus
import kotlinx.android.synthetic.main.fragment_integration_submit_order_layout.*
import rx.Subscription

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class SubmitIntegrationOrderFragment : BaseNetWorkingFragment() {

    private lateinit var subject: Subscription

    override fun getLayoutId() = R.layout.fragment_integration_submit_order_layout

    override fun initView(view: View?) {

        subject = RxBus.getDefault().toObservable(AddressBean::class.java).subscribe {
//            mTvOrderPersonName.text = it.name
//            mTvOrderPersonPhone.text = it.phone
//            mTvOrderPersonDetail.text = it.detail
        }

        mTvSubmitOrder.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付完成", true, false, PayResultFragment::class.java)
        }
        mLlSubmitOrderAddress.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
        }
    }


    override fun onDestroyView() {
        if (!subject.isUnsubscribed) {
            subject.unsubscribe()
        }
        super.onDestroyView()
    }
}