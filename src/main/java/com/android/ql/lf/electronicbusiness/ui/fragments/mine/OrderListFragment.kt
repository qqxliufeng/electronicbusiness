package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.OrderListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderListFragment : BaseRecyclerViewFragment<MyOrderBean>() {

    companion object {
        val ORDER_STATUE_FLAG = "order_statue_flag"

        val REFRESH_ORDER_FLAG = "refresh order"
    }

    private lateinit var currentOrder: MyOrderBean

    private val orderPresent: OrderPresent by lazy {
        OrderPresent(mPresent)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == REFRESH_ORDER_FLAG) {
                OrderPresent.notifyRefreshOrderNum()
                onPostRefresh()
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<MyOrderBean, BaseViewHolder> =
            OrderListItemAdapter(R.layout.adapter_order_list_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.order_list_search_menu, menu)
        val menuItem = menu?.findItem(R.id.mOrderListSearch)
        val searchView = menuItem?.actionView as SearchView
        searchView.isIconified = true
        val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text)
        searchAutoComplete.setHintTextColor(Color.WHITE)
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
        loadData()
    }

    private fun loadData() {
        if (arguments != null) {
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MYORDER_STATUS,
                    RequestParamsHelper.getMyorderStatusParam(arguments.getString(ORDER_STATUE_FLAG), currentPage))
        } else {
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MYORDER,
                    RequestParamsHelper.getMyOrderParams(currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            progressDialog = MyProgressDialog(mContext, "正在取消订单……")
            progressDialog.show()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                processList(json, MyOrderBean::class.java)
            }
            0x1 -> { // 取消订单
                if (json != null) {
                    onPostRefresh()
                    OrderPresent.notifyRefreshOrderNum()
                }
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentOrder = mArrayList[position]
        when (view?.id) {
            R.id.mBtOrderListItemAction1 -> {
                action1()
            }
            R.id.mBtOrderListItemAction2 -> {
                action2()
            }
        }
    }

    private fun action1() {
        when (OrderPresent.getOrderStatus(currentOrder.order_token)) {
            OrderPresent.OrderStatus.STATUS_OF_DFK -> {
                //取消订单
                AlertDialog.Builder(context).setMessage("是否要取消订单？").setPositiveButton("是") { _, _ ->
                    orderPresent.cancelOrder(0x1, currentOrder.order_id)
                }.setNegativeButton("不了", null).create().show()
            }
            OrderPresent.OrderStatus.STATUS_OF_DFH -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_DPJ -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_FINISH -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_CANCEL -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_BACK -> {
            }
            else -> {
            }
        }
    }

    private fun action2() {
        when (OrderPresent.getOrderStatus(currentOrder.order_token)) {
            OrderPresent.OrderStatus.STATUS_OF_DFK -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_DFH -> {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "申请退款", true, false, bundleOf(Pair("oid", currentOrder.order_id)), RefundFragment::class.java)
            }
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_DPJ -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_FINISH -> {
                FragmentContainerActivity.startFragmentContainerActivity(mContext,
                        "商品评价",
                        true,
                        false,
                        bundleOf(Pair(OrderCommentSubmitFragment.ORDER_ID_FLAG, currentOrder.order_id),
                                Pair(OrderCommentSubmitFragment.PRODUCT_ID_FLAG, currentOrder.product_id)),
                        OrderCommentSubmitFragment::class.java)
            }
            OrderPresent.OrderStatus.STATUS_OF_CANCEL -> {
            }
            OrderPresent.OrderStatus.STATUS_OF_BACK -> {
            }
            else -> {
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bundle = Bundle()
        bundle.putString(OrderInfoFragment.ORDER_INFO_ID_FLAG, mArrayList[position].order_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "订单详情", true, false, bundle, OrderInfoFragment::class.java)
    }
}