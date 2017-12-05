package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.ShoppingCarItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.SubmitOrderFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_shopping_car_layout.*
import org.jetbrains.anko.support.v4.toast
import java.text.DecimalFormat

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class ShoppingCarFragment : BaseRecyclerViewFragment<ShoppingCarItemBean>() {

    private lateinit var currentItem: ShoppingCarItemBean

    private var currentEditMode = 1// 1 减  0  加

    override fun getLayoutId(): Int = R.layout.fragment_shopping_car_layout

    override fun createAdapter(): BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder> =
            ShoppingCarItemAdapter(R.layout.adapter_shopping_car_item_layout, mArrayList)

    @SuppressLint("RestrictedApi")
    override fun initView(view: View?) {
        super.initView(view)
        mEmptyShoppingCarContainer.visibility = View.GONE
        mShoppingCarContainer.visibility = View.VISIBLE
        mCalculate.isEnabled = false
        setRefreshEnable(false)
        mLlShoppingCarAllSelectContainer.setOnClickListener {
            mCivShoppingCarAllSelect.isChecked = !mCivShoppingCarAllSelect.isChecked
            var money = 0.00F
            mArrayList.forEach {
                it.isSelector = mCivShoppingCarAllSelect.isChecked
                if (it.isSelector) {
                    money += (it.shopcart_price.toFloat() * it.shopcart_num.toInt())
                }
            }
            mTvShoppingCarAllSelectMoney.text = "￥${DecimalFormat("0.00").format(money)}"
            mCalculate.isEnabled = money != 0.00f
            mBaseAdapter.notifyDataSetChanged()
        }
        mCalculate.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(SubmitOrderFragment.GOODS_ID_FLAG, currentItem)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "确认订单", true, false, bundle, SubmitOrderFragment::class.java)
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration: DividerItemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_SHOPCART, RequestParamsHelper.getShopcartParam(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        setLoadEnable(false)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            progressDialog = MyProgressDialog(context, "正在删除……")
            progressDialog.show()
        } else if (requestID == 0x2) {
            mPbShoppingCarItem.visibility = View.VISIBLE
        }
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        if (requestID == 0x2) {
            mPbShoppingCarItem.visibility = View.GONE
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null && json.optJSONArray("result").length() != 0) {
                processList(json, ShoppingCarItemBean::class.java)
            } else {
                emptyShoppingCar()
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                toast("删除成功")
                mArrayList.remove(currentItem)
                mBaseAdapter.notifyDataSetChanged()
            }
        } else if (requestID == 0x2) {
            if (json != null) {
                if (currentEditMode == 0) {
                    currentItem.shopcart_num = (currentItem.shopcart_num.toInt() + 1).toString()
                } else {
                    currentItem.shopcart_num = (currentItem.shopcart_num.toInt() - 1).toString()
                }
                mBaseAdapter.notifyItemChanged(mArrayList.indexOf(currentItem))
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        emptyShoppingCar()
    }

    private fun emptyShoppingCar() {
        mEmptyShoppingCarContainer.visibility = View.VISIBLE
        mShoppingCarContainer.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        currentItem = mArrayList[position]
        when (view?.id) {
            R.id.mTvShoppingCarItemEditMode -> {
                currentItem.isEditorMode = !currentItem.isEditorMode
                mBaseAdapter.notifyItemChanged(position)
            }
            R.id.mIvShoppingCarItemSelector -> {
                currentItem.isSelector = !currentItem.isSelector
                var money = 0.00f
                val isAllSelected = mArrayList.filter { it.isSelector }.size == mArrayList.size
                mArrayList.forEach {
                    if (it.isSelector) {
                        money += (it.shopcart_price.toFloat() * it.shopcart_num.toInt())
                    }
                }
                if (isAllSelected) {
                    mCivShoppingCarAllSelect.isChecked = true
                    mBaseAdapter.notifyDataSetChanged()
                    return
                } else {
                    mCivShoppingCarAllSelect.isChecked = false
                }
                mTvShoppingCarAllSelectMoney.text = "￥${DecimalFormat("0.00").format(money)}"
                mCalculate.isEnabled = money != 0.00f
                mBaseAdapter.notifyItemChanged(position)
            }
            R.id.mTvShoppingCarItemEditDel -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("是否要删除当前商品？")
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("删除") { _, _ ->
                    mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_DEL_SHOPCART, RequestParamsHelper.getDelShopcartParam(currentItem.shopcart_id))
                }
                builder.create().show()
            }
            R.id.mTvShoppingCarItemDelCount -> {
                if (currentItem.shopcart_num.toInt() <= 1) {
                    return
                }
                currentEditMode = 1
                mPresent.getDataByPost(0x2,
                        RequestParamsHelper.MEMBER_MODEL,
                        RequestParamsHelper.ACT_UPDATE_SHOPCART,
                        RequestParamsHelper.getUpdateShopcart(currentItem.shopcart_id, (currentItem.shopcart_num.toInt() - 1).toString()))
            }
            R.id.mTvShoppingCarItemAddCount -> {
                currentEditMode = 0
                mPresent.getDataByPost(0x2,
                        RequestParamsHelper.MEMBER_MODEL,
                        RequestParamsHelper.ACT_UPDATE_SHOPCART,
                        RequestParamsHelper.getUpdateShopcart(currentItem.shopcart_id, (currentItem.shopcart_num.toInt() + 1).toString()))
            }
        }
    }
}