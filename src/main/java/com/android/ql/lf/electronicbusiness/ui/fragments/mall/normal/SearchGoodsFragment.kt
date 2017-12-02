package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.adapters.SearchGoodsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.electronicbusiness.utils.DividerGridItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import android.view.View
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_search_goods_layout.*


/**
 * Created by lf on 2017/11/14 0014.
 * @author lf on 2017/11/14 0014
 */
class SearchGoodsFragment : BaseRecyclerViewFragment<String>() {

    enum class PRICE {
        PRICE_UP_TO_DOWN, PRICE_DOWN_TO_UP
    }

    enum class SELL {
        SELL_UP_TO_DOWN, SELL_DOWN_TO_UP
    }

    private var sort: String = ""
    private var price: PRICE = PRICE.PRICE_UP_TO_DOWN
    private var sell: SELL = SELL.SELL_UP_TO_DOWN

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_search_goods_layout

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
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
        mIvSearchGoodsBack.setOnClickListener {
            finish()
        }
        mLlSearchGoodsZHContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = true
            mCTvSearchGoodsPrice.isChecked = false
            mCTvSearchGoodsSell.isChecked = false
        }
        mLlSearchGoodsPriceContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = false
            mCTvSearchGoodsPrice.isChecked = true
            mCTvSearchGoodsSell.isChecked = false
            if (price == PRICE.PRICE_UP_TO_DOWN) {
                price = PRICE.PRICE_DOWN_TO_UP
                mCTvSearchGoodsPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n, 0)
            } else {
                price = PRICE.PRICE_UP_TO_DOWN
                mCTvSearchGoodsPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n2, 0)
            }
        }
        mLlSearchGoodsSellContainer.setOnClickListener {
            mCTvSearchGoodsZH.isChecked = false
            mCTvSearchGoodsPrice.isChecked = false
            mCTvSearchGoodsSell.isChecked = true
            if (sell == SELL.SELL_UP_TO_DOWN) {
                sell = SELL.SELL_DOWN_TO_UP
                mCTvSearchGoodsSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n, 0)
            } else {
                sell = SELL.SELL_UP_TO_DOWN
                mCTvSearchGoodsSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_icon_price_n2, 0)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT,
                RequestParamsHelper.getProductParams("3", sort, currentPage))
    }

}