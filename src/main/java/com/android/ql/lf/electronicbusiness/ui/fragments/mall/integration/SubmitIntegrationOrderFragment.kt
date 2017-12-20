package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.data.IMallGoodsItemBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AddressSelectFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_integration_submit_order_layout.*
import kotlinx.android.synthetic.main.layout_submit_new_order_header_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class SubmitIntegrationOrderFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_FLAG = "goods_flag"
    }

    override fun getLayoutId() = R.layout.fragment_integration_submit_order_layout

    private var addressBean: AddressBean? = null

    private val itemBean: IMallGoodsItemBean by lazy {
        arguments.classLoader = this.javaClass.classLoader
        arguments.getParcelable<IMallGoodsItemBean>(GOODS_FLAG)
    }


    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(AddressBean::class.java).subscribe {
            addressBean = it
            if (addressBean != null) {
                setAddress()
            }
        }

        mTvSubmitOrder.setOnClickListener {
            if (addressBean != null) {
                mPresent.getDataByPost(0x1,
                        RequestParamsHelper.ORDER_MODEL,
                        RequestParamsHelper.ACT_ADD_JORDER,
                        RequestParamsHelper.getAddJorderParam(itemBean.jproduct_id, itemBean.jproduct_price, addressBean!!.address_id))
            } else {
                toast("请先选择地址")
            }
        }

        mBtSubmitOrderHeaderNoAddress.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
        }
        mLlSubmitOrderAddress.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
        }

        GlideManager.loadImage(mContext, if (itemBean.jproduct_pic.isEmpty()) "" else itemBean.jproduct_pic[0], mIvIntegrationSubmitOrderGoodsPic)
        mTvIntegrationSubmitOrderGoodsName.text = itemBean.jproduct_name
        mTvIntegrationSubmitOrderGoodsPrice.text = "￥ ${itemBean.jproduct_price}"
        mTvIntegrationSubmitOrderPrice.text = "￥ ${itemBean.jproduct_price}"

        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_DEFAULT_ADDRESS, RequestParamsHelper.getDefaultAddress())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(context)
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                addressBean = Gson().fromJson(json.optJSONObject("result").toString(), AddressBean::class.java)
                if (addressBean != null) {
                    setAddress()
                } else {
                    showEmptyAddress()
                }
            } else {
                showEmptyAddress()
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                toast("恭喜，兑换成功")
                UserInfo.getInstance().memberIntegral = json.optString("arr")
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "兑换完成", true, false, bundleOf(Pair(IntegrationMallGoodsInfoFragment.GOODS_ID_FLAG, 0)), IntegrationPayResultFragment::class.java)
                finish()
            } else {
                exchangeFailed()
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            exchangeFailed()
        }
    }

    private fun exchangeFailed() {
        toast("抱歉，兑换失败，请稍后重试……")
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "兑换完成", true, false, bundleOf(Pair(IntegrationMallGoodsInfoFragment.GOODS_ID_FLAG, 1)), IntegrationPayResultFragment::class.java)
        finish()
    }


    private fun showEmptyAddress() {
        mLlSubmitOrderAddress.visibility = View.GONE
        mBtSubmitOrderHeaderNoAddress.visibility = View.VISIBLE

    }

    private fun setAddress() {
        mLlSubmitOrderAddress.visibility = View.VISIBLE
        mBtSubmitOrderHeaderNoAddress.visibility = View.GONE
        mIvSubmitOrderAddressName.text = "收货人：${addressBean!!.address_name}"
        mIvSubmitOrderAddressPhone.text = addressBean!!.address_phone
        mIvSubmitOrderAddressDetail.text = "${addressBean!!.address_addres}  ${addressBean!!.address_detail}"
    }


}