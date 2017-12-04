package com.a

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.webkit.WebViewClient

import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextPaint
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.TeamCutItemInfoFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.Constants
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiao.nicevideoplayer.NiceVideoPlayer
import kotlinx.android.synthetic.main.vip_privilege_item_info_layout.*
import com.xiao.nicevideoplayer.TxVideoPlayerController
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import org.jetbrains.anko.backgroundColor
import java.net.URL


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

    override fun getLayoutId() = R.layout.vip_privilege_item_info_layout

    override fun initView(view: View?) {
        val controller = TxVideoPlayerController(mContext)
        controller.setTitle("")
        controller.setImage(R.drawable.img_icon_test_pic_05)
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
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
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
            val detailHtml = detailJson.optString("product_content")
            mNiceVideoPlayer.setUp(detailJson.optString("product_video"), null)
        }
    }


//    class MyImageGetter : Html.ImageGetter {
//        override fun getDrawable(source: String?): Drawable {
//            var drawable: Drawable = ColorDrawable()
//            if (source != null) {
//                var newSrc = source
//                if (!source.startsWith("http://")) {
//                    newSrc = Constants.BASE_IP + source
//                }
////                GlideManager.downImage()
//            } else {
//                return drawable
//            }
//        }
//    }


    class VipPrivilegeItemGoodsInfoAdapter(layoutId: Int, list: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId, list) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }
    }
}