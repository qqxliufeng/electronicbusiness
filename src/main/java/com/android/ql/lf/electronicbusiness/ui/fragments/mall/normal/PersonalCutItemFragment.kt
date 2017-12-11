package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.PersonalCutItemAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import rx.Subscription

/**
 * Created by lf on 2017/11/11 0011.
 * @author lf on 2017/11/11 0011
 */
class PersonalCutItemFragment : AbstractLazyLoadFragment<GoodsItemBean>() {

    companion object {
        fun newInstance(cid: String): PersonalCutItemFragment {
            val bundle = Bundle()
            bundle.putString("cid", cid)
            val personalCutFragment = PersonalCutItemFragment()
            personalCutFragment.arguments = bundle
            return personalCutFragment
        }
    }

    private lateinit var currentItem: GoodsItemBean

    private lateinit var subscription: Subscription

    private val currentLoginFlag by lazy {
        "${this@PersonalCutItemFragment.hashCode()}${this@PersonalCutItemFragment}"
    }


    override fun createAdapter(): BaseQuickAdapter<GoodsItemBean, BaseViewHolder> =
            PersonalCutItemAdapter(R.layout.adapter_personal_cut_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            when (UserInfo.getInstance().loginTag) {
                currentLoginFlag -> {
                    enterGoodsInfo()
                }
            }
        }
    }

    override fun loadData() {
        if (TextUtils.isEmpty(arguments.getString("cid"))) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT,
                    RequestParamsHelper.getProductParams("1", "", currentPage))
        } else {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.PRODUCT_MODEL,
                    RequestParamsHelper.ACT_PRODUCT_TYPE_SEARCH,
                    RequestParamsHelper.getProductTypeSearchParams(arguments.getString("cid"), "", "1", "",currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        isLoad = true
        processList(checkResultCode(result), GoodsItemBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentItem = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            enterGoodsInfo()
        } else {
            UserInfo.getInstance().loginTag = currentLoginFlag
            LoginFragment.startLogin(mContext)
        }
    }

    private fun enterGoodsInfo() {
        val bundle = Bundle()
        bundle.putString(CutGoodsInfoFragment.GOODS_ID_FLAG, currentItem.product_id)
        FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, CutGoodsInfoFragment::class.java)
    }

    override fun onDestroyView() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroyView()
    }

}