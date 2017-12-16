package com.android.ql.lf.electronicbusiness.interfaces;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

/**
 * Created by lf on 2017/12/14 0014.
 *
 * @author lf on 2017/12/14 0014
 */

public class MyWbAuthListener implements WbAuthListener{
    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {

    }

    @Override
    public void cancel() {
    }

    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {

    }
}
