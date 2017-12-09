package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class ShoppingCarItemAdapter(layoutId: Int, list: ArrayList<ShoppingCarItemBean>) : BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: ShoppingCarItemBean?) {
        helper!!.addOnClickListener(R.id.mTvShoppingCarItemEditMode)
        helper.addOnClickListener(R.id.mIvShoppingCarItemSelector)
        helper.addOnClickListener(R.id.mTvShoppingCarItemEditDel)
        helper.addOnClickListener(R.id.mTvShoppingCarItemDelCount)
        helper.addOnClickListener(R.id.mTvShoppingCarItemAddCount)
        val ivSelector = helper.getView<ImageView>(R.id.mIvShoppingCarItemSelector)
        val tvEditorMode = helper.getView<TextView>(R.id.mTvShoppingCarItemEditMode)
        val llInfoContainer = helper.getView<LinearLayout>(R.id.mLlShoppingCarItemInfoContainer)
        val rlEditContainer = helper.getView<RelativeLayout>(R.id.mRlShoppingCarItemEditContainer)
        val tvEditCount = helper.getView<TextView>(R.id.mTvShoppingCarItemEditCount)
        if (item!!.isSelector) {
            ivSelector?.setImageResource(R.drawable.img_shopping_car_selector_icon)
        } else {
            ivSelector?.setImageResource(R.drawable.img_shopping_car_unselector_icon)
        }
        if (item.isEditorMode) {
            tvEditorMode?.text = "完成"
            llInfoContainer?.visibility = View.GONE
            rlEditContainer?.visibility = View.VISIBLE
        } else {
            llInfoContainer?.visibility = View.VISIBLE
            rlEditContainer?.visibility = View.GONE
            tvEditorMode?.text = "编辑"
        }
        tvEditCount?.text = "${item.shopcart_num}"

        helper.setText(R.id.mTvShoppingCarItemName, item.shopcart_name)
        val tv_k_type = helper.getView<TextView>(R.id.mTvShoppingCarItemKType)
        tv_k_type.text = when (item.shopcart_ktype) {
            "1" -> {
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_group, 0, 0, 0)
                "拇指斗价团体砍"
            }
            "2" -> {
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_presonal, 0, 0, 0)
                "拇指斗价个人砍"
            }
            "3" -> {
                tv_k_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_vip_s, 0, 0, 0)
                "拇指斗价会员专享"
            }
            else -> {
                ""
            }
        }
        helper.setText(R.id.mTvShoppingCarItemPrice, "￥ ${item.shopcart_price}")
        helper.setText(R.id.mTvShoppingCarItemSpe, item.shopcart_specification)
        helper.setText(R.id.mTvShoppingCarItemNum, "X ${item.shopcart_num}")

        val goods_pic = helper.getView<ImageView>(R.id.mTvShoppingCarItemPic)
        if (!item.shopcart_pic.isEmpty()) {
            GlideManager.loadImage(goods_pic.context, item.shopcart_pic[0], goods_pic)
        }
    }
}