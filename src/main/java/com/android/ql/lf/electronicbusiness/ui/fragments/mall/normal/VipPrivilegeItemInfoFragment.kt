package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextPaint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.*
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.VipInfoFragment
import com.android.ql.lf.electronicbusiness.ui.views.BottomGoodsParamDialog
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideManager
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
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

    private var picJsonArray: ArrayList<String>? = null
    private lateinit var specifications: ArrayList<SpecificationBean>

    private var goodsId: String? = null

    //0加入购物车  1立即购买
    private var currentMode = 0

    private var vipGoodsInfoBean:VipGoodsInfoBean? = null


    override fun getLayoutId() = R.layout.vip_privilege_item_info_layout

    override fun initView(view: View?) {
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
                vipGoodsInfoBean = Gson().fromJson(json.toString(),VipGoodsInfoBean::class.java)
                loadComment()
                goodsId = vipGoodsInfoBean!!.result.detail.product_id
                tv_sell.text = "${vipGoodsInfoBean!!.result.detail.product_sv}人购买"
                tv_release_count.text = "库存${vipGoodsInfoBean!!.result.detail.product_entrepot}"
                tv_price.text = "￥ ${vipGoodsInfoBean!!.result.detail.product_price}"
                tv_old_price.text = "￥ ${vipGoodsInfoBean!!.result.detail.product_yprice}"
                tv_goods_name.text = vipGoodsInfoBean!!.result.detail.product_name
                tv_goods_content.text = Html.fromHtml(vipGoodsInfoBean!!.result.detail.product_ms)
                picJsonArray = vipGoodsInfoBean!!.result.detail.product_pic
                specifications = vipGoodsInfoBean!!.result.detail.product_specification
                mTvVipPrivilegeGoodsInfoCollection.setOnClickListener {
                    if (UserInfo.getInstance().memberRank == "1") {
                        if (picJsonArray != null && picJsonArray!!.size > 0) {
                            currentMode = 0
                            showBottomParamDialog(picJsonArray!!, specifications!!)
                        }
                    } else {
                        //开通会员
                        openVip()
                    }
                }
                mTvVipPrivilegeGoodsInfoBuy.setOnClickListener {
                    if (UserInfo.getInstance().memberRank == "1") {
                        if (picJsonArray != null && picJsonArray!!.size > 0) {
                            currentMode = 1
                            showBottomParamDialog(picJsonArray!!, specifications!!)
                        }
                    } else {
                        //开通会员
                        openVip()
                    }
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
                if (picJsonArray != null && picJsonArray!!.size > 0) {
                    val thumbImageView = ImageView(mContext)
                    thumbImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    GlideManager.loadImage(mContext, picJsonArray!![0], thumbImageView)
                    mNiceVideoPlayer.thumbImageView = thumbImageView
                }
                mNiceVideoPlayer.setUp(vipGoodsInfoBean!!.result.detail.product_video, true, "")
                val detailHtml = vipGoodsInfoBean!!.result.detail.product_content
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

    private fun loadComment() {
        tv_comment_num.text = "订单评价(${vipGoodsInfoBean!!.arr.count})"
        list.addAll(vipGoodsInfoBean!!.arr.list)
        adapter.notifyDataSetChanged()
    }

    private fun showBottomParamDialog(picJsonArray: ArrayList<String>, specifications: ArrayList<SpecificationBean>) {
        if (bottomParamDialog == null) {
            val defaultPicPath = if (picJsonArray.size > 0) {
                picJsonArray[0]
            } else {
                ""
            }
            bottomParamDialog = BottomGoodsParamDialog(context)
            bottomParamDialog!!.bindDataToView(tv_price.text.toString(),
                    tv_release_count.text.toString(),
                    tv_goods_name.text.toString(),
                    defaultPicPath, specifications)
            bottomParamDialog!!.setOnGoodsConfirmClickListener { specification, num ->
                if (currentMode == 0) { //加入购物车
                    mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_ADD_SHOPCART, RequestParamsHelper.getAddShopCartParam(goodsId!!, specification, num))
                }else{ // 立即购买
                    val shoppingCarItem = ShoppingCarItemBean()
                    shoppingCarItem.shopcart_mdprice = vipGoodsInfoBean!!.result.detail.product_mdprice
                    shoppingCarItem.shopcart_num = num
                    shoppingCarItem.shopcart_price = vipGoodsInfoBean!!.result.detail.product_price
                    shoppingCarItem.shopcart_name = vipGoodsInfoBean!!.result.detail.product_name
                    shoppingCarItem.shopcart_gid = vipGoodsInfoBean!!.result.detail.product_id
                    shoppingCarItem.shopcart_id = ""
                    shoppingCarItem.shopcart_ktype = vipGoodsInfoBean!!.result.detail.product_ktype
                    shoppingCarItem.shopcart_pic = vipGoodsInfoBean!!.result.detail.product_pic
                    shoppingCarItem.shopcart_specification = specification
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(SubmitNewOrderFragment.GOODS_ID_FLAG, arrayListOf(shoppingCarItem))
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "确认订单", true, false, bundle, SubmitNewOrderFragment::class.java)
                }
            }
        }
        bottomParamDialog!!.show()
    }

    override fun onDestroyView() {
        mNiceVideoPlayer.release()
        super.onDestroyView()
    }

}