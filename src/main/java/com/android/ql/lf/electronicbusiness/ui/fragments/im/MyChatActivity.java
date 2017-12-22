package com.android.ql.lf.electronicbusiness.ui.fragments.im;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.android.ql.lf.electronicbusiness.ui.activities.BaseChatActivity;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;
import com.hyphenate.helpdesk.easeui.util.Config;

/**
 * Created by lf on 2017/12/22 0022.
 *
 * @author lf on 2017/12/22 0022
 */

public class MyChatActivity extends BaseChatActivity {

    protected ChatFragment chatFragment = null;
    protected String toChatUsername = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(com.hyphenate.helpdesk.R.layout.hd_activity_chat);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //IM服务号
        toChatUsername = bundle.getString(Config.EXTRA_SERVICE_IM_NUMBER);
        chatFragment = new MyChatFragment();
        // 传入参数
        chatFragment.setArguments(intent.getExtras());
        getSupportFragmentManager().beginTransaction().add(com.hyphenate.helpdesk.R.id.container, chatFragment).commit();
        ChatClient.getInstance().chatManager().bindChat(toChatUsername);
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Config.EXTRA_SERVICE_IM_NUMBER);
        if (toChatUsername.equals(username)) {
            super.onNewIntent(intent);
        }
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
