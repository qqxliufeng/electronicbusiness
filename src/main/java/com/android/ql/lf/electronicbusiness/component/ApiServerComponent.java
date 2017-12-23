package com.android.ql.lf.electronicbusiness.component;

import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.electronicbusiness.ui.activities.SelectAddressActivity;
import com.android.ql.lf.electronicbusiness.ui.activities.SplashActivity;
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment;

import dagger.Component;

/**
 * @author Administrator
 * @date 2017/10/16 0016
 */

@ApiServerScope
@Component(modules = {ApiServerModule.class}, dependencies = AppComponent.class)
public interface ApiServerComponent {

    void inject(FragmentContainerActivity activity);

    void inject(SelectAddressActivity selectAddressActivity);

    void inject(BaseNetWorkingFragment baseNetWorkingFragment);

    void inject(SplashActivity splashActivity);

}
