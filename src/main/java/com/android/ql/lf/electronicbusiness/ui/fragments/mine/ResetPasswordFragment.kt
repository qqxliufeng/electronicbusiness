package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_reset_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

/**
 * Created by lf on 2017/11/24 0024.
 * @author lf on 2017/11/24 0024
 */
class ResetPasswordFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_reset_password_layout

    override fun initView(view: View?) {
        mBtEtResetPasswordSave.setOnClickListener {
            if (TextUtils.isEmpty(mEtResetPasswordOldPW.text.toString())) {
                toast("请输入原密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtResetPasswordNewOne.text.toString())) {
                toast("请输入新密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtResetPasswordNewTwo.text.toString())) {
                toast("请再次输入新密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewOne.text.toString() != mEtResetPasswordNewTwo.text.toString()) {
                toast("两次密码不一致")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_RESET_PW,
                    RequestParamsHelper.getResetPassword(mEtResetPasswordOldPW.text.toString(), mEtResetPasswordNewOne.text.toString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在修改密码……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val jsonObject = JSONObject(result.toString())
        if ("200" != jsonObject.optString("code")) {
            toast(jsonObject.optString("msg"))
        } else {
            toast("${jsonObject.optString("msg")},请牢记新密码！")
            finish()
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("修改失败，请稍后重试")
    }

}