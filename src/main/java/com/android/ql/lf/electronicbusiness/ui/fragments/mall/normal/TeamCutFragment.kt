package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal_cut_layout.*

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class TeamCutFragment : BaseFragment() {

    companion object {
        val TITLES = listOf("推荐", "服装", "数码", "家具", "美妆", "自营")
        fun newInstance() = TeamCutFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_layout

    override fun initView(view: View?) {
        mVpPersonalCut.offscreenPageLimit = 5
        mVpPersonalCut.adapter = MyPersonalCutAdapter(childFragmentManager)
        mTlPersonalCut.setupWithViewPager(mVpPersonalCut)
    }

    class MyPersonalCutAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        override fun getItem(position: Int): Fragment = TeamCutItemFragment()

        override fun getCount(): Int = TITLES.size

        override fun getPageTitle(position: Int): CharSequence = TITLES[position]

    }


}