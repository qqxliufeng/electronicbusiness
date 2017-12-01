package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class ShoppingCarItemAdapter(layoutId: Int, list: ArrayList<ShoppingCarItemBean>) : BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: ShoppingCarItemBean?) {
        helper?.addOnClickListener(R.id.mTvShoppingCarItemEditMode)
        helper?.addOnClickListener(R.id.mIvShoppingCarItemSelector)
        val ivSelector = helper?.getView<ImageView>(R.id.mIvShoppingCarItemSelector)
        val tvEditorMode = helper?.getView<TextView>(R.id.mTvShoppingCarItemEditMode)
        val llInfoContainer = helper?.getView<LinearLayout>(R.id.mLlShoppingCarItemInfoContainer)
        val rlEditContainer = helper?.getView<RelativeLayout>(R.id.mRlShoppingCarItemEditContainer)
        val tvEditCount = helper?.getView<TextView>(R.id.mTvShoppingCarItemEditCount)
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
        tvEditCount?.text = "${item.count}"
    }
}