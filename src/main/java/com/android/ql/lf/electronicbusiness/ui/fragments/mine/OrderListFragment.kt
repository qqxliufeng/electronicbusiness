package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MyOrderBean
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.WXPayBean
import com.android.ql.lf.electronicbusiness.present.OrderPresent
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.OrderListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration.ExpressInfoFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PayResultFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.SelectPayTypeView
import com.android.ql.lf.electronicbusiness.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

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

    private var bottomPayDialog: BottomSheetDialog? = null

    private val orderPresent: OrderPresent by lazy {
        OrderPresent(mPresent)
    }

    private var payType: String = SelectPayTypeView.WX_PAY


    private val handle = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                PayManager.SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    val bundle = Bundle()
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_SUCCESS_CODE)
                    } else {
                        //支付失败
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_FAIL_CODE)
                    }
                    OrderPresent.notifyRefreshOrderNum()
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付结果", true, false, bundle, PayResultFragment::class.java)
                }
            }
        }
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
            if (it.isRefresh && it.any == "支付成功") {
                onPostRefresh()
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<MyOrderBean, BaseViewHolder> =
            OrderListItemAdapter(R.layout.adapter_order_list_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (arguments == null) {
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
                    if (!TextUtils.isEmpty(query)) {
                        mPresent.getDataByPost(0x3,
                                RequestParamsHelper.MEMBER_MODEL,
                                RequestParamsHelper.ACT_MYORDER_SEARCH,
                                RequestParamsHelper.getMyOrderSearchParam(query!!))
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    override fun getEmptyMessage() = "暂没有商品哦~~~"

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

        if (requestID == 0x2) {
            progressDialog = MyProgressDialog(mContext, "正在支付……")
            progressDialog.show()
        }

        if (requestID == 0x3) {
            super.onRefresh()
            mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = true }
        }
    }


    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        if (requestID == 0x3) {
            mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = false }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0, 0x3 -> {
                processList(json, MyOrderBean::class.java)
            }
            0x1 -> { // 取消订单
                if (json != null) {
                    onPostRefresh()
                    OrderPresent.notifyRefreshOrderNum()
                } else {
                    toast("订单取消失败，请稍后重试……")
                }
            }
            0x2 -> { //付款
                if (json != null) {
                    OrderPresent.notifyRefreshShoppingCarList()
                    PreferenceUtils.setPrefString(mContext, PayResultFragment.PAY_ORDER_RESULT_JSON_FLAG, json.optJSONObject("arr").toString())
                    if (payType == SelectPayTypeView.WX_PAY) {
                        val wxBean = Gson().fromJson(json.optJSONObject("result").toString(), WXPayBean::class.java)
                        PayManager.wxPay(mContext, wxBean)
                    } else {
                        PayManager.aliPay(mContext, handle, json.optString("result"))
                    }
                }
            }
            0x4 -> { //确认收货
                if (json != null) {
                    OrderPresent.notifyRefreshOrderNum()
                    onPostRefresh()
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
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
                if (!TextUtils.isEmpty(currentOrder.order_tn)) {
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "快递信息", true, false, bundleOf(Pair(ExpressInfoFragment.ORDER_BEAN_FLAG, currentOrder)), ExpressInfoFragment::class.java)
                }
            }
            else -> {
            }
        }
    }

    private fun action2() {
        when (OrderPresent.getOrderStatus(currentOrder.order_token)) {
        //待付款
            OrderPresent.OrderStatus.STATUS_OF_DFK -> {
                if (bottomPayDialog == null) {
                    bottomPayDialog = BottomSheetDialog(mContext)
                    val contentView = SelectPayTypeView(mContext)
                    contentView.setShowConfirmView(View.VISIBLE)
                    contentView.setOnConfirmClickListener {
                        bottomPayDialog!!.dismiss()
                        payType = contentView.payType
                        mPresent.getDataByPost(0x2, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_PAY,
                                RequestParamsHelper.getPayParam(currentOrder.order_id, currentOrder.product_id, payType))
                    }
                    bottomPayDialog!!.setContentView(contentView)
                } else {
                    bottomPayDialog!!.show()
                }
            }
            OrderPresent.OrderStatus.STATUS_OF_DFH -> {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "申请退款", true, false, bundleOf(Pair("oid", currentOrder.order_id)), RefundFragment::class.java)
            }
            OrderPresent.OrderStatus.STATUS_OF_DSH -> {
                AlertDialog.Builder(mContext).setMessage("是否确认收货？").setPositiveButton("是") { _, _ ->
                    orderPresent.confirmGoods(0x4, currentOrder.order_id)
                }.setNegativeButton("再等等", null).create().show()
            }
            OrderPresent.OrderStatus.STATUS_OF_DPJ -> {
                //去评价
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品评价", true, false,
                        bundleOf(Pair(OrderCommentSubmitFragment.ORDER_ID_FLAG, currentOrder.order_id),
                                Pair(OrderCommentSubmitFragment.PRODUCT_ID_FLAG, currentOrder.product_id)),
                        OrderCommentSubmitFragment::class.java)
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