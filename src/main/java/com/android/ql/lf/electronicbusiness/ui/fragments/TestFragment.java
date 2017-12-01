package com.android.ql.lf.electronicbusiness.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ql.lf.electronicbusiness.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by Administrator on 2017/10/17 0017.
 * @author lf
 */

public class TestFragment extends BaseFragment implements RichEditor.OnTextChangeListener {

    public static TestFragment newInstance(Bundle bundle) {
        TestFragment testFragment = new TestFragment();
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @BindView(R.id.id_tv)
    RichEditor mEditor;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_layout;
    }

    @Override
    protected void initView(View view) {
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        mEditor.setOnTextChangeListener(this);
        mEditor.getHtml();
    }

    @OnClick(R.id.id_bt)
    public void onClick(View view){
        Toast.makeText(mContext, "insertImage", Toast.LENGTH_SHORT).show();
        mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG","alt");
    }


    @OnClick(R.id.id_com)
    public void onCom(View view){
        Log.e("TAG",mEditor.getHtml());
    }



    @Override
    public void onTextChange(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
