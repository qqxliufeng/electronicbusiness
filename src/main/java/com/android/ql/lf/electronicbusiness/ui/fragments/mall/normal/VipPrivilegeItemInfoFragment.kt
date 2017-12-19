package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextPaint
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.android.ql.lf.electronicbusiness.data.SpecificationBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.VipInfoFragment
import com.android.ql.lf.electronicbusiness.ui.views.BottomGoodsParamDialog
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.google.gson.Gson
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.hyphenate.helpdesk.model.ContentFactory
import kotlinx.android.synthetic.main.vip_privilege_item_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private val list = arrayListOf<CommentForGoodsBean>()
    private lateinit var adapter: GoodsInfoCommentAdapter

    private lateinit var tv_sell: TextView
    private lateinit var tv_release_count: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_old_price: TextView
    private lateinit var tv_goods_name: TextView
    private lateinit var tv_goods_content: TextView
    private lateinit var tv_comment_num: TextView
    private lateinit var htv_content_info: HtmlTextView

    private var bottomParamDialog: BottomGoodsParamDialog? = null

    private var picJsonArray: JSONArray? = null
    private var specifications: ArrayList<SpecificationBean>? = null

    private var goodsId: String? = null

    override fun getLayoutId() = R.layout.vip_privilege_item_info_layout

    override fun initView(view: View?) {
//        subscription = RxBus.getDefault().toObservable(UserInfo::class.java).subscribe {
//            when (UserInfo.getInstance().loginTag) {
//                0x50 -> {
//                    mTvVipPrivilegeGoodsInfoCollection.performClick()
//                }
//                0x51 -> {
//                    mTvVipPrivilegeGoodsInfoBuy.performClick()
//                }
//            }
//        }

        mNiceVideoPlayer.fullscreenButton.visibility = View.GONE
        mNiceVideoPlayer.titleTextView.visibility = View.GONE
        mNiceVideoPlayer.backButton.visibility = View.GONE
        adapter = GoodsInfoCommentAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
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
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "全部评价", true, false,
                    bundleOf(Pair(AllCommentFragment.GOODS_ID_FLAG, goodsId!!)), AllCommentFragment::class.java)
        }

        adapter.addHeaderView(topView)
        val bottomView = View.inflate(mContext, R.layout.layout_vip_privilege_item_goods_info_bottom_layout, null)
        htv_content_info = bottomView.findViewById(R.id.mHTvPersonalCutItemGoodsInfo)
        adapter.addFooterView(bottomView)
    }

    override fun onPause() {
        super.onPause()
        mNiceVideoPlayer.onVideoPause()
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL,
                RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在加载……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                loadComment(json)
                val detailJson = json.optJSONObject("result").optJSONObject("detail")
                goodsId = detailJson.optString("product_id")
                tv_sell.text = "${detailJson.optString("product_sv")}人购买"
                tv_release_count.text = "库存${detailJson.optString("product_entrepot")}"
                tv_price.text = "￥ ${detailJson.optString("product_price")}"
                tv_old_price.text = "￥ ${detailJson.optString("product_yprice")}"
                tv_goods_name.text = detailJson.optString("product_name")
                tv_goods_content.text = Html.fromHtml(detailJson.optString("product_ms"))
                picJsonArray = detailJson.optJSONArray("product_pic")
                val specificationJsonArray = detailJson.optJSONArray("product_specification")
                specifications = arrayListOf()
                (0 until specificationJsonArray.length()).forEach {
                    val specification = Gson().fromJson(specificationJsonArray.optJSONObject(it).toString(), SpecificationBean::class.java)
                    specifications!!.add(specification)
                }
                mTvVipPrivilegeGoodsInfoCollection.setOnClickListener {
                    if (UserInfo.getInstance().memberRank == "1") {
                        if (picJsonArray != null && picJsonArray!!.length() > 0) {
                            showBottomParamDialog(picJsonArray!!, specifications!!)
                        }
                    } else {
                        //开通会员
                        openVip()
                    }
//                    if (UserInfo.getInstance().isLogin) {
//
//                    } else {
//                        UserInfo.getInstance().loginTag = 0x50 //加入购物车
//                        LoginFragment.startLogin(context)
//                    }
                }
                mTvVipPrivilegeGoodsInfoBuy.setOnClickListener {
                    if (UserInfo.getInstance().memberRank == "1") {
                        if (picJsonArray != null && picJsonArray!!.length() > 0) {
                            showBottomParamDialog(picJsonArray!!, specifications!!)
                        }
                    } else {
                        //开通会员
                        openVip()
                    }
//                    if (UserInfo.getInstance().isLogin) {
//
//                    } else {
//                        UserInfo.getInstance().loginTag = 0x51 //立即购买
//                        LoginFragment.startLogin(context)
//                    }
                }
                mTvVipInfoOnlineAsk.setOnClickListener {
                    if (ChatClient.getInstance().isLoggedInBefore) {
                        val intent = IntentBuilder(mContext)
                                .setServiceIMNumber("kefuchannelimid_872049")
                                .setScheduleQueue(ContentFactory.createQueueIdentityInfo("砍价产品"))
                                .build()
                        startActivity(intent)
                    }
                }
                if (picJsonArray != null && picJsonArray!!.length() > 0) {
                    val thumbImageView = ImageView(mContext)
                    thumbImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    GlideManager.loadImage(mContext, picJsonArray!!.optString(0), thumbImageView)
                    mNiceVideoPlayer.thumbImageView = thumbImageView
                }
                mNiceVideoPlayer.setUp(detailJson.optString("product_video"), true, "")
                val detailHtml = detailJson.optString("product_content")
                htv_content_info.setHtmlFromString(detailHtml, false)
            } else {
                setEmptyView()
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                toast("加入购物车成功")
            }
        }
    }

    private fun openVip() {
        val build = AlertDialog.Builder(mContext)
        build.setMessage("您当前还不是VIP会员")
        build.setNegativeButton("暂不开通", null)
        build.setPositiveButton("立即开通") { _, _ ->
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "会员中心", true, false, VipInfoFragment::class.java)
            finish()
        }
        build.create().show()
    }


    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            setEmptyView()
        }
    }

    private fun setEmptyView() {
        mClVipItemInfoContainer.visibility = View.GONE
        mTvVipItemInfoEmpty.visibility = View.VISIBLE
    }

    private fun loadComment(json: JSONObject) {
        val optJSONObject = json.optJSONObject("arr")
        val commentJsonArray = optJSONObject.optJSONArray("list")
        tv_comment_num.text = "订单评价(${optJSONObject.optString("count")})"
        if (commentJsonArray != null && commentJsonArray.length() > 0) {
            (0 until commentJsonArray.length()).forEach {
                list.add(Gson().fromJson(commentJsonArray.optJSONObject(it).toString(), CommentForGoodsBean::class.java))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun showBottomParamDialog(picJsonArray: JSONArray, specifications: ArrayList<SpecificationBean>) {
        if (bottomParamDialog == null) {
            val defaultPicPath = if (picJsonArray.length() > 0) {
                picJsonArray.optString(0)
            } else {
                ""
            }
            bottomParamDialog = BottomGoodsParamDialog(context)
            bottomParamDialog!!.bindDataToView(tv_price.text.toString(),
                    tv_release_count.text.toString(),
                    tv_goods_name.text.toString(),
                    defaultPicPath, specifications)
            bottomParamDialog!!.setOnGoodsConfirmClickListener { specification, num ->
                mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_ADD_SHOPCART, RequestParamsHelper.getAddShopCartParam(goodsId!!, specification, num))
            }
        }
        bottomParamDialog!!.show()
    }

    override fun onDestroyView() {
        mNiceVideoPlayer.release()
        super.onDestroyView()
    }

}