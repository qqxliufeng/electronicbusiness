package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AnswerListAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AnswerListFragment : BaseRecyclerViewFragment<IndexAskInfoBean>() {

    private lateinit var currentItem:IndexAskInfoBean


    override fun createAdapter(): BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder> =
            AnswerListAdapter(R.layout.adapter_answer_list_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            if (it.isLogin && it.loginTag == 0x24){
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "问题详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)),AnswerInfoFragment::class.java)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    private fun loadData(){
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_TYPE_SEARCH,
                RequestParamsHelper.getQuizTypeSearch("", page = currentPage))
    }


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result),IndexAskInfoBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentItem = mArrayList[position]
        if (UserInfo.getInstance().isLogin){
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "问题详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)),AnswerInfoFragment::class.java)
        }else{
            UserInfo.getInstance().loginTag = 0x24
            LoginFragment.startLogin(mContext)
        }
    }

}