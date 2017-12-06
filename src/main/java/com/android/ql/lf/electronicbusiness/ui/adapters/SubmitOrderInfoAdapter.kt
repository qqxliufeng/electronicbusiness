package com.android.ql.lf.electronicbusiness.ui.adapters

import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.text.DecimalFormat

/**
 * Created by lf on 2017/12/6 0006.
 * @author lf on 2017/12/6 0006
 */
class SubmitOrderInfoAdapter(layout: Int, list: ArrayList<ShoppingCarItemBean>) : BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder>(layout, list) {

    override fun convert(helper: BaseViewHolder?, item: ShoppingCarItemBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvSubmitOrderGoodsPic)
        GlideManager.loadImage(iv_pic.context, if (item!!.shopcart_pic.isEmpty()) "" else item.shopcart_pic[0], iv_pic)
        helper.setText(R.id.mIvSubmitOrderGoodsName, item.shopcart_name)
        helper.setText(R.id.mIvSubmitOrderGoodsSpe, item.shopcart_specification)
        helper.setText(R.id.mIvSubmitOrderGoodsPrice, "￥ ${item.shopcart_price}")
        helper.setText(R.id.mIvSubmitOrderGoodsNum, "X ${item.shopcart_num}")
        val tv_k_type = helper.getView<TextView>(R.id.mIvSubmitOrderGoodsKType)
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
        helper.setText(R.id.mIvSubmitOrderGoodsCount, "共${item.shopcart_num}件商品")
        helper.setText(R.id.mIvSubmitOrderGoodsAllPrice, Html.fromHtml("小计：<span style='color:#ff0000'>￥ ${DecimalFormat("0.00").format(item.shopcart_num.toInt() * item.shopcart_price.toFloat())}</span>"))
        helper.setText(R.id.mTvSubmitOrderGoodsBBSContent, if (TextUtils.isEmpty(item.bbs)) "选填" else item.bbs)
        helper.addOnClickListener(R.id.mRlSubmitOrderGoodsBBS)
    }
}