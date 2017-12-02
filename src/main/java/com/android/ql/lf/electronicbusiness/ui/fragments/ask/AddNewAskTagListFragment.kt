package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.TagBean
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.adapters.AddNewAskTagsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AddNewAskTagListFragment : BaseRecyclerViewFragment<TagBean>() {

    companion object {
        val MULTI_MODE_FLAG = "multi_mode_flag"
        val MULTI_MODE_MAX_SELECTED_ITEMS_FLAG = "multi_mode_max_selected_items_flag"
    }

    private var isMultiMode = false
    private var maxSelectedItems = 0

    private var selectedItemList = ArrayList<TagBean>()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        isMultiMode = arguments.getBoolean(MULTI_MODE_FLAG)
        if (isMultiMode) {
            maxSelectedItems = arguments.getInt(MULTI_MODE_MAX_SELECTED_ITEMS_FLAG, 0)
            Log.e("TAG", "max items is -->  $maxSelectedItems")
        }
        setHasOptionsMenu(isMultiMode)
    }

    override fun createAdapter(): BaseQuickAdapter<TagBean, BaseViewHolder> =
            AddNewAskTagsAdapter(R.layout.adapter_add_new_ask_tags_item_layout, mArrayList)

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(mContext, 4)
        mManager = gridLayoutManager
        return gridLayoutManager
    }

    override fun initView(view: View?) {
        super.initView(view)
        val headerView = View.inflate(mContext, android.R.layout.simple_list_item_1, null) as TextView
        headerView.text = "选择标签"
        mBaseAdapter.addHeaderView(headerView)
        setLoadEnable(false)
        setRefreshEnable(false)
        mBaseAdapter.disableLoadMoreIfNotFullPage()
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_new_ask_tag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mAddNewAskTagsComplement) {
            RxBus.getDefault().post(selectedItemList)
            finish()
        }
        return true
    }

    override fun onRefresh() {
        super.onRefresh()
        setLoadEnable(false)
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_TAG)
    }


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            val tempList = ListParseHelper<TagBean>().fromJson(json.toString(), TagBean::class.java)
            if (tempList != null) {
                Collections.sort(tempList) { o1, o2 -> o1!!.tag_sort.toInt().compareTo(o2!!.tag_sort.toInt()) }
                mArrayList.addAll(tempList)
                mBaseAdapter.notifyDataSetChanged()
            }
        } else {
            mBaseAdapter.setEmptyView(emptyLayoutId)
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val tagBean = mArrayList[position]
        if (isMultiMode) {
            if (tagBean.isChecked) {
                selectedItemList.remove(tagBean)
            } else {
                if (selectedItemList.size >= maxSelectedItems) {
                    toast("最多可以选择 $maxSelectedItems 个标签")
                    return
                } else {
                    selectedItemList.add(tagBean)
                }
            }
            tagBean.isChecked = !tagBean.isChecked
            mBaseAdapter.notifyItemChanged(position + 1)
        } else {
            RxBus.getDefault().post(tagBean)
            finish()
        }
    }

}