package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
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
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.android.ql.lf.electronicbusiness.data.CutGoodsInfoBean
import com.android.ql.lf.electronicbusiness.data.PersonalCutGoodsItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.adapters.RecommedGoodsInfoAdatper
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.BottomGoodsParamDialog
import com.android.ql.lf.electronicbusiness.ui.views.EasyCountDownTextureView
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideImageLoader
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_personal_cut_item_info_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class CutGoodsInfoFragment : BaseNetWorkingFragment(), SwipeRefreshLayout.OnRefreshListener {


    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var shareDialog: BottomSheetDialog? = null
    private val commentList = arrayListOf<CommentForGoodsBean>()
    private lateinit var adapter: GoodsInfoCommentAdapter

    private val recommendList = arrayListOf<PersonalCutGoodsItemBean>()
    private lateinit var recommendAdapter: BaseQuickAdapter<PersonalCutGoodsItemBean, BaseViewHolder>


    private lateinit var tv_has_cut_money: TextView
    private lateinit var tv_goods_name: TextView
    private lateinit var tv_goods_desc: TextView

    private lateinit var mRvRecommend: RecyclerView

    private lateinit var tv_comment_count: TextView
    private lateinit var htv_content_info: HtmlTextView

    private var cutInfoBean: CutGoodsInfoBean? = null

    private var bottomParamDialog: BottomGoodsParamDialog? = null

    //1 代表个人砍  2代表团体砍
    private var currentMode = 1

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_item_info_layout

    override fun initView(view: View?) {

//        mSrfCutItemInfo.setColorSchemeColors(ContextCompat.getColor(mContext, android.R.color.holo_blue_dark),
//                ContextCompat.getColor(mContext, android.R.color.holo_green_dark),
//                ContextCompat.getColor(mContext, android.R.color.holo_orange_dark))
//        mSrfCutItemInfo.setOnRefreshListener(this)
//
//        mAlCutItemInfo.addOnOffsetChangedListener { _, verticalOffset ->
//            mSrfCutItemInfo.isEnabled = verticalOffset >= 0
//        }

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
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "全部评价", true, false, AllCommentFragment::class.java)
        }
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "提交订单", true, false, SubmitOrderFragment::class.java)
        }

        val bottomRecommendView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_recommend_layout, null)
        mRvRecommend = bottomRecommendView.findViewById(R.id.mRvPersonalCutItemGoodsInfoRecommend)
        mRvRecommend.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvRecommend.isNestedScrollingEnabled = false

        recommendAdapter = RecommedGoodsInfoAdatper(R.layout.layout_personal_cut_item_goods_info_bootom_recommend_item_layout, recommendList)
        mRvRecommend.adapter = recommendAdapter
        adapter.addFooterView(bottomRecommendView, LinearLayoutManager.HORIZONTAL)

        val bottomInfoContentView = View.inflate(mContext, R.layout.layout_personal_cut_item_goods_info_bootom_layout, null)
        htv_content_info = bottomInfoContentView.findViewById(R.id.mHTvPersonalCutItemGoodsInfo)
        adapter.addFooterView(bottomInfoContentView)
        mTvPersonalCutItemInfoBuy.setOnClickListener {
            if (cutInfoBean != null) {
                showBottomParamDialog()
            }
        }
        mTvPersonalCutItemInfoCollection.setOnClickListener {
            if (cutInfoBean != null) {
                showBottomParamDialog()
            }
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

    private fun showBottomParamDialog() {
        if (bottomParamDialog == null) {
            bottomParamDialog = BottomGoodsParamDialog(mContext)
            bottomParamDialog!!.bindDataToView(
                    "￥ ${cutInfoBean!!.result.detail.product_price}",
                    "库存：${cutInfoBean!!.result.detail.product_entrepot}",
                    cutInfoBean!!.result.detail.product_name,
                    if (!cutInfoBean!!.result.detail.product_pic.isEmpty()) cutInfoBean!!.result.detail.product_pic[0] else "",
                    cutInfoBean!!.result.detail.product_specification)
        }
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
        if (requestID == 0x0) {
            if (json != null) {
                bindData(json)
            }
        } else if (requestID == 0x1) { //个人砍
            if (json != null) {
                toast(json.optString("msg"))
                refreshDataToView(json)
            }
        } else if (requestID == 0x2) { //团体砍
        }
    }

    private fun refreshDataToView(json: JSONObject?) {
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

    private fun bindData(json: JSONObject) {
        cutInfoBean = Gson().fromJson(json.toString(), CutGoodsInfoBean::class.java)
        currentMode = cutInfoBean!!.result.detail.product_ktype.toInt()
        setCommentList()
        when (currentMode) {
            1 -> { //个人砍
                mTvPersonalCutItemInfoEveryOneCut.text = ("每个人砍价${cutInfoBean!!.result.kprice}元")
                mTvPersonalCutItemInfoEveryOneCut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_mark_personal_cut,0,0,0)
                mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
                mTvPersonalCutItemInfoCut.text = "- ￥${cutInfoBean!!.result.kprice}\n砍价"
                mTvPersonalCutItemInfoCut.setOnClickListener {
                    mPresent.getDataByPost(0x1,
                            RequestParamsHelper.PRODUCT_MODEL,
                            RequestParamsHelper.ACT_ONEBARGAIN,
                            RequestParamsHelper.getOnebargainParam(cutInfoBean!!.result.detail.product_id))
                }
            }
            2 -> { //团体砍
                mTvPersonalCutItemInfoEveryOneCut.text = ("距离${cutInfoBean!!.result.kprice}元还差两人")
                mTvPersonalCutItemInfoEveryOneCut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_mark_team_cut,0,0,0)
                mTvPersonalCutItemInfoBuy.text = "￥${cutInfoBean!!.result.detail.product_price}\n立即购买"
                mTvPersonalCutItemInfoCut.text = "参与砍价"
                mTvPersonalCutItemInfoCut.setOnClickListener {
                    mPresent.getDataByPost(0x2,
                            RequestParamsHelper.PRODUCT_MODEL,
                            RequestParamsHelper.ACT_MOREBARGAIN,
                            RequestParamsHelper.getMorebargainParam(cutInfoBean!!.result.detail.product_id))
                }
            }
        }
        setActivityStatus(cutInfoBean!!.result.detail.product_endstatus)
        setRecommendList()
        mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${cutInfoBean!!.result.detail.product_knum}人"
        mTvPersonalCutItemInfoPrice.text = "￥ ${cutInfoBean!!.result.detail.product_price}"
        mTvPersonalCutItemInfoOldPrice.text = "￥ ${cutInfoBean!!.result.detail.product_yprice}"
        mTvPersonalCutItemInfoReleaseCount.text = cutInfoBean!!.result.detail.product_entrepot
        tv_has_cut_money.text = "累计已减${cutInfoBean!!.result.detail.product_minus}元"
        tv_goods_name.text = cutInfoBean!!.result.detail.product_name
        tv_goods_desc.text = Html.fromHtml(cutInfoBean!!.result.detail.product_ms)

        setBanner()

        htv_content_info.setHtmlFromString(cutInfoBean!!.result.detail.product_content, false)
        adapter.notifyDataSetChanged()
        mRvPersonalCutItemInfo.smoothScrollToPosition(0)
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
            mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_personal_cut_bg)
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
        mTvPersonalCutItemInfoDownTime.stop()
        if (shareDialog != null) {
            if (shareDialog!!.isShowing) {
                shareDialog!!.dismiss()
            }
            shareDialog = null
        }
        super.onDestroyView()
    }
}