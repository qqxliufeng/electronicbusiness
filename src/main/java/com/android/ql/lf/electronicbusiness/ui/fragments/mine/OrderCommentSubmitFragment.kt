package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_order_comment_submit_layout.*

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderCommentSubmitFragment : BaseNetWorkingFragment() {

    private val imageList = arrayListOf<Bitmap>()


    override fun getLayoutId(): Int = R.layout.fragment_order_comment_submit_layout


    override fun initView(view: View?) {
        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_icon_star_n)
        val layoutParams = mRatingBar.layoutParams
        layoutParams.height = bmp.height
        layoutParams.width = -2
        mRatingBar.layoutParams = layoutParams


        BitmapFactory.decodeResource(mContext.resources, R.drawable.img_add_image)
                .apply {
            imageList.add(this)
        }
                .apply {
            imageList.add(this)
        }.apply {
            imageList.add(this)
        }.apply {
            imageList.add(this)
        }.apply {
            imageList.add(this)
        }
        mRvOrderComment.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvOrderComment.adapter = CommentImageAdapter(R.layout.adapter_comment_image_item_layout, imageList)
        mRvOrderComment.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            }
        })
    }


    class CommentImageAdapter(layoutId: Int, list: ArrayList<Bitmap>) : BaseQuickAdapter<Bitmap, BaseViewHolder>(layoutId, list) {
        override fun convert(helper: BaseViewHolder?, item: Bitmap?) {
            helper!!.setImageBitmap(R.id.mIvCommentImageItem, item)
        }
    }

}