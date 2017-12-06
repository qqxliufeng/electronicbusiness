package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PersonalCutFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchAndClassifyFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchGoodsFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.TeamCutFragment
import kotlinx.android.synthetic.main.fragment_main_normal_privilege_layout.*

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class MainCutPrivilegeFragment : BaseFragment() {

    companion object {

        val TITLES = listOf("个人砍", "团体砍")

        fun newInstance() = MainCutPrivilegeFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_normal_privilege_layout

    @SuppressLint("RestrictedApi")
    override fun initView(view: View?) {
        mTvNormalPrivilegeRule.setOnClickListener {
            showRuleDialog()
        }
        mVpMainNormalPrivilege.offscreenPageLimit = 2
        mVpMainNormalPrivilege.adapter = MyNormalPrivilegeAdapter(childFragmentManager)
        mTlMainNormalPrivilege.setupWithViewPager(mVpMainNormalPrivilege)
        mLlNormalSearchContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true, SearchGoodsFragment::class.java)
        }
    }

    private fun showRuleDialog() {
        val dialog = Dialog(mContext)
        val ruleDialog = View.inflate(mContext, R.layout.dialog_rule_layout, null)
        val btKnow = ruleDialog.findViewById<Button>(R.id.mBtRuleDialogKnow)
        val btNoTip = ruleDialog.findViewById<Button>(R.id.mBtRuleDialogNoTip)
        btKnow.setOnClickListener { dialog.dismiss() }
        btNoTip.setOnClickListener { dialog.dismiss() }
        btKnow.isEnabled = false
        btNoTip.isEnabled = false
        dialog.setContentView(ruleDialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
        dialog.show()
        val timer = object : CountDownTimer(6 * 1000, 1000) {
            override fun onFinish() {
                btKnow.text = "我知道了"
                btKnow.isEnabled = true
                btNoTip.isEnabled = true
            }
            override fun onTick(millisUntilFinished: Long) {
                btKnow.text = "我知道了（${millisUntilFinished / 1000}）"
            }
        }
        timer.start()
    }

    class MyNormalPrivilegeAdapter(manage: FragmentManager) : FragmentStatePagerAdapter(manage) {

        override fun getItem(position: Int): Fragment = if (position == 0) PersonalCutFragment.newInstance() else TeamCutFragment.newInstance()

        override fun getCount(): Int = TITLES.size

        override fun getPageTitle(position: Int): CharSequence = TITLES[position]
    }

}