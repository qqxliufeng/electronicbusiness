package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.ClassifyBean
import com.android.ql.lf.electronicbusiness.data.ClassifyItemEntity
import com.android.ql.lf.electronicbusiness.data.IClassifyBean
import com.android.ql.lf.electronicbusiness.data.IClassifyItemEntity
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainCutPrivilegeFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_normal_mall_search_and_classify_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lf on 2017/11/7 0007.
 * @author lf on 2017/11/7 0007
 */
class SearchAndClassifyFragment : BaseNetWorkingFragment() {

    companion object {
        val K_TYPE_FLAG = "k_type_flag"
    }

    private val mMenuArrayList = arrayListOf<ClassifyBean>()
    private val mItemArrayList = arrayListOf<ClassifyItemEntity>()

    private lateinit var menuItem: ClassifyBean
    private lateinit var contentItem: ClassifyItemEntity

    override fun getLayoutId(): Int = R.layout.fragment_normal_mall_search_and_classify_layout

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var contentAdapter: ContentAdapter

    override fun initView(view: View?) {
        mLlSearchAndClassifySearch.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true,
                    bundleOf(Pair(SearchFragment.K_TYPE_FLAG, arguments.getString(SearchGoodsFragment.K_TYPE_FLAG, ""))), SearchFragment::class.java)
        }
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        mRcContent.layoutManager = gridLayoutManager
        contentAdapter = ContentAdapter(R.layout.adapter_search_and_classify_content_item_layout,
                R.layout.adapter_search_and_classify_content_header_item_layout, mItemArrayList)
        mRcContent.adapter = contentAdapter
        mRcContent.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                contentItem = mItemArrayList[position]
                if (contentItem.isHeader){
                    return
                }
                FragmentContainerActivity.startFragmentContainerActivity(mContext, contentItem.t.classify_title, true, true,
                        bundleOf(Pair(SearchGoodsFragment.TYPE_ID_FLAG, menuItem.classify_id),
                                Pair(SearchGoodsFragment.STYPE_ID_FLAG, contentItem.t.classify_id),
                                Pair(SearchGoodsFragment.K_TYPE_FLAG, arguments.getString(SearchGoodsFragment.K_TYPE_FLAG, ""))),
                        SearchGoodsFragment::class.java)
            }
        })
        mRcMenu.layoutManager = LinearLayoutManager(mContext)
        menuAdapter = MenuAdapter(R.layout.adapter_search_and_classify_menu_item_layout, mMenuArrayList)
        mRcMenu.adapter = menuAdapter
        mRcMenu.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                menuItem = mMenuArrayList[position]
                mMenuArrayList.forEachIndexed { index, searchMenuItemBean ->
                    searchMenuItemBean.isChecked = index == position
                }
                menuAdapter.notifyDataSetChanged()
                var contentItemIndex = 0
                for (entity in mItemArrayList) {
                    if (entity.header != null && entity.header == mMenuArrayList[position].classify_title) {
                        contentItemIndex = mItemArrayList.indexOf(entity)
                        break
                    }
                }
                gridLayoutManager.scrollToPositionWithOffset(contentItemIndex, 0)
            }
        })
        mRcContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = gridLayoutManager.findFirstVisibleItemPosition()
                val entity = mItemArrayList[position]
                if (entity.header != null) {
                    var index = 0
                    mMenuArrayList.forEachIndexed { _, searchMenuItemBean ->
                        searchMenuItemBean.isChecked = false
                        if (entity.header == searchMenuItemBean.classify_title) {
                            index = mMenuArrayList.indexOf(searchMenuItemBean)
                        }
                    }
                    mMenuArrayList[index].isChecked = true
                    menuAdapter.notifyDataSetChanged()
                    mRcMenu.smoothScrollToPosition(index)
                } else if (!mRcContent.canScrollVertically(1)) {
                    mMenuArrayList.forEachIndexed { _, searchMenuItemBean ->
                        searchMenuItemBean.isChecked = false
                    }
                    mMenuArrayList[mMenuArrayList.size - 1].isChecked = true
                    menuAdapter.notifyDataSetChanged()
                    mRcMenu.smoothScrollToPosition(mMenuArrayList.size - 1)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_TYPE, RequestParamsHelper.getProductTypeParams(arguments.getString(K_TYPE_FLAG)))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            mMenuArrayList.addAll(ListParseHelper<ClassifyBean>().fromJson(json.toString(), ClassifyBean::class.java))
            mMenuArrayList.forEach {
                val contentEntity = ClassifyItemEntity(true, it.classify_title)
                mItemArrayList.add(contentEntity)
                it.sub.forEach {
                    val item = ClassifyItemEntity(it)
                    mItemArrayList.add(item)
                }
            }
            if (!mMenuArrayList.isEmpty()) {
                menuItem = mMenuArrayList[0]
                mMenuArrayList[0].isChecked = true
            }
            menuAdapter.notifyDataSetChanged()
            contentAdapter.notifyDataSetChanged()
        } else {
            toast("加载失败，请稍后重试……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("加载失败，请稍后重试……")
    }

    class MenuAdapter(layoutId: Int, list: ArrayList<ClassifyBean>) : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(layoutId, list) {

        override fun convert(helper: BaseViewHolder?, item: ClassifyBean?) {
            helper?.setText(R.id.mSearchClassifyItemName, item?.classify_title)
            val ckName = helper?.getView<CheckedTextView>(R.id.mSearchClassifyItemName)
            ckName?.isChecked = item?.isChecked!!
        }
    }

    class ContentAdapter(layoutId: Int, headerLayoutId: Int, list: ArrayList<ClassifyItemEntity>) : BaseSectionQuickAdapter<ClassifyItemEntity, BaseViewHolder>(layoutId, headerLayoutId, list) {

        override fun convertHead(helper: BaseViewHolder?, item: ClassifyItemEntity?) {
            helper?.setText(R.id.mSearchClassifyContentItemTitle, item!!.header)
        }

        override fun convert(helper: BaseViewHolder?, item: ClassifyItemEntity?) {
            helper!!.setText(R.id.mSearchClassifyContentItemName, item!!.t.classify_title)
            val iv_icon = helper.getView<ImageView>(R.id.mSearchClassifyContentItemIcon)
            GlideManager.loadCircleImage(iv_icon.context, item.t.classify_pic, iv_icon)
        }
    }
}