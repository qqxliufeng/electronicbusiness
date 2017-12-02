package com.android.ql.lf.electronicbusiness.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by lf on 2017/11/30 0030.
 *
 * @author lf on 2017/11/30 0030
 */

public abstract class AbstractLazyLoadFragment<T> extends BaseRecyclerViewFragment<T> {

    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected boolean isLoad = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 懒加载方法
     */
    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
}
