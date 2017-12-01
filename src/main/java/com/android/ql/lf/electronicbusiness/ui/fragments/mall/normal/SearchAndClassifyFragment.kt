package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.CheckedTextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.SearchContentItemBean
import com.android.ql.lf.electronicbusiness.data.SearchMenuItemBean
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_normal_mall_search_and_classify_layout.*

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class SearchAndClassifyFragment : BaseFragment() {

    companion object {
        val MENUS = arrayListOf("女装", "男装", "母婴", "家具", "家纺", "美妆", "美食", "手机", "电器", "图书")
    }

    private val menuItems = arrayListOf<SearchMenuItemBean>()
    private val contentItems = arrayListOf<SearchContentItemBean>()

    override fun getLayoutId(): Int = R.layout.fragment_normal_mall_search_and_classify_layout

    override fun initView(view: View?) {
        MENUS.forEachIndexed { _, s ->
            val item = SearchMenuItemBean()
            item.isChecked = false
            item.itemName = s
            menuItems.add(item)
        }

        (0 until MENUS.size * 10).forEachIndexed { index, _ ->
            val item = SearchContentItemBean(index % 10 == 0, MENUS[index / 10])
            item.t = "item$index"
            contentItems.add(item)
        }

        val gridLayoutManager = GridLayoutManager(mContext, 3)
        mRcContent.layoutManager = gridLayoutManager
        mRcContent.adapter = ContentAdapter(R.layout.adapter_search_and_classify_content_item_layout,
                R.layout.adapter_search_and_classify_content_header_item_layout, contentItems)
        mRcMenu.layoutManager = LinearLayoutManager(mContext)
        val menuAdapter = MenuAdapter(R.layout.adapter_search_and_classify_menu_item_layout, menuItems)
        mRcMenu.adapter = menuAdapter
        mRcMenu.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                menuItems.forEachIndexed { index, searchMenuItemBean ->
                    searchMenuItemBean.isChecked = index == position
                }
                menuAdapter.notifyDataSetChanged()
                gridLayoutManager.scrollToPositionWithOffset(position * 10, 0)
            }
        })
        mRcContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = gridLayoutManager.findFirstVisibleItemPosition()
                if (position % 10 == 0) {
                    menuItems.forEachIndexed { _, searchMenuItemBean ->
                        searchMenuItemBean.isChecked = false
                    }
                    val i = gridLayoutManager.findFirstVisibleItemPosition() / 10
                    menuItems[i].isChecked = true
                    menuAdapter.notifyDataSetChanged()
                    mRcMenu.smoothScrollToPosition(i)
                }else if (!mRcContent.canScrollVertically(1)) {
                    menuItems.forEachIndexed { _, searchMenuItemBean ->
                        searchMenuItemBean.isChecked = false
                    }
                    menuItems[MENUS.size - 1].isChecked = true
                    menuAdapter.notifyDataSetChanged()
                    mRcMenu.smoothScrollToPosition(MENUS.size - 1)
                }
            }
        })
    }

    class MenuAdapter(layoutId: Int, list: ArrayList<SearchMenuItemBean>) : BaseQuickAdapter<SearchMenuItemBean, BaseViewHolder>(layoutId, list) {

        override fun convert(helper: BaseViewHolder?, item: SearchMenuItemBean?) {
            helper?.setText(R.id.mSearchClassifyItemName, item?.itemName)
            val ckName = helper?.getView<CheckedTextView>(R.id.mSearchClassifyItemName)
            ckName?.isChecked = item?.isChecked!!
        }
    }

    class ContentAdapter(layoutId: Int, headerLayoutId: Int, list: ArrayList<SearchContentItemBean>) : BaseSectionQuickAdapter<SearchContentItemBean, BaseViewHolder>(layoutId, headerLayoutId, list) {

        override fun convertHead(helper: BaseViewHolder?, item: SearchContentItemBean?) {
            helper?.setText(R.id.mSearchClassifyContentItemTitle, item?.header)
        }

        override fun convert(helper: BaseViewHolder?, item: SearchContentItemBean?) {
            helper?.setText(R.id.mSearchClassifyContentItemName, item?.t)
        }
    }

}