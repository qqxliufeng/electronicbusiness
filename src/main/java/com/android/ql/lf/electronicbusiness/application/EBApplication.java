package com.android.ql.lf.electronicbusiness.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.android.ql.lf.electronicbusiness.component.AppComponent;
import com.android.ql.lf.electronicbusiness.component.AppModule;
import com.android.ql.lf.electronicbusiness.component.DaggerAppComponent;
import com.android.ql.lf.electronicbusiness.utils.Constants;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * @author lf
 * @date 2017/10/16 0016
 */

public class EBApplication extends MultiDexApplication {

    private AppComponent appComponent;

    private static EBApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "d83d97f053", true);
        application = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        if (!ChatClient.getInstance().init(this, new ChatClient.Options().setAppkey(Constants.HX_APP_ID).setTenantId(Constants.HX_TENANT_ID))) {
            return;
        }
        UIProvider.getInstance().init(this);
        WbSdk.install(this, new AuthInfo(this, Constants.WB_APP_ID, Constants.REDIRECT_URL, Constants.SCOPE));
    }


    public static EBApplication getInstance() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
