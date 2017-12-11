package com.android.ql.lf.electronicbusiness.ui.fragments.ask

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.data.SelectImageItemBean
import com.android.ql.lf.electronicbusiness.data.TagBean
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.adapters.AddNewAskTagsAdapter
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.ImageUploadHelper
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.android.ql.lf.electronicbusiness.utils.SelectImageManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.fragment_add_new_ask_next_step_layout.*
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
    private val tags: StringBuilder = StringBuilder()
    lateinit var content: String

    companion object {
        val TITLE_FLAG = "title_flag"
    }

    private val MAX_SELECTED_ITEMS = 3

    private val imageList = arrayListOf<SelectImageItemBean>()
    private val tagsList = arrayListOf<TagBean>()
    private var imageListFile: ArrayList<ImageItem>? = null

    private lateinit var addNewAskTagsAdapter: AddNewAskTagsAdapter
    private lateinit var imagesAdapter: SelectImageManager.SelectImagesAdapter
    private lateinit var topView: View

    private val firstImageItemBean by lazy {
        SelectImageItemBean(null, R.drawable.img_add_image)
    }

    override fun getLayoutId() = R.layout.fragment_add_new_ask_next_step_layout

    override fun initView(view: View?) {
        title = arguments.getString(TITLE_FLAG)
        imagesAdapter = SelectImageManager.SelectImagesAdapter(R.layout.adapter_comment_image_item_layout, imageList)
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

        imageList.add(firstImageItemBean)
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
                imagePicker.imageLoader = SelectImageManager.SelectImageLoader()
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
                tagsList.forEach {
                    tags.append(it.tag_title).append(",")
                }
                tags.deleteCharAt(tags.length - 1)
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
                        builder.addFormDataPart("type", tags.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FragmentContainerActivity.IMAGE_PICKER) {
            if (data != null) {
                val list = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                list.forEach {
                    imageList.add(SelectImageItemBean(it.path,0))
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

}