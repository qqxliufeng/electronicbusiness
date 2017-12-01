package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.ui.adapters.AddressSelectListItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class AddressSelectFragment : BaseRecyclerViewFragment<AddressBean>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<AddressBean, BaseViewHolder> =
            AddressSelectListItemAdapter(R.layout.adapter_select_list_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_new_address_menu, menu)
    }

    override fun onRefresh() {
        (0..2).forEach {
            val item = AddressBean()
//            item.detail = "这是详细地址"
//            item.name = "张三"
//            item.phone = "159879853455"

            mArrayList.add(item)
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        onRequestEnd(-1)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuAddNewAddress) {
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        RxBus.getDefault().post(mArrayList[position])
        finish()
    }
}