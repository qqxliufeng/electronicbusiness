package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IndexAskInfoBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.QuestionSearchAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_question_search_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/12/18 0018.
 * @author lf on 2017/12/18 0018
 */
class QuestionSearchFragment : BaseRecyclerViewFragment<IndexAskInfoBean>() {

    private var keyword: String? = null

    private var isLoading = false

    private lateinit var currentItem: IndexAskInfoBean

    override fun createAdapter(): BaseQuickAdapter<IndexAskInfoBean, BaseViewHolder> =
            QuestionSearchAdapter(R.layout.adapter_question_search_item_layout, mArrayList)

    override fun getLayoutId() = R.layout.fragment_question_search_layout

    override fun initView(view: View?) {
        isFirstRefresh = false
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            if (it.isLogin && it.loginTag == 0x25) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "问题详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)), AnswerInfoFragment::class.java)
                finish()
            }
        }
        setRefreshEnable(false)
        mIvSearchQuestionBack.setOnClickListener { finish() }
        mTvSearchQuestionCancel.setOnClickListener {
            if (!TextUtils.isEmpty(mEtSearchQuestionContent.text.toString())) {
                finish()
            }
        }
        mEtSearchQuestionContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mTvSearchQuestionCancel.isEnabled = !(s != null && TextUtils.isEmpty(s.toString()))
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    if (!isLoading) {
                        keyword = s.toString()
                        isLoading = true
                        mEtSearchQuestionContent.postDelayed({
                            onRefresh()
                        }, 1000)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        mEtSearchQuestionContent.postDelayed({
            val inputMethod = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.showSoftInput(mEtSearchQuestionContent, 0)
        }, 100)
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
        if (!TextUtils.isEmpty(keyword)) {
            mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_SEARCH, RequestParamsHelper.getQuizSearchParam(keyword!!, currentPage))
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        isLoading = false
        processList(checkResultCode(result), IndexAskInfoBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentItem = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "问题详情", true, false, bundleOf(Pair(AnswerInfoFragment.ASK_ID_FLAG, currentItem.quiz_id)), AnswerInfoFragment::class.java)
            finish()
        } else {
            UserInfo.getInstance().loginTag = 0x25
            LoginFragment.startLogin(mContext)
        }
    }

}