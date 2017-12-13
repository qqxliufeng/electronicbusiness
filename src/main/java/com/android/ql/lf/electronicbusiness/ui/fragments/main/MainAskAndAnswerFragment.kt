package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.TagBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.MainAskAndAnswerAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AddNewAskFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AddNewAskTagListFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AnswerInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.ask.AnswerListFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AskAndAnswerFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_main_ask_and_answer_layout.*
import kotlinx.android.synthetic.main.layout_main_ank_and_answer_top_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.collections.forEachWithIndex
import org.json.JSONObject
import rx.Subscription
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by lf on 2017/11/4 0004.
@author lf on 2017/11/4 0004
 */
class MainAskAndAnswerFragment : BaseRecyclerViewFragment<IndexAskInfoBean>() {

    private val DEFAULT_MAX_ITEM = 9
    val tagList = ArrayList<TagBean>()
    val topRvAdapter = object : BaseQuickAdapter<TagBean, BaseViewHolder>(R.layout.adapter_main_top_ask_and_answer_item_layout, tagList) {
        override fun convert(helper: BaseViewHolder?, item: TagBean?) {
            helper?.setText(R.id.mTvMainTopAskAndAnswerTopItemName, item!!.tag_title)
            helper?.setChecked(R.id.mTvMainTopAskAndAnswerTopItemName, item!!.isChecked)
        }
    }

    private lateinit var tabSubscription: Subscription
    private lateinit var loginSubscription: Subscription
    private lateinit var refreshSubscription: Subscription

    private var currentTag: TagBean? = null

    override fun createAdapter(): BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder> =
            MainAskAndAnswerAdapter(mArrayList)

    companion object {
        fun newInstance() = MainAskAndAnswerFragment()
    }

    override fun getLayoutId() = R.layout.fragment_main_ask_and_answer_layout

    override fun initView(view: View?) {
        super.initView(view)
        /**
         * 监听标签
         */
        tabSubscription = RxBus.getDefault().toObservable(TagBean::class.java).subscribe {
            tagList.forEachWithIndex { _, item ->
                item.isChecked = item.tag_title == it.tag_title
            }
            topRvAdapter.notifyDataSetChanged()
            currentTag = it
            onPostRefresh()
        }
        /**
         * 监听登录
         */
        loginSubscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            if (UserInfo.getInstance().loginTag == 0x21) {//我的问答
                UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的问答", true, false, AskAndAnswerFragment::class.java)
            } else if (UserInfo.getInstance().loginTag == 0x22) {//提问
                UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "提问", true, false, AddNewAskFragment::class.java)
            }
        }
        refreshSubscription = RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any is String && "提问问题" == it.any) {
                tagList[0].isChecked = true
                topRvAdapter.notifyItemChanged(0)
                onSelectedTag(tagList[0])
            }
        }
        setDividerDecoration()
        mRvMainAskAndAnswerContainer.layoutManager = GridLayoutManager(mContext, 5)
        mRvMainAskAndAnswerContainer.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (position == tagList.size - 1) {
                    val bundle = Bundle()
                    bundle.putBoolean(AddNewAskTagListFragment.MULTI_MODE_FLAG, false)
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择标签", true, false, bundle, AddNewAskTagListFragment::class.java)
                    return
                }
                tagList.forEachWithIndex { index, item ->
                    item.isChecked = index == position
                }
                topRvAdapter.notifyDataSetChanged()
                currentTag = tagList[position]
                onPostRefresh()
            }
        })
        mRvMainAskAndAnswerContainer.adapter = topRvAdapter

        mllMainAskContainer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "提问", true, false, AddNewAskFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 0x22
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mllMainAnswerContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "答题", true, false, AnswerListFragment::class.java)
        }
        mllMainMineContainer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的问答", true, false, AskAndAnswerFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 0x21
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
    }

    private fun onSelectedTag(it: TagBean?) {
        if (it != null) {
            mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_TYPE_SEARCH,
                    RequestParamsHelper.getQuizTypeSearch(if ("全部" == it.tag_title) "" else it.tag_title, page = currentPage))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_TAG)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            progressDialog = MyProgressDialog(mContext)
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            parseTagJson(json)
        } else if (requestID == 0x1) {
            if (json != null) {
                processList(json, IndexAskInfoBean::class.java)
            }
        }
    }

    private fun parseTagJson(json: JSONObject?) {
        if (json != null) {
            val tempList = ListParseHelper<TagBean>().fromJson(json.toString(), TagBean::class.java)
            if (tempList != null) {
                val list = ArrayList<TagBean>()
                if (tempList.size > DEFAULT_MAX_ITEM) {
                    (0 until DEFAULT_MAX_ITEM).forEach {
                        list.add(tempList[it])
                    }
                } else {
                    list.addAll(tempList)
                }
                tempList.clear()
                Collections.sort(list) { o1, o2 -> o1!!.tag_sort.toInt().compareTo(o2!!.tag_sort.toInt()) }
                val lastTag = TagBean()
                lastTag.tag_title = "………"
                list.add(lastTag)
                tagList.addAll(list)
                if (!tagList.isEmpty()) {
                    currentTag = tagList[0]
                    tagList[0].isChecked = true
                    mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_TYPE_SEARCH,
                            RequestParamsHelper.getQuizTypeSearch(if ("全部" == tagList[0].tag_title) "" else tagList[0].tag_id, page = currentPage))
                }
                topRvAdapter.notifyDataSetChanged()
            } else {
                mBaseAdapter.setEmptyView(emptyLayoutId)
            }
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        if (currentTag != null) {
            onSelectedTag(currentTag)
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        if (currentTag != null) {
            onSelectedTag(currentTag)
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "问答详情", true, false,
                bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, mArrayList[position].quiz_id)),
                AnswerInfoFragment::class.java)
    }

    override fun onDestroyView() {
        if (!tabSubscription.isUnsubscribed) {
            tabSubscription.unsubscribe()
        }
        if (!loginSubscription.isUnsubscribed) {
            loginSubscription.unsubscribe()
        }
        if (!refreshSubscription.isUnsubscribed) {
            refreshSubscription.unsubscribe()
        }
        super.onDestroyView()
    }
}