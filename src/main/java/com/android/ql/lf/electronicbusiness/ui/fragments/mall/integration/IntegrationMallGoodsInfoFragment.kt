package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.os.Bundle
import android.text.Html
import android.text.TextPaint
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IMallGoodsItemBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_integration_mall_goods_info_layout.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class IntegrationMallGoodsInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var itemBean: IMallGoodsItemBean? = null

    override fun getLayoutId(): Int = R.layout.fragment_integration_mall_goods_info_layout

    override fun initView(view: View?) {
        mIvIntegrationMallDetailExchange.setOnClickListener {
            if (itemBean != null) {
                if (itemBean!!.jproduct_price.toInt() > UserInfo.getInstance().memberIntegral.toInt()) {
                    toast("当前积分不够哦~")
                } else {
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitIntegrationOrderFragment::class.java)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_JPRODUCT_DETAIL, RequestParamsHelper.getJproductDetail(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载详情……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            mIvIntegrationMallDetailExchange.isEnabled = true
            itemBean = Gson().fromJson(json.optJSONObject("result").toString(), IMallGoodsItemBean::class.java)
            if (itemBean != null) {
                GlideManager.loadImage(mContext, itemBean!!.jproduct_pic[0], mIvIntegrationMallDetailPic)
                mTvIntegrationMallDetailName.text = itemBean!!.jproduct_name
                mIvIntegrationMallDetailPrice.text = itemBean!!.jproduct_price
                mIvIntegrationMallDetailOldPrice.paint.flags = TextPaint.ANTI_ALIAS_FLAG
                mIvIntegrationMallDetailOldPrice.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
                mIvIntegrationMallDetailOldPrice.text = "￥ ${itemBean!!.jproduct_yprice}"
                mIvIntegrationMallDetailContent.text = Html.fromHtml(itemBean!!.jproduct_content)
            }
        } else {
            mIvIntegrationMallDetailExchange.isEnabled = false
        }
    }
}