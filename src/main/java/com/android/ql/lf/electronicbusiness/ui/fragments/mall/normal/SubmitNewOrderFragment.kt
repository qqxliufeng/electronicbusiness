package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.data.OrderBean
import com.android.ql.lf.electronicbusiness.data.ShoppingCarItemBean
import com.android.ql.lf.electronicbusiness.data.WXPayBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.SubmitOrderInfoAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.AddressSelectFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.ui.views.PopupWindowDialog
import com.android.ql.lf.electronicbusiness.utils.PayManager
import com.android.ql.lf.electronicbusiness.utils.PayResult
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_submit_order_layout.*
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import java.text.DecimalFormat

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class SubmitNewOrderFragment : BaseRecyclerViewFragment<ShoppingCarItemBean>() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
    }

    private var addressBean: AddressBean? = null
    private lateinit var subscription: Subscription
    private var tempList: ArrayList<ShoppingCarItemBean>? = null

    private lateinit var tv_address_name: TextView
    private lateinit var tv_address_phone: TextView
    private lateinit var tv_address_detail: TextView
    private lateinit var bt_empty_address: Button
    private lateinit var ll_address_container: LinearLayout

    private lateinit var rl_wx_container: RelativeLayout
    private lateinit var rl_ali_container: RelativeLayout
    private lateinit var cb_wx: CheckBox
    private lateinit var cb_ali: CheckBox

    private val orderList = arrayListOf<OrderBean>()

    private val contentView: View by lazy {
        View.inflate(mContext, R.layout.dialog_bbs_layout, null)
    }

    override fun createAdapter(): BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder> =
            SubmitOrderInfoAdapter(R.layout.adapter_submit_order_info_item_layout, mArrayList)

    val handle = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                PayManager.SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg!!.obj as Map<String, String>)
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    if (TextUtils.equals(resultStatus, "9000")) {
                        toast("支付成功")
                    } else {
                        toast("支付失败")
                    }
                }
            }
        }
    }


    override fun getLayoutId() = R.layout.fragment_submit_order_layout

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return decoration
    }

    override fun onRefresh() {
        super.onRefresh()
        setRefreshEnable(false)
        setLoadEnable(false)
        if (tempList != null && !tempList!!.isEmpty()) {
            var money = 0.00f
            var num = 0
            tempList!!.forEach {
                money += ((it.shopcart_price.toFloat() * it.shopcart_num.toInt()) + it.shopcart_mdprice.toFloat())
                num += it.shopcart_num.toInt()
            }
            mTvSubmitOrderGoodsCount.text = Html.fromHtml("共<span style='color:#78BFFF'> $num </span>件")
            mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(money)
            mArrayList.addAll(tempList!!)
            mBaseAdapter.notifyDataSetChanged()
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_DEFAULT_ADDRESS, RequestParamsHelper.getDefaultAddress())
    }

    override fun initView(view: View?) {
        arguments.classLoader = this.javaClass.classLoader
        tempList = arguments.getParcelableArrayList(GOODS_ID_FLAG)

        val headerView = View.inflate(mContext, R.layout.layout_submit_new_order_header_layout, null)
        tv_address_name = headerView.findViewById(R.id.mIvSubmitOrderAddressName)
        tv_address_phone = headerView.findViewById(R.id.mIvSubmitOrderAddressPhone)
        tv_address_detail = headerView.findViewById(R.id.mIvSubmitOrderAddressDetail)
        bt_empty_address = headerView.findViewById(R.id.mBtSubmitOrderHeaderNoAddress)
        ll_address_container = headerView.findViewById(R.id.mLlSubmitOrderAddress)

        val bottomView = View.inflate(mContext, R.layout.layout_submit_new_order_bottom_layout, null)
        rl_wx_container = bottomView.findViewById(R.id.mRlSubmitOrderWXContainer)
        rl_ali_container = bottomView.findViewById(R.id.mRlSubmitOrderAliPayContainer)
        cb_ali = bottomView.findViewById(R.id.mCbALiPay)
        cb_wx = bottomView.findViewById(R.id.mCbWX)

        cb_ali.setOnCheckedChangeListener { _, isChecked ->
            cb_wx.isChecked = !isChecked
        }
        cb_wx.setOnCheckedChangeListener { _, isChecked ->
            cb_ali.isChecked = !isChecked
        }
        rl_wx_container.setOnClickListener {
            cb_wx.isChecked = true
        }
        rl_ali_container.setOnClickListener {
            cb_ali.isChecked = true
        }
        mTvSubmitOrder.setOnClickListener {
            mArrayList.forEach {
                val orderBean = OrderBean()
                orderBean.address = addressBean!!.address_id
                orderBean.gid = it.shopcart_id
                orderBean.ktype = it.shopcart_ktype
                orderBean.mliuyan = it.bbs
                orderBean.num = it.shopcart_num
                orderBean.specification = it.shopcart_specification
                orderBean.price = it.shopcart_price
                orderBean.mdprice = it.shopcart_mdprice
                orderBean.bbs = it.bbs
                orderList.add(orderBean)
            }
            val json = Gson().toJson(orderList)
            mPresent.getDataByPost(0x1, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ADD_ORDER,
                    RequestParamsHelper.getAddOrderParams(if (cb_wx.isChecked) {
                        "wxpay"
                    } else {
                        "alipay"
                    }, json))
        }
        ll_address_container.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
        }
        bt_empty_address.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "选择地址", true, false, AddressSelectFragment::class.java)
        }
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(AddressBean::class.java).subscribe {
            addressBean = it
            if (addressBean != null) {
                setAddress()
            }
        }
        mBaseAdapter.addHeaderView(headerView)
        mBaseAdapter.addFooterView(bottomView)
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (requestID == 0x0) {
            if (json != null) {
                addressBean = Gson().fromJson(json.optJSONObject("result").toString(), AddressBean::class.java)
                if (addressBean != null) {
                    setAddress()
                } else {
                    showEmptyAddress()
                }
            } else {
                showEmptyAddress()
            }
        } else if (requestID == 0x1) {
            if (json != null) {
                if (cb_wx.isChecked) {
                    val wxBean = Gson().fromJson(json.optJSONObject("result").toString(), WXPayBean::class.java)
                    PayManager.wxPay(mContext, wxBean)
                } else {
                    PayManager.aliPay(mContext, handle, json.optString("result"))
                }
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(context)
        progressDialog.show()
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            showEmptyAddress()
        }
    }

    private fun showEmptyAddress() {
        ll_address_container.visibility = View.GONE
        bt_empty_address.visibility = View.VISIBLE
    }

    private fun setAddress() {
        ll_address_container.visibility = View.VISIBLE
        bt_empty_address.visibility = View.GONE
        tv_address_name.text = "收货人：${addressBean!!.address_name}"
        tv_address_phone.text = addressBean!!.address_phone
        tv_address_detail.text = "${addressBean!!.address_addres}  ${addressBean!!.address_detail}"
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val currentItem = mArrayList[position]
        when (view!!.id) {
            R.id.mRlSubmitOrderGoodsBBS -> {
                val popupDialog = PopupWindowDialog.showReplyDialog(mContext, contentView)
                val et_content = contentView.findViewById<EditText>(R.id.mEtDialogBbsContent)
                val tv_finish = contentView.findViewById<TextView>(R.id.mTvDialogBbsFinish)
                et_content.setText("")
                tv_finish.setOnClickListener {
                    if (TextUtils.isEmpty(et_content.text.toString())) {
                        toast("请输入留言内容")
                        return@setOnClickListener
                    }
                    currentItem.bbs = et_content.text.toString().trim()
                    mBaseAdapter.notifyItemChanged(position + 1)
                    popupDialog.dismiss()
                }
            }
        }
    }


    override fun onDestroyView() {
        handle.removeMessages(PayManager.SDK_PAY_FLAG)
        super.onDestroyView()
    }
}

