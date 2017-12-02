package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyFocusBean
import com.android.ql.lf.electronicbusiness.ui.adapters.AskAndAnswerOfFocusAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerOfFocusFragment : BaseRecyclerViewFragment<MyFocusBean>() {

    companion object {
        fun newInstance() = AskAndAnswerOfFocusFragment()
    }

    override fun createAdapter(): BaseQuickAdapter<MyFocusBean, BaseViewHolder> =
            AskAndAnswerOfFocusAdapter(R.layout.adapter_ask_and_aswer_of_focus_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
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
    }
}