package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.CutGoodsInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.TeamCutItemInfoFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.SelectPayTypeView
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import com.hyphenate.helpdesk.model.OrderInfo
import kotlinx.android.synthetic.main.fragment_order_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val ORDER_INFO_ID_FLAG = "order_info_id_flag"
    }

    private val orderPresent by lazy {
        OrderPresent(mPresent)
    }

    private var orderInfo: OrderInfoBean? = null
    private var bottomPayDialog: BottomSheetDialog? = null


    override fun getLayoutId(): Int = R.layout.fragment_order_info_layout

    override fun initView(view: View?) {
        mTvOrderInfoTopState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_success, 0)
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
        when (requestID) {
            0x0 -> {
                if (json != null) {
                    orderInfo = Gson().fromJson(json.optJSONObject("result").toString(), OrderInfoBean::class.java)
                    if (orderInfo != null) {
                        bindData()
                    }
                }
            }
            0x1 -> { //取消订单
                if (json != null) {
                    OrderPresent.notifyRefreshOrderList()
                    OrderPresent.notifyRefreshOrderNum()
                    finish()
                }
            }
            0x2 -> { //付款

            }
            0x4->{
                if (json != null) {
                    OrderPresent.notifyRefreshOrderList()
                    OrderPresent.notifyRefreshOrderNum()
                    finish()
                }
            }
        }
    }

    private fun bindData() {
        mTvOrderInfoTopState.text = when (orderInfo!!.order_token.toInt()) {
            OrderPresent.OrderStatus.STATUS_OF_DFK -> {
                mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dfk, 0)
                mBtOrderInfoAction1.visibility = View.VISIBLE
                mBtOrderInfoAction1.text = resources.getString(R.string.order_status_cancel_str)
                mBtOrderInfoAction1.setOnClickListener {
                    //取消订单
                    AlertDialog.Builder(context).setMessage("是否要取消订单？").setPositiveButton("是") { _, _ ->
                        orderPresent.cancelOrder(0x1, orderInfo!!.order_id)
                    }.setNegativeButton("不了", null).create().show()
                }
                mBtOrderInfoAction2.visibility = View.VISIBLE
                mBtOrderInfoAction2.text = resources.getString(R.string.order_status_pay_str)
                mBtOrderInfoAction2.setOnClickListener {
                    if (bottomPayDialog == null) {
                        bottomPayDialog = BottomSheetDialog(mContext)
                        val contentView = SelectPayTypeView(mContext)
                        contentView.setShowConfirmView(View.VISIBLE)
                        contentView.setOnConfirmClickListener {
                            bottomPayDialog!!.dismiss()
                            mPresent.getDataByPost(0x2, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_PAY,
                                    RequestParamsHelper.getPayParam(orderInfo!!.order_id, orderInfo!!.product_id, contentView.payType))
                        }
                        bottomPayDialog!!.setContentView(contentView)
                    } else {
                        bottomPayDialog!!.show()
                    }
                }
                resources.getString(R.string.order_status_dfk_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_DFH -> {
                mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dfh, 0)
                mBtOrderInfoAction1.visibility = View.GONE
                mBtOrderInfoAction2.visibility = View.VISIBLE
                mBtOrderInfoAction2.text = resources.getString(R.string.order_status_sqtk_str)
                mBtOrderInfoAction2.setOnClickListener {
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "申请退款", true, false, bundleOf(Pair("oid", orderInfo!!.order_id)), RefundFragment::class.java)
                }
                resources.getString(R.string.order_status_dfh_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
                mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_dsh, 0)
                mBtOrderInfoAction1.visibility = View.VISIBLE
                mBtOrderInfoAction1.text = resources.getString(R.string.order_status_express_str)
                mBtOrderInfoAction1.setOnClickListener {
                    val myOrder = MyOrderBean()
                    myOrder.product_pic = orderInfo!!.product_pic
                    myOrder.product_name = orderInfo!!.product_name
                    myOrder.order_tn = orderInfo!!.order_tn
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "快递信息", true, false,
                            bundleOf(Pair(ExpressInfoFragment.ORDER_BEAN_FLAG, myOrder)),
                            ExpressInfoFragment::class.java)
                }
                mBtOrderInfoAction2.visibility = View.VISIBLE
                mBtOrderInfoAction2.text = resources.getString(R.string.order_status_confirm_str)
                mBtOrderInfoAction2.setOnClickListener {
                    AlertDialog.Builder(mContext).setMessage("是否确认收货？").setPositiveButton("是") { _, _ ->
                        orderPresent.confirmGoods(0x4, orderInfo!!.order_id)
                    }.setNegativeButton("再等等", null).create().show()
                }
                resources.getString(R.string.order_status_dsh_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_DPJ -> {
                mBtOrderInfoAction1.visibility = View.GONE
                mBtOrderInfoAction2.visibility = View.VISIBLE
                mBtOrderInfoAction2.text = resources.getString(R.string.order_status_go_comment_str)
                mBtOrderInfoAction2.setOnClickListener {
                    //去评价
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品评价", true, false,
                            bundleOf(Pair(OrderCommentSubmitFragment.ORDER_ID_FLAG, orderInfo!!.order_id),
                                    Pair(OrderCommentSubmitFragment.PRODUCT_ID_FLAG, orderInfo!!.product_id)),
                            OrderCommentSubmitFragment::class.java)
                }
                resources.getString(R.string.order_status_dpj_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_FINISH -> {
                mTvOrderInfoTopState.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.img_pic_goods_info_success, 0)
                resources.getString(R.string.order_status_dfinish_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_CANCEL -> {
                resources.getString(R.string.order_status_dcancel_str)
            }
            OrderPresent.OrderStatus.STATUS_OF_BACK -> {
                resources.getString(R.string.order_status_dback_str)
            }
            else -> {
                "已退款"
            }
        }
        mTvShoppingCarItemEditMode.text = mTvOrderInfoTopState.text
        mTvOrderInfoDetailPersonName.text = "收货人：${orderInfo!!.address_name}"
        mTvOrderInfoDetailPersonPhone.text = orderInfo!!.address_phone
        mTvOrderInfoDetailPersonDetail.text = "${orderInfo!!.address_addres} ${orderInfo!!.address_detail}"
        GlideManager.loadImage(context, if (orderInfo!!.product_pic.isEmpty()) "" else orderInfo!!.product_pic[0], mIvOrderInfoDetailGoodsPic)
        mTvOrderInfoDetailGoodsTitle.text = orderInfo!!.product_name
        mTvOrderInfoDetailGoodsPrice.text = "￥ ${orderInfo!!.product_price}"
        mTvOrderInfoDetailGoodsNum.text = "X ${orderInfo!!.order_num}"

        mTvOrderInfoDetailGoodsAllPrice.text = "￥ ${orderInfo!!.order_oprice}"
        mTvOrderInfoDetailGoodsExpressPrice.text = "￥ ${orderInfo!!.product_mdprice}"
        mTvOrderInfoDetailOrderAllPrice.text = "￥ ${orderInfo!!.order_oprice.toFloat() + orderInfo!!.product_mdprice.toFloat()}"

        mTvOrderInfoDetailGoodsSpecification.text = orderInfo!!.order_specification
        mTvOrderInfoDetailKtype.text = when (orderInfo!!.product_ktype) {
            OrderPresent.GoodsType.PERSONAL_CUT_GOODS -> {
                mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
                "拇指斗价个人砍"
            }
            OrderPresent.GoodsType.TEAM_CUT_GOODS -> {
                mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
                "拇指斗价团体砍"
            }
            OrderPresent.GoodsType.VIP_GOODS -> {
                mTvOrderInfoDetailKtype.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
                "拇指斗价会员专享"
            }
            else -> {
                "其它"
            }
        }
        mTvOrderInfoDetailOrderSN.text = "订单编号：${orderInfo!!.order_sn}"
        mTvOrderInfoDetailOrderCTime.text = "创建时间：${orderInfo!!.order_ctime}"

        if (TextUtils.isEmpty(orderInfo!!.order_ftime) || "null" == orderInfo!!.order_ftime) {
            mTvOrderInfoDetailOrderFTime.visibility = View.GONE
        } else {
            mTvOrderInfoDetailOrderFTime.text = "付款时间：${orderInfo!!.order_ftime}"
        }

        if (TextUtils.isEmpty(orderInfo!!.order_ftime) || "null" == orderInfo!!.order_htime) {
            mTvOrderInfoDetailOrderHTime.visibility = View.GONE
        } else {
            mTvOrderInfoDetailOrderHTime.text = "发货时间：${orderInfo!!.order_htime}"
        }
        if (TextUtils.isEmpty(orderInfo!!.order_ftime) || "null" == orderInfo!!.order_fintime) {
            mTvOrderInfoDetailOrderFinTime.visibility = View.GONE
        } else {
            mTvOrderInfoDetailOrderFinTime.text = "完成时间：${orderInfo!!.order_fintime}"
        }
        mTvOrderInfoDetailOrderCopySN.setOnClickListener {
            val clipBoardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipBoardManager.text = orderInfo!!.order_sn
            toast("复制成功")
        }
        mLlOrderInfoDetailGoodsContainer.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CutGoodsInfoFragment.GOODS_ID_FLAG, orderInfo!!.product_id)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, TeamCutItemInfoFragment::class.java)
        }
    }

    class OrderInfoBean : MyOrderBean(){
        lateinit var address_name: String
        lateinit var address_phone: String
        lateinit var address_addres: String
        lateinit var address_detail: String
    }
}