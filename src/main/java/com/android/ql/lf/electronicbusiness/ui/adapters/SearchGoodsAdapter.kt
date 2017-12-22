package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/14 0014.
 * @author lf on 2017/11/14 0014
 */
class SearchGoodsAdapter(layoutId: Int, list: ArrayList<GoodsItemBean>) : BaseQuickAdapter<GoodsItemBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: GoodsItemBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvSearchGoodsItemPic)
        GlideManager.loadImage(iv_pic.context, if (item!!.product_pic.isEmpty()) "" else item.product_pic[0], iv_pic)
        helper.setText(R.id.mIvSearchGoodsItemName, item.product_name)
        helper.setText(R.id.mIvSearchGoodsItemPrice, "￥${item.product_price}")
        helper.setText(R.id.mIvSearchGoodsItemHasNum, "${item.product_sv}人已购买")
    }
}