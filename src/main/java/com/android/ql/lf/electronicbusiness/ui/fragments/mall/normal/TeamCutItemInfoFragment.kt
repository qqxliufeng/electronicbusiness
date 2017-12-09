package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextPaint
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.android.ql.lf.electronicbusiness.data.TeamCutInfoBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideImageLoader
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_team_cut_item_info_layout.*
import org.jetbrains.anko.backgroundColor

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
@Deprecated("过期了，请使用 CutGoodsInfoFragment")
class TeamCutItemInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var teamCutInfoBean: TeamCutInfoBean? = null

    private var shareDialog: BottomSheetDialog? = null

    private lateinit var tv_has_cut_money: TextView
    private lateinit var tv_goods_name: TextView
    private lateinit var tv_goods_desc: TextView
    private lateinit var tv_comment_count: TextView
    private lateinit var htv_content_info: HtmlTextView


    private val list = arrayListOf<CommentForGoodsBean>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_team_cut_item_info_layout

    override fun initView(view: View?) {
        mTvTeamCutItemInfoOldPrice.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mTvTeamCutItemInfoOldPrice.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        mRvTeamCutItemInfo.layoutManager = LinearLayoutManager(mContext)
        val adapter = GoodsInfoCommentAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
        mRvTeamCutItemInfo.adapter = adapter
        val topView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_top_layout, null)

        tv_has_cut_money = topView.findViewById(R.id.mTvPersonalCutItemInfoHasCutMoney)
        tv_goods_name = topView.findViewById(R.id.mTvPersonalCutItemInfoName)
        tv_goods_desc = topView.findViewById(R.id.mTvPersonalCutItemInfoDescription)

        tv_comment_count = topView.findViewById(R.id.mTvPersonalCutItemInfoCommentCount)

        adapter.addHeaderView(topView)
        mTvTeamCutItemInfoBuy.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitOrderFragment::class.java)
        }
        val bottomContent = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_layout, null)
        htv_content_info = bottomContent.findViewById(R.id.mHTvPersonalCutItemGoodsInfo)
        adapter.addFooterView(bottomContent)

        mTvTeamCutItemInfoBuy.setOnClickListener {
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载商品详情……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            teamCutInfoBean = Gson().fromJson(result.toString(), TeamCutInfoBean::class.java)
            mTvTeamCutItemInfoEveryOneCut.text = ("距离${teamCutInfoBean!!.result.kprice}元还差两人")
            mTvTeamCutItemInfoBuy.text = "￥${teamCutInfoBean!!.result.detail.product_price}\n立即购买"
            val endTime = teamCutInfoBean!!.result.endtime
            if ("0" == endTime) {
                mLlTeamCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)
                mTvTeamCutItemInfoDownTimeTitle.text = "活动已经结束"
                mTvTeamCutItemInfoBuy.isEnabled = false
                mTvTeamCutItemInfoCollection.isEnabled = false
                mTvTeamCutItemInfoCut.isEnabled = false
                mTvTeamCutItemInfoDownTime.setTime(0)
                mTvTeamCutItemInfoDownTime.stop()
            }
            mTvTeamCutItemInfoHasCutNum.text = "已参砍人数${teamCutInfoBean!!.result.detail.product_knum}人"
            mTvTeamCutItemInfoPrice.text = "￥ ${teamCutInfoBean!!.result.detail.product_price}"
            mTvTeamCutItemInfoOldPrice.text = "￥ ${teamCutInfoBean!!.result.detail.product_yprice}"
            mTvTeamCutItemInfoReleaseCount.text = teamCutInfoBean!!.result.detail.product_entrepot
            tv_has_cut_money.text = "累计已减${teamCutInfoBean!!.result.detail.product_minus}元"
            tv_goods_name.text = teamCutInfoBean!!.result.detail.product_name
            tv_goods_desc.text = Html.fromHtml(teamCutInfoBean!!.result.detail.product_ms)

            htv_content_info.setHtmlFromString(teamCutInfoBean!!.result.detail.product_content, false)

            setBanner()
        }
    }

    /**
     * 设置Banner
     */
    private fun setBanner() {
        mCBTeamCutItemInfo.setImageLoader(GlideImageLoader()).setImages(teamCutInfoBean!!.result.detail.product_pic)
                .setDelayTime(5000)
                .start()
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