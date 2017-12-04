package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyFlexboxLayout
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.Constants
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import com.xiao.nicevideoplayer.TxVideoPlayerController
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.vip_privilege_item_info_layout.*
import org.jetbrains.anko.backgroundColor


/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private val list = arrayListOf("", "")

    private lateinit var tv_sell: TextView
    private lateinit var tv_release_count: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_old_price: TextView
    private lateinit var tv_goods_name: TextView
    private lateinit var tv_goods_content: TextView
    private lateinit var tv_comment_num: TextView
    private lateinit var wb_detail: WebView

    private lateinit var controller: TxVideoPlayerController

    override fun getLayoutId() = R.layout.vip_privilege_item_info_layout

    override fun initView(view: View?) {
        controller = TxVideoPlayerController(mContext)
        controller.setTitle("")
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK)
        mNiceVideoPlayer.setController(controller)
        mNiceVideoPlayer.backgroundColor = Color.WHITE


        val adapter = VipPrivilegeItemGoodsInfoAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
        mRvVipPrivilegeItemGoodsInfo.layoutManager = LinearLayoutManager(mContext)
        mRvVipPrivilegeItemGoodsInfo.adapter = adapter

        val topView = View.inflate(mContext, R.layout.layout_vip_privilege_item_goods_info_top_layout, null)
        tv_sell = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopSell)
        tv_release_count = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopReleaseCount)
        tv_price = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopPrice)
        tv_old_price = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopOldPrice)
        tv_old_price.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        tv_goods_name = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopName)
        tv_goods_content = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopContent)
        tv_comment_num = topView.findViewById(R.id.mTvVipPrivilegeGoodsInfoTopAllCommentNum)
        topView.findViewById<TextView>(R.id.mTvVipPrivilegeGoodsInfoTopAllComment).setOnClickListener {
        }

        adapter.addHeaderView(topView)
        val bottomView = View.inflate(mContext, R.layout.layout_vip_privilege_item_goods_info_bottom_layout, null)
        wb_detail = bottomView.findViewById(R.id.mTvVipPrivilegeGoodsInfoBottomDetail)
        wb_detail.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                super.shouldOverrideUrlLoading(view, url)
                view.loadUrl(url)
                return true
            }
        }
        wb_detail.settings.javaScriptEnabled = true
        adapter.addFooterView(bottomView)

    }

    override fun onStop() {
        super.onStop()
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
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
        val json = checkResultCode(result)
        if (json != null) {
            val detailJson = json.optJSONObject("result").optJSONObject("detail")
            tv_sell.text = "${detailJson.optString("product_sv")}人购买"
            tv_release_count.text = "库存${detailJson.optString("product_entrepot")}"
            tv_price.text = "￥ ${detailJson.optString("product_price")}"
            tv_old_price.text = "￥ ${detailJson.optString("product_yprice")}"
            tv_goods_name.text = detailJson.optString("product_name")
            tv_goods_content.text = Html.fromHtml(detailJson.optString("product_ms"))

            val picJsonArray = detailJson.optJSONArray("product_pic")

            val specificationJsonArray = detailJson.optJSONArray("product_specification")
            val specifications = arrayListOf<SpecificationBean>()
            (0 until specificationJsonArray.length()).forEach {
                val specification = Gson().fromJson(specificationJsonArray.optJSONObject(it).toString(), SpecificationBean::class.java)
                specifications.add(specification)
            }
            mTvVipPrivilegeGoodsInfoCollection.setOnClickListener {
                val bottomDialog = BottomSheetDialog(mContext)
                val contentView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_params_layout, null)
                val tvPrice = contentView.findViewById<TextView>(R.id.mTvBottomParamPrice)
                val tvReleaseCount = contentView.findViewById<TextView>(R.id.mTvBottomParamReleaseCount)
                val tvName = contentView.findViewById<TextView>(R.id.mTvBottomParamName)
                tvPrice.text = tv_price.text
                tvReleaseCount.text = tv_release_count.text
                tvName.text = tv_goods_name.text

                val llContainer = contentView.findViewById<LinearLayout>(R.id.mLlBottomParamRuleContainer)
                val pic = contentView.findViewById<ImageView>(R.id.mIvGoodsPic)
                if (!specifications.isEmpty()) {
                    for (specification in specifications) {
                        val myFlexboxLayout = MyFlexboxLayout(context)
                        myFlexboxLayout.setTitle(specification.name)
                        myFlexboxLayout.addItems(specification.item)
                        myFlexboxLayout.setOnItemClickListener {
                            GlideManager.loadRoundImage(context, specification.pic[it], pic, 15)
                        }
                        llContainer.addView(myFlexboxLayout)
                    }
                }
                val ivClose = contentView.findViewById<ImageView>(R.id.mTvBottomParamClose)
                ivClose.setOnClickListener {
                    bottomDialog.dismiss()
                }

                if (picJsonArray != null && picJsonArray.length() > 0) {
                    GlideManager.loadRoundImage(context, picJsonArray.optString(0), pic, 15)
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


            mNiceVideoPlayer.setUp(detailJson.optString("product_video"), null)
            val detailHtml = detailJson.optString("product_content")
            wb_detail.loadData(detailHtml.replace("src=\"", "src=\"${Constants.BASE_IP}"), "text/html; charset=UTF-8", null)
        }
    }


    class VipPrivilegeItemGoodsInfoAdapter(layoutId: Int, list: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId, list) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }
    }


    class SpecificationBean {
        lateinit var name: String
        lateinit var item: ArrayList<String>
        lateinit var pic: ArrayList<String>
    }
}