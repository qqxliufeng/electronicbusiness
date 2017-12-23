package com.android.ql.lf.electronicbusiness.ui.fragments.im;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ql.lf.electronicbusiness.R;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;

/**
 * Created by lf on 2017/12/22 0022.
 *
 * @author lf on 2017/12/22 0022
 */

public class MyChatFragment extends ChatFragment {

    private Toolbar mToolbar;

    @Override
    protected void initView() {
        itemStrings = new int[]{com.hyphenate.helpdesk.R.string.attach_take_pic, com.hyphenate.helpdesk.R.string.attach_picture};
        itemdrawables = new int[]{R.drawable.img_take_photo_icon, R.drawable.img_select_pic_icon};
        itemIds = new int[]{ITEM_TAKE_PICTURE, ITEM_PICTURE};
        itemResIds = new int[]{com.hyphenate.helpdesk.R.id.chat_menu_take_pic, com.hyphenate.helpdesk.R.id.chat_menu_pic};
        super.initView();
        hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_chat_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.mTlChatBar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("在线客服");
        ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
