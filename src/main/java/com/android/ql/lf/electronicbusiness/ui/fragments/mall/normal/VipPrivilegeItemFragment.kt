package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.a.VipPrivilegeItemInfoFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.VipPrivilegeItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemFragment : BaseRecyclerViewFragment<String>(){

    companion object {
        fun newInstance() = VipPrivilegeItemFragment()
    }

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            VipPrivilegeItemAdapter(R.layout.adapter_vip_privilege_item_layout,mArrayList)


    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        (0..10).forEach {
            mArrayList.add("")
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        setRefreshEnable(false)
        onRequestEnd(-1)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.startFragmentContainerActivity(mContext,"商品详情",true,false, VipPrivilegeItemInfoFragment::class.java)
    }

}