package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextPaint
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_personal_cut_item_info_layout.*
import org.jetbrains.anko.backgroundColor

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class TeamCutItemInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var shareDialog: BottomSheetDialog? = null

    private val list = arrayListOf("", "")

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_team_cut_item_info_layout

    override fun initView(view: View?) {
        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        mRvPersonalCutItemInfo.layoutManager = LinearLayoutManager(mContext)
        val adapter = VipPrivilegeItemInfoFragment.VipPrivilegeItemGoodsInfoAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
        mRvPersonalCutItemInfo.adapter = adapter
        val topView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_top_layout, null)
        adapter.addHeaderView(topView)
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitOrderFragment::class.java)
        }
        adapter.addFooterView(View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_layout, null))
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            val bottomDialog = BottomSheetDialog(mContext)
            val contentView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_params_layout, null)
            val ivClose = contentView.findViewById<ImageView>(R.id.mTvBottomParamClose)
            val pic = contentView.findViewById<ImageView>(R.id.mIvGoodsPic)
            ivClose.setOnClickListener {
                bottomDialog.dismiss()
            }
            Glide.with(this)
                    .load(R.drawable.img_icon_test_pic_05)
                    .bitmapTransform(CenterCrop(mContext), RoundedCornersTransformation(mContext, 15, 0))
                    .into(pic)
            bottomDialog.setContentView(contentView)
            bottomDialog.window.findViewById<View>(R.id.design_bottom_sheet).backgroundColor = Color.TRANSPARENT
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, mContext.resources.displayMetrics).toInt()
            contentView.layoutParams.height = height
            val behavior = BottomSheetBehavior.from(contentView.parent as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = height
            bottomDialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuShare) {
            if (shareDialog == null) {
                shareDialog = BottomSheetDialog(mContext)
                val shareContentView = View.inflate(mContext, R.layout.layout_share_layout, null)
                shareContentView.findViewById<TextView>(R.id.mTvShareClose).setOnClickListener { shareDialog!!.dismiss() }
                shareContentView.findViewById<TextView>(R.id.mTvShareWX).setOnClickListener { shareDialog!!.dismiss() }
                shareContentView.findViewById<TextView>(R.id.mTvShareQQ).setOnClickListener { shareDialog!!.dismiss() }
                shareContentView.findViewById<TextView>(R.id.mTvShareWB).setOnClickListener { shareDialog!!.dismiss() }
                shareContentView.findViewById<TextView>(R.id.mTvShareCircle).setOnClickListener { shareDialog!!.dismiss() }
                shareDialog!!.setContentView(shareContentView)
            }
            shareDialog!!.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载商品详情……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
    }

    override fun onDestroyView() {
        if (shareDialog != null) {
            if (shareDialog!!.isShowing) {
                shareDialog!!.dismiss()
            }
            shareDialog = null
        }
        super.onDestroyView()
    }

}