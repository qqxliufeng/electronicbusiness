package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.CutGoodsInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.TeamCutItemInfoFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_order_info_layout.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val ORDER_INFO_ID_FLAG = "order_info_id_flag"
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.MEMBER_MODEL,
                RequestParamsHelper.ACT_ORDER_DETAIL,
                RequestParamsHelper.getOrderDetailParam(arguments.getString(ORDER_INFO_ID_FLAG, "")))
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
            val orderInfo = Gson().fromJson(json.optJSONObject("result").toString(), OrderInfoBean::class.java)
            if (orderInfo != null) {
                mTvOrderInfoTopState.text = when (orderInfo.order_token) {
                    "0" -> {
                        mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dfk, 0)
                        "待付款"
                    }
                    "1" -> {
                        mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dfh, 0)
                        "待发货"
                    }
                    "2" -> {
                        mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dsh, 0)
                        "待收货"
                    }
                    "3" -> {
                        "待评价"
                    }
                    "4" -> {
                        mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_success, 0)
                        "完成"
                    }
                    "5" -> {
                        "已取消"
                    }
                    "6" -> {
                        "已退款"
                    }
                    else -> {
                        "已退款"
                    }
                }
                mTvShoppingCarItemEditMode.text = mTvOrderInfoTopState.text
                mTvOrderInfoDetailPersonName.text = "收货人：${orderInfo.address_name}"
                mTvOrderInfoDetailPersonPhone.text = orderInfo.address_phone
                mTvOrderInfoDetailPersonDetail.text = "${orderInfo.address_addres} ${orderInfo.address_detail}"
                GlideManager.loadImage(context, orderInfo.product_pic, mIvOrderInfoDetailGoodsPic)
                mTvOrderInfoDetailGoodsTitle.text = orderInfo.product_name
                mTvOrderInfoDetailGoodsPrice.text = "￥ ${orderInfo.product_price}"
                mTvOrderInfoDetailGoodsNum.text = "X ${orderInfo.order_num}"

                mTvOrderInfoDetailGoodsAllPrice.text = "￥ ${orderInfo.order_oprice}"
                mTvOrderInfoDetailGoodsExpressPrice.text = "￥ ${orderInfo.product_mdprice}"
                mTvOrderInfoDetailOrderAllPrice.text = "￥ ${orderInfo.order_oprice.toFloat() + orderInfo.product_mdprice.toFloat()}"

                mTvOrderInfoDetailGoodsSpecification.text = orderInfo.order_specification
                mTvOrderInfoDetailKtype.text = when (orderInfo.product_ktype) {
                    "1" -> {
                        mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
                        "拇指斗价个人砍"
                    }
                    "2" -> {
                        mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
                        "拇指斗价团体砍"
                    }
                    "3" -> {
                        mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
                        "拇指斗价会员专享"
                    }
                    else -> {
                        "其它"
                    }
                }
                mTvOrderInfoDetailOrderSN.text = "订单编号：${orderInfo.order_sn}"
                mTvOrderInfoDetailOrderCTime.text = "创建时间：${orderInfo.order_ctime}"
                mTvOrderInfoDetailOrderFTime.text = "付款时间：${orderInfo.order_ftime}"
                mTvOrderInfoDetailOrderHTime.text = "发货时间：${orderInfo.order_htime}"
                mTvOrderInfoDetailOrderFinTime.text = "完成时间：${orderInfo.order_fintime}"
                mTvOrderInfoDetailOrderCopySN.setOnClickListener {
                    val clipBoardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipBoardManager.text = orderInfo.order_sn
                    toast("复制成功")
                }
                mLlOrderInfoDetailGoodsContainer.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(CutGoodsInfoFragment.GOODS_ID_FLAG, orderInfo.product_id)
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, TeamCutItemInfoFragment::class.java)
                }
            }
        }
    }

    class OrderInfoBean {
        lateinit var product_id: String
        lateinit var product_name: String
        lateinit var product_pic: String
        lateinit var product_hname: String
        lateinit var product_price: String
        lateinit var product_mdprice: String//运费
        lateinit var product_md: String //是否是包邮
        lateinit var order_num: String
        lateinit var order_oprice: String
        lateinit var order_fc: String
        lateinit var order_token: String
        lateinit var order_id: String
        lateinit var order_ctime: String
        var order_ftime: String? = "暂无"
        var order_htime: String? = "暂无"
        var order_fintime: String? = "暂无"
        lateinit var address_name: String
        lateinit var address_phone: String
        lateinit var address_addres: String
        lateinit var address_detail: String
        lateinit var order_sn: String
        lateinit var product_ktype: String
        var order_specification: String? = "暂无"
    }

}