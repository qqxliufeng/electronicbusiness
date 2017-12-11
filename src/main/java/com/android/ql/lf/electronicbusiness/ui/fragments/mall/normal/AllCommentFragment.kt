package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_all_comment_layout.*

/**
 * Created by lf on 2017/11/21 0021.
 * @author lf on 2017/11/21 0021
 */
class AllCommentFragment : BaseRecyclerViewFragment<CommentForGoodsBean>() {


    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    override fun getLayoutId() = R.layout.fragment_all_comment_layout

    override fun createAdapter(): BaseQuickAdapter<CommentForGoodsBean, BaseViewHolder> =
            GoodsInfoCommentAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_ALL_COMMENT,
                RequestParamsHelper.getAllCommentParam(arguments.getString(GOODS_ID_FLAG, ""), currentPage))
    }


    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            mTvAllCommentNum.text = "全部评价（${json.optString("arr")}）"
        } else {
            mTvAllCommentNum.text = "全部评价（0）"
        }
        processList(json, CommentForGoodsBean::class.java)
    }

}