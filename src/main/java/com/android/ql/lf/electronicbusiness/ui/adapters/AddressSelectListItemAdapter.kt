package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class AddressSelectListItemAdapter(layoutId: Int,list: ArrayList<AddressBean>):BaseQuickAdapter<AddressBean,BaseViewHolder>(layoutId,list){
    override fun convert(helper: BaseViewHolder?, item: AddressBean?) {
    }

}