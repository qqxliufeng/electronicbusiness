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
import com.a.WebViewContentFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ProductBannerBean
import com.android.ql.lf.electronicbusiness.data.TabItemBean
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchAndClassifyFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SearchFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.VipPrivilegeItemFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_vip_privilege_layout.*
import org.jetbrains.anko.bundleOf

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

    private var ruleContent:String? = null

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
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_TYPE, RequestParamsHelper.getProductTypeParams("3"))
    }

    override fun getLayoutId(): Int = R.layout.fragment_vip_privilege_layout

    @SuppressLint("ResourceType")
    override fun initView(view: View?) {
        mTvVipPrivilegeRule.setOnClickListener {
            if(ruleContent!=null){
                FragmentContainerActivity.startFragmentContainerActivity(mContext,"会员规则",true,false, bundleOf(Pair(WebViewContentFragment.PATH_FLAG,ruleContent!!)),WebViewContentFragment::class.java)
            }else {
                mPresent.getDataByPost(0x3, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("3"))
            }
        }
        mLlSearchContainer.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true, bundleOf(Pair(SearchFragment.K_TYPE_FLAG, "3")), SearchFragment::class.java)
        }
        mTvClassMore.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, false, bundleOf(Pair(SearchAndClassifyFragment.K_TYPE_FLAG, "3")), SearchAndClassifyFragment::class.java)
        }
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            progressDialog = MyProgressDialog(mContext)
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            mPresent.getDataByPost(0x1, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_LUNBO, RequestParamsHelper.getLunBoParam("2"))
            if (json != null) {
                isLoaded = true
                val tempTitles = arrayListOf<TabItemBean>()
                val item = TabItemBean()
                item.classify_id = ""
                item.classify_title = "全部"
                tempTitles.add(item)
                val resultJsonArray = json.optJSONArray("result")
                (0 until resultJsonArray.length()).forEach {
                    tempTitles.add(Gson().fromJson(resultJsonArray.optJSONObject(it).toString(), TabItemBean::class.java))
                }
                mVipPrivilegeViewPager.adapter = MyViewPagerAdapter(tempTitles, childFragmentManager)
                mVipPrivilegeViewPager.offscreenPageLimit = 5
                mVipPrivilegeTabLayout.setupWithViewPager(mVipPrivilegeViewPager)
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                val tempPics = arrayListOf<String>()
                ListParseHelper<ProductBannerBean>().fromJson(json.toString(), ProductBannerBean::class.java).forEach {
                    tempPics.add(it.lunbo_pic)
                }
                if (!tempPics.isEmpty()) {
                    mBannerProductCut.setImageLoader(GlideImageLoader()).setImages(tempPics).setDelayTime(5000).start()
                }
            }
        } else if (requestID == 0x3) {
            if (json != null) {
                ruleContent = json.optJSONObject("result").optString("ptgg_content")
                FragmentContainerActivity.startFragmentContainerActivity(mContext,"会员规则",true,false, bundleOf(Pair(WebViewContentFragment.PATH_FLAG,ruleContent!!)),WebViewContentFragment::class.java)
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