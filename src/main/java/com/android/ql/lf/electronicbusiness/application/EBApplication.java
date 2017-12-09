package com.android.ql.lf.electronicbusiness.application;

import android.support.multidex.MultiDexApplication;

import com.android.ql.lf.electronicbusiness.component.AppComponent;
import com.android.ql.lf.electronicbusiness.component.AppModule;
import com.android.ql.lf.electronicbusiness.component.DaggerAppComponent;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.easeui.UIProvider;

/**
 * @author lf
 * @date 2017/10/16 0016
 */

public class EBApplication extends MultiDexApplication {

    private AppComponent appComponent;

    private static EBApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        if (!ChatClient.getInstance().init(this, new ChatClient.Options().setAppkey("1402171117061450#kefuchannelapp49918").setTenantId("49918"))) {
            return;
        }
        UIProvider.getInstance().init(this);
    }


    public static EBApplication getInstance() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
