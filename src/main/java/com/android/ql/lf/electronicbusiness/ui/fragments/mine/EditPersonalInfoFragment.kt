package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.android.ql.lf.electronicbusiness.utils.replaceBlank
import kotlinx.android.synthetic.main.fragment_edit_personal_info_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

/**
 * Created by lf on 2017/11/21 0021.
 * @author lf on 2017/11/21 0021
 */
class EditPersonalInfoFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_edit_personal_info_layout

    override fun initView(view: View?) {
        if (arguments != null) {
            val nick = arguments[PersonalInfoFragment.HINT_KEY] as String
            mEtPersonalInfoContent.hint = "$nick(最大输入6个字符)"
        }
        mBtPersonalInfoSave.setOnClickListener {
            if (TextUtils.isEmpty(mEtPersonalInfoContent.text)) {
                toast(mEtPersonalInfoContent.hint)
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_EDIT_PERSONAL,
                    RequestParamsHelper.getEditPersonalParams(mEtPersonalInfoContent.text.toString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在提交……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result.toString())
        if (json != null) {
            toast(json.optString("msg"))
            UserInfo.getInstance().memberName = json.optJSONObject("result").optString("member_name")
            UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
            RxBus.getDefault().post(UserInfo.getInstance())
            finish()
        } else {
            toast("保存失败")
        }
    }


}