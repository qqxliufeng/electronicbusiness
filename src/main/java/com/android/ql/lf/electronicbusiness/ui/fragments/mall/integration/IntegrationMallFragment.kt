package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.a.WebViewContentFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.TabItemBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.IntegrationExchangeListFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_integration_mall_layout.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/6 0006.
 * @author lf on 2017/11/6 0006
 */
class IntegrationMallFragment : BaseNetWorkingFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_integration_mall_layout

    override fun initView(view: View?) {
        mTvMallIntegrationCount.text = UserInfo.getInstance().memberIntegral
        mIntegrationExchangeContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分兑换列表", true, false, IntegrationExchangeListFragment::class.java)
        }
        mViewpager.offscreenPageLimit = 3
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.integration_mall_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuRule) {
            mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("2"))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_JPRODUCT_TYPE, RequestParamsHelper.getProductTypeParams())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                val tempTitles = arrayListOf<TabItemBean>()
                val item = TabItemBean()
                item.jclassify_id = ""
                item.jclassify_title = "全部"
                tempTitles.add(item)
                val resultJsonArray = json.optJSONArray("result")
                (0 until resultJsonArray.length()).forEach {
                    tempTitles.add(Gson().fromJson(resultJsonArray.optJSONObject(it).toString(), TabItemBean::class.java))
                }
                mViewpager.adapter = IntegrationMallAdapter(tempTitles, childFragmentManager)
                mTabLayout.setupWithViewPager(mViewpager)
            } else {
                toast("请求失败，请稍后重试……")
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                val bundle = Bundle()
                bundle.putString(WebViewContentFragment.PATH_FLAG, json.optJSONObject("result").optString("ptgg_content"))
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "规则", false, false, bundle, WebViewContentFragment::class.java)
            }
        }
    }

    class IntegrationMallAdapter(private var titles: ArrayList<TabItemBean>, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            val bundle = when (position) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putString(IntegrationMallItemFragment.TYPE_PARAM_FLAG, "")
                    bundle.putString(IntegrationMallItemFragment.ACT_TYPE_FLAG, RequestParamsHelper.ACT_JPRODUCT)
                    bundle
                }
                else -> {
                    val bundle = Bundle()
                    bundle.putString(IntegrationMallItemFragment.TYPE_PARAM_FLAG, titles[position].jclassify_id)
                    bundle.putString(IntegrationMallItemFragment.ACT_TYPE_FLAG, RequestParamsHelper.ACT_JPRODUCT_SEARCH)
                    bundle
                }
            }
            return IntegrationMallItemFragment.newInstance(bundle)
        }

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence = titles[position].jclassify_title
    }

}