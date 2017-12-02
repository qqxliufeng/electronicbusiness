package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CodeBean
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.CounterHelper
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_forget_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class ForgetPasswordFragment : BaseNetWorkingFragment() {

    private var mCode = ""

    private lateinit var counterHelper: CounterHelper

    override fun getLayoutId(): Int = R.layout.fragment_forget_password_layout

    override fun initView(view: View?) {
        counterHelper = CounterHelper()
        counterHelper.onTick = { millisUntilFinished ->
            mCodeGet.text = "剩余(${millisUntilFinished / 1000}秒)"
        }
        counterHelper.onFinish = {
            mCodeGet.text = "没有收到验证码？"
            mCodeGet.isEnabled = true
        }

        mClearPhone.setOnClickListener { mEtPhone.setText("") }
        mBack.setOnClickListener {
            finish()
        }
        mCodeGet.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("手机号不能为空")
                return@setOnClickListener
            }
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            counterHelper.start()
            mCodeGet.isEnabled = false
            mPresent.getDataByPost(0x0, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtPhone.text.toString()))
        }
        mBtSubmit.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtCode.text.toString())) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (mCode != mEtCode.text.toString()) {
                toast("验证码不正确")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtPassword.text.toString())) {
                toast("请输入密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtPasswordConfirm.text.toString())) {
                toast("请输入确认密码")
                return@setOnClickListener
            }
            if (mEtPassword.text.toString() != mEtPasswordConfirm.text.toString()) {
                toast("两次验证码不一致")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_FORGETPW, RequestParamsHelper.getForgetPWParams(mEtPhone.text.toString(), mEtPassword.text.toString()))
        }
    }


    override fun onRequestStart(requestID: Int) {
        progressDialog = MyProgressDialog(mContext, "正在修改密码……")
        progressDialog.show()
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        toast("修改失败，请稍后重试")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            Log.e("TAG", result.toString())
            val codeBean: CodeBean = Gson().fromJson(result.toString(), CodeBean::class.java)
            if ("200" == codeBean.status) {
                mCode = codeBean.code
                toast("验证码已经发送，请注意查收")
            }
        } else if (requestID == 0x1) {
            val json = JSONObject(result.toString())
            val code = json.optString("code")
            toast(json.optString("msg"))
            if ("200" == code) {
                toast("${json.optString("msg")},请登录")
                finish()
            } else {
                toast(json.optString("msg"))
            }
        }
    }

    override fun onDestroyView() {
        counterHelper.stop()
        super.onDestroyView()
    }

}