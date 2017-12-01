package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.utils.CacheDataManager
import kotlinx.android.synthetic.main.fragment_setting_layout.*

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class SettingFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_setting_layout


    override fun initView(view: View?) {
        val versionName = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
        mVersionName.text = "V$versionName"
        val cacheSize = CacheDataManager.getTotalCacheSize(mContext)
        mCacheSize.text = "$cacheSize"
        mCacheSizeContainer.setOnClickListener {
            CacheDataManager.clearAllCache(mContext)
            mCacheSize.text = "暂无缓存"
        }
    }
}