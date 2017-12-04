package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.OrderListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.hintTextColor
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderListFragment : BaseRecyclerViewFragment<MyOrderBean>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<MyOrderBean, BaseViewHolder> =
            OrderListItemAdapter(R.layout.adapter_order_list_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.order_list_search_menu, menu)
        val menuItem = menu?.findItem(R.id.mOrderListSearch)
        val searchView = menuItem?.actionView as SearchView
        searchView.isIconified = true
        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text)
        searchAutoComplete.hintTextColor = Color.WHITE
        searchAutoComplete.hint = "输入商品名或首字母"
        searchAutoComplete.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4.0f, mContext.resources.displayMetrics)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                toast("$query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration: DividerItemDecoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return decoration
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MYORDER,
                RequestParamsHelper.getMyOrderParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MYORDER,
                RequestParamsHelper.getMyOrderParams(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        processList(json, MyOrderBean::class.java)
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.mBtOrderListItemExpress ->
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "物流信息", true, false, ExpressInfoFragment::class.java)
            R.id.mBtOrderListItemComment ->
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品评价", true, false, OrderCommentSubmitFragment::class.java)
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bundle = Bundle()
        bundle.putString(OrderInfoFragment.ORDER_INFO_ID_FLAG,mArrayList[position].order_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "订单详情", true, false,bundle, OrderInfoFragment::class.java)
    }
}