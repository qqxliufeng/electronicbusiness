package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.VipGoodsBean
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class VipPrivilegeItemAdapter(layoutId: Int, list: ArrayList<VipGoodsBean>) : BaseQuickAdapter<VipGoodsBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: VipGoodsBean?) {
        helper!!.setText(R.id.mTvVipPrivilegeGoodsItemTitle, item!!.product_name)
        val iv_pic = helper.getView<ImageView>(R.id.mIvVipPrivilegeGoodsItemPic)
        if (item.product_pic!=null && !item.product_pic.isEmpty()){
            GlideManager.loadImage(iv_pic.context,item.product_pic[0],iv_pic)
        }else{
            iv_pic.setImageResource(R.drawable.img_glide_load_default)
        }
    }
}