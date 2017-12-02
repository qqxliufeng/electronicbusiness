package com.android.ql.lf.electronicbusiness.ui.fragments;

import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.lists.ListParseHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17 0017.
 *
 * @author lf
 */

public abstract class BaseRecyclerViewFragment<T> extends BaseNetWorkingFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_srl_base_recycler_view)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.id_rv_base_recycler_view)
    public RecyclerView mRecyclerView;

    protected BaseQuickAdapter<T, BaseViewHolder> mBaseAdapter;
    protected ArrayList<T> mArrayList = new ArrayList<>();
    protected RecyclerView.LayoutManager mManager;

    protected int currentPage = 0;

    protected boolean isFirstRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_recycler_view_layout;
    }

    protected int getErrorLayoutId() {
        return R.layout.layout_list_error_layout;
    }

    protected int getEmptyLayoutId() {
        return R.layout.layout_list_empty_layout;
    }

    @Override
    @CallSuper
    protected void initView(View view) {
        mBaseAdapter = createAdapter();
        if (mBaseAdapter == null) {
            return;
        }
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, android.R.color.holo_blue_dark),
                ContextCompat.getColor(mContext, android.R.color.holo_green_dark),
                ContextCompat.getColor(mContext, android.R.color.holo_orange_dark));
        mBaseAdapter.openLoadAnimation();
        mBaseAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(getItemDecoration());
        if (isFirstRefresh) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            });
        }
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    onMyItemClick(adapter, view, position);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    onMyItemChildClick(adapter, view, position);
                }
            }
        });
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mManager = linearLayoutManager;
        return linearLayoutManager;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_divider));
        return decoration;
    }

    public void setDividerDecoration() {
        DividerItemDecoration decor = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        decor.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_divider));
        mRecyclerView.addItemDecoration(decor);
    }

    public void onMyItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    public void onMyItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> createAdapter();

    public void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void setLoadEnable(boolean enable) {
        mBaseAdapter.setEnableLoadMore(enable);
    }

    @Override
    public void onLoadMoreRequested() {
        onLoadMore();
    }

    @CallSuper
    protected void onLoadMore() {
        currentPage++;
    }

    @Override
    public void onRequestEnd(int requestID) {
        super.onRequestEnd(requestID);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRequestFail(int requestID, @NotNull Throwable e) {
        super.onRequestFail(requestID, e);
        mBaseAdapter.setEmptyView(getEmptyLayoutId());
    }

    @CallSuper
    @Override
    public void onRefresh() {
        currentPage = 0;
        mArrayList.clear();
        mBaseAdapter.setNewData(mArrayList);
    }

    public void onPostRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void processList(JSONObject json, Class<T> clazz) {
        try {
            if (json != null) {
                ArrayList<T> tempList = new ListParseHelper<T>().fromJson(json.toString(), clazz);
                if (tempList != null && !tempList.isEmpty()) {
                    mArrayList.addAll(tempList);
                    mBaseAdapter.loadMoreComplete();
                    mBaseAdapter.disableLoadMoreIfNotFullPage();
                } else {
                    if (currentPage == 0) {
                        mBaseAdapter.setEmptyView(getEmptyLayoutId());
                    } else {
                        mBaseAdapter.loadMoreEnd();
                    }
                }
                mBaseAdapter.notifyDataSetChanged();
            } else {
                if (currentPage == 0) {
                    setEmptyView();
                } else {
                    mBaseAdapter.loadMoreEnd();
                    mBaseAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            setEmptyView();
        }
    }

    private void setEmptyView() {
        mBaseAdapter.setEmptyView(getEmptyLayoutId());
        mBaseAdapter.notifyDataSetChanged();
    }

}
