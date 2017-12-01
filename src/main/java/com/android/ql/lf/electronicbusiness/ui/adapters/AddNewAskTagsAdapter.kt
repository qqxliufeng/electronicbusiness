package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.TagBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AddNewAskTagsAdapter(layoutId: Int, list: ArrayList<TagBean>) : BaseQuickAdapter<TagBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: TagBean?) {
        helper!!.setText(R.id.mTvAddNewAskTags, item!!.tag_title)
        helper.setChecked(R.id.mTvAddNewAskTags, item.isChecked)
    }
}