package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.NestedScrollView
import android.text.Html
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.VipSelectPayTimeBean
import com.android.ql.lf.electronicbusiness.data.WXPayBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PayResultFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.SelectPayTypeView
import com.android.ql.lf.electronicbusiness.ui.views.VipPayTypeView
import com.android.ql.lf.electronicbusiness.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_vip_info_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class VipInfoFragment : BaseNetWorkingFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_vip_info_layout

    private var vipPaySelectTimeViewList = arrayListOf<VipPayTypeView>()

    private var currentVipPayTimeBean: VipSelectPayTimeBean? = null

    private var isRefreshStatus = false

    private val handle = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                PayManager.SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    val bundle = Bundle()
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_SUCCESS_CODE)
                    } else {
                        //支付失败
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_FAIL_CODE)
                    }
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付结果", true, false, bundle, PayResultFragment::class.java)
                }
            }
        }
    }

    override fun initView(view: View?) {
        setUserInfo()
        mTvVipInfoPay.setOnClickListener {
            if (currentVipPayTimeBean == null) {
                toast("请先选择一种VIP时间类型")
                return@setOnClickListener
            }
            mNSVVipInfoContainer.fullScroll(NestedScrollView.FOCUS_DOWN)
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_BBS,
                    RequestParamsHelper.getBBSParam(mSptvContainer.payType, currentVipPayTimeBean!!.m_p_price))
        }
    }

    private fun setUserInfo() {
        GlideManager.loadFaceCircleImage(mContext, UserInfo.getInstance().memberPic, mVipFace)
        mVipName.text = UserInfo.getInstance().memberName
        mVipTime.movementMethod = LinkMovementMethod.getInstance()
        val spanStr = if (UserInfo.getInstance().memberRank == "1") {
            mVipName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_vip_s, 0)
            SpannableString("VIP会员于${UserInfo.getInstance().memberMtime}到期，立即续费")
        } else {
            SpannableString("您当前暂不是VIP会员，立即开通")
        }
        spanStr.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View?) {
                mNSVVipInfoContainer.fullScroll(NestedScrollView.FOCUS_DOWN)
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds!!.isUnderlineText = false
            }
        }, spanStr.indexOf("立"), spanStr.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        mVipTime.text = spanStr
    }

    override fun onResume() {
        super.onResume()
        if (isRefreshStatus) {
            isRefreshStatus = false
            mPresent.getDataByPost(0x2, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MEMBERVIP, RequestParamsHelper.getMemberVipParams())
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_M_P, RequestParamsHelper.getVipInfoParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID != 0x2) {
            progressDialog = MyProgressDialog(context, if (requestID == 0x0) "正在支付……" else "正在加载……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                isRefreshStatus = true
                PreferenceUtils.setPrefString(mContext, PayResultFragment.PAY_ORDER_RESULT_JSON_FLAG, "")
                if (mSptvContainer.payType == SelectPayTypeView.WX_PAY) {
                    val wxBean = Gson().fromJson(json.optJSONObject("result").toString(), WXPayBean::class.java)
                    PayManager.wxPay(mContext, wxBean)
                } else {
                    PayManager.aliPay(mContext, handle, json.optString("result"))
                }
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                val jsonArray = json.optJSONArray("result")
                mTvVipInfoPrivilege.text = Html.fromHtml(json.optJSONObject("arr").optString("ptgg_content"))
                (0 until jsonArray.length()).forEach {
                    val vipSelectTimeBean = Gson().fromJson(jsonArray.optJSONObject(it).toString(), VipSelectPayTimeBean::class.java)
                    val vipPayTypeView = VipPayTypeView(mContext)
                    vipPayTypeView.bindData(vipSelectTimeBean)
                    mLlVipInfoSelectPayTimeContainer.addView(vipPayTypeView)
                    vipPaySelectTimeViewList.add(vipPayTypeView)
                    vipPayTypeView.setOnClickListener { view ->
                        vipPaySelectTimeViewList.forEach {
                            val isChecked = view == it
                            it.onSelect(isChecked)
                            if (isChecked) {
                                currentVipPayTimeBean = it.vipSelectPayTimeBean
                                mTvVipInfoPayPrice.text = vipSelectTimeBean.m_p_price.toFloat().toString()
                            }
                        }
                    }
                }
            }
        }else if(requestID == 0x2){
            if (json!=null){
                parseUserInfo(json.optJSONObject("result"))
                setUserInfo()
            }
        }
    }


    private fun parseUserInfo(userJson: JSONObject?) {
        UserInfo.getInstance().memberId = userJson!!.optString("member_id")
        UserInfo.getInstance().memberName = userJson.optString("member_name")
        UserInfo.getInstance().memberPhone = userJson.optString("member_phone")
        UserInfo.getInstance().memberRank = userJson.optString("member_rank")
        UserInfo.getInstance().memberSex = userJson.optString("member_sex")
        UserInfo.getInstance().memberMtime = userJson.optString("member_mtime")
        UserInfo.getInstance().memberIntegral = userJson.optString("member_integral")
        UserInfo.getInstance().memberForm = userJson.optString("member_form")
        UserInfo.getInstance().memberAddress = userJson.optString("member_address")
        UserInfo.getInstance().memberPic = userJson.optString("member_pic")
        UserInfo.getInstance().member_hxname = userJson.optString("member_hxname")
        UserInfo.getInstance().member_hxpw = userJson.optString("member_hxpw")
    }
}