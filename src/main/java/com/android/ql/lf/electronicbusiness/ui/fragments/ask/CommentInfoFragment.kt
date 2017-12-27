package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.android.ql.lf.electronicbusiness.data.ReplyAnswerBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.CommentInfoItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.PopupWindowDialog
import com.android.ql.lf.electronicbusiness.ui.views.PraiseView
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_comment_info_layout.*
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 * Created by lf on 2017/11/17 0017.
 * @author lf on 2017/11/17 0017
 */
class CommentInfoFragment : BaseRecyclerViewFragment<ReplyAnswerBean>() {

    companion object {
        val ANSWER_BEAN_FLAG = "answer_bean_flag"
    }

    private lateinit var answerBean: AnswerBean
    private var replyAnswerBean: ReplyAnswerBean? = null

    private var currentReplyTag = 0x25

    override fun getLayoutId() = R.layout.fragment_comment_info_layout

    override fun createAdapter(): BaseQuickAdapter<ReplyAnswerBean, BaseViewHolder> =
            CommentInfoItemAdapter(R.layout.adapter_comment_info_item_layout, mArrayList)

    override fun initView(view: View?) {
        arguments.classLoader = this.javaClass.classLoader
        answerBean = arguments.getParcelable(ANSWER_BEAN_FLAG)
        super.initView(view)
        mTvCommentInfoNickName.text = answerBean.member_name
        GlideManager.loadFaceCircleImage(mContext, answerBean.member_pic, mIvCommentInfoFace)
        mTvCommentInfoContent.text = answerBean.answer_content
        mRlCommentInfoReply.setOnClickListener {
            currentReplyTag = 0x26
            showReplyDialog()
        }
    }

    private fun showReplyDialog() {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_answer_info_repay_layout, null)
        val et_content = contentView.findViewById<EditText>(R.id.mEtReplyContent)
        et_content.hint = if (currentReplyTag == 0x25) {
            "回复：${replyAnswerBean!!.member_name}"
        } else "回复："
        val bt_send = contentView.findViewById<Button>(R.id.mBtReplaySend)
        val popupWindow = PopupWindowDialog.showReplyDialog(mContext, contentView)
        bt_send.setOnClickListener {
            if (TextUtils.isEmpty(et_content.text.toString())) {
                toast("请输入评论内容")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.QAA_MODEL,
                    RequestParamsHelper.ACT_H_REPLY,
                    RequestParamsHelper.getHReply(answerBean.answer_id, if (currentReplyTag == 0x25) replyAnswerBean!!.reply_id else "", et_content.text.toString()))
            popupWindow.dismiss()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_ANSWER_DETAIL, RequestParamsHelper.getAnswerDetailParams(answerBean.answer_id, currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_ANSWER_DETAIL, RequestParamsHelper.getAnswerDetailParams(answerBean.answer_id, currentPage))
    }

    override fun getEmptyMessage() = "暂无回复哦~~~"

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x2) {
            progressDialog = MyProgressDialog(mContext)
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                if (json != null) {
                    val jsonResult = json.optJSONObject("result")
                    mTvCommentInfoTitle.text = jsonResult.optString("answer_qtitle")
                    mTvCommentInfoAskCount.text = "${jsonResult.optString("answer_qnum")}个回答"
                    mTvCommentInfoTime.text = "回答于 ${jsonResult.optString("answer_time")}"
                    mTvCommentInfoReplyCount.text = "${jsonResult.optString("answer_num")} 回复"
                    val picArray = jsonResult.optJSONArray("answer_pic")
                    if (picArray != null && picArray.length() > 0) {
                        mLlCommentInfoImageContainer.removeAllViews()
                        val picList = arrayListOf<String>()
                        (0 until picArray.length()).forEach {
                            picList.add(picArray.optString(it))
                        }
                        mLlCommentInfoImageContainer.setImages(picList)
                    }

                    val jsonArray = json.optJSONArray("arr")
                    if (jsonArray.length() == 0) {
                        if (currentPage == 0) {
                            setEmptyView()
                        }
                        mBaseAdapter.loadMoreEnd(false)
                    } else {
                        (0 until jsonArray.length()).forEach {
                            mArrayList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString().replace("null", "暂无"), ReplyAnswerBean::class.java))
                        }
                        mBaseAdapter.notifyDataSetChanged()
                        mBaseAdapter.loadMoreComplete()
                    }
                }
            }
            0x1 -> {
                if (json != null) {
                    if (currentReplyTag == 0x26) {
                        if (answerBean.member_id == UserInfo.getInstance().memberId) {
                            OrderPresent.notifyRefreshOrderNum()
                        }
                    } else {
                        if (replyAnswerBean != null) {
                            if (replyAnswerBean!!.member_id == UserInfo.getInstance().memberId) {
                                OrderPresent.notifyRefreshOrderNum()
                            }
                        }
                    }
                    onPostRefresh()
                }
            }
            0x2 -> {
                if (json != null) {
                    mArrayList.remove(replyAnswerBean)
                    if (mArrayList.isEmpty()) {
                        setEmptyView()
                        return
                    }
                    mBaseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        replyAnswerBean = mArrayList[position]
        when (view?.id) {
            R.id.mTvReplyInfoItemReply -> {
                currentReplyTag = 0x25
                showReplyDialog()
            }
            R.id.mTvReplyInfoItemDelete -> {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage("确认删除此回复？")
                builder.setTitle("提示")
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定") { _, _ ->
                    mPresent.getDataByPost(0x2, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(hid = replyAnswerBean!!.reply_id))
                }
                builder.create().show()
            }
        }
    }
}