package com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.GoodsItemBean;
import com.android.ql.lf.electronicbusiness.data.UserInfo;
import com.android.ql.lf.electronicbusiness.present.OrderPresent;
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseRecyclerViewFragment;
import com.android.ql.lf.electronicbusiness.ui.adapters.SearchAdapter;
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.LoginFragment;
import com.android.ql.lf.electronicbusiness.ui.views.EditTextWithDel;
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper;
import com.android.ql.lf.electronicbusiness.utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.functions.Action1;

/**
 * @author liufeng
 * @date 2017/12/11
 */

public class SearchFragment extends BaseRecyclerViewFragment<GoodsItemBean> {

    public static final String K_TYPE_FLAG = "k_type_flag";

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.mEtSearchContent)
    EditText et_content;

    private String keyword = "";
    private String kType = "";


    private Object currentFlag = this.hashCode() + this.toString();

    private GoodsItemBean selectGoodsItem = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_layout;
    }

    @Override
    protected BaseQuickAdapter<GoodsItemBean, BaseViewHolder> createAdapter() {
        return new SearchAdapter(R.layout.adapter_search_item_layout, mArrayList);
    }

    @Override
    protected void initView(View view) {
        isFirstRefresh = false;
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance().getClass()).subscribe(new Action1<UserInfo>() {
            @Override
            public void call(UserInfo userInfo) {
                if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().getLoginTag().equals(currentFlag)) {
                    enterGoodsInfo();
                }
            }
        });
        super.initView(view);
        kType = getArguments().getString(K_TYPE_FLAG, "");
        setRefreshEnable(false);
        setEmpty();
        et_content.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_content, 0);
            }
        }, 100);
    }

    private void setEmpty() {
        mBaseAdapter.setEmptyView(R.layout.search_empty_view_layout);
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (!TextUtils.isEmpty(keyword)) {
            super.onRefresh();
            mPresent.getDataByPost(0x0, RequestParamsHelper.Companion.getPRODUCT_MODEL(), RequestParamsHelper.Companion.getACT_PRODUCT_SEARCH(),
                    RequestParamsHelper.Companion.getProductSearchParam(keyword, kType, currentPage, 10)
            );
        }
    }

    @Override
    protected void onLoadMore() {
        super.onLoadMore();
        setLoadEnable(false);
    }

    @OnClick({R.id.mIvSearchGoodsBack, R.id.mTvSearchSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mIvSearchGoodsBack:
                finish();
                break;
            case R.id.mTvSearchSubmit:
                keyword = et_content.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    ((SearchAdapter) mBaseAdapter).setSearchContent(keyword);
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                    onPostRefresh();
                } else {
                    mArrayList.clear();
                    setEmpty();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
        super.onRequestSuccess(requestID, result);
        processList(checkResultCode(result), GoodsItemBean.class);
    }

    @Override
    public void onMyItemClick(BaseQuickAdapter adapter, View view, int position) {
        super.onMyItemClick(adapter, view, position);
        selectGoodsItem = mArrayList.get(position);
        if (UserInfo.getInstance().isLogin()) {
            enterGoodsInfo();
        } else {
            UserInfo.getInstance().setLoginTag(currentFlag);
            LoginFragment.Companion.startLogin(mContext);
        }
    }

    public void enterGoodsInfo() {
        Bundle bundle = new Bundle();
        bundle.putString(VipPrivilegeItemInfoFragment.Companion.getGOODS_ID_FLAG(), selectGoodsItem.getProduct_id());
        if (OrderPresent.GoodsType.VIP_GOODS.equals(kType)) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, VipPrivilegeItemInfoFragment.class);
        } else {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品详情", true, false, bundle, CutGoodsInfoFragment.class);
        }
        finish();
    }

}
