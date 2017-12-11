package com.android.ql.lf.electronicbusiness.ui.adapters

import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/21 0021.
 * @author lf on 2017/11/21 0021
 */
class AllCommentAdapter(layoutId:Int,list: ArrayList<CommentForGoodsBean>) : BaseQuickAdapter<CommentForGoodsBean,BaseViewHolder>(layoutId,list){
    override fun convert(helper: BaseViewHolder?, item: CommentForGoodsBean?) {

    }
}