package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.android.ql.lf.electronicbusiness.data.ReplyAnswerBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.CommentInfoItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
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

    private lateinit var subscription: Subscription

    override fun getLayoutId() = R.layout.fragment_comment_info_layout

    override fun createAdapter(): BaseQuickAdapter<ReplyAnswerBean, BaseViewHolder> =
            CommentInfoItemAdapter(R.layout.adapter_comment_info_item_layout, mArrayList)

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            when (UserInfo.getInstance().loginTag) {
                0x25 -> {
                    showReplyDialog(0x25)
                }
                0x26 -> {
                    showReplyDialog(0x26)
                }
            }
        }
        arguments.classLoader = this.javaClass.classLoader
        answerBean = arguments.getParcelable(ANSWER_BEAN_FLAG)
        super.initView(view)
        mTvCommentInfoTitle.text = answerBean.ask_title
        mTvCommentInfoAskCount.text = "${answerBean.ask_num}个回答"
        mTvCommentInfoNickName.text = answerBean.member_name
        GlideManager.loadFaceCircleImage(mContext, answerBean.member_pic, mIvCommentInfoFace)
        mTvCommentInfoContent.text = answerBean.answer_content
        mTvCommentInfoTime.text = "回答于 ${answerBean.answer_time}"
        mTvCommentInfoReplyCount.text = "${answerBean.answer_num} 回复"
        mRlCommentInfoReply.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                showReplyDialog(0x26)
            } else {
                UserInfo.getInstance().loginTag = 0x26
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
    }

    private fun showReplyDialog(tag: Int) {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_answer_info_repay_layout, null)
        val et_content = contentView.findViewById<EditText>(R.id.mEtReplyContent)
        et_content.hint = if (tag == 0x25) { "回复：${replyAnswerBean!!.member_name}"} else "回复："
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
                    RequestParamsHelper.getHReply(answerBean.answer_id, if (tag == 0x25) replyAnswerBean!!.reply_id else "", et_content.text.toString()))
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

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                if (json != null) {
                    val jsonArray = json.optJSONArray("arr")
                    if (jsonArray.length() == 0) {
                        if (currentPage == 0) {
                            mBaseAdapter.setEmptyView(emptyLayoutId)
                            mBaseAdapter.notifyDataSetChanged()
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
                    onPostRefresh()
                }
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        replyAnswerBean = mArrayList[position]
        when (view?.id) {
            R.id.mPraiseView -> {
                (view as PraiseView).toggle()
            }
            R.id.mTvReplyInfoItemReply -> {
                if (UserInfo.getInstance().isLogin) {
                    showReplyDialog(0x25)
                } else {
                    UserInfo.getInstance().loginTag = 0x25
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
                }
            }
        }
    }

    override fun onDestroyView() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroyView()
    }
}