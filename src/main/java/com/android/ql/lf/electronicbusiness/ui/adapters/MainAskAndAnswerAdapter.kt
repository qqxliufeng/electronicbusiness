package com.android.ql.lf.electronicbusiness.ui.adapters

import android.view.View
import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.android.ql.lf.electronicbusiness.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MainAskAndAnswerAdapter(list: ArrayList<IndexAskInfoBean>) : BaseMultiItemQuickAdapter<IndexAskInfoBean, BaseViewHolder>(list) {

    init {
        addItemType(IndexAskInfoBean.LARGE_IMAGE, R.layout.adapter_index_ask_info_large_image_layout)
        addItemType(IndexAskInfoBean.MULTI_IMAGE, R.layout.adapter_index_ask_info_multi_image_layout)
    }

    override fun convert(helper: BaseViewHolder?, item: IndexAskInfoBean?) {
        when (item!!.itemType) {
            IndexAskInfoBean.LARGE_IMAGE -> {
                helper!!.setText(R.id.mTvIndexAskInfoLargeImageTitle, item.quiz_title)
                helper.setText(R.id.mTvIndexAskInfoLargeImageAskCount, "${item.quiz_num}个回答")
                helper.addOnClickListener(R.id.mTvIndexAskInfoLargeImageAskDelete)
                val image = helper.getView<ImageView>(R.id.mIvIndexAskInfoLargeImage)
                if (item.quiz_pic.isEmpty()) {
                    image.visibility = View.GONE
                } else {
                    image.visibility = View.VISIBLE
                    GlideManager.loadImage(image.context, item.quiz_pic[0], image)
                }
            }
            IndexAskInfoBean.MULTI_IMAGE -> {
                helper!!.setText(R.id.mTvIndexAskInfoMultiImageTitle, item.quiz_title)
                helper.setText(R.id.mTvIndexAskInfoMultiImageAskCount, "${item.quiz_num}个回答")
                helper.addOnClickListener(R.id.mTvIndexAskInfoMultiImageAskDelete)
                val ll_container = helper.getView<ImageContainerLinearLayout>(R.id.mLlIndexAskInfoMultiImageAskImageContainer)
                ll_container.setImages(item.quiz_pic)
            }
        }
    }
}