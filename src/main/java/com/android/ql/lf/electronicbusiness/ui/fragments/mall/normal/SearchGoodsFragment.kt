package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.adapters.SearchGoodsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.DividerGridItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import android.view.View
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import kotlinx.android.synthetic.main.fragment_search_goods_layout.*
import org.jetbrains.anko.bundleOf
import rx.Subscription


/**
 * Created by lf on 2017/11/14 0014.
 * @author lf on 2017/11/14 0014
 */
class SearchGoodsFragment : BaseRecyclerViewFragment<GoodsItemBean>() {

    companion object {
        val K_TYPE_FLAG = "k_type_flag"
        val TYPE_ID_FLAG = "type_id"
        val STYPE_ID_FLAG = "stype_id"
    }

    enum class PRICE {
        PRICE_UP_TO_DOWN, PRICE_DOWN_TO_UP
    }

    enum class SELL {
        SELL_UP_TO_DOWN, SELL_DOWN_TO_UP
    }

    private var sort: String = ""
    private var price: PRICE = PRICE.PRICE_UP_TO_DOWN
    private var sell: SELL = SELL.SELL_UP_TO_DOWN

    private lateinit var currentItem: GoodsItemBean

    private val currentKType by lazy {
        arguments.getString(K_TYPE_FLAG)
    }

    private val currentLoginFlag by lazy { "${this@SearchGoodsFragment.hashCode()}${this@SearchGoodsFragment}" }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_search_goods_layout

    override fun createAdapter(): BaseQuickAdapter<GoodsItemBean, BaseViewHolder> =
            SearchGoodsAdapter(R.layout.adapter_search_goods_item_layout, mArrayList)

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val manager = GridLayoutManager(mContext, 2)
        mManager = manager
        return manager
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
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
        mIvSearchGoodsBack.setOnClickListener {
            finish()
        }
        mLlSearchGoodsZHContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = true
            mCTvSearchGoodsPrice.isChecked = false
            mCTvSearchGoodsSell.isChecked = false
            sort = ""
            onPostRefresh()
        }
        mLlSearchGoodsPriceContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = false
            mCTvSearchGoodsPrice.isChecked = true
            mCTvSearchGoodsSell.isChecked = false
            if (price == PRICE.PRICE_UP_TO_DOWN) {
                price = PRICE.PRICE_DOWN_TO_UP
                mCTvSearchGoodsPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n, 0)
                sort = "p1"
            } else {
                price = PRICE.PRICE_UP_TO_DOWN
                mCTvSearchGoodsPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n2, 0)
                sort = "p2"
            }
            onPostRefresh()
        }
        mLlSearchGoodsSellContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = false
            mCTvSearchGoodsPrice.isChecked = false
            mCTvSearchGoodsSell.isChecked = true
            if (sell == SELL.SELL_UP_TO_DOWN) {
                sell = SELL.SELL_DOWN_TO_UP
                mCTvSearchGoodsSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n, 0)
                sort = "sv1"
            } else {
                sell = SELL.SELL_UP_TO_DOWN
                mCTvSearchGoodsSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n2, 0)
                sort = "sv2"
            }
            onPostRefresh()
        }
        mEtSearchGoodsContent.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "搜索", true, true,
                    bundleOf(Pair(SearchFragment.K_TYPE_FLAG,currentKType)), SearchFragment::class.java)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT_TYPE_SEARCH,
                RequestParamsHelper.getProductTypeSearchParams(
                        arguments.getString(TYPE_ID_FLAG),
                        arguments.getString(STYPE_ID_FLAG),
                        currentKType,
                        sort,
                        currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(checkResultCode(result), GoodsItemBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
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
        if ("3" == currentKType) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundleOf(Pair(VipPrivilegeItemInfoFragment.GOODS_ID_FLAG, currentItem.product_id)), VipPrivilegeItemInfoFragment::class.java)
        } else {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundleOf(Pair(CutGoodsInfoFragment.GOODS_ID_FLAG, currentItem.product_id)), CutGoodsInfoFragment::class.java)
        }
    }
}