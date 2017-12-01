package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_ask_and_answer_layout.*

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AskAndAnswerFragment : BaseFragment() {

    companion object {
        val TITLES = listOf("关注", "回答", "提问")
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine_ask_and_answer_layout

    override fun initView(view: View?) {
        mVpMienAskAndAnswer.adapter = MineAskAndAnswerAdapter(childFragmentManager)
        mVpMienAskAndAnswer.offscreenPageLimit = 3
        mTlMienAskAndAnswer.setupWithViewPager(mVpMienAskAndAnswer)
    }

    class MineAskAndAnswerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

        override fun getItem(position: Int): Fragment =
                when (position) {
                    0 -> AskAndAnswerOfFocusFragment.newInstance()
                    1 -> AskAndAnswerOfAnswerFragment.newInstance()
                    2 -> AskAndAnswerOfAskFragment.newInstance()
                    else ->{
                        AskAndAnswerOfAskFragment.newInstance()
                    }
                }

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]

    }

}