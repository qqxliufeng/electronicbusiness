package com.android.ql.lf.electronicbusiness.ui.fragments.ask;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.RefreshData;
import com.android.ql.lf.electronicbusiness.data.SelectImageItemBean;
import com.android.ql.lf.electronicbusiness.data.UserInfo;
import com.android.ql.lf.electronicbusiness.present.OrderPresent;
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment;
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog;
import com.android.ql.lf.electronicbusiness.utils.ExtensionUtilsKt;
import com.android.ql.lf.electronicbusiness.utils.ImageUploadHelper;
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper;
import com.android.ql.lf.electronicbusiness.utils.RxBus;
import com.android.ql.lf.electronicbusiness.utils.SelectImageManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by lf on 2017/12/16 0016.
 *
 * @author lf on 2017/12/16 0016
 */

public class ReplyQuestionFragment extends BaseNetWorkingFragment {

    @BindView(R.id.mTvAddNewReplyTitle)
    TextView tv_title;
    @BindView(R.id.mEtAddNewReplyContent)
    EditText et_content;
    @BindView(R.id.mRvAddNeReplyImage)
    RecyclerView rv_image;


    private final int MAX_SELECTED_ITEMS = 3;

    private ArrayList<SelectImageItemBean> imageList = new ArrayList();
    private ArrayList<ImageItem> imageListFile;

    private SelectImageManager.SelectImagesAdapter imagesAdapter;
    private SelectImageItemBean firstImageItemBean = new SelectImageItemBean(null, R.drawable.img_add_image);


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_replay_question_layout;
    }

    @Override
    protected void initView(View view) {
        tv_title.setText(getArguments().getString("title"));
        imagesAdapter = new SelectImageManager.SelectImagesAdapter(R.layout.adapter_comment_image_item_layout, imageList);
        rv_image.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        imageList.add(firstImageItemBean);
        rv_image.setAdapter(imagesAdapter);
        rv_image.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setShowCamera(true);
                imagePicker.setImageLoader(new SelectImageManager.SelectImageLoader());
                imagePicker.setFocusWidth(ExtensionUtilsKt.getScreenWidth(mContext) - 50 * 2);
                imagePicker.setMultiMode(true);
                imagePicker.setSelectLimit(MAX_SELECTED_ITEMS - imageList.size() + 1);
                imagePicker.setFocusHeight(imagePicker.getFocusWidth());
                Intent intent = new Intent(mContext, ImageGridActivity.class);
                startActivityForResult(intent, FragmentContainerActivity.IMAGE_PICKER);
            }
        });
    }

    @OnClick(R.id.mBtSubmit)
    public void onClick() {
        if (TextUtils.isEmpty(et_content.getText().toString())) {
            Toast.makeText(mContext, "请输入回答内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageListFile == null || imageListFile.isEmpty()) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.Companion.getQAA_MODEL(),
                    RequestParamsHelper.Companion.getACT_ADD_ANSWER(),
                    RequestParamsHelper.Companion.getAddAnswerParams(getArguments().getString("qid"), et_content.getText().toString()));
        } else {
            //上传图片
            new ImageUploadHelper(new ImageUploadHelper.OnImageUploadListener() {
                @Override
                public void onActionStart() {
                    progressDialog = new MyProgressDialog(mContext, "正在上传……");
                    progressDialog.show();
                }

                @Override
                public void onActionEnd(ArrayList<String> paths) {
                    MultipartBody.Builder builder = ImageUploadHelper.createMultipartBody();
                    builder.addFormDataPart("qid", getArguments().getString("qid"));
                    builder.addFormDataPart("content", et_content.getText().toString());
                    for (int i = 0; i < paths.size(); i++) {
                        File file = new File(paths.get(i));
                        builder.addFormDataPart(i + "", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                    }
                    mPresent.uploadFile(0x1, RequestParamsHelper.Companion.getQAA_MODEL(), RequestParamsHelper.Companion.getACT_ADD_ANSWER(), builder.build().parts());
                }

                @Override
                public void onActionFailed() {
                    Toast.makeText(mContext, "上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }).upload(imageListFile);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FragmentContainerActivity.IMAGE_PICKER) {
            if (data != null) {
                ArrayList<ImageItem> list = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem imageItem : list) {
                    imageList.add(new SelectImageItemBean(imageItem.path, 0));
                }
                if (imageListFile == null) {
                    imageListFile = new ArrayList();
                }
                imageListFile.addAll(list);
                if (imageList.size() == MAX_SELECTED_ITEMS + 1) {
                    imageList.remove(firstImageItemBean);
                }
                imagesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
        super.onRequestSuccess(requestID, result);
        JSONObject json = checkResultCode(result);
        if (json != null) {
            if (Objects.equals(json.optString("code"), "200")) {
                UserInfo.getInstance().setMemberIntegral(json.optString("arr"));
                Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                RefreshData.INSTANCE.setRefresh(true);
                RefreshData.INSTANCE.setAny("回复成功");
                RxBus.getDefault().post(RefreshData.INSTANCE);
                if (Objects.equals(getArguments().getString("uid"), UserInfo.getInstance().getMemberId())) {
                    OrderPresent.notifyRefreshOrderNum();
                }
                finish();
            }
        } else {
            try {
                if (result != null && Objects.equals(new JSONObject(result.toString()).optString("code"), "400")) {
                    Toast.makeText(mContext, "该问题已被作者删除，暂无法回答", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                Toast.makeText(mContext, "上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(mContext, "上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
