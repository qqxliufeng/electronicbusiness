package com.android.ql.lf.electronicbusiness.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.ui.fragments.mall.normal.PayResultFragment;
import com.android.ql.lf.electronicbusiness.utils.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;

/**
 * Created by lf on 2017/12/5 0005.
 *
 * @author lf on 2017/12/5 0005
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_result_layout);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
        Toolbar mToolbar = findViewById(R.id.id_tl_activity_fragment_container);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mFlWxPayResultContainer, PayResultFragment.Companion.newInstance(baseResp.errCode))
                    .commit();
        }
    }
}
