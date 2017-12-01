package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CodeBean
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.CounterHelper
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_register_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class RegisterFragment : BaseNetWorkingFragment() {

    var mCode: String = ""

    private lateinit var counterHelper: CounterHelper

    companion object {
        val REGEX_MOBILE = "^1[34578]\\d{9}\$"
    }

    override fun getLayoutId(): Int = R.layout.fragment_register_layout

    override fun initView(view: View?) {
        counterHelper = CounterHelper()
        counterHelper.onTick = { millisUntilFinished ->
            mCodeGet.text = "剩余(${millisUntilFinished / 1000}秒)"
        }
        counterHelper.onFinish = {
            mCodeGet.text = "没有收到验证码？"
            mCodeGet.isEnabled = true
        }
        mBack.setOnClickListener { finish() }
        mCodeGet.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("手机号不能为空")
                return@setOnClickListener
            }
            if (!Pattern.matches(REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            counterHelper.start()
            mCodeGet.isEnabled = false
            mPresent.getDataByPost(0x0, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtPhone.text.toString()))
        }
        mClearPhone.setOnClickListener {
            mEtPhone.setText("")
        }
        mBtRegister.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("手机号不能为空")
                return@setOnClickListener
            }
            if (!Pattern.matches(REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtCode.text.toString())) {
                toast("验证码不能为空")
                return@setOnClickListener
            }
            if (mCode != mEtCode.text.toString()) {
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtPassword.text.toString())) {
                toast("密码不能为空")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_REGISTER,
                    RequestParamsHelper.getRegisterParams(mEtPhone.text.toString(), mEtPassword.text.toString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            progressDialog = MyProgressDialog(mContext, "正在注册，请稍后……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
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

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("注册失败，请稍后重试……")
    }


    override fun onDestroyView() {
        counterHelper.stop()
        super.onDestroyView()
    }

}