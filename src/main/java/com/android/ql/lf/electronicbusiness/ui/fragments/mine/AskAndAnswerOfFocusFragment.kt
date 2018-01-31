package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyFocusBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AskAndAnswerOfFocusAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AnswerInfoFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerOfFocusFragment : BaseRecyclerViewFragment<MyFocusBean>() {

    companion object {
        fun newInstance() = AskAndAnswerOfFocusFragment()
    }

    private lateinit var headerView: TextView

    override fun createAdapter(): BaseQuickAdapter<MyFocusBean, BaseViewHolder> =
            AskAndAnswerOfFocusAdapter(R.layout.adapter_ask_and_aswer_of_focus_item_layout, mArrayList)

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
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYCONCERM, RequestParamsHelper.getMyFocus(page = currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYCONCERM, RequestParamsHelper.getMyFocus(page = currentPage))
    }


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        processList(json, MyFocusBean::class.java)
        if (json != null) {
            val tempList = mArrayList.filter {
                !TextUtils.isEmpty(it.quiz_id)
            }
            mArrayList.clear()
            mArrayList.addAll(tempList)
            mBaseAdapter.notifyDataSetChanged()
            headerView.text = "我的关注（${mArrayList.size}）"
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "问答详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, mArrayList[position].quiz_id)), AnswerInfoFragment::class.java)
    }


}