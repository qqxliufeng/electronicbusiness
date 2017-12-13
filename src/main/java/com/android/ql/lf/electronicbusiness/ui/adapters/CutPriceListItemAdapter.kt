package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyCutPriceBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class CutPriceListItemAdapter(layoutId: Int, list: ArrayList<MyCutPriceBean>) : BaseQuickAdapter<MyCutPriceBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: MyCutPriceBean?) {
        val iv_image = helper!!.getView<ImageView>(R.id.mIvCutPriceItemPic)
        GlideManager.loadImage(mContext, if (item!!.product_pic.isEmpty()) "" else item.product_pic[0], iv_image)
        helper.setText(R.id.mTvCutPriceItemName, item.product_name)
        helper.setText(R.id.mTvCutPriceItemPrice, "￥ ${item.product_price}")
    }
}