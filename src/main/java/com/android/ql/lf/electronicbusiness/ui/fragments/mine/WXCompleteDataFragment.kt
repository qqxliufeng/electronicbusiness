package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CodeBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.WXUserInfo
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.CounterHelper
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.google.gson.Gson
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.callback.Callback
import kotlinx.android.synthetic.main.fragment_wx_complete_data_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * Created by lf on 2017/11/28 0028.
 * @author lf on 2017/11/28 0028
 */
class WXCompleteDataFragment : BaseNetWorkingFragment() {


    companion object {
        val WX_USER_INFO_FLAG = "wx_user_info_flag"
    }

    private var mCode: String = ""

    private lateinit var counterHelper: CounterHelper

    private val wxUserInfo: WXUserInfo by lazy {
        arguments.classLoader = this.javaClass.classLoader
        arguments.getParcelable<WXUserInfo>(WX_USER_INFO_FLAG)
    }

    override fun getLayoutId() = R.layout.fragment_wx_complete_data_layout

    override fun initView(view: View?) {
        counterHelper = CounterHelper()
        counterHelper.onTick = { millisUntilFinished ->
            mCodeGet.text = "剩余(${millisUntilFinished / 1000}秒)"
        }
        counterHelper.onFinish = {
            mCodeGet.text = "没有收到验证码？"
            mCodeGet.isEnabled = true
        }
        mTvWxCompleteDataTitle.text = "完善资料 - ${wxUserInfo.nickname}"

        mBack.setOnClickListener { finish() }
        mCodeGet.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("手机号不能为空")
                return@setOnClickListener
            }
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            mCodeGet.isEnabled = false
            counterHelper.start()
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
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtPhone.text.toString())) {
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
            mPresent.getDataByPost(0x1, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_WX_PERFECT,
                    RequestParamsHelper.getWXCompleteDataParam(mEtPhone.text.toString(), wxUserInfo.headimgurl, wxUserInfo.openid, wxUserInfo.nickname))
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
            }else{
                getCodeFailed()
            }
        } else if (requestID == 0x1) {
            val json = JSONObject(result.toString())
            val code = json.optString("code")
            if ("200" == code) {
                onLoginSuccess(json)
            } else {
                toast(json.optString("msg"))
                counterHelper.stop()
                mCodeGet.isEnabled = true
                mCodeGet.text = "获取验证码"
            }
        }
    }

    private fun onLoginSuccess(json: JSONObject) {
        val memId = json.optString("result")
        UserInfo.getInstance().memberId = memId
        UserInfo.parseUserInfo(mContext,json.optJSONObject("arr"))
        RxBus.getDefault().post(UserInfo.getInstance())
        if (ChatClient.getInstance().isLoggedInBefore) {
            ChatClient.getInstance().logout(true, object : Callback {
                override fun onSuccess() {
                    loginHx()
                }
                override fun onProgress(p0: Int, p1: String?) {
                }
                override fun onError(p0: Int, p1: String?) {
                    Log.e("TAG", "logout --> "+p1)
                }
            })
        }else{
            loginHx()
        }
        finish()
    }

    private fun loginHx(){
        ChatClient.getInstance().login(UserInfo.getInstance().member_hxname, UserInfo.getInstance().member_hxpw, object : Callback {
            override fun onSuccess() {
            }
            override fun onProgress(p0: Int, p1: String?) {
            }
            override fun onError(p0: Int, p1: String?) {
            }
        })
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("完善资料失败，请稍后重试……")
        } else {
            getCodeFailed()
            counterHelper.stop()
        }
    }

    private fun getCodeFailed() {
        toast("获取验证码失败")
        mCodeGet.text = "获取验证码"
        mCodeGet.isEnabled = true
    }

    override fun onDestroyView() {
        counterHelper.stop()
        super.onDestroyView()
    }

}