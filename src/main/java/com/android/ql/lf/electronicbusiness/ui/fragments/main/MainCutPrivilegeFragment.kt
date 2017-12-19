package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ProductBannerBean
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PersonalCutFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.TeamCutFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.CounterHelper
import com.android.ql.lf.electronicbusiness.utils.GlideImageLoader
import com.android.ql.lf.electronicbusiness.utils.PreferenceUtils
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_main_normal_privilege_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class MainCutPrivilegeFragment : BaseNetWorkingFragment() {

    companion object {
        val TITLES = listOf("个人砍", "团体砍")
        fun newInstance() = MainCutPrivilegeFragment()

        var currentMode = OrderPresent.GoodsType.PERSONAL_CUT_GOODS
    }

    private var isVisiable = false
    private var isPrepared = false
    private var isLoad = false

    private var isInitFragment = false

    private var ruleContent: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_main_normal_privilege_layout


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisiable = true
            loadData()
        } else {
            isVisiable = false
        }
    }

    private fun loadData() {
        if (isVisiable && isPrepared) {
            if (!isLoad) {
                mPresent.getDataByPost(0x1, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_LUNBO, RequestParamsHelper.getLunBoParam("1"))
            }
            if (!isInitFragment) {
                initFragment()
            }
        }
    }

    private fun initFragment() {
        isInitFragment = true
        mVpMainNormalPrivilege.offscreenPageLimit = 2
        mVpMainNormalPrivilege.adapter = MyNormalPrivilegeAdapter(childFragmentManager)
        mTlMainNormalPrivilege.setupWithViewPager(mVpMainNormalPrivilege)
        mLlNormalSearchContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true, bundleOf(Pair(SearchFragment.K_TYPE_FLAG, currentMode)), SearchFragment::class.java)
        }
    }


    @SuppressLint("RestrictedApi")
    override fun initView(view: View?) {
        mTvNormalPrivilegeRule.setOnClickListener {
            if (ruleContent == null) {
                mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("2"))
            } else {
                showRuleDialog()
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                ruleContent = json.optJSONObject("result").optString("ptgg_content")
                showRuleDialog()
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                isLoad = true
                //加载规则
                if (!PreferenceUtils.getPrefBoolean(mContext, "no_show_rule_cut_dialog", false)) {
                    mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("2"))
                }
                val tempPics = arrayListOf<String>()
                ListParseHelper<ProductBannerBean>().fromJson(json.toString(), ProductBannerBean::class.java).forEach {
                    tempPics.add(it.lunbo_pic)
                }
                if (!tempPics.isEmpty()) {
                    mBannerProductCut.setImageLoader(GlideImageLoader()).setImages(tempPics).setDelayTime(5000).start()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBannerProductCut.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        mBannerProductCut.stopAutoPlay()
    }

    private fun showRuleDialog() {
        val dialog = Dialog(mContext)
        dialog.window.setWindowAnimations(R.style.dialog_show_animation)
        val ruleDialogContent = View.inflate(mContext, R.layout.dialog_rule_layout, null)
        val content = ruleDialogContent.findViewById<TextView>(R.id.mTvRuleDialogContent)
        val btKnow = ruleDialogContent.findViewById<Button>(R.id.mBtRuleDialogKnow)
        val btNoTip = ruleDialogContent.findViewById<Button>(R.id.mBtRuleDialogNoTip)
        content.text = Html.fromHtml(ruleContent!!)
        btKnow.setOnClickListener { dialog.dismiss() }
        btNoTip.setOnClickListener {
            PreferenceUtils.setPrefBoolean(mContext, "no_show_rule_cut_dialog", true)
            dialog.dismiss()
        }
        btKnow.isEnabled = false
        btNoTip.isEnabled = false
        dialog.setContentView(ruleDialogContent)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
        dialog.show()
        val counterHelper = CounterHelper(5000)
        counterHelper.onFinish = {
            btKnow.text = "我知道了"
            btKnow.isEnabled = true
            btNoTip.isEnabled = true
        }
        counterHelper.onTick = {
            btKnow.text = "我知道了（${it / 1000}）"
        }
        counterHelper.start()
    }

    class MyNormalPrivilegeAdapter(manage: FragmentManager) : FragmentStatePagerAdapter(manage) {

        override fun getItem(position: Int): Fragment = if (position == 0) PersonalCutFragment.newInstance() else TeamCutFragment.newInstance()

        override fun getCount(): Int = TITLES.size

        override fun getPageTitle(position: Int): CharSequence = TITLES[position]
    }

}