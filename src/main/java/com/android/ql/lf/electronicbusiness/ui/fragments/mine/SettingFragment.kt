package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseFragment
import com.android.ql.lf.electronicbusiness.utils.CacheDataManager
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.hyphenate.chat.ChatClient
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.fragment_setting_layout.*
import q.rorbin.badgeview.QBadgeView

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class SettingFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_setting_layout

    override fun initView(view: View?) {
        val packageInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
        val versionName = packageInfo.versionName
        mVersionName.text = "V$versionName"
        val cacheSize = CacheDataManager.getTotalCacheSize(mContext)
        mCacheSize.text = "$cacheSize"
        val upgradeInfo = Beta.getUpgradeInfo()
        if (upgradeInfo != null && packageInfo.versionCode < upgradeInfo.versionCode) {
            mTvNewVersionNotify.visibility = View.VISIBLE
            mRlVersionUpContainer.setOnClickListener {
                Beta.checkUpgrade(false, false)
            }
        } else {
            mTvNewVersionNotify.visibility = View.GONE
        }
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
                if (ChatClient.getInstance().isLoggedInBefore) {
                    ChatClient.getInstance().logout(true, null)
                }
                RxBus.getDefault().post(UserInfo.getInstance())
                finish()
            }
            build.setNegativeButton("取消", null)
            build.setMessage("是否要退出当前帐号？")
            build.create().show()
        }
    }
}