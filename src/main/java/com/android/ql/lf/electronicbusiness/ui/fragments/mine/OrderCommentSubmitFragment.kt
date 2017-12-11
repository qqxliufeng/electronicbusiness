package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.SelectImageItemBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.ImageUploadHelper
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.SelectImageManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.fragment_order_comment_submit_layout.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.toast
import java.io.File

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
class OrderCommentSubmitFragment : BaseNetWorkingFragment() {

    companion object {
        val ORDER_ID_FLAG = "order_id_flag"
        val PRODUCT_ID_FLAG = "product_id_flag"
    }

    private val imageList = arrayListOf<SelectImageItemBean>()

    private val MAX_SELECTED_ITEMS = 3

    private var imageListFile: ArrayList<ImageItem>? = null
    private lateinit var imagesAdapter: SelectImageManager.SelectImagesAdapter

    private val firstImageItemBean by lazy {
        SelectImageItemBean(null, R.drawable.img_add_image)
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_comment_submit_layout

    override fun initView(view: View?) {
        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_icon_star_n)
        val layoutParams = mRbGoodsCommentStart.layoutParams
        layoutParams.height = bmp.height
        layoutParams.width = -2
        mRbGoodsCommentStart.layoutParams = layoutParams

        imageList.add(firstImageItemBean)

        mRvOrderComment.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = SelectImageManager.SelectImagesAdapter(R.layout.adapter_comment_image_item_layout, imageList)
        mRvOrderComment.adapter = imagesAdapter
        val metrics = DisplayMetrics()
        (mContext as FragmentContainerActivity).windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels
        mRvOrderComment.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (imageList[position].resId != 0) {
                    val imagePicker = ImagePicker.getInstance()
                    imagePicker.isShowCamera = true
                    imagePicker.imageLoader = SelectImageManager.SelectImageLoader()
                    imagePicker.focusWidth = width - 50 * 2
                    imagePicker.isMultiMode = true
                    imagePicker.selectLimit = MAX_SELECTED_ITEMS - imageList.size + 1
                    imagePicker.focusHeight = imagePicker.focusWidth
                    val intent = Intent(mContext, ImageGridActivity::class.java)
                    startActivityForResult(intent, FragmentContainerActivity.IMAGE_PICKER)
                }
            }
        })

        mBtGoodsCommentSubmit.setOnClickListener {
            if (TextUtils.isEmpty(mEtGoodsCommentContent.text.toString())) {
                toast("请输入商品评价")
                return@setOnClickListener
            }
            if (mEtGoodsCommentContent.text.toString().length < 15) {
                toast("请输入至少15字")
                return@setOnClickListener
            }
            if (imageListFile != null && !imageListFile!!.isEmpty()) {
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionEnd(paths: java.util.ArrayList<String>?) {
                        val builder = ImageUploadHelper.createMultipartBody()
                        builder.addFormDataPart("oid", arguments.getString(ORDER_ID_FLAG, ""))
                        builder.addFormDataPart("gid", arguments.getString(PRODUCT_ID_FLAG, ""))
                        builder.addFormDataPart("content", mEtGoodsCommentContent.text.toString())
                        builder.addFormDataPart("f", mRbGoodsCommentStart.rating.toString())
                        paths!!.forEachIndexed { index, s ->
                            val file = File(s)
                            builder.addFormDataPart("$index", file.name, RequestBody.create(MultipartBody.FORM, file))
                        }
                        mPresent.uploadFile(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EVALUATE, builder.build().parts())
                    }

                    override fun onActionFailed() {
                    }

                    override fun onActionStart() {
                        progressDialog = MyProgressDialog(mContext, "正在上传……")
                        progressDialog.show()
                    }
                }).upload(imageListFile)
            } else {
                mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EVALUATE,
                        RequestParamsHelper.getEvaluateParam(arguments.getString(ORDER_ID_FLAG, ""),
                                arguments.getString(PRODUCT_ID_FLAG, ""),
                                mEtGoodsCommentContent.text.toString(),
                                mRbGoodsCommentStart.rating.toString()
                        ))
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在上传……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            toast("上传成功！")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FragmentContainerActivity.IMAGE_PICKER) {
            if (data != null) {
                val list = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                list.forEach {
                    imageList.add(SelectImageItemBean(it.path, 0))
                }
                if (imageListFile == null) {
                    imageListFile = ArrayList()
                }
                imageListFile!!.addAll(list)
                if (imageList.size == MAX_SELECTED_ITEMS + 1) {
                    imageList.remove(firstImageItemBean)
                }
                imagesAdapter.notifyDataSetChanged()
            }
        }
    }

}