package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment;
import com.android.ql.lf.electronicbusiness.ui.fragments.mine.IntegrationExchangeListFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lf on 2017/12/13 0013.
 *
 * @author lf on 2017/12/13 0013
 */

public class IntegrationPayResultFragment extends BaseFragment {

    public static final String PAY_RESULT_CODE_FLAG = "pay_result_code";

    @BindView(R.id.mTvPayResultTitle)
    TextView tv_title;
    @BindView(R.id.mBtBack)
    Button bt_back;


    //1 失败 0 成功
    private int code = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integration_pay_result_layout;
    }

    @Override
    protected void initView(View view) {
        code = getArguments().getInt(PAY_RESULT_CODE_FLAG);
        if (code == 0) {
            bt_back.setText("查看兑换记录");
            tv_title.setText("兑换成功");
        } else {
            bt_back.setText("立即返回");
            tv_title.setText("兑换失败");
        }
    }

    @OnClick(R.id.mBtBack)
    public void onClick() {
        if (code == 0) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "兑换记录", true, false, IntegrationExchangeListFragment.class);
        }
        finish();
    }

}
