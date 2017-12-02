package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import kotlinx.android.synthetic.main.fragment_order_info_layout.*

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderInfoFragment : BaseNetWorkingFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_order_info_layout

    override fun initView(view: View?) {
        mTvOrderInfoTopState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_success, 0)
        mBtOrderInfoExpress.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "物流信息", true, false, ExpressInfoFragment::class.java)
        }
        mBtOrderInfoComment.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品评价", true, false, OrderCommentSubmitFragment::class.java)
        }
    }
}