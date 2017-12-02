package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.IntegrationMallFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.*
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.Constants
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import kotlinx.android.synthetic.main.activity_fragment_container_layout.*
import kotlinx.android.synthetic.main.fragment_main_mine_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import q.rorbin.badgeview.QBadgeView
import rx.Subscription

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MainMineFragment : BaseNetWorkingFragment() {

    lateinit var subscribe: Subscription

    private var badge0: QBadgeView? = null

    companion object {
        fun newInstance() = MainMineFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_mine_layout

    override fun initView(view: View?) {
        subscribe = RxBus.getDefault().toObservable(UserInfo::class.java).subscribe {
            loadUserInfo()
            when (UserInfo.getInstance().loginTag) {
                1 -> { //点击个人信息返回的结果
                    mPersonalInfo.performClick()
                }
                2 -> {
                    mMainMineMessage.performClick()
                }
                3 -> {
                    mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_SIGNIN, RequestParamsHelper.getSigninParams())
                }
                4 -> {
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分明细", true, false, IntegrationDetailFragment::class.java)
                }
                5 -> {
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的问答", true, false, AskAndAnswerFragment::class.java)
                }
            }
        }
        mMineTopView.alpha = 0.0f
        val heightPixels = context.resources.displayMetrics.heightPixels.toFloat()
        mMineScrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            val alpha = 1 - scrollY / (heightPixels / 3)// 0~1  透明度是1~0
            mMineTopView.alpha = 1 - alpha
        }
        mPersonalInfo.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "个人信息", true, false, PersonalInfoFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 1
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mVipContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "VIP会员", true, false, VipInfoFragment::class.java)
        }
        mIntegrationDetailContainer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分明细", true, false, IntegrationDetailFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 4
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mSetting.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "设置", true, false, SettingFragment::class.java)
        }
        mMainMineMessage.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "系统消息", true, false, MessageListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 2
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mWaitingGoods.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "物流信息", true, false, ExpressInfoFragment::class.java)
        }
        mIntegrationCountContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "登录", true, true, LoginFragment::class.java)
        }
        mIntegrationMall.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分商城", true, false, IntegrationMallFragment::class.java)
        }
        mCutPrice.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的砍价", true, false, CutPriceListFragment::class.java)
        }
        mFeedBack.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "意见反馈", true, false, FeedBackFragment::class.java)
        }
        mShoppingCar.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "购物车", true, false, ShoppingCarFragment::class.java)
        }
        mAllOrder.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "所有订单", true, false, OrderListFragment::class.java)
        }
        mDFKOrder.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "待付款", true, false, OrderListFragment::class.java)
        }
        mDFHOrder.setOnClickListener {
            //            FragmentContainerActivity.startFragmentContainerActivity(mContext, "待发货", true, false, OrderListFragment::class.java)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "待发货", true, false, AddressManagerFragment::class.java)
        }
        mWaitingGoods.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "待收货", true, false, OrderListFragment::class.java)
        }
        mSuccessOrder.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "待评价", true, false, OrderListFragment::class.java)
        }
        mAskAnswer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的问答", true, false, AskAndAnswerFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 5
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mLlMainMineUserInfoContainer.setOnClickListener {
            if (!UserInfo.getInstance().isLogin) {
                UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mTvMainMineSign.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_SIGNIN, RequestParamsHelper.getSigninParams())
            } else {
                UserInfo.getInstance().loginTag = 3
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        loadUserInfo()
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            progressDialog = MyProgressDialog(mContext, "正在签到……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                if (result != null) {
                    val jsonObject = JSONObject(result.toString())
                    if ("200" == jsonObject.optString("code")) {
                        val arrJsonObject = jsonObject.optJSONObject("arr")
                        val s0 = arrJsonObject.optString("s0")
                        if ("0" != s0) {
                            badge0 = QBadgeView(mContext)
                            badge0!!.bindTarget(mDFKOrder).badgeNumber = s0.toInt()
                        }
                        val s1 = arrJsonObject.optString("s1")
                        if ("0" != s1) {
                            QBadgeView(mContext).bindTarget(mDFHOrder).badgeNumber = s1.toInt()
                        }
                        val s2 = arrJsonObject.optString("s2")
                        if ("0" != s2) {
                            QBadgeView(mContext).bindTarget(mWaitingGoods).badgeNumber = s2.toInt()
                        }
                        val s3 = arrJsonObject.optString("s3")
                        if ("0" != s3) {
                            QBadgeView(mContext).bindTarget(mSuccessOrder).badgeNumber = s3.toInt()
                        }
                    }
                }
            }
            0x1 -> {
                val json = checkResultCode(result)
                if (json != null) {
                    toast(json.optString("msg"))
                } else {
                    toast("签到失败，请稍后重试……")
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("签到失败，请稍后重试……")
        }
    }

    private fun loadUserInfo() {
        if (UserInfo.getInstance().isLogin) {
            GlideManager.loadFaceCircleImage(mContext, Constants.BASE_IP + UserInfo.getInstance().memberPic, mIvMainMineFace)
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PERSONAL, RequestParamsHelper.getPersonal())
        } else {
            badge0?.hide(true)
            mIvMainMineFace.setImageResource(R.drawable.pic_headportrait)
        }
        mTvMainMineIntegration.text = if (UserInfo.getInstance().isLogin && !TextUtils.isEmpty(UserInfo.getInstance().memberIntegral) && "null" != UserInfo.getInstance().memberIntegral) UserInfo.getInstance().memberIntegral else "0"
        mTvMainMineNickName.text = if (UserInfo.getInstance().isLogin) UserInfo.getInstance().memberName else "登录/注册"
        //1 代表会员  0 代表非会员
        mTvMainMineNickName
                .setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        if (UserInfo.getInstance().isLogin && "1" == UserInfo.getInstance().memberRank) R.drawable.img_icon_vip_s else 0,
                        0
                )
    }

    override fun onDestroyView() {
        if (!subscribe.isUnsubscribed) {
            subscribe.unsubscribe()
        }
        super.onDestroyView()
    }

}