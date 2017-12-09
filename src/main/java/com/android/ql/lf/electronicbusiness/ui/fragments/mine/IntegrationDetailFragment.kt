package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.a.WebViewContentFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.IntegrationBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.IntegrationDetailAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.IntegrationMallFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class IntegrationDetailFragment : BaseRecyclerViewFragment<IntegrationBean>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<IntegrationBean, BaseViewHolder> =
            IntegrationDetailAdapter(R.layout.adapter_integration_detail_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.integration_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuMXGZ) {
            mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("1"))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView(view: View?) {
        super.initView(view)
        val topView = View.inflate(mContext, R.layout.layout_integration_detail_top_layout, null)
        val tv_integration_count = topView.findViewById<TextView>(R.id.mIntegrationCount)
        tv_integration_count.text = UserInfo.getInstance().memberIntegral
        val mall: Button = topView.findViewById(R.id.mBtDHJF)
        mall.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分商城", true, false, IntegrationMallFragment::class.java)
        }
        mBaseAdapter.addHeaderView(topView)
        setLoadEnable(false)
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_INTEGRAL, RequestParamsHelper.getIntegralParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_INTEGRAL, RequestParamsHelper.getIntegralParams(currentPage))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            progressDialog = MyProgressDialog(mContext, "正在加载……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val json = checkResultCode(result)
            processList(json, IntegrationBean::class.java)
        } else if (requestID == 0x1) {
            val json = checkResultCode(result)
            if (json != null) {
                val bundle = Bundle()
                bundle.putString(WebViewContentFragment.PATH_FLAG, json.optJSONObject("result").optString("ptgg_content"))
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "积分规则", false, false, bundle, WebViewContentFragment::class.java)
            }
        }
    }
}
