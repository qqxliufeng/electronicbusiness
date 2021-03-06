package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.IntegrationMallFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.*
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.*
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.Error
import com.hyphenate.helpdesk.callback.Callback
import kotlinx.android.synthetic.main.fragment_main_mine_layout.*
import org.jetbrains.anko.support.v4.toast
import q.rorbin.badgeview.QBadgeView
import rx.Subscription

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class MainMineFragment : BaseNetWorkingFragment() {

    private lateinit var loginSubscribe: Subscription
    private lateinit var qBadgeSubScribe: Subscription

    private var badge0: QBadgeView? = null //侍付款
    private var badge1: QBadgeView? = null //待发货
    private var badge2: QBadgeView? = null //待收货
    private var badge3: QBadgeView? = null //待评价
    private var badge4: QBadgeView? = null //未读消息

    companion object {
        fun newInstance() = MainMineFragment()
        val REFRESH_QBADGE_VIEW_FLAG = "refresh QBadgeView"
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_mine_layout

    override fun initView(view: View?) {
        loginSubscribe = RxBus.getDefault().toObservable(UserInfo::class.java).subscribe {
            loadUserInfo()
            when (UserInfo.getInstance().loginTag) {
                1 -> { //点击个人信息返回的结果
                    mPersonalInfo.performClick()
                }
                2 -> {//我的消息
                    mMainMineMessage.performClick()
                }
                3 -> {//签到
                    mTvMainMineSign.performClick()
                }
                4 -> {//我的积分
                    mIntegrationDetailContainer.performClick()
                }
                5 -> {//我的问答
                    mAskAnswer.performClick()
                }
                6 -> {//我的订单
                    mAllOrder.performClick()
                }
                7 -> { //购物车
                    mShoppingCar.performClick()
                }
                8 -> { //积分商城
                    mIntegrationMall.performClick()
                }
                9 -> { //意见反馈
                    mFeedBack.performClick()
                }
                10 -> { //设置
                    mSetting.performClick()
                }
                11 -> {//待付款
                    mDFKOrder.performClick()
                }
                12 -> {//待发货
                    mDFHOrder.performClick()
                }
                13 -> {//待收货
                    mWaitingGoods.performClick()
                }
                14 -> {//待评价
                    mSuccessOrder.performClick()
                }
                15 -> {//我的砍价
                    mCutPrice.performClick()
                }
                16 -> { //会员充值
                    mVipContainer.performClick()
                }
            }
        }
        qBadgeSubScribe = RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (RefreshData.isRefresh && RefreshData.any == REFRESH_QBADGE_VIEW_FLAG) {
                mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PERSONAL, RequestParamsHelper.getPersonal())
            }
        }
        mMineTopView.alpha = 0.0f
        val heightPixels = context.resources.displayMetrics.heightPixels.toFloat()
        mMineScrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            val alpha = 1 - scrollY / (heightPixels / 3)// 0~1  透明度是1~0
            mMineTopView.alpha = 1 - alpha
        }
        mSrlMainMineContainer.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        mSrlMainMineContainer.setOnRefreshListener {
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PERSONAL, RequestParamsHelper.getPersonal())
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
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "VIP会员", true, false, VipInfoFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 16
                LoginFragment.startLogin(mContext)
            }
        }
        mIntegrationDetailContainer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分明细", true, false, IntegrationDetailFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 4
                LoginFragment.startLogin(mContext)
            }
        }
        mSetting.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "设置", true, false, SettingFragment::class.java)
        }
        mMainMineMessage.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "消息", true, false, MessageListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 2
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "", true, true, LoginFragment::class.java)
            }
        }
        mIntegrationCountContainer.setOnClickListener {
            if (!UserInfo.getInstance().isLogin) {
                UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
                LoginFragment.startLogin(mContext)
            } else {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分明细", true, false, IntegrationDetailFragment::class.java)
            }
        }
        mIntegrationMall.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分商城", true, false, IntegrationMallFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 8
                LoginFragment.startLogin(mContext)
            }
        }
        mCutPrice.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的砍价", true, false, CutPriceListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 15
                LoginFragment.startLogin(mContext)
            }
        }
        mFeedBack.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "意见反馈", true, false, FeedBackFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 9
                LoginFragment.startLogin(mContext)
            }
        }
        mShoppingCar.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "购物车", true, false, ShoppingCarFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 7
                LoginFragment.startLogin(context)
            }
        }
        mAllOrder.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "所有订单", true, false, OrderListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 6
                LoginFragment.startLogin(mContext)
            }
        }
        mDFKOrder.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                val bundle = Bundle()
                bundle.putString(OrderListFragment.ORDER_STATUE_FLAG, OrderPresent.OrderStatus.STATUS_OF_DFK.toString())
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "待付款", true, false, bundle, OrderListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 11
                LoginFragment.startLogin(mContext)
            }
        }
        mDFHOrder.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                val bundle = Bundle()
                bundle.putString(OrderListFragment.ORDER_STATUE_FLAG, OrderPresent.OrderStatus.STATUS_OF_DFH.toString())
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "待发货", true, false, bundle, OrderListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 12
                LoginFragment.startLogin(mContext)
            }
        }
        mWaitingGoods.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                val bundle = Bundle()
                bundle.putString(OrderListFragment.ORDER_STATUE_FLAG, OrderPresent.OrderStatus.STATUS_OF_DSH.toString())
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "待收货", true, false, bundle, OrderListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 13
                LoginFragment.startLogin(mContext)
            }
        }
        mSuccessOrder.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                val bundle = Bundle()
                bundle.putString(OrderListFragment.ORDER_STATUE_FLAG, OrderPresent.OrderStatus.STATUS_OF_DPJ.toString())
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "待评价", true, false, bundle, OrderListFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 14
                LoginFragment.startLogin(mContext)
            }
        }
        mAskAnswer.setOnClickListener {
            if (UserInfo.getInstance().isLogin) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的问答", true, false, AskAndAnswerFragment::class.java)
            } else {
                UserInfo.getInstance().loginTag = 5
                LoginFragment.startLogin(mContext)
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
                LoginFragment.startLogin(mContext)
            }
        }
        loadUserInfo()
    }

    override fun onResume() {
        super.onResume()
        if (UserInfo.getInstance().isLogin) {
            //更新积分
            mTvMainMineIntegration.text = UserInfo.getInstance().memberIntegral
            mTvMainMineNickName.text = UserInfo.getInstance().memberName
            //1 代表会员  0 代表非会员
            mTvMainMineNickName.setCompoundDrawablesWithIntrinsicBounds(0, 0, if ("1" == UserInfo.getInstance().memberRank) R.drawable.img_icon_vip_s else 0, 0)
        }
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
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                val arrJsonObject = json.optJSONObject("arr")
                val s0 = arrJsonObject.optString("s0")
                if ("0" != s0) {
                    if (badge0 == null) {
                        badge0 = BadgeViewFactory.createBadge(mContext, mDFKOrder, s0.toInt(), 10.0f, 0f, true)
                    }
                    badge0!!.badgeNumber = s0.toInt()
                } else {
                    if (badge0 != null) {
                        badge0!!.hide(false)
                    }
                }
                val s1 = arrJsonObject.optString("s1")
                if ("0" != s1) {
                    if (badge1 == null) {
                        badge1 = BadgeViewFactory.createBadge(mContext, mDFHOrder, s1.toInt(), 10.0f, 0f, true)
                    }
                    badge1!!.badgeNumber = s1.toInt()
                } else {
                    if (badge1 != null) {
                        badge1!!.hide(false)
                    }
                }
                val s2 = arrJsonObject.optString("s2")
                if ("0" != s2) {
                    if (badge2 == null) {
                        badge2 = BadgeViewFactory.createBadge(mContext, mWaitingGoods, s2.toInt(), 10.0f, 0f, true)
                    }
                    badge2!!.badgeNumber = s2.toInt()
                } else {
                    if (badge2 != null) {
                        badge2!!.hide(false)
                    }
                }
                val s3 = arrJsonObject.optString("s3")
                if ("0" != s3) {
                    if (badge3 == null) {
                        badge3 = BadgeViewFactory.createBadge(mContext, mSuccessOrder, s3.toInt(), 10.0f, 0f, true)
                    }
                    badge3!!.badgeNumber = s3.toInt()
                } else {
                    if (badge3 != null) {
                        badge3!!.hide(false)
                    }
                }
                val s4 = arrJsonObject.optString("s4")
                if ("0" != s4) {
                    if (badge4 == null) {
                        badge4 = BadgeViewFactory.createBadge(mContext, mMainMineMessage, -1, 40.0f, 0f, true)
                    }
                    badge4!!.badgeNumber = -1
                } else {
                    if (badge4 != null) {
                        badge4!!.hide(false)
                    }
                }
            }
            0x1 -> {
                if (json != null) {
                    toast(json.optString("msg"))
                    val result = json.optString("result")
                    if (!TextUtils.isEmpty(result)) {
                        UserInfo.getInstance().memberIntegral = result
                        mTvMainMineIntegration.text = UserInfo.getInstance().memberIntegral
                    }
                } else {
                    toast("签到失败，请稍后重试……")
                }
            }
        }
    }

    private fun setBadgeView(targetView: View, badge: QBadgeView, count: Int) {
        val bindTarget = badge.bindTarget(targetView)
        bindTarget.setGravityOffset(10.0f, 0f, true)
        bindTarget.badgeNumber = count
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("签到失败，请稍后重试……")
        }
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        if (mSrlMainMineContainer.isRefreshing) {
            mSrlMainMineContainer.post {
                mSrlMainMineContainer.isRefreshing = false
            }
        }
    }

    private fun loadUserInfo() {
        if (UserInfo.getInstance().isLogin) {
            GlideManager.loadFaceCircleImage(mContext, Constants.BASE_IP + UserInfo.getInstance().memberPic, mIvMainMineFace)
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PERSONAL, RequestParamsHelper.getPersonal())
            registerHX()
        } else {
            if (badge0 != null) {
                badge0!!.hide(false)
                badge0 = null
            }
            if (badge1 != null) {
                badge1!!.hide(false)
                badge1 = null
            }
            if (badge2 != null) {
                badge2!!.hide(false)
                badge2 = null
            }
            if (badge3 != null) {
                badge3!!.hide(false)
                badge3 = null
            }
            if (badge4 != null) {
                badge4!!.hide(false)
                badge4 = null
            }
            mIvMainMineFace.setImageResource(R.drawable.pic_headportrait)
        }
        mTvMainMineIntegration.text = if (UserInfo.getInstance().isLogin && !TextUtils.isEmpty(UserInfo.getInstance().memberIntegral) && "null" != UserInfo.getInstance().memberIntegral) UserInfo.getInstance().memberIntegral else "0"
        mTvMainMineNickName.text = if (UserInfo.getInstance().isLogin) UserInfo.getInstance().memberName else "登录/注册"
        //1 代表会员  0 代表非会员
        mTvMainMineNickName.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (UserInfo.getInstance().isLogin && "1" == UserInfo.getInstance().memberRank) R.drawable.img_icon_vip_s else 0, 0)
    }

    /**
     * 注册帐号到环信
     */
    private fun registerHX() {
        //注册环信
        ChatClient.getInstance().register(UserInfo.getInstance().member_hxname, UserInfo.getInstance().member_hxpw, object : Callback {
            override fun onSuccess() {
                //注册成功，直接登录
                Log.e("TAG", "直接登录")
                UserInfo.loginHx()
            }

            override fun onProgress(p0: Int, p1: String?) {
            }

            override fun onError(p0: Int, p1: String?) {
                //注册失败，若是已经注册，直接登录
                if (p0 == Error.USER_ALREADY_EXIST) {
                    Log.e("TAG", "已经注册，直接登录")
                    UserInfo.loginHx()
                }
            }
        })
    }


    override fun onDestroyView() {
        if (!loginSubscribe.isUnsubscribed) {
            loginSubscribe.unsubscribe()
        }
        if (!qBadgeSubScribe.isUnsubscribed) {
            qBadgeSubScribe.unsubscribe()
        }
        super.onDestroyView()
    }

}