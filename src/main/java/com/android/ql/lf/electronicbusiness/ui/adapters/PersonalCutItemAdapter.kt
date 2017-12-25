package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.Button
import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class PersonalCutItemAdapter(layoutId: Int, list: ArrayList<GoodsItemBean>) : BaseQuickAdapter<GoodsItemBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: GoodsItemBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvPersonalCutItemPic)
        GlideManager.loadImage(iv_pic.context, if (!item!!.product_pic.isEmpty()) {
            item.product_pic[0]
        } else {
            ""
        }, iv_pic)

        helper.setText(R.id.mIvPersonalCutItemName, item.product_name)
        helper.setText(R.id.mTvPersonalCutItemMoney, "￥ ${item.product_price}")
        helper.setText(R.id.mTvPersonalCutItemHasCut, "已减${item.product_minus}元")
        helper.setText(R.id.mTvPersonalCutItemHasPersonNum, "${item.product_knum}人参与")
        val bt_status = helper.getView<Button>(R.id.mBtPersonalCutItemCut)
        if ("1" == item.product_ptype) {
            helper.setText(R.id.mBtPersonalCutItemCut, "已砍到最底价")
            bt_status.isEnabled = false
        } else {
            bt_status.isEnabled = true
        }
        if ("1" == item.product_endstatus) {
            helper.setText(R.id.mBtPersonalCutItemCut, "活动已结束")
            bt_status.isEnabled = false
        } else {
            bt_status.isEnabled = true
        }
    }
}