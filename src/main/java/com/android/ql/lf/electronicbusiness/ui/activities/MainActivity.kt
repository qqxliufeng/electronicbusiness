package com.android.ql.lf.electronicbusiness.ui.activities

import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainAskAndAnswerFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainCutPrivilegeFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainMineFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainVipPrivilegeFragment
import com.android.ql.lf.electronicbusiness.utils.BottomNavigationViewHelper
import com.hyphenate.chat.ChatClient
import com.hyphenate.chat.ChatManager
import com.hyphenate.chat.Message
import com.hyphenate.helpdesk.Error
import com.hyphenate.helpdesk.callback.Callback
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import q.rorbin.badgeview.QBadgeView

class MainActivity : BaseActivity(), ViewPager.OnPageChangeListener {


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        BottomNavigationViewHelper.disableShiftMode(mMainNavigation)
        mMainNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mMainViewPager.adapter = MyInnerViewPagerAdapter(supportFragmentManager)
        mMainViewPager.offscreenPageLimit = 4
//        testBadgeView()
//        loginHX()
    }

    private fun testBadgeView() {
        val menuView = mMainNavigation.getChildAt(0) as BottomNavigationMenuView
        (0 until menuView.childCount).forEach {
            val badgeView = QBadgeView(this)
            val bindTarget = badgeView.bindTarget(menuView.getChildAt(it))
            bindTarget.setGravityOffset(5.0f, 5.0f, true)
            bindTarget.badgeNumber = it - 1
        }
    }

    private fun loginHX() {
        ChatClient.getInstance().chatManager().addMessageListener(object : ChatManager.MessageListener {
            override fun onMessage(p0: MutableList<Message>?) {
            }

            override fun onMessageSent() {
            }

            override fun onCmdMessage(p0: MutableList<Message>?) {
            }

            override fun onMessageStatusUpdate() {
            }
        })
        ChatClient.getInstance().register("test", "123456", object : Callback {

            override fun onSuccess() {
                toast("注册成功")
            }

            override fun onProgress(p0: Int, p1: String?) {
            }

            override fun onError(errorCode: Int, p1: String?) {
                when (errorCode) {
                    Error.NETWORK_ERROR -> {
                        toast("网络联接错误")
                    }
                    Error.USER_ALREADY_EXIST -> {
                        ChatClient.getInstance().login("test", "123456", object : Callback {
                            override fun onSuccess() {
                                runOnUiThread {
                                    val message = Message.createTxtSendMessage("hello", "kefuchannelimid_866700")
                                    ChatClient.getInstance().chatManager().sendMessage(message, object : Callback {
                                        override fun onSuccess() {
                                            runOnUiThread {
                                                toast("发送成功")
                                            }
                                        }

                                        override fun onProgress(p0: Int, p1: String?) {
                                        }

                                        override fun onError(p0: Int, p1: String?) {
                                            runOnUiThread {
                                                toast("发送失败$p0  $p1")
                                            }
                                        }
                                    })
                                }
                            }

                            override fun onProgress(p0: Int, p1: String?) {
                            }

                            override fun onError(p0: Int, p1: String?) {
                            }
                        })
                    }
                    Error.USER_AUTHENTICATION_FAILED -> {
                        toast("无开放注册权限")
                    }
                    Error.USER_ILLEGAL_ARGUMENT -> {
                        toast("用户名非法")
                    }
                    else -> {
                        toast("其它错误$errorCode")
                    }
                }
            }
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                mMainViewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                mMainViewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                mMainViewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                mMainViewPager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    class MyInnerViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

        override fun getItem(position: Int): Fragment? = when (position) {
            0 -> MainAskAndAnswerFragment.newInstance()
            1 -> MainCutPrivilegeFragment.newInstance()
            2 -> MainVipPrivilegeFragment.newInstance()
            3 -> MainMineFragment.newInstance()
            else -> {
                null
            }
        }

        override fun getCount() = 4
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> mMainNavigation.selectedItemId = R.id.navigation_home
            1 -> mMainNavigation.selectedItemId = R.id.navigation_dashboard
            2 -> mMainNavigation.selectedItemId = R.id.navigation_notifications
            3 -> mMainNavigation.selectedItemId = R.id.navigation_message
        }
    }

    override fun onDestroy() {
        UserInfo.getInstance().loginOut()
        super.onDestroy()
    }

}
