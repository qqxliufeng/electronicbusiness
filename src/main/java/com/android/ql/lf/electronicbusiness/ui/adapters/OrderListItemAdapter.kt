package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderListItemAdapter(layoutId: Int, list: ArrayList<MyOrderBean>) : BaseQuickAdapter<MyOrderBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: MyOrderBean?) {
        helper!!.addOnClickListener(R.id.mBtOrderListItemAction1)
        helper.addOnClickListener(R.id.mBtOrderListItemAction2)
        val iv_pic = helper.getView<ImageView>(R.id.mIvOrderListItemPic)
        GlideManager.loadImage(iv_pic.context, if (item!!.product_pic.isEmpty()) "" else item.product_pic[0], iv_pic)
        helper.setText(R.id.mTvOrderListItemTitle, item.product_name)
        helper.setText(R.id.mTvOrderListItemPrice, "￥ ${item.product_price}")
        helper.setText(R.id.mIvOrderListItemNum, "X ${item.order_num}")
        helper.setText(R.id.mTvOrderListItemInfo, "共${item.order_num}件商品 合计 ￥${item.order_oprice} 元（含运费￥${item.order_mdprice}元）")
        helper.setText(R.id.mTvOrderListItemSpecification, item.order_specification)
        val tv_action1 = helper.getView<TextView>(R.id.mBtOrderListItemAction1)
        helper.addOnClickListener(R.id.mBtOrderListItemAction1)
        val tv_action2 = helper.getView<TextView>(R.id.mBtOrderListItemAction2)
        helper.addOnClickListener(R.id.mBtOrderListItemAction2)
        val tv_k_type = helper.getView<TextView>(R.id.mTvOrderListItemKType)
        when (item.product_ktype) {
            OrderPresent.GoodsType.PERSONAL_CUT_GOODS -> { //个人砍
                tv_k_type.text = "拇指斗价个人砍"
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
            }
            OrderPresent.GoodsType.TEAM_CUT_GOODS -> { //团体砍
                tv_k_type.text = "拇指斗价团体砍"
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
            }
            OrderPresent.GoodsType.VIP_GOODS -> { //会员专享
                tv_k_type.text = "拇指斗价会员专享"
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
            }
        }
        helper.setText(R.id.mTvShoppingCarItemEditMode, when (OrderPresent.getOrderStatus(item.order_token)) {
            OrderPresent.OrderStatus.STATUS_OF_DFK -> {
                tv_action1.visibility = View.VISIBLE
                tv_action2.visibility = View.VISIBLE
                tv_action1.text = "取消订单"
                tv_action2.text = "付款"
                "待付款"
            }
            OrderPresent.OrderStatus.STATUS_OF_DFH -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.VISIBLE
                tv_action2.text = "申请退款"
                "待发货"
            }
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
                tv_action1.visibility = View.VISIBLE
                tv_action2.visibility = View.VISIBLE
                tv_action2.text = "确认收货"
                tv_action1.text = "查看物流"
                "待收货"
            }
            OrderPresent.OrderStatus.STATUS_OF_DPJ -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.VISIBLE
                tv_action2.text = "去评价"
                "待评价"
            }
            OrderPresent.OrderStatus.STATUS_OF_SQTK -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.GONE
                "退款中……"
            }
            OrderPresent.OrderStatus.STATUS_OF_CANCEL -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.GONE
                "已取消"
            }
            OrderPresent.OrderStatus.STATUS_OF_BACK -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.GONE
                "已退款"
            }
            OrderPresent.OrderStatus.STATUS_OF_YPJ -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.GONE
                "已评价"
            }
            else -> {
                tv_action1.visibility = View.GONE
                tv_action2.visibility = View.GONE
                ""
            }
        })
    }
}