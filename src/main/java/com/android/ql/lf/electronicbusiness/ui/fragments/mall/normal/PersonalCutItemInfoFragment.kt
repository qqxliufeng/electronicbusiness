package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.a.VipPrivilegeItemInfoFragment
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.ChatActivity
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_personal_cut_item_info_layout.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class PersonalCutItemInfoFragment : BaseNetWorkingFragment() {

    private var shareDialog: BottomSheetDialog? = null

    private val list = arrayListOf("", "")

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_item_info_layout

    override fun initView(view: View?) {
        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        mRvPersonalCutItemInfo.layoutManager = LinearLayoutManager(mContext)
        val adapter = VipPrivilegeItemInfoFragment.VipPrivilegeItemGoodsInfoAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
        mRvPersonalCutItemInfo.adapter = adapter
        val topView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_top_layout, null)
        adapter.addHeaderView(topView)
        topView.findViewById<TextView>(R.id.mTvAllComment).setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "全部评价", true, false, AllCommentFragment::class.java)
        }
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitOrderFragment::class.java)
        }

        val bottomRecommendView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_recommend_layout, null)
        val mRvRecommend = bottomRecommendView.findViewById<RecyclerView>(R.id.mRvPersonalCutItemGoodsInfoRecommend)
        mRvRecommend.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvRecommend.isNestedScrollingEnabled = false
        val recommendList = arrayListOf("", "", "", "")
        mRvRecommend.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_personal_cut_item_goods_info_bootom_recommend_item_layout, recommendList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
            }
        }
        adapter.addFooterView(bottomRecommendView, LinearLayoutManager.HORIZONTAL)

        adapter.addFooterView(View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_layout, null))
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            val bottomDialog = BottomSheetDialog(mContext)
            val contentView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_params_layout, null)
            val ivClose = contentView.findViewById<ImageView>(R.id.mTvBottomParamClose)
            val pic = contentView.findViewById<ImageView>(R.id.mIvGoodsPic)
            Glide.with(this)
                    .load(R.drawable.img_icon_test_pic_05)
                    .bitmapTransform(CenterCrop(mContext), RoundedCornersTransformation(mContext, 15, 0))
                    .into(pic)
            ivClose.setOnClickListener {
                bottomDialog.dismiss()
            }
            bottomDialog.setContentView(contentView)
            bottomDialog.window.findViewById<View>(R.id.design_bottom_sheet).backgroundColor = Color.TRANSPARENT
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, mContext.resources.displayMetrics).toInt()
            contentView.layoutParams.height = height
            val behavior = BottomSheetBehavior.from(contentView.parent as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = height
            bottomDialog.show()
        }
        mTvPersonalCutItemInfoCollection.setOnClickListener {
            toast("收藏成功")
        }
        mTvAskOnline.setOnClickListener {
            toast("请先登录")
//            val intent = IntentBuilder(mContext)
//                    .setServiceIMNumber("kefuchannelimid_866700")
//                    .build()
//            startActivity(intent)
//            startActivity(intentFor<ChatActivity>())
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