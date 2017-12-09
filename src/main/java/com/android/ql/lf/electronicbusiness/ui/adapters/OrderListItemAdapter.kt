package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
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
        GlideManager.loadImage(iv_pic.context, if (item!!.product_pic.isEmpty()) {
            ""
        } else {
            item.product_pic[0]
        }, iv_pic)
        helper.setText(R.id.mTvOrderListItemTitle, item.product_name)
        helper.setText(R.id.mTvOrderListItemPrice, "￥ ${item.product_price}")
        helper.setText(R.id.mIvOrderListItemNum, "X ${item.order_num}")
        helper.setText(R.id.mTvOrderListItemInfo, "共${item.order_num}件商品 合计 ￥${item.order_oprice} 元（含运费￥${item.product_mdprice}元）")
        helper.setText(R.id.mTvOrderListItemSpecification, item.order_specification)
        val tv_action1 = helper.getView<TextView>(R.id.mBtOrderListItemAction1)
        helper.addOnClickListener(R.id.mBtOrderListItemAction1)
        val tv_action2 = helper.getView<TextView>(R.id.mBtOrderListItemAction2)
        helper.addOnClickListener(R.id.mBtOrderListItemAction2)
        helper.setText(R.id.mTvShoppingCarItemEditMode, when (item.order_token) {
            "0" -> {
                tv_action1.text = "取消订单"
                tv_action2.text = "付款"
                "待付款"
            }
            "1" -> {
                tv_action1.visibility = View.GONE
                tv_action2.text = "申请退款"
                "待发货"
            }
            "2" -> {
                "待收货"
            }
            "3" -> {
                "待评价"
            }
            "4" -> {
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
        })
    }
}