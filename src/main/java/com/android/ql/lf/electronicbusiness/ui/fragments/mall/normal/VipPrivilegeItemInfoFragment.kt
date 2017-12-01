package com.a

import android.graphics.Color
import android.webkit.WebViewClient

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.webkit.WebView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiao.nicevideoplayer.NiceVideoPlayer
import kotlinx.android.synthetic.main.vip_privilege_item_info_layout.*
import com.xiao.nicevideoplayer.TxVideoPlayerController
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import org.jetbrains.anko.backgroundColor


/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class VipPrivilegeItemInfoFragment : BaseNetWorkingFragment() {

    private val list = arrayListOf("", "")

    override fun getLayoutId() = R.layout.vip_privilege_item_info_layout

    override fun initView(view: View?) {
        val controller = TxVideoPlayerController(mContext)
        controller.setTitle("")
        controller.setImage(R.drawable.img_icon_test_pic_05)
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK)
        mNiceVideoPlayer.setController(controller)
        mNiceVideoPlayer.backgroundColor = Color.WHITE
        mNiceVideoPlayer.setUp("http://baobab.wdjcdn.com/14564977406580.mp4", null)

        val adapter = VipPrivilegeItemGoodsInfoAdapter(R.layout.adapter_vip_privilege_item_goods_info_item_layout, list)
        mRvVipPrivilegeItemGoodsInfo.layoutManager = LinearLayoutManager(mContext)
        mRvVipPrivilegeItemGoodsInfo.adapter = adapter

        val topView = View.inflate(mContext, R.layout.layout_vip_privilege_item_goods_info_top_layout, null)
        adapter.addHeaderView(topView)
        val bottomView = View.inflate(mContext, R.layout.layout_vip_privilege_item_goods_info_bottom_layout, null)
//        bottomView.settings.javaScriptEnabled = true
//        bottomView.loadUrl("http://www.baidu.com")
//        bottomView.webViewClient = object : WebViewClient() {
//
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                super.shouldOverrideUrlLoading(view, url)
//                view.loadUrl(url)
//                return true
//            }
//        }
        adapter.addFooterView(bottomView)
    }

    override fun onStop() {
        super.onStop()
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
    }

    class VipPrivilegeItemGoodsInfoAdapter(layoutId: Int, list: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId, list) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }
    }


}