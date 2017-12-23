package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.WXUserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.ApiParams
import com.android.ql.lf.electronicbusiness.utils.Constants
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.google.gson.Gson
import com.hyphenate.chat.ChatClient
import com.hyphenate.chat.Message
import com.hyphenate.chat.adapter.EMAChatManager
import com.hyphenate.helpdesk.callback.Callback
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.fragment_login_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.regex.Pattern


/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class LoginFragment : BaseNetWorkingFragment() {

    companion object {
        fun startLogin(context: Context) {
            FragmentContainerActivity.startFragmentContainerActivity(context, "", true, true, LoginFragment::class.java)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_login_layout

    private lateinit var iwxApi: IWXAPI

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(BaseResp::class.java).subscribe {
            if (it is SendAuth.Resp) {
                val param = ApiParams()
                param.addParam("code", it.code)
                mPresent.getDataByGet(0x1, "t", "wxlogin", param)
            }
        }

        mLoginRegister.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "注册", true, true, RegisterFragment::class.java)
        }
        mLoginForgetPassword.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "忘记密码", true, true, ForgetPasswordFragment::class.java)
        }
        mClearPhone.setOnClickListener {
            mEtPhone.setText("")
        }
        mBack.setOnClickListener {
            finish()
        }
        mBtLogin.setOnClickListener {
            if (TextUtils.isEmpty(mEtPhone.text.toString())) {
                toast("手机号不能为空")
                return@setOnClickListener
            }
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtPhone.text.toString())) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEtPassword.text.toString())) {
                toast("密码不能为空")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_LOGIN, RequestParamsHelper.getLoginParams(mEtPhone.text.toString(), mEtPassword.text.toString()))
        }
        mIvLoginWx.setOnClickListener {
            iwxApi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID, true)
            iwxApi.registerApp(Constants.WX_APP_ID)
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "wechat_sdk_ql_bs"
            iwxApi.sendReq(req)
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在登录……")
        progressDialog.show()
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("登录失败，请稍后重试……")
    }

    override fun onRequestEnd(requestID: Int) {
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                try {
                    val json = JSONObject(result.toString())
                    val code = json.optString("code")
                    if ("200" == code) {
                        onLoginSuccess(json.optJSONObject("result"))
                    } else {
                        toast(json.optString("msg"))
                    }
                } catch (e: Exception) {
                    toast("登录失败，请稍后重试")
                }
            }
            0x1 -> {
                val json = checkResultCode(result)
                if (json != null) {
                    val jsonObject = json.optJSONObject("result")
                    val memberId = jsonObject.optString("member_id")
                    if (TextUtils.isEmpty(memberId)) {
                        val wxUserInfo = Gson().fromJson(jsonObject.toString(), WXUserInfo::class.java)
                        FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, bundleOf(Pair(WXCompleteDataFragment.WX_USER_INFO_FLAG, wxUserInfo)), WXCompleteDataFragment::class.java)
                        finish()
                    } else {
                        onLoginSuccess(jsonObject)
                    }
                }
            }
        }
    }

    private fun LoginFragment.onLoginSuccess(userJson: JSONObject) {
        UserInfo.parseUserInfo(mContext, userJson)
        RxBus.getDefault().post(UserInfo.getInstance())
        if (!ChatClient.getInstance().isLoggedInBefore) {
            loginHx()
        }
    }

    private fun loginHx() {
        ChatClient.getInstance().login(UserInfo.getInstance().member_hxname, UserInfo.getInstance().member_hxpw, object : Callback {
            override fun onSuccess() {
                close()
            }

            override fun onProgress(p0: Int, p1: String?) {
            }

            override fun onError(p0: Int, p1: String?) {
                close()
            }
        })
    }

    private fun close() {
        mContext.runOnUiThread {
            if (progressDialog != null && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
            finish()
        }
    }


}