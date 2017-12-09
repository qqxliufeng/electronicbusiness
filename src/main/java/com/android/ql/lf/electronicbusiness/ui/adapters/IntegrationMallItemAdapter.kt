package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IMallGoodsItemBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class IntegrationMallItemAdapter(layoutId: Int, list: ArrayList<IMallGoodsItemBean>) : BaseQuickAdapter<IMallGoodsItemBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: IMallGoodsItemBean?) {
        val iv_pic = helper!!.getView<ImageView>(R.id.mIvIntegrationMallItemPic)
        GlideManager.loadImage(iv_pic.context, item!!.jproduct_pic[0], iv_pic)
        helper.setText(R.id.mTvIntegrationMallItemName, item.jproduct_name)
        helper.setText(R.id.mIvIntegrationMallItemIntegrationCount, "${item.jproduct_price} 积分")
    }
}