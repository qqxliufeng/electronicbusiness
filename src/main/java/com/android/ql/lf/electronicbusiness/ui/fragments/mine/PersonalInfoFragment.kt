package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.UserInfo
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.*
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.fragment_mine_personal_info_layout.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import rx.Subscription
import java.io.File
import java.util.*

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class PersonalInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val NICK_ID = 0x0
        val PHONE_ID = 0x1

        val HINT_KEY = "hint_key"
        val ACTION_ID_KEY = "action_id_key"
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine_personal_info_layout

    private lateinit var subscription: Subscription

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(UserInfo.getInstance()::class.java).subscribe {
            mTvPersonalInfoNickName.text = UserInfo.getInstance().memberName
        }
        mFaceContainer.setOnClickListener {
            val metrics = DisplayMetrics()
            (mContext as FragmentContainerActivity).windowManager.defaultDisplay.getMetrics(metrics)
            val width = metrics.widthPixels
            val imagePicker = ImagePicker.getInstance()
            imagePicker.isShowCamera = true
            imagePicker.imageLoader = MyImageLoader()
            imagePicker.focusWidth = width - 50 * 2
            imagePicker.isMultiMode = false
            imagePicker.selectLimit = 1
            imagePicker.isCrop = true
            imagePicker.focusHeight = imagePicker.focusWidth
            val intent = Intent(mContext, ImageGridActivity::class.java)
            startActivityForResult(intent, FragmentContainerActivity.IMAGE_PICKER)
        }
        mNickNameContainer.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(HINT_KEY, "请输入昵称")
            bundle.putInt(ACTION_ID_KEY, NICK_ID)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "昵称", true, false, bundle, EditPersonalInfoFragment::class.java)
        }
        mTvPersonalInfoResetPassword.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "修改密码", true, false, ResetPasswordFragment::class.java)
        }
        mBtLogout.setOnClickListener {
            val build = AlertDialog.Builder(mContext)
            build.setPositiveButton("退出") { _, _ ->
                UserInfo.getInstance().loginOut()
                UserInfo.getInstance().loginTag = -1
                RxBus.getDefault().post(UserInfo.getInstance())
                finish()
            }
            build.setNegativeButton("取消", null)
            build.setMessage("是否要退出当前帐号？")
            build.create().show()
        }
        GlideManager.loadFaceCircleImage(mContext, "${Constants.BASE_IP}${UserInfo.getInstance().memberPic}", mTvPersonalInfoFace)
        mTvPersonalInfoNickName.text = UserInfo.getInstance().memberName
        mTvPersonalInfoPhone.text = UserInfo.getInstance().memberPhone
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FragmentContainerActivity.IMAGE_PICKER) {
            if (data != null) {
                val list = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionFailed() {
                        toast("头像上传失败，请稍后重试！")
                    }

                    override fun onActionStart() {
                        progressDialog = MyProgressDialog(mContext, "正在上传头像……")
                        progressDialog.show()
                    }

                    override fun onActionEnd(paths: ArrayList<String>) {
                        val builder = ImageUploadHelper.createMultipartBody()
                        paths.forEachIndexed { index, s ->
                            val file = File(s)
                            builder.addFormDataPart("$index", file.name, RequestBody.create(MultipartBody.FORM, file))
                        }
                        mPresent.uploadFile(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_PERSONAL, builder.build().parts())
                    }

                }).upload(list)
            }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (result != null) {
            val jsonObject = JSONObject(result.toString())
            if ("200" == jsonObject.optString("code")) {
                val resultObj = jsonObject.optJSONObject("result")
                val facePath = resultObj.optString("member_pic")
                GlideManager.loadFaceCircleImage(mContext, "${Constants.BASE_IP}$facePath", mTvPersonalInfoFace)
                UserInfo.getInstance().memberPic = facePath
                UserInfo.getInstance().loginTag = UserInfo.DEFAULT_LOGIN_TAG
                RxBus.getDefault().post(UserInfo.getInstance())
            } else {
                toast(jsonObject.optString("msg"))
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("头像上传失败，请稍后重试！")
    }

    override fun onDestroyView() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroyView()
    }

}