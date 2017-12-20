package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextPaint
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.*
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.adapters.RecommedGoodsInfoAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.BottomGoodsParamDialog
import com.android.ql.lf.electronicbusiness.ui.views.EasyCountDownTextureView
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.easeui.util.IntentBuilder
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.fragment_personal_cut_item_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class CutGoodsInfoFragment : BaseNetWorkingFragment(), SwipeRefreshLayout.OnRefreshListener, WbShareCallback, IUiListener {


    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private val commentList = arrayListOf<CommentForGoodsBean>()
    private lateinit var adapter: GoodsInfoCommentAdapter

    private val recommendList = arrayListOf<GoodsItemBean>()
    private lateinit var recommendAdapter: BaseQuickAdapter<GoodsItemBean, BaseViewHolder>


    private lateinit var tv_has_cut_money: TextView
    private lateinit var tv_goods_name: TextView
    private lateinit var tv_goods_desc: TextView

    private lateinit var mRvRecommend: RecyclerView

    private lateinit var tv_comment_count: TextView
    private lateinit var htv_content_info: HtmlTextView

    private var cutInfoBean: CutGoodsInfoBean? = null

    private var bottomParamDialog: BottomGoodsParamDialog? = null
    private var shareDialog: BottomSheetDialog? = null

    private var shareBitmapPic: Bitmap? = null


    //1 代表个人砍  2代表团体砍
    private var currentMode = 1

    // 0 代表添加到购物车   1 代表立即购买
    private var bottomDialogActionType = 0

    /**
     * 微信api
     */
    private val api by lazy {
        WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID)
    }

    /**
     * 微博api
     */
    private val shareHandler by lazy {
        WbShareHandler(mContext as Activity)
    }

    /**
     * qq api
     */
    private val tencent by lazy {
        Tencent.createInstance(Constants.QQ_APP_ID, mContext)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_item_info_layout

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(Intent::class.java).subscribe {
            val requestCode = it.getIntExtra("requestCode", 0)
            if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE) {
                Tencent.handleResultData(it, this@CutGoodsInfoFragment)
            } else if (requestCode == -1) {
                shareHandler.doResultIntent(it, this@CutGoodsInfoFragment)
            }
        }

        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.ANTI_ALIAS_FLAG
        mTvPersonalCutItemInfoOldPrice.paint.flags = TextPaint.STRIKE_THRU_TEXT_FLAG

        mRvPersonalCutItemInfo.layoutManager = LinearLayoutManager(mContext)
        adapter = GoodsInfoCommentAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, commentList)
        mRvPersonalCutItemInfo.adapter = adapter


        val topView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_top_layout, null)
        tv_has_cut_money = topView.findViewById(R.id.mTvPersonalCutItemInfoHasCutMoney)
        tv_goods_name = topView.findViewById(R.id.mTvPersonalCutItemInfoName)
        tv_goods_desc = topView.findViewById(R.id.mTvPersonalCutItemInfoDescription)

        tv_comment_count = topView.findViewById(R.id.mTvPersonalCutItemInfoCommentCount)

        adapter.addHeaderView(topView)
        topView.findViewById<TextView>(R.id.mTvPersonalCutItemInfoCommentCountAll).setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "全部评价", true, false, bundleOf(Pair(AllCommentFragment.GOODS_ID_FLAG, cutInfoBean!!.result.detail.product_id)), AllCommentFragment::class.java)
        }
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitOrderFragment::class.java)
        }

        val bottomRecommendView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_recommend_layout, null)
        mRvRecommend = bottomRecommendView.findViewById(R.id.mRvPersonalCutItemGoodsInfoRecommend)
        mRvRecommend.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvRecommend.isNestedScrollingEnabled = false

        recommendAdapter = RecommedGoodsInfoAdapter(R.layout.layout_personal_cut_item_goods_info_bootom_recommend_item_layout, recommendList)
        mRvRecommend.adapter = recommendAdapter
        mRvRecommend.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundleOf(Pair(CutGoodsInfoFragment.GOODS_ID_FLAG, recommendList[position].product_id)), CutGoodsInfoFragment::class.java)
            }
        })
        adapter.addFooterView(bottomRecommendView, LinearLayoutManager.HORIZONTAL)

        val bottomInfoContentView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_layout, null)
        htv_content_info = bottomInfoContentView.findViewById(R.id.mHTvPersonalCutItemGoodsInfo)
        adapter.addFooterView(bottomInfoContentView)
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            if (cutInfoBean != null) {
                //重新获取新的价格
                bottomDialogActionType = 1 //立即购买
                mPresent.getDataByPost(0x4, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
            }
        }
        mTvPersonalCutItemInfoCollection.setOnClickListener {
            if (cutInfoBean != null) {
                //重新获取新的价格
                bottomDialogActionType = 0 //加入购物车
                mPresent.getDataByPost(0x4, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
            }
        }
        mTvAskOnline.setOnClickListener {
            if (cutInfoBean != null && ChatClient.getInstance().isLoggedInBefore) {
                val intent = IntentBuilder(mContext)
                        .setServiceIMNumber(Constants.HX_IM_SERVICE_NUM)
                        .build()
                startActivity(intent)
            }
        }
    }

    private fun showBottomParamDialog() {
        if (bottomParamDialog == null) {
            bottomParamDialog = BottomGoodsParamDialog(mContext)
            bottomParamDialog!!.setOnGoodsConfirmClickListener { specification, selectPic, num ->
                if (bottomDialogActionType == 0) {
                    mPresent.getDataByPost(0x3,
                            RequestParamsHelper.MEMBER_MODEL,
                            RequestParamsHelper.ACT_ADD_SHOPCART,
                            RequestParamsHelper.getAddShopCartParam(cutInfoBean!!.result.detail.product_id, selectPic+","+specification, num))
                } else if (bottomDialogActionType == 1) {
                    val shoppingCarItem = ShoppingCarItemBean()
                    shoppingCarItem.shopcart_mdprice = cutInfoBean!!.result.detail.product_mdprice
                    shoppingCarItem.shopcart_num = num
                    shoppingCarItem.shopcart_price = cutInfoBean!!.result.detail.product_price
                    shoppingCarItem.shopcart_name = cutInfoBean!!.result.detail.product_name
                    shoppingCarItem.shopcart_gid = cutInfoBean!!.result.detail.product_id
                    shoppingCarItem.shopcart_id = ""
                    shoppingCarItem.shopcart_ktype = cutInfoBean!!.result.detail.product_ktype
                    if (TextUtils.isEmpty(selectPic)) {
                        shoppingCarItem.shopcart_pic = cutInfoBean!!.result.detail.product_pic as ArrayList<String>
                    } else {
                        shoppingCarItem.shopcart_pic = arrayListOf(selectPic)
                    }
                    shoppingCarItem.shopcart_specification = specification
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(SubmitNewOrderFragment.GOODS_ID_FLAG, arrayListOf(shoppingCarItem))
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "确认订单", true, false, bundle, SubmitNewOrderFragment::class.java)
                }
            }
        }
        bottomParamDialog!!.bindDataToView(
                "￥ ${cutInfoBean!!.result.detail.product_price}",
                "库存：${cutInfoBean!!.result.detail.product_entrepot}",
                cutInfoBean!!.result.detail.product_name,
                if (!cutInfoBean!!.result.detail.product_pic.isEmpty()) cutInfoBean!!.result.detail.product_pic[0] else "",
                cutInfoBean!!.result.detail.product_specification)
        bottomParamDialog!!.show()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRefresh() {
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext)
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        when (requestID) {
            0x0 -> {
                if (json != null) {
                    bindData(json)
                } else {
                    setEmptyView()
                }
            }
            0x1 -> {//个人砍
                onPersonalCutAction(json, result)
            }
            0x2 -> {//团体砍
                onTeamCutAction(json, result)
            }
            0x3 -> { //加入到购物车
                if (json != null) {
                    toast("加入购物车成功")
                }
            }
            0x4 -> { //重新获取数据  刷新界面
                if (json != null) {
                    if (cutInfoBean != null) {
                        cutInfoBean = null
                    }
                    cutInfoBean = Gson().fromJson(json.toString(), CutGoodsInfoBean::class.java)
                    reBindData()
                    if ("1" != cutInfoBean!!.result.detail.product_endstatus) {
                        showBottomParamDialog()
                    } else {
                        toast("活动已经结束")
                    }
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        setEmptyView()
    }

    private fun setEmptyView() {
        mClCutInfoContainer.visibility = View.GONE
        mTvCutItemInfoEmpty.visibility = View.VISIBLE
    }


    private fun bindData(json: JSONObject) {
        cutInfoBean = Gson().fromJson(json.toString(), CutGoodsInfoBean::class.java)
        reBindData()
        setRecommendList()
        setCommentList()
        setBanner()
        tv_goods_desc.text = Html.fromHtml(cutInfoBean!!.result.detail.product_ms)
        htv_content_info.setHtmlFromString(cutInfoBean!!.result.detail.product_content, false)
        adapter.notifyDataSetChanged()
        mRvPersonalCutItemInfo.smoothScrollToPosition(0)
    }

    /**
     * 重新绑定数据
     */
    private fun reBindData() {
        currentMode = cutInfoBean!!.result.detail.product_ktype.toInt()
        when (currentMode) {
            1 -> { //个人砍
                reBindPersonalData()
            }
            2 -> { //团体砍
                reBindTeamData()
            }
        }
        setActivityStatus(cutInfoBean!!.result.detail.product_endstatus)
        mTvPersonalCutItemInfoPrice.text = "￥ ${cutInfoBean!!.result.detail.product_price}"
        mTvPersonalCutItemInfoOldPrice.text = "￥ ${cutInfoBean!!.result.detail.product_yprice}"
        mTvPersonalCutItemInfoReleaseCount.text = cutInfoBean!!.result.detail.product_entrepot
        tv_goods_name.text = cutInfoBean!!.result.detail.product_name
    }

    private fun setActivityStatus(status: String) {
        if (TextUtils.equals("1", status)) { // 1 是结束
            mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)
            mTvPersonalCutItemInfoDownTimeTitle.text = "活动已经结束"
            mTvPersonalCutItemInfoBuy.isEnabled = false
            mTvPersonalCutItemInfoCollection.isEnabled = false
            mTvPersonalCutItemInfoCut.isEnabled = false
            mTvPersonalCutItemInfoDownTime.setTimeHour(0)
            mTvPersonalCutItemInfoDownTime.setTimeMinute(0)
            mTvPersonalCutItemInfoDownTime.setTimeSecond(0)
            mTvPersonalCutItemInfoDownTime.stop()
        } else if (TextUtils.equals("0", status)) { // 0 是正在进行
            if (currentMode == 1) { //个人砍
                mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_personal_cut_bg)
            } else { //团体砍
                when (cutInfoBean!!.result.detail.product_jtoken) {
                    "0" -> {
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_personal_cut_bg)
                    }
                    "1" -> {
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_step_one)
                    }
                    "2" -> {
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_step_two)
                    }
                    "3" -> {
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_step_three)
                    }
                    else -> {
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)
                    }
                }
            }
            mTvPersonalCutItemInfoDownTimeTitle.text = "距结束还剩："
            mTvPersonalCutItemInfoBuy.isEnabled = true
            mTvPersonalCutItemInfoCollection.isEnabled = true
            mTvPersonalCutItemInfoCut.isEnabled = true
            val endTime = cutInfoBean!!.result.endtime
            if (!TextUtils.isEmpty(endTime) && !TextUtils.equals("0", endTime)) {
                val splitList = endTime.split(":")
                mTvPersonalCutItemInfoDownTime.setTimeHour(splitList[0].toInt())
                mTvPersonalCutItemInfoDownTime.setTimeMinute(splitList[1].toInt())
                mTvPersonalCutItemInfoDownTime.setTimeSecond(splitList[2].toInt())
                mTvPersonalCutItemInfoDownTime.setEasyCountDownListener(object : EasyCountDownTextureView.EasyCountDownListener {
                    override fun onCountDownStart() {
                    }

                    override fun onCountDownTimeError() {
                    }

                    override fun onCountDownStop(millisInFuture: Long) {
                    }

                    override fun onCountDownCompleted() {
                        //活动结束不能再砍了
                        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)
                        mTvPersonalCutItemInfoCut.isEnabled = false
                        mTvPersonalCutItemInfoBuy.isEnabled = false
                        mTvPersonalCutItemInfoCollection.isEnabled = false
                    }
                })
                mTvPersonalCutItemInfoDownTime.start()
            } else {
                mTvPersonalCutItemInfoDownTime.setTime(0)
                mTvPersonalCutItemInfoDownTime.stop()
            }
            if (TextUtils.equals("1", cutInfoBean!!.result.detail.product_jtoken) || TextUtils.equals("1", cutInfoBean!!.result.detail.product_ptype)) {
                mTvPersonalCutItemInfoCut.isEnabled = false //已经砍过一次价了 或者已经到最低价了 都不能再砍了
            }
        }
    }

    private fun setRecommendList() {
        recommendList.addAll(cutInfoBean!!.result.kind)
        recommendAdapter.notifyDataSetChanged()
    }

    private fun setCommentList() {
        tv_comment_count.text = "商品评价(${cutInfoBean!!.arr.count})"
        commentList.addAll(cutInfoBean!!.arr.list)
    }

    /**
     * 设置Banner
     */
    private fun setBanner() {
        mCBPersonalCutItemInfo.setImageLoader(GlideImageLoader()).setImages(cutInfoBean!!.result.detail.product_pic)
                .setDelayTime(5000)
                .start()
    }

    override fun onResume() {
        super.onResume()
        mCBPersonalCutItemInfo.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        mCBPersonalCutItemInfo.stopAutoPlay()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuShare) {
            if (cutInfoBean != null) {
                if (shareDialog == null) {
                    shareDialog = BottomSheetDialog(mContext)
                    val shareContentView = View.inflate(mContext, R.layout.layout_share_layout, null)
                    shareContentView.findViewById<TextView>(R.id.mTvShareClose).setOnClickListener { shareDialog!!.dismiss() }
                    shareContentView.findViewById<TextView>(R.id.mTvShareWX).setOnClickListener {
                        if (shareBitmapPic == null) {
                            Glide.with(mContext).load("${Constants.BASE_IP}${cutInfoBean!!.result.share.pic}").asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    shareBitmapPic = resource
                                    shareToWx(SendMessageToWX.Req.WXSceneSession)
                                }
                            })
                        } else {
                            shareToWx(SendMessageToWX.Req.WXSceneSession)
                        }
                        shareDialog!!.dismiss()
                    }
                    shareContentView.findViewById<TextView>(R.id.mTvShareQQ).setOnClickListener {
                        ShareManager.shareToQQ(mContext, tencent, cutInfoBean!!.result.detail.product_name,
                                Html.fromHtml(cutInfoBean!!.result.share.ms).toString(),
                                "${Constants.BASE_IP}${cutInfoBean!!.result.share.url}",
                                Constants.BASE_IP + cutInfoBean!!.result.share.pic,
                                this@CutGoodsInfoFragment)
                        shareDialog!!.dismiss()
                    }
                    shareContentView.findViewById<TextView>(R.id.mTvShareWB).setOnClickListener {
                        if (shareBitmapPic == null) {
                            Glide.with(mContext).load("${Constants.BASE_IP}${cutInfoBean!!.result.share.pic}").asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    shareBitmapPic = resource
                                    shareToWb()
                                }
                            })
                        } else {
                            shareToWb()
                        }
                        shareDialog!!.dismiss()
                    }
                    shareContentView.findViewById<TextView>(R.id.mTvShareCircle).setOnClickListener {
                        if (shareBitmapPic == null) {
                            Glide.with(mContext).
                                    load("${Constants.BASE_IP}${cutInfoBean!!.result.share.pic}").
                                    asBitmap().
                                    diskCacheStrategy(DiskCacheStrategy.NONE).into(object : SimpleTarget<Bitmap>(ShareManager.SHARE_PIC_WIDTH, ShareManager.SHARE_PIC_HEIGHT) {
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    shareBitmapPic = resource
                                    shareToWx(SendMessageToWX.Req.WXSceneTimeline)
                                }
                            })
                        } else {
                            shareToWx(SendMessageToWX.Req.WXSceneTimeline)
                        }
                        shareDialog!!.dismiss()
                    }
                    shareDialog!!.setContentView(shareContentView)
                }
                shareDialog!!.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 分享到微信好友
     */
    private fun shareToWx(scene: Int) {
        ShareManager.shareToWxWebPager(api,
                scene,
                "${Constants.BASE_IP}${cutInfoBean!!.result.share.url}",
                cutInfoBean!!.result.share.title,
                Html.fromHtml(cutInfoBean!!.result.share.ms).toString(),
                shareBitmapPic!!
        )
    }

    /**
     * 分享到微博
     */
    private fun shareToWb() {
        ShareManager.shareToWBWebPager(shareHandler,
                cutInfoBean!!.result.detail.product_name,
                Html.fromHtml(cutInfoBean!!.result.share.ms).toString(),
                "${Constants.BASE_IP}${cutInfoBean!!.result.share.url}",
                shareBitmapPic, cutInfoBean!!.result.detail.product_name)
    }


    //========================微博分享回调========================//
    override fun onWbShareFail() {
        toast("分享失败")
    }

    override fun onWbShareCancel() {
        toast("分享取消")
    }

    override fun onWbShareSuccess() {
        toast("分享成功")
    }


    //========================QQ分享回调========================//
    override fun onComplete(p0: Any?) {
        toast("分享成功")
    }

    override fun onCancel() {
        toast("分享取消")
    }

    override fun onError(p0: UiError?) {
        toast("分享失败")
    }


    /**          个人砍            **/

    /**
     * 绑定个人砍数据
     */
    private fun reBindPersonalData() {
        mTvPersonalCutItemInfoEveryOneCut.text = ("每个人砍价${cutInfoBean!!.result.kprice}元")
        mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${cutInfoBean!!.result.detail.product_knum}人"
        mTvPersonalCutItemInfoEveryOneCut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_mark_personal_cut, 0, 0, 0)
        mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
        mTvPersonalCutItemInfoCut.text = "- ￥${cutInfoBean!!.result.kprice}\n砍价"
        tv_has_cut_money.text = "累计已减${cutInfoBean!!.result.detail.product_minus}元"
        mTvPersonalCutItemInfoCut.setOnClickListener {
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_ONEBARGAIN,
                    RequestParamsHelper.getOnebargainParam(cutInfoBean!!.result.detail.product_id))
        }
    }

    /**
     * 点击砍价（个人砍）操作
     */
    private fun <T : Any?> onPersonalCutAction(json: JSONObject?, result: T) {
        if (json != null) {
            UserInfo.getInstance().memberIntegral = json.optString("arr1")
            toast(json.optString("msg"))
            refreshPersonalDataToView(json)
        } else {
            toast(JSONObject(result.toString()).optString("msg"))
            mTvPersonalCutItemInfoCut.isEnabled = false //已经砍过一次价了 或者已经到最低价了 都不能再砍了
        }
    }

    /**
     * 刷新界面
     */
    private fun refreshPersonalDataToView(json: JSONObject?) {
        cutInfoBean!!.result.detail.product_knum = json!!.optJSONObject("result").optString("product_knum")
        cutInfoBean!!.result.detail.product_price = json.optJSONObject("result").optString("product_price")
        cutInfoBean!!.result.detail.product_minus = json.optJSONObject("result").optString("product_minus")
        cutInfoBean!!.result.detail.product_jtoken = json.optJSONObject("result").optString("product_jtoken")
        mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${cutInfoBean!!.result.detail.product_knum}人"
        mTvPersonalCutItemInfoPrice.text = "￥ ${cutInfoBean!!.result.detail.product_price}"
        tv_has_cut_money.text = "累计已减${cutInfoBean!!.result.detail.product_minus}元"
        mTvPersonalCutItemInfoCut.isEnabled = false// 已经砍过一次了，不能再砍了
        mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
    }
    /**          个人砍            **/


    /**          团体砍            **/

    /**
     * 绑定团体砍数据
     */
    private fun reBindTeamData() {

        mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)


        mTvPersonalCutItemInfoEveryOneCut.text = "距离${cutInfoBean!!.result.nextprice}元还差${cutInfoBean!!.result.resnum}人"
        mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${cutInfoBean!!.result.detail.product_knum}人"
        mTvPersonalCutItemInfoEveryOneCut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_mark_team_cut, 0, 0, 0)
        mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
        mTvPersonalCutItemInfoCut.text = "参与砍价"
        tv_has_cut_money.text = "累计已减${cutInfoBean!!.result.detail.product_minus}元"
        mTvPersonalCutItemInfoCut.setOnClickListener {
            mPresent.getDataByPost(0x2,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_MOREBARGAIN,
                    RequestParamsHelper.getMorebargainParam(cutInfoBean!!.result.detail.product_id))
        }
    }

    private fun <T : Any?> onTeamCutAction(json: JSONObject?, result: T) {
        if (json != null) {
            UserInfo.getInstance().memberIntegral = json.optString("arr1")
            toast(json.optString("msg"))
            refreshTeamDataToView(json)
        } else {
            toast(JSONObject(result.toString()).optString("msg"))
            mTvPersonalCutItemInfoCut.isEnabled = false //已经砍过一次价了 或者已经到最低价了 都不能再砍了
        }
    }

    private fun refreshTeamDataToView(json: JSONObject) {
        cutInfoBean!!.result.detail.product_knum = json.optJSONObject("arr").optString("product_knum")
        cutInfoBean!!.result.detail.product_price = json.optJSONObject("arr").optString("product_price")
        cutInfoBean!!.result.detail.product_minus = json.optJSONObject("arr").optString("product_minus")
        cutInfoBean!!.result.nextprice = json.optJSONObject("result").optString("nextprice")
        cutInfoBean!!.result.kprice = json.optJSONObject("result").optString("kprice")
        cutInfoBean!!.result.resnum = json.optJSONObject("result").optString("resnum")
        mTvPersonalCutItemInfoEveryOneCut.text = "距离${cutInfoBean!!.result.nextprice}元还差${cutInfoBean!!.result.resnum}人"
        mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${cutInfoBean!!.result.detail.product_knum}人"
        mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
        tv_has_cut_money.text = "累计已减${cutInfoBean!!.result.detail.product_minus}元"
    }


    override fun onDestroyView() {
        if (shareBitmapPic != null) {
            if (!shareBitmapPic!!.isRecycled) {
                shareBitmapPic!!.recycle()
                shareBitmapPic = null
            }
        }
        mTvPersonalCutItemInfoDownTime.stop()
        super.onDestroyView()
    }
}