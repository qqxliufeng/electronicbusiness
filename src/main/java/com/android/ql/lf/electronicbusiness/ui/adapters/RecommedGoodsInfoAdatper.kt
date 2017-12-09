package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.PersonalCutGoodsItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/12/8 0008.
 * @author lf on 2017/12/8 0008
 */
class RecommedGoodsInfoAdatper(layout:Int,list: ArrayList<PersonalCutGoodsItemBean>): BaseQuickAdapter<PersonalCutGoodsItemBean, BaseViewHolder>(layout,list) {
    override fun convert(helper: BaseViewHolder?, item: PersonalCutGoodsItemBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvGoodsInfoRecommendPic)
        GlideManager.loadImage(iv_pic.context, if (item!!.product_pic.isEmpty()) {
            ""
        } else {
            item!!.product_pic[0]
        }, iv_pic)
        helper.setText(R.id.mTvGoodsInfoRecommendName, item.product_name)
        helper.setText(R.id.mTvGoodsInfoRecommendPrice, "￥ ${item.product_price}")
    }
}