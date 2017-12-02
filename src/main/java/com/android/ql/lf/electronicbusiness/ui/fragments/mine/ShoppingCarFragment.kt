package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.ui.adapters.ShoppingCarItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_shopping_car_layout.*

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class ShoppingCarFragment : BaseRecyclerViewFragment<ShoppingCarItemBean>() {

    override fun getLayoutId(): Int = R.layout.fragment_shopping_car_layout

    override fun createAdapter(): BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder> =
            ShoppingCarItemAdapter(R.layout.adapter_shopping_car_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        mEmptyShoppingCarContainer.visibility = View.GONE
        mShoppingCarContainer.visibility = View.VISIBLE
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration: DividerItemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        (0..10).forEach {
            val item = ShoppingCarItemBean()
            item.isEditorMode = false
            item.isSelector = false
            mArrayList.add(item)
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        setRefreshEnable(false)
        onRequestEnd(-1)
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        val item = mArrayList[position]
        when (view?.id) {
            R.id.mTvShoppingCarItemEditMode ->
                item.isEditorMode = !item.isEditorMode
            R.id.mIvShoppingCarItemSelector ->
                item.isSelector = !item.isSelector
        }
        mBaseAdapter.notifyItemChanged(position)
    }
}