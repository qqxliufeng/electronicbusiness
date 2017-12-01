package com.android.ql.lf.electronicbusiness.ui.fragments;

import android.content.Context;

import com.android.ql.lf.electronicbusiness.application.EBApplication;
import com.android.ql.lf.electronicbusiness.component.DaggerApiServerComponent;
import com.android.ql.lf.electronicbusiness.interfaces.INetDataPresenter;
import com.android.ql.lf.electronicbusiness.present.GetDataFromNetPresent;
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Administrator
 * @date 2017/10/17 0017
 */

public abstract class BaseNetWorkingFragment extends BaseFragment implements INetDataPresenter {

    @Inject
    public GetDataFromNetPresent mPresent;

    public MyProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentContainerActivity) {
            if (getParentFragment() == null) {
                GetDataFromNetPresent present = ((FragmentContainerActivity) context).getPresent();
                if (present != null) {
                    this.mPresent = present;
                }
            }else {
                DaggerApiServerComponent.builder().appComponent(EBApplication.getInstance().getAppComponent()).build().inject(this);
            }
        } else {
            DaggerApiServerComponent.builder().appComponent(EBApplication.getInstance().getAppComponent()).build().inject(this);
        }
        if (mPresent != null) {
            this.mPresent.setNetDataPresenter(this);
        }
    }

    @Override
    public void onRequestStart(int requestID) {
    }

    @Override
    public void onRequestFail(int requestID, @NotNull Throwable e) {

    }

    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
    }

    @Override
    public void onRequestEnd(int requestID) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public <T> JSONObject checkResultCode(T json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json.toString());
                if ("200".equals(jsonObject.optString("code"))) {
                    return jsonObject;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresent != null) {
            mPresent.unSubscription();
            mPresent = null;
        }
    }
}
