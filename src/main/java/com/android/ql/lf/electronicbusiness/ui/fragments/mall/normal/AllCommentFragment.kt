package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.adapters.AllCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/21 0021.
 * @author lf on 2017/11/21 0021
 */
class AllCommentFragment : BaseRecyclerViewFragment<String>() {


    override fun getLayoutId() = R.layout.fragment_all_comment_layout

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
            AllCommentAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, mArrayList)

    override fun onRefresh() {
        (0..9).forEach {
            mArrayList.add("")
        }
        mBaseAdapter.notifyDataSetChanged()
        super.onRefresh()
        setLoadEnable(false)
        setRefreshEnable(false)
    }

}