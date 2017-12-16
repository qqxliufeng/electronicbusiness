package com.android.ql.lf.electronicbusiness.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.application.EBApplication;
import com.android.ql.lf.electronicbusiness.component.ApiServerModule;
import com.android.ql.lf.electronicbusiness.component.DaggerApiServerComponent;
import com.android.ql.lf.electronicbusiness.present.GetDataFromNetPresent;
import com.android.ql.lf.electronicbusiness.utils.RxBus;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17 0017.
 *
 * @author lf
 */

public class FragmentContainerActivity extends BaseActivity {

    public static final int IMAGE_PICKER = 1;
    public static final String EXTRA_INFO_FLAG = "extra_info_flag";

    private IntentExtraInfo extraInfo;

    @BindView(R.id.id_tl_activity_fragment_container)
    Toolbar mToolbar;
    @BindView(R.id.id_ll_tl_container)
    LinearLayout ll_container;

    @Inject
    GetDataFromNetPresent present;

    private ArrayList<ImageItem> imageList = null;


    public GetDataFromNetPresent getPresent() {
        return present;
    }

    private void parseExtraInfo() {
        extraInfo = getIntent().getParcelableExtra(EXTRA_INFO_FLAG);
        if (extraInfo == null) {
            throw new NullPointerException("extra_info is null");
        }
        if (extraInfo.isNeedNetWorking()) {
            DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(EBApplication.getInstance().getAppComponent()).build().inject(this);
        }
        ll_container.setVisibility(extraInfo.isHiddenToolBar ? View.GONE : View.VISIBLE);
        if (!extraInfo.isHiddenToolBar) {
            mToolbar.setTitle(extraInfo.getTitle());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initFragment();
    }

    private void initFragment() {
        Fragment fragment;
        Method method;
        try {
            if (extraInfo.extraBundle != null) {
                method = extraInfo.clazz.getMethod("newInstance", Bundle.class);
                fragment = (Fragment) method.invoke(null, extraInfo.extraBundle);
            } else {
                method = extraInfo.clazz.getMethod("newInstance");
                fragment = (Fragment) method.invoke(null);
            }
        } catch (NoSuchMethodException e) {
            try {
                fragment = (Fragment) extraInfo.clazz.newInstance();
                fragment.setArguments(extraInfo.extraBundle);
            } catch (Exception e1) {
                fragment = null;
            }
        } catch (Exception e) {
            fragment = null;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.id_fl_fragment_container, fragment);
            transaction.commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_container_layout;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null) {
            intent.putExtra("requestCode",-1);
            RxBus.getDefault().post(intent);
        }
    }

    @Override
    public void initView() {
        parseExtraInfo();
    }

    public static void startFragmentContainerActivity(Context context, IntentExtraInfo intentExtraInfo) {
        Intent intent = new Intent(context, FragmentContainerActivity.class);
        intent.putExtra(FragmentContainerActivity.EXTRA_INFO_FLAG, intentExtraInfo);
        context.startActivity(intent);
    }


    public static <T> void startFragmentContainerActivity(Context context, String title, boolean isNetWorking, boolean isHiddenBar, Class<T> clazz) {
        IntentExtraInfo<T> intentExtraInfo = new IntentExtraInfo<>();
        intentExtraInfo.isHiddenToolBar = isHiddenBar;
        intentExtraInfo.isNeedNetWorking = isNetWorking;
        intentExtraInfo.title = title;
        intentExtraInfo.clazz = clazz;
        startFragmentContainerActivity(context, intentExtraInfo);
    }

    public static <T> void startFragmentContainerActivity(Context context, String title, boolean isNetWorking, boolean isHiddenBar, Bundle bundle, Class<T> clazz) {
        IntentExtraInfo<T> intentExtraInfo = new IntentExtraInfo<>();
        intentExtraInfo.isHiddenToolBar = isHiddenBar;
        intentExtraInfo.isNeedNetWorking = isNetWorking;
        intentExtraInfo.title = title;
        intentExtraInfo.extraBundle = bundle;
        intentExtraInfo.clazz = clazz;
        startFragmentContainerActivity(context, intentExtraInfo);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER) {
            if (data != null) {
                imageList = new ArrayList();
                imageList.addAll((ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
                RxBus.getDefault().post(imageList);
            }
        } else if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE) {
            if (data != null) {
                data.putExtra("requestCode",com.tencent.connect.common.Constants.REQUEST_QQ_SHARE);
                RxBus.getDefault().post(data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (present != null) {
            present = null;
        }
        super.onDestroy();
    }

    public static class IntentExtraInfo<T> implements Parcelable {

        private int fragmentId = 0x0;
        private Bundle extraBundle = null;
        private String title = "";
        private boolean isNeedNetWorking = false;
        private boolean isHiddenToolBar = false;

        private Class<T> clazz;

        public Class<T> getClazz() {
            return clazz;
        }

        public IntentExtraInfo setClazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public int getFragmentId() {
            return fragmentId;
        }

        public IntentExtraInfo setFragmentId(int fragmentId) {
            this.fragmentId = fragmentId;
            return this;
        }

        public Bundle getExtraBundle() {
            return extraBundle;
        }

        public IntentExtraInfo setExtraBundle(Bundle extraBundle) {
            this.extraBundle = extraBundle;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public IntentExtraInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public boolean isNeedNetWorking() {
            return isNeedNetWorking;
        }

        public IntentExtraInfo setNeedNetWorking(boolean needNetWorking) {
            isNeedNetWorking = needNetWorking;
            return this;
        }

        public boolean isHiddenToolBar() {
            return isHiddenToolBar;
        }

        public IntentExtraInfo setHiddenToolBar(boolean hiddenToolBar) {
            isHiddenToolBar = hiddenToolBar;
            return this;
        }

        public IntentExtraInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.fragmentId);
            dest.writeBundle(this.extraBundle);
            dest.writeString(this.title);
            dest.writeByte(this.isNeedNetWorking ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isHiddenToolBar ? (byte) 1 : (byte) 0);
            dest.writeSerializable(this.clazz);
        }

        protected IntentExtraInfo(Parcel in) {
            this.fragmentId = in.readInt();
            this.extraBundle = in.readBundle();
            this.title = in.readString();
            this.isNeedNetWorking = in.readByte() != 0;
            this.isHiddenToolBar = in.readByte() != 0;
            this.clazz = (Class<T>) in.readSerializable();
        }

        public static final Creator<IntentExtraInfo> CREATOR = new Creator<IntentExtraInfo>() {
            @Override
            public IntentExtraInfo createFromParcel(Parcel source) {
                return new IntentExtraInfo(source);
            }

            @Override
            public IntentExtraInfo[] newArray(int size) {
                return new IntentExtraInfo[size];
            }
        };
    }
}
