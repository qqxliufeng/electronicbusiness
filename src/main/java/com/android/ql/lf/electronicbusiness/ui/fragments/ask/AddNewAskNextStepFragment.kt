package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AddNewAskTagsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_add_new_ask_next_step_layout.*
import com.lzy.imagepicker.ui.ImageGridActivity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.TagBean
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.*
import com.lzy.imagepicker.bean.ImageItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import java.io.File


/**
 * Created by lf on 2017/11/10 0010.
 * @author lf on 2017/11/10 0010
 */
class AddNewAskNextStepFragment : BaseNetWorkingFragment() {

    lateinit var title: String
    lateinit var content: String
    private lateinit var addIconBitmap: Bitmap

    companion object {
        val TITLE_FLAG = "title_flag"
    }

    private val MAX_SELECTED_ITEMS = 3

    private val imageList = arrayListOf<Bitmap>()
    private val tagsList = arrayListOf<TagBean>()
    private var imageListFile: ArrayList<ImageItem>? = null

    private lateinit var addNewAskTagsAdapter: AddNewAskTagsAdapter
    private lateinit var imagesAdapter: AddNewAskImagesAdapter
    private lateinit var topView: View

    private lateinit var subscription: Subscription

    override fun getLayoutId() = R.layout.fragment_add_new_ask_next_step_layout

    override fun initView(view: View?) {
        title = arguments.getString(TITLE_FLAG)
        imagesAdapter = AddNewAskImagesAdapter(R.layout.adapter_comment_image_item_layout, imageList)
        subscription = RxBus.getDefault().toObservable(ArrayList::class.java).subscribe { tempList ->
            if (tempList != null && !tempList.isEmpty() && tempList[0] is TagBean) {
                tempList.forEach {
                    val tempIt = it as TagBean
                    val title = tempIt.tag_title
                    tagsList.forEach {
                        if (title == it.tag_title) {
                            tempList.remove(tempIt)
                        }
                    }
                }
                tagsList.addAll(tempList as ArrayList<TagBean>)
                if (tagsList.size == MAX_SELECTED_ITEMS) {
                    addNewAskTagsAdapter.removeHeaderView(topView)
                }
                addNewAskTagsAdapter.notifyDataSetChanged()
            }
        }
        val metrics = DisplayMetrics()
        (mContext as FragmentContainerActivity).windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        addIconBitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.img_add_image)
        imageList.add(addIconBitmap)
        mRvAddNewAskImage.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvAddNewAskTags.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvAddNewAskImage.adapter = imagesAdapter
        addNewAskTagsAdapter = AddNewAskTagsAdapter(R.layout.adapter_add_new_ask_tags_item_layout, tagsList)
        topView = View.inflate(mContext, R.layout.layout_add_new_ask_tag_top_layout, null)
        topView.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(AddNewAskTagListFragment.MULTI_MODE_FLAG, true)
            bundle.putInt(AddNewAskTagListFragment.MULTI_MODE_MAX_SELECTED_ITEMS_FLAG, MAX_SELECTED_ITEMS - tagsList.size)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "标签", true, false, bundle, AddNewAskTagListFragment::class.java)
        }
        addNewAskTagsAdapter.addHeaderView(topView, -1, LinearLayoutManager.HORIZONTAL)
        mRvAddNewAskTags.adapter = addNewAskTagsAdapter
        mRvAddNewAskImage.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val imagePicker = ImagePicker.getInstance()
                imagePicker.isShowCamera = true
                imagePicker.imageLoader = MyImageLoader()
                imagePicker.focusWidth = width - 50 * 2
                imagePicker.isMultiMode = true
                imagePicker.selectLimit = MAX_SELECTED_ITEMS - imageList.size + 1
                imagePicker.focusHeight = imagePicker.focusWidth
                val intent = Intent(mContext, ImageGridActivity::class.java)
                startActivityForResult(intent, FragmentContainerActivity.IMAGE_PICKER)
            }
        })
        mBtSubmit.setOnClickListener {
            if (TextUtils.isEmpty(mEtAddNewAskNextStepContent.text.toString())) {
                toast("请输入问题内容")
                return@setOnClickListener
            } else {
                content = mEtAddNewAskNextStepContent.text.toString()
            }
            if (tagsList.isEmpty()) {
                toast("请选择标签 最多3个，最少1个")
                return@setOnClickListener
            }
            if (!mCkAddNewAskNextStep.isChecked) {
                toast("请先同意免责声明")
                return@setOnClickListener
            }
            if (imageListFile == null || imageListFile?.isEmpty()!!) {
                mPresent.getDataByPost(0x0,
                        RequestParamsHelper.QAA_MODEL,
                        RequestParamsHelper.ACT_ADD_QUIZ,
                        RequestParamsHelper.getAddQuizParams(title, content, addTags()))
            } else {
                //上传图片
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionFailed() {
                        toast("上传失败，请稍后重试！")
                    }

                    override fun onActionStart() {
                        progressDialog = MyProgressDialog(mContext, "正在上传……")
                        progressDialog.show()
                    }

                    override fun onActionEnd(paths: java.util.ArrayList<String>) {
                        val builder = ImageUploadHelper.createMultipartBody()
                        builder.addFormDataPart("title", title)
                        builder.addFormDataPart("content", content)
                        builder.addFormDataPart("type", addTags())
                        paths.forEachIndexed { index, s ->
                            val file = File(s)
                            builder.addFormDataPart("$index", file.name, RequestBody.create(MultipartBody.FORM, file))
                        }
                        mPresent.uploadFile(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_ADD_QUIZ, builder.build().parts())
                    }

                }).upload(imageListFile)
            }
        }
    }

    /**
     * 添加选择的标签
     */
    private fun addTags(): String {
        val tags = StringBuilder()
        tagsList.forEach {
            tags.append(it.tag_title).append(",")
        }
        tags.deleteCharAt(tags.length - 1)
        return tags.toString()
    }

    fun uploadImage() {
        //            progressDialog = ProgressDialog.show(mContext, null, "正在上传……")
//            val dir = File(Constants.IMAGE_PATH)
//            if (!dir.exists()) {
//                dir.mkdirs()
//            }
//            val builder = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("token", Constants.md5Token())
//            val compressImageList = ArrayList<String>()
//            if (imageListFile != null && !imageListFile!!.isEmpty()) {
//                Observable.from(imageListFile)
//                        .map {
//                            val path = "$dir/${System.currentTimeMillis()}.jpg"
//                            ImageFactory.compressAndGenImage(it.path, path, 100, false)
//                            compressImageList.add(path)
//                        }
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            if (compressImageList.size == imageListFile!!.size) {
//                                compressImageList.forEachWithIndex { i, it ->
//                                    val file = File(it)
//                                    builder.addFormDataPart("$i", file.name, RequestBody.create(MultipartBody.FORM, file))
//                                }
//                                mPresent.uploadFile(0x1, "t", "pictime", builder.build().parts())
//                            }
//                        }
//            }else{
//                mPresent.uploadFile(0x1, "t", "pictime", builder.build().parts())
//            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FragmentContainerActivity.IMAGE_PICKER) {
            if (data != null) {
                val list = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                list.forEach {
                    imageList.add(ImageFactory.getBitmap(it.path))
                }
                if (imageListFile == null) {
                    imageListFile = ArrayList()
                }
                imageListFile!!.addAll(list)
                if (imageList.size == MAX_SELECTED_ITEMS + 1) {
                    imageList.remove(addIconBitmap)
                }
                imagesAdapter.notifyDataSetChanged()
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
            toast("提问成功")
            RefreshData.isRefresh = true
            RefreshData.any = "提问问题"
            RxBus.getDefault().post(RefreshData)
            finish()
        } else {
            toast("提问失败")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("提问失败")
    }

    override fun onDestroyView() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
        super.onDestroyView()
    }

    class AddNewAskImagesAdapter(layoutId: Int, list: ArrayList<Bitmap>) : BaseQuickAdapter<Bitmap, BaseViewHolder>(layoutId, list) {
        override fun convert(helper: BaseViewHolder?, item: Bitmap?) {
            helper!!.setImageBitmap(R.id.mIvCommentImageItem, item)
        }
    }

    class MyImageLoader : ImageLoader {
        override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
            Glide.with(activity).load(Uri.fromFile(File(path))).error(R.drawable.img_glide_load_default).placeholder(R.drawable.img_glide_load_default).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

        override fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
            Glide.with(activity)
                    .load(Uri.fromFile(File(path)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
        }

        override fun clearMemoryCache() {}
    }

}