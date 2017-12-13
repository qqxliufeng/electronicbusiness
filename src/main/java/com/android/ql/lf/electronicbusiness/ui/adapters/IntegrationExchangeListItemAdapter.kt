package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IntegrationExChangeRecordBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class IntegrationExchangeListItemAdapter(layoutId: Int, list: ArrayList<IntegrationExChangeRecordBean>) : BaseQuickAdapter<IntegrationExChangeRecordBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: IntegrationExChangeRecordBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvExchangeItemGoodsPic)
        GlideManager.loadImage(iv_pic.context, if (item!!.jproduct_pic.isEmpty()) "" else item.jproduct_pic[0], iv_pic)
        helper.setText(R.id.mIvExchangeItemGoodsName, item.jproduct_name)
        helper.setText(R.id.mIvExchangeItemGoodsTime, item.order_ctime)
        helper.setText(R.id.mIvExchangeItemGoodsPrice, "${item.jproduct_price}积分")
    }
}