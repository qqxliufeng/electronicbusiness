package com.android.ql.lf.electronicbusiness.ui.adapters

import android.media.Image
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
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
                val ll_container = helper.getView<LinearLayout>(R.id.mLlIndexAskInfoMultiImageAskImageContainer)
                ll_container.removeAllViews()
                item.quiz_pic.forEach {
                    val image = ImageView(ll_container.context)
                    val params = LinearLayout.LayoutParams(0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0f, ll_container.context.resources.displayMetrics).toInt())
                    params.weight = 1.0f
                    val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, ll_container.context.resources.displayMetrics).toInt()
                    params.leftMargin = margin
                    params.rightMargin = margin
                    image.layoutParams = params
                    image.scaleType = ImageView.ScaleType.CENTER_CROP
                    GlideManager.loadImage(ll_container.context, it, image)
                    ll_container.addView(image)
                }
            }
        }
    }
}