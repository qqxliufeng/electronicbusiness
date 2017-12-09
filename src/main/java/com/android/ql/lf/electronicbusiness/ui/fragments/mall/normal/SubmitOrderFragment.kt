package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AddressSelectFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_submit_order_layout.*
import rx.Subscription

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
@Deprecated("已经过期了，请使用新的类")
class SubmitOrderFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var addressBean: AddressBean? = null

    private lateinit var goodsItem: ShoppingCarItemBean

    private lateinit var subscription: Subscription

    override fun getLayoutId() = R.layout.fragment_submit_order_layout

    override fun initView(view: View?) {
//        arguments.classLoader = this.javaClass.classLoader
//        goodsItem = arguments.getParcelable(GOODS_ID_FLAG)
//
//        subscription = RxBus.getDefault().toObservable(AddressBean::class.java).subscribe {
//            addressBean = it
//            if (addressBean != null) {
//                setAddress()
//            }
//        }
//
//        //填充数据
//        GlideManager.loadImage(mContext, goodsItem.shopcart_pic[0], mIvSubmitOrderGoodsPic)
//        mIvSubmitOrderGoodsName.text = goodsItem.shopcart_name
//        mIvSubmitOrderGoodsSpe.text = goodsItem.shopcart_specification
//        mIvSubmitOrderGoodsPrice.text = "￥ ${goodsItem.shopcart_price}"
//        mIvSubmitOrderGoodsNum.text = "X ${goodsItem.shopcart_num}"
//        mIvSubmitOrderGoodsKType.text = when (goodsItem.shopcart_ktype) {
//            "1" -> {
//                mIvSubmitOrderGoodsKType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
//                "拇指斗价团体砍"
//            }
//            "2" -> {
//                mIvSubmitOrderGoodsKType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
//                "拇指斗价个人砍"
//            }
//            "3" -> {
//                mIvSubmitOrderGoodsKType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
//                "拇指斗价会员专享"
//            }
//            else -> {
//                ""
//            }
//        }
//
//        mCbALiPay.setOnCheckedChangeListener { _, isChecked ->
//            mCbWX.isChecked = !isChecked
//        }
//        mCbWX.setOnCheckedChangeListener { _, isChecked ->
//            mCbALiPay.isChecked = !isChecked
//        }
//        mRlSubmitOrderWXContainer.setOnClickListener {
//            mCbWX.isChecked = true
//        }
//        mRlSubmitOrderAliPayContainer.setOnClickListener {
//            mCbALiPay.isChecked = true
//        }
//        mTvSubmitOrder.setOnClickListener {
//            FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付完成", true, false, PayResultFragment::class.java)
//        }
//        mLlSubmitOrderAddress.setOnClickListener {
//            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_DEFAULT_ADDRESS, RequestParamsHelper.getDefaultAddress())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            progressDialog = MyProgressDialog(mContext, "正在加载……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            addressBean = Gson().fromJson(json.optJSONObject("result").toString(), AddressBean::class.java)
            if (addressBean != null) {
                setAddress()
            }
        }
    }

    private fun setAddress() {
//        mIvSubmitOrderAddressName.text = "收货人：${addressBean!!.address_name}"
//        mIvSubmitOrderAddressPhone.text = addressBean!!.address_phone
//        mIvSubmitOrderAddressDetail.text = "${addressBean!!.address_addres}  ${addressBean!!.address_detail}"
    }


    override fun onDestroyView() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroyView()
    }

}