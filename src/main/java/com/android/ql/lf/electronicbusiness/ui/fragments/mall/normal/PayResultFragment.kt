package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CreateOrderSuccessBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.OrderInfoFragment
import com.android.ql.lf.electronicbusiness.utils.PreferenceUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_pay_result_layout.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class PayResultFragment : BaseFragment() {

    companion object {
        val PAY_ORDER_RESULT_JSON_FLAG = "create_order_result_json"
        val PAY_CODE_FLAG = "code"
        val PAY_SUCCESS_CODE = 0
        val PAY_FAIL_CODE = -1

        fun newInstance(code: Int): PayResultFragment {
            val fragment = PayResultFragment()
            val bundle = Bundle()
            bundle.putInt(PAY_CODE_FLAG, code)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_pay_result_layout

    override fun initView(view: View?) {
        mBtBack.setOnClickListener {
            finish()
        }
        if (arguments != null) {
            when (arguments.getInt(PAY_CODE_FLAG)) {
                PAY_SUCCESS_CODE -> {
                    mLlPayResultOrderInfoContainer.visibility = View.VISIBLE
                    mTvPayResultTitle.text = "支付成功"
                    mBtBack.text = "查看订单详情"
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_success, 0, 0, 0)
                    val json = PreferenceUtils.getPrefString(mContext, PAY_ORDER_RESULT_JSON_FLAG, "")
                    if (!TextUtils.isEmpty(json)) {
                        val createOrderSuccessBean = Gson().fromJson(json, CreateOrderSuccessBean::class.java)
                        mBtBack.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString(OrderInfoFragment.ORDER_INFO_ID_FLAG, createOrderSuccessBean.osn)
                            FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付结果", true, false, bundle, OrderInfoFragment::class.java)
                            finish()
                        }
                        mTvPayResultOrderSn.text = createOrderSuccessBean.osn
                        mTvPayResultOrderPrice.text = "${createOrderSuccessBean.price}元"
                        mTvPayResultOrderPayPrice.text = "${createOrderSuccessBean.price}元"
                        mTvPayResultOrderTime.text = createOrderSuccessBean.paytime
                        mLlPayResultContainer.removeAllViews()
                        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, mContext.resources.displayMetrics).toInt()
                        createOrderSuccessBean.p_order.forEach {
                            val textView = TextView(mContext)
                            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView.setPadding(padding, padding, padding, padding)
                            textView.compoundDrawablePadding = padding
                            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv_color))
                            textView.text = it.name
                            when (it.ktype) {
                                "1" -> {
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
                                }
                                "2" -> {
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
                                }
                                "3" -> {
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
                                }
                            }
                            mLlPayResultContainer.addView(textView)
                        }
                    }
                }
                PAY_FAIL_CODE -> {
                    mLlPayResultOrderInfoContainer.visibility = View.GONE
                    mTvPayResultTitle.text = "支付失败"
                    mBtBack.text = "立即返回"
                    mBtBack.setOnClickListener {
                        finish()
                    }
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_fail, 0, 0, 0)
                }
            }
        }
    }
}