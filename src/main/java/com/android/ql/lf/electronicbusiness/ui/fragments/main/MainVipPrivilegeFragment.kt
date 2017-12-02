package com.android.ql.lf.electronicbusiness.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.TabItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchAndClassifyFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchGoodsFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.VipPrivilegeItemFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_vip_privilege_layout.*

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class MainVipPrivilegeFragment : BaseNetWorkingFragment() {

    companion object {
        fun newInstance() = MainVipPrivilegeFragment()
    }

    private var isMvisible: Boolean = false
    private var isPrepared: Boolean = false
    private var isLoaded: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isMvisible = true
            loadData()
        } else {
            isMvisible = false
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        loadData()
    }

    private fun loadData() {
        if (!isMvisible || !isPrepared || isLoaded) {
            return
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_TYPE, RequestParamsHelper.getProductTypeParams())
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
        mLlSearchContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true, SearchGoodsFragment::class.java)
        }
        mTvClassMore.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, false, SearchAndClassifyFragment::class.java)
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            isLoaded = true
            val tempTitles = arrayListOf<TabItemBean>()
//            val item = TabItemBean()
//            item.classify_id = ""
//            item.classify_title = "全部"
//            tempTitles.add(item)
            val resultJsonArray = json.optJSONArray("result")
            (0 until resultJsonArray.length()).forEach {
                tempTitles.add(Gson().fromJson(resultJsonArray.optJSONObject(it).toString(), TabItemBean::class.java))
            }
            mVipPrivilegeViewPager.adapter = MyViewPagerAdapter(tempTitles, childFragmentManager)
            mVipPrivilegeTabLayout.setupWithViewPager(mVipPrivilegeViewPager)
        }
    }

    class MyViewPagerAdapter(var list: ArrayList<TabItemBean>, manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString(VipPrivilegeItemFragment.ITEM_ID_FLAG, list[position].classify_id)
            return VipPrivilegeItemFragment.newInstance(bundle)
        }

        override fun getCount(): Int = list.size

        override fun getPageTitle(position: Int): CharSequence = list[position].classify_title

    }

}