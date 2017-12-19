package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_feed_back_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class FeedBackFragment : BaseNetWorkingFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_feed_back_layout

    override fun initView(view: View?) {
        mBtFeedBackSubmit.setOnClickListener {
            if (TextUtils.isEmpty(mEtFeedBackContent.text.toString())) {
                toast("请输入留言内容")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_ADD_IDEA,
                    RequestParamsHelper.getAddIdeaParam(mEtFeedBackContent.text.toString(), mEtFeedBackPhone.text.toString()))
        }
        mTvFeedBackKFPhone.text = "客服电话：加载中……"
        mTvFeedBackKFPhone.isEnabled = false
        mPresent.getDataByPost(0x1, "t", "kf_tel", RequestParamsHelper.getKeFuTelParams())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            progressDialog = MyProgressDialog(context, "正在提交……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val json = checkResultCode(result)
            if (json != null) {
                toast("提交成功，感谢您给我们留下宝贵意见")
                finish()
            } else {
                toast("提交失败")
            }
        } else {
            val json = JSONObject(result.toString())
            if ("请求成功" == json.optString("msg")) {
                mTvFeedBackKFPhone.isEnabled = true
                mTvFeedBackKFPhone.text = "客服电话：${json.optString("code")}"
            } else {
                mTvFeedBackKFPhone.text = "客服电话：加载失败"
            }
        }
    }


    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            toast("提交失败")
        }else{
            mTvFeedBackKFPhone.text = "客服电话：加载失败"
        }
    }
}