package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AnswerInfoListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.BrowserImageFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.PopupWindowDialog
import com.android.ql.lf.electronicbusiness.ui.views.PraiseView
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_fragment_container_layout.*
import kotlinx.android.synthetic.main.fragment_answer_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import rx.Subscription

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AnswerInfoFragment : BaseRecyclerViewFragment<AnswerBean>() {

    companion object {
        val ASK_ID_FLAG = "ask_id_flag"
    }

    private var askInfoBean: IndexAskInfoBean? = null
    private var currentAnswerBean: AnswerBean? = null

    private lateinit var replySubscription: Subscription

    override fun getLayoutId() = R.layout.fragment_answer_info_layout

    override fun createAdapter(): BaseQuickAdapter<AnswerBean, BaseViewHolder> = AnswerInfoListAdapter(R.layout.adapter_answer_info_list_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(UserInfo::class.java).subscribe {
            when (UserInfo.getInstance().loginTag) {
                0x23 -> {
                    focusAsk()
                }
                0x24 -> {
                    showReplyDialog()
                }
            }
        }
        replySubscription = RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.any == "回复成功")
                onPostRefresh()
        }
        mLlAnswerInfoReply.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                showReplyDialog()
            } else {
                UserInfo.getInstance().loginTag = 0x24
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mLlAnswerInfoAsk.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提问", true, false, AddNewAskFragment::class.java)
        }

    }

    private fun showReplyDialog() {
        if (askInfoBean != null) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "回答问题", true, false, bundleOf(
                    Pair("qid", askInfoBean!!.quiz_id),
                    Pair("title", askInfoBean!!.quiz_title),
                    Pair("uid", askInfoBean!!.quiz_uid)), ReplyQuestionFragment::class.java)
        }
    }

    private fun focusAsk() {
        mPresent.getDataByPost(0x2, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_CONCERM, RequestParamsHelper.getConcermParams(askInfoBean!!.quiz_id))
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_DETAIL, RequestParamsHelper.getQuizDetailParams(arguments.getString(ASK_ID_FLAG, ""), currentPage))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x2 || requestID == 0x3 || requestID == 0x4) {
            progressDialog = MyProgressDialog(mContext, when (requestID) {
                0x2 -> "正在关注……"
                0x3 -> "评论中……"
                0x4 -> "正在删除……"
                else -> {
                    ""
                }
            })
            progressDialog.show()
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_DETAIL, RequestParamsHelper.getQuizDetailParams(arguments.getString(ASK_ID_FLAG, ""), currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                if (json != null) {
                    if (currentPage == 0) {
                        askInfoBean = Gson().fromJson(json.optJSONObject("result").toString(), IndexAskInfoBean::class.java)
                        if (askInfoBean != null && askInfoBean!!.quiz_concerm == "0") {
                            mTvAnswerInfoFocus.text = "点击关注"
                            mTvAnswerInfoFocus.isEnabled = true
                            mTvAnswerInfoFocus.setOnClickListener {
                                if (askInfoBean != null) {
                                    if (UserInfo.getInstance().isLogin) {
                                        focusAsk()
                                    } else {
                                        UserInfo.getInstance().loginTag = 0x23
                                        FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
                                    }
                                }
                            }
                        } else {
                            mTvAnswerInfoFocus.text = "已关注"
                            mTvAnswerInfoFocus.isEnabled = false
                        }
                        mTvAnswerInfoTitle.text = askInfoBean?.quiz_title
                        mLlAnswerInfoImageContainer.removeAllViews()
                        mLlAnswerInfoTagsContainer.removeAllViews()
                        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, mContext.resources.displayMetrics).toInt()
                        askInfoBean?.quiz_type?.forEach {
                            val textView = TextView(mContext)
                            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView.setPadding(padding, padding / 2, padding, padding / 2)
                            textView.background = ContextCompat.getDrawable(mContext, R.drawable.shape_tv_bg2)
                            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black_tv_color))
                            textView.text = it
                            param.leftMargin = padding / 2
                            param.rightMargin = padding / 2
                            textView.layoutParams = param
                            mLlAnswerInfoTagsContainer.addView(textView)
                        }
                        askInfoBean?.quiz_pic?.forEachWithIndex { index, it ->
                            val image = ImageView(mContext)
                            val params = LinearLayout.LayoutParams(0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150.0f, mLlAnswerInfoImageContainer.context.resources.displayMetrics).toInt())
                            params.weight = 1.0f
                            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, mLlAnswerInfoImageContainer.context.resources.displayMetrics).toInt()
                            params.leftMargin = margin
                            params.rightMargin = margin
                            image.layoutParams = params
                            image.scaleType = ImageView.ScaleType.CENTER_CROP
                            image.setOnClickListener {
                                BrowserImageFragment.startBrowserImage(mContext, askInfoBean!!.quiz_pic, index)
                            }
                            GlideManager.loadImage(mLlAnswerInfoImageContainer.context, it, image)
                            mLlAnswerInfoImageContainer.addView(image)
                        }
                        mTvAnswerInfoContent.text = askInfoBean?.quiz_content
                        mTvAnswerInfoAnswerCount.text = "${askInfoBean?.quiz_num} 个回答"
                        mTvAnswerInfoFocusCount.text = "${askInfoBean?.quiz_click} 个关注"
                    }
                    val jsonArray = json.optJSONArray("arr")
                    if (jsonArray.length() == 0) {
                        if (currentPage == 0) {
                            setEmptyView()
                        }
                        mBaseAdapter.loadMoreEnd(false)
                    } else {
                        (0 until jsonArray.length()).forEach {
                            mArrayList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString().replace("null", "暂无"), AnswerBean::class.java))
                        }
                        mBaseAdapter.notifyDataSetChanged()
                        mBaseAdapter.loadMoreComplete()
                    }
                } else {
                    if (result != null) {
                        if ("400" == JSONObject(result.toString()).optString("code")) {
                            onAnswerDelete()
                        }
                    }
                }
            }
            0x1 -> { //点赞
            }
            0x2 -> {
                if (json != null) {
                    toast("关注成功")
                    mTvAnswerInfoFocus.text = "已关注"
                    mTvAnswerInfoFocus.isEnabled = false
                }else{
                    if(result != null) {
                        if ("400" == JSONObject(result.toString()).optString("code")) {
                            onAnswerDelete()
                        }
                    }
                }
            }
            0x3 -> {
                if (json != null) {
                    toast("评论成功")
                    onPostRefresh()
                } else {
                    if(result != null) {
                        if ("400" == JSONObject(result.toString()).optString("code")) {
                            onAnswerDelete()
                        }
                    }
                }
            }
            0x4 -> {
                if (json != null) {
                    mArrayList.remove(currentAnswerBean)
                    if (mArrayList.isEmpty()) {
                        setEmptyView()
                        return
                    }
                    mBaseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onAnswerDelete() {
        mTvAnswerInfoDeleteInfo.visibility = View.VISIBLE
        mClAnswerInfoInfoContainer.visibility = View.GONE
    }

    override fun getEmptyMessage(): String = "暂无评论哦~~~"

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bundle = Bundle()
        val answerBean = mArrayList[position]
        answerBean.ask_title = askInfoBean?.quiz_title
        answerBean.ask_num = askInfoBean?.quiz_num
        bundle.putParcelable(CommentInfoFragment.ANSWER_BEAN_FLAG, answerBean)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "回答详情", true, false, bundle, CommentInfoFragment::class.java)
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentAnswerBean = mArrayList[position]
        when (view?.id) {
            R.id.mPraiseView -> {
                (view as PraiseView).toggle()
                mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_PRAISE, RequestParamsHelper.getPraise(currentAnswerBean!!.answer_id))
            }
            R.id.mTvAnswerInfoItemDelete -> {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage("确认删除此评论？")
                builder.setTitle("提示")
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定") { _, _ ->
                    mPresent.getDataByPost(0x4, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(aid = currentAnswerBean!!.answer_id))
                }
                builder.create().show()
            }
        }
    }
}