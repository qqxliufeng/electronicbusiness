package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextPaint
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.CommentForGoodsBean
import com.android.ql.lf.electronicbusiness.data.PersonalCutGoodsItemBean
import com.android.ql.lf.electronicbusiness.data.CutGoodsInfoBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.GoodsInfoCommentAdapter
import com.android.ql.lf.electronicbusiness.ui.adapters.RecommedGoodsInfoAdatper
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.BottomGoodsParamDialog
import com.android.ql.lf.electronicbusiness.ui.views.HtmlTextView
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.GlideImageLoader
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_personal_cut_item_info_layout.*
import org.jetbrains.anko.support.v4.toast


/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
@Deprecated("过期了，请使用 CutGoodsInfoFragment")
class PersonalCutItemInfoFragment : BaseNetWorkingFragment() {

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


    private var personalCutInfoBean: CutGoodsInfoBean? = null

    private var bottomParamDialog: BottomGoodsParamDialog? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_cut_item_info_layout

    override fun initView(view: View?) {
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
            if (personalCutInfoBean != null) {
                showBottomParamDialog()
            }
        }
        mTvPersonalCutItemInfoCollection.setOnClickListener {
            if (personalCutInfoBean != null) {
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
                    personalCutInfoBean!!.result.detail.product_price,
                    personalCutInfoBean!!.result.detail.product_entrepot,
                    personalCutInfoBean!!.result.detail.product_name,
                    if (!personalCutInfoBean!!.result.detail.product_pic.isEmpty()) {
                        personalCutInfoBean!!.result.detail.product_pic[0]
                    } else {
                        ""
                    },
                    personalCutInfoBean!!.result.detail.product_specification)
        }
        bottomParamDialog!!.show()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT_DETAIL, RequestParamsHelper.getProductDetailParam(arguments.getString(GOODS_ID_FLAG, "")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext)
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            personalCutInfoBean = Gson().fromJson(json.toString(), CutGoodsInfoBean::class.java)
            setCommentList()
            mTvPersonalCutItemInfoEveryOneCut.text = ("每个人砍价${personalCutInfoBean!!.result.kprice}元")
            mTvPersonalCutItemInfoBuy.text = "￥${personalCutInfoBean!!.result.detail.product_price}\n立即购买"
            mTvPersonalCutItemInfoCut.text = "- ￥${personalCutInfoBean!!.result.kprice}\n砍价"
            val endTime = personalCutInfoBean!!.result.endtime
            if ("0" == endTime) {
                mLlPersonalCutItemInfoContainer.setBackgroundResource(R.drawable.img_icon_mark_team_cut_bg)
                mTvPersonalCutItemInfoDownTimeTitle.text = "活动已经结束"
                mTvPersonalCutItemInfoBuy.isEnabled = false
                mTvPersonalCutItemInfoCollection.isEnabled = false
                mTvPersonalCutItemInfoCut.isEnabled = false
                mTvPersonalCutItemInfoDownTime.setTime(0)
                mTvPersonalCutItemInfoDownTime.stop()
            }

            setRecommendList()

            mTvPersonalCutItemInfoHasCutNum.text = "已参砍人数${personalCutInfoBean!!.result.detail.product_knum}人"
            mTvPersonalCutItemInfoPrice.text = "￥ ${personalCutInfoBean!!.result.detail.product_price}"
            mTvPersonalCutItemInfoOldPrice.text = "￥ ${personalCutInfoBean!!.result.detail.product_yprice}"
            mTvPersonalCutItemInfoReleaseCount.text = personalCutInfoBean!!.result.detail.product_entrepot
            tv_has_cut_money.text = "累计已减${personalCutInfoBean!!.result.detail.product_minus}元"
            tv_goods_name.text = personalCutInfoBean!!.result.detail.product_name
            tv_goods_desc.text = Html.fromHtml(personalCutInfoBean!!.result.detail.product_ms)

            setBanner()

            htv_content_info.setHtmlFromString(personalCutInfoBean!!.result.detail.product_content, false)
            adapter.notifyDataSetChanged()
            mRvPersonalCutItemInfo.smoothScrollToPosition(0)
        }
    }

    private fun setRecommendList() {
        recommendList.addAll(personalCutInfoBean!!.result.kind)
        recommendAdapter.notifyDataSetChanged()
    }

    private fun setCommentList() {
        tv_comment_count.text = "商品评价(${personalCutInfoBean!!.arr.count})"
        commentList.addAll(personalCutInfoBean!!.arr.list)
    }


    /**
     * 设置Banner
     */
    private fun setBanner() {
        mCBPersonalCutItemInfo.setImageLoader(GlideImageLoader()).setImages(personalCutInfoBean!!.result.detail.product_pic)
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
        if (shareDialog != null) {
            if (shareDialog!!.isShowing) {
                shareDialog!!.dismiss()
            }
            shareDialog = null
        }
        super.onDestroyView()
    }


}