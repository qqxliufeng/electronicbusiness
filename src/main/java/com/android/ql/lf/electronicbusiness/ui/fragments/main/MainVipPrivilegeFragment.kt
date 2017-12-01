package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.annotation.SuppressLint
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchAndClassifyFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchGoodsFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.VipPrivilegeItemFragment
import com.android.ql.lf.electronicbusiness.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.fragment_vip_privilege_layout.*

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class MainVipPrivilegeFragment : BaseFragment() {

    companion object {
        val TITLES = listOf("推荐", "服装", "数码", "家具", "美妆", "自营", "推荐", "服装", "数码", "家具", "美妆", "自营")

        fun newInstance() = MainVipPrivilegeFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_vip_privilege_layout

    @SuppressLint("ResourceType")
    override fun initView(view: View?) {
        mTvVipPrivilegeRule.setOnClickListener {
            //            val dialog = Dialog(mContext)
//            dialog.setContentView(R.layout.dialog_notify_layout)
//            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
//            dialog.show()
        }
        mVipPrivilegeViewPager.adapter = MyViewPagerAdapter(childFragmentManager)
        mVipPrivilegeTabLayout.setupWithViewPager(mVipPrivilegeViewPager)
        mLlSearchContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true, SearchGoodsFragment::class.java)
        }
        mTvClassMore.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, false, SearchAndClassifyFragment::class.java)
        }
    }

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        override fun getItem(position: Int): Fragment = VipPrivilegeItemFragment.newInstance()

        override fun getCount(): Int = TITLES.size

        override fun getPageTitle(position: Int): CharSequence = TITLES[position]

    }

}