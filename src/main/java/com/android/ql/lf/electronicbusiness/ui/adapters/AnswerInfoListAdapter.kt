package com.android.ql.lf.electronicbusiness.ui.adapters

import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.android.ql.lf.electronicbusiness.ui.views.PraiseView
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AnswerInfoListAdapter(layoutId: Int, list: ArrayList<AnswerBean>) : BaseQuickAdapter<AnswerBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: AnswerBean?) {
        helper!!.addOnClickListener(R.id.mPraiseView)
        val iv_face = helper.getView<ImageView>(R.id.mIvAnswerInfoItemFace)
        GlideManager.loadFaceCircleImage(iv_face.context, item!!.member_pic, iv_face)
        helper.setText(R.id.mTvAnswerInfoItemName, item.member_name)
        helper.setText(R.id.mTvAnswerInfoItemContent, item.answer_content)
        helper.setText(R.id.mTvAnswerInfoItemTime, item.answer_time)
        helper.setText(R.id.mTvPraiseText, if ("暂无" == item.answer_click) PraiseView.PRAISE_TEXT else item.answer_click)
    }
}