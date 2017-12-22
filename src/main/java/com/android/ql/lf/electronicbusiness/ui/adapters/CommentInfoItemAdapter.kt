package com.android.ql.lf.electronicbusiness.ui.adapters

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ReplyAnswerBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class CommentInfoItemAdapter(layoutId: Int, list: ArrayList<ReplyAnswerBean>) : BaseQuickAdapter<ReplyAnswerBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: ReplyAnswerBean?) {
        helper!!.addOnClickListener(R.id.mTvReplyInfoItemReply)
        val iv_face = helper.getView<ImageView>(R.id.mIvReplyInfoItemFace)
        GlideManager.loadFaceCircleImage(iv_face.context, item!!.member_pic, iv_face)
        helper.setText(R.id.mTvReplyInfoItemNickName, item.member_name)
        helper.setText(R.id.mTvReplyInfoItemTime, item.reply_time)
        val tv_delete = helper.getView<TextView>(R.id.mTvReplyInfoItemDelete)
        helper.addOnClickListener(R.id.mTvReplyInfoItemDelete)
        if (item.member_id == UserInfo.getInstance().memberId) {
            tv_delete.visibility = View.VISIBLE
        } else {
            tv_delete.visibility = View.GONE
        }
        if (TextUtils.isEmpty(item.name)) {
            helper.setText(R.id.mTvReplyInfoItemContent, item.reply_content)
        } else {
            val span = SpannableString("@" + item.name + " " + item.reply_content)
            val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(iv_face.context, R.color.colorPrimary))
            span.setSpan(foregroundColorSpan, 0, item.name.length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            helper.setText(R.id.mTvReplyInfoItemContent, span)
        }
    }
}