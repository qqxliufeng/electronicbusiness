package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_add_new_ask_layout.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AddNewAskFragment : BaseNetWorkingFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_add_new_ask_layout

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.add_new_ask_menu, menu)
    }

    override fun initView(view: View?) {
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuNext) {
            if (TextUtils.isEmpty(mEtAddNewAskTitle.text.toString())) {
                toast("请输入问题标题")
                return false
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_ISQUIZ, RequestParamsHelper.getIsquizParams(mEtAddNewAskTitle.text.toString()))
        }
        return true
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在检测是否有敏感词……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            val bundle = Bundle()
            bundle.putString(AddNewAskNextStepFragment.TITLE_FLAG, mEtAddNewAskTitle.text.toString())
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提问", true, false, bundle, AddNewAskNextStepFragment::class.java)
            finish()
        } else {
            toast("标题中含有敏感词，请查正")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("提交失败，请稍后重试……")
    }

}