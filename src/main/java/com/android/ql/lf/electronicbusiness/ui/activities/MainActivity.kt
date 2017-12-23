package com.android.ql.lf.electronicbusiness.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.Log
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainAskAndAnswerFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainCutPrivilegeFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainMineFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainVipPrivilegeFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.CutGoodsInfoFragment
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

    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        //从第三方跳转来的
        startGoodsInfo(intent)
    }

    private fun startGoodsInfo(intent: Intent?) {
        if (intent != null) {
            if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
                val param = intent.data.getQueryParameter("gid")
                val bundle = Bundle()
                if (!TextUtils.isEmpty(param)) {
                    bundle.putString(CutGoodsInfoFragment.GOODS_ID_FLAG, param)
                    FragmentContainerActivity.startFragmentContainerActivity(this, "商品详情", true, false, bundle, CutGoodsInfoFragment::class.java)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        startGoodsInfo(intent)
    }

    override fun initView() {
        BottomNavigationViewHelper.disableShiftMode(mMainNavigation)
        mMainNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mMainViewPager.adapter = MyInnerViewPagerAdapter(supportFragmentManager)
        mMainViewPager.offscreenPageLimit = 4
//        testBadgeView()
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

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis()
            toast("再一次退出")
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        UserInfo.getInstance().loginOut()
        super.onDestroy()
    }

}
