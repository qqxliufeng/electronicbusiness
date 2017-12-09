package com.android.ql.lf.electronicbusiness.ui.fragments.im;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 作者：Rance on 2016/11/18 15:19
 * 邮箱：rance935@163.com
 */
public class BaseFragment extends Fragment {
    public Context mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = context;
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public ProgressDialog progressDialog;

    public ProgressDialog showProgressDialog() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }
}
