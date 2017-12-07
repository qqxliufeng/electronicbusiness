package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.TabItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainCutPrivilegeFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_personal_cut_layout.*

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class PersonalCutFragment : BaseNetWorkingFragment() {

    companion object {
        fun newInstance() = PersonalCutFragment()
    }

    private var isMvisible: Boolean = false
    private var isPrepared: Boolean = false
    private var isLoaded: Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isMvisible = true
            loadData()
            MainCutPrivilegeFragment.currentMode = "1"
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
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_TYPE, RequestParamsHelper.getProductTypeParams("1"))
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_layout

    override fun initView(view: View?) {
        mTvClassMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(SearchGoodsFragment.K_TYPE_FLAG, "1")
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, false, bundle,SearchAndClassifyFragment::class.java)
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext)
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
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
            mVpPersonalCut.adapter = MyPersonalCutAdapter(tempTitles, childFragmentManager)
            mTlPersonalCut.setupWithViewPager(mVpPersonalCut)
        }
    }

    class MyPersonalCutAdapter(private val titles: ArrayList<TabItemBean>, manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

        override fun getItem(position: Int): Fragment = PersonalCutItemFragment.newInstance(titles[position].classify_id)

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence = titles[position].classify_title

    }

}