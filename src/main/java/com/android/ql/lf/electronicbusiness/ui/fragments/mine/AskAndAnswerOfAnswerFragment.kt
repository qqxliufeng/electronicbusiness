package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AnswerBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AskAndAnswerOfAnswerAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.CommentInfoFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerOfAnswerFragment : BaseRecyclerViewFragment<AnswerBean>() {

    companion object {
        fun newInstance() = AskAndAnswerOfAnswerFragment()
    }

    private lateinit var headerView: TextView

    override fun createAdapter(): BaseQuickAdapter<AnswerBean, BaseViewHolder> =
            AskAndAnswerOfAnswerAdapter(R.layout.adapter_ask_and_aswer_of_answer_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun initView(view: View?) {
        super.initView(view)
        mBaseAdapter.setHeaderAndEmpty(true)
        headerView = View.inflate(mContext, android.R.layout.simple_list_item_1, null) as TextView
        mBaseAdapter.addHeaderView(headerView)
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MY_ANSWER, RequestParamsHelper.getMyAnswerParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MY_ANSWER, RequestParamsHelper.getMyAnswerParams(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        processList(json, AnswerBean::class.java)
        if (json != null) {
            headerView.text = "我的回答（${json.optString("arr")}）"
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val answerBean = mArrayList[position]
        when (view!!.id) {
            R.id.mTvAnswerItemExpand -> {
                answerBean.isExpand = !answerBean.isExpand
                answerBean.maxLines = if (answerBean.isExpand) Int.MAX_VALUE else 3
                mBaseAdapter.notifyItemChanged(position  + 1)
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val answerBean = mArrayList[position]
        answerBean.member_pic = UserInfo.getInstance().memberPic
        answerBean.member_name = UserInfo.getInstance().memberName
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "问答详情", true, false, bundleOf(Pair(CommentInfoFragment.ANSWER_BEAN_FLAG, answerBean)), CommentInfoFragment::class.java)
    }

}