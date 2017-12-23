package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v7.app.AlertDialog
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.utils.CacheDataManager
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.hyphenate.chat.ChatClient
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
        mBtLogout.isEnabled = UserInfo.getInstance().isLogin
        mBtLogout.setOnClickListener {
            val build = AlertDialog.Builder(mContext)
            build.setPositiveButton("退出") { _, _ ->
                UserInfo.getInstance().loginOut()
                UserInfo.getInstance().clearUserCache(mContext)
                UserInfo.getInstance().loginTag = -1
                ChatClient.getInstance().logout(true, null)
                RxBus.getDefault().post(UserInfo.getInstance())
                finish()
            }
            build.setNegativeButton("取消", null)
            build.setMessage("是否要退出当前帐号？")
            build.create().show()
        }
    }
}