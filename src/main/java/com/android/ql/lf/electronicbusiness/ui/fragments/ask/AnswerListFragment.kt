package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AnswerListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AnswerListFragment : BaseRecyclerViewFragment<String>(){

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            AnswerListAdapter(R.layout.adapter_answer_list_item_layout,mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        (0..10).forEach {
            mArrayList.add("")
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        setRefreshEnable(false)
        onRequestEnd(-1)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        FragmentContainerActivity.startFragmentContainerActivity(mContext,"问题详情",true,false, AnswerInfoFragment::class.java)
    }

}