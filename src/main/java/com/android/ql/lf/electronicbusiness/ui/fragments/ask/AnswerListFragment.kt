package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

    private lateinit var currentItem: IndexAskInfoBean

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder> =
            AnswerListAdapter(R.layout.adapter_answer_list_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_qustion_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, QuestionSearchFragment::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            if (it.isLogin && it.loginTag == 0x24) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext,
                        "问题详情",
                        true,
                        false,
                        bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)),
                        AnswerInfoFragment::class.java)
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

    private fun loadData() {
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_TYPE_SEARCH,
                RequestParamsHelper.getQuizTypeSearch("", page = currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                processList(json, IndexAskInfoBean::class.java)
            }
            0x1 -> {
                if (json != null) {
                    mArrayList.remove(currentItem)
                    if (mArrayList.isEmpty()) {
                        setEmptyView()
                        return
                    }
                    mBaseAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun getEmptyMessage() = "暂无问题哦~~~"


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentItem = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "问题详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)), AnswerInfoFragment::class.java)
        } else {
            UserInfo.getInstance().loginTag = 0x24
            LoginFragment.startLogin(mContext)
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        currentItem = mArrayList[position]
        when (view?.id) {
            R.id.mTvAskListItemDelete -> {
                val builder = AlertDialog.Builder(mContext)
                builder.setTitle("提示")
                builder.setMessage("是否要删除此问答？")
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定") { _, _ ->
                    mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(qid = currentItem.quiz_id))
                }
                builder.create().show()
            }
        }
    }

}