package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextPaint
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.WXPayBean
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.Constants
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.fragment_vip_info_layout.*

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class VipInfoFragment : BaseNetWorkingFragment() {

    private lateinit var wxApi: IWXAPI

    override fun getLayoutId(): Int = R.layout.fragment_vip_info_layout

    override fun initView(view: View?) {
        wxApi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID, true)
        mVipOldMoney.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mVipOldMoney.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        mCbWC.isChecked = true
        mCbWC.setOnCheckedChangeListener { _, isChecked ->
            mCbALiPay.isChecked = !isChecked
        }
        mCbALiPay.setOnCheckedChangeListener { _, isChecked ->
            mCbWC.isChecked = !isChecked
        }

        mTvVipInfoPay.setOnClickListener {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_BBS,
                    RequestParamsHelper.getBBSParam("wxpay", "0.01"))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(context, "正在支付……")
        progressDialog.show()
    }


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            val wxBean = Gson().fromJson(json.optJSONObject("result").toString(), WXPayBean::class.java)
            val req = PayReq()
            req.appId = wxBean.appid
            req.partnerId = wxBean.partnerid
            req.prepayId = wxBean.prepayid
            req.nonceStr = wxBean.noncestr
            req.timeStamp = wxBean.timestamp
            req.packageValue = "Sign=WXPay"


//            val list = LinkedList<Constants.WXModel>()
//            list.add(Constants.WXModel("appid", req.appId))
//            list.add(Constants.WXModel("noncestr", req.nonceStr))
//            list.add(Constants.WXModel("package", req.packageValue))
//            list.add(Constants.WXModel("partnerid", req.partnerId))
//            list.add(Constants.WXModel("prepayid", req.prepayId))
//            list.add(Constants.WXModel("timestamp", req.timeStamp))

            req.sign = wxBean.sign
            req.extData = "app data"
            wxApi.registerApp(Constants.WX_APP_ID)
            wxApi.sendReq(req)
        }
    }
}