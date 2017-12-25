package com.android.ql.lf.electronicbusiness.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.ql.lf.electronicbusiness.ui.activities.SplashActivity;
import com.android.ql.lf.electronicbusiness.ui.fragments.im.MyChatActivity;
import com.android.ql.lf.electronicbusiness.utils.Constants;
import com.android.ql.lf.electronicbusiness.utils.PreferenceUtils;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * @author Administrator
 * @date 2017/10/21 0021
 */

public class UserInfo {

    public static final String USER_ID_FLAG = "user_id";

    public static final int DEFAULT_LOGIN_TAG = -1000;

    private UserInfo() {
    }

    private static UserInfo instance;

    public static UserInfo getInstance() {
        if (instance == null) {
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new UserInfo();
                }
            }
        }
        return instance;
    }


    private String memberId;
    private String memberPhone;
    private String memberName;
    private String memberForm;
    private String memberIntegral;
    private String memberMtime;
    private String memberRank;
    private String memberAddress;
    private String memberSex;
    private String memberPic;
    private String member_hxname;
    private String member_hxpw;
    private String member_openid = "";

    public String getMember_openid() {
        return member_openid;
    }

    public void setMember_openid(String member_openid) {
        this.member_openid = member_openid;
    }

    public String getMember_hxname() {
        return member_hxname;
    }

    public void setMember_hxname(String member_hxname) {
        this.member_hxname = member_hxname;
    }

    public String getMember_hxpw() {
        return member_hxpw;
    }

    public void setMember_hxpw(String member_hxpw) {
        this.member_hxpw = member_hxpw;
    }

    public String getMemberPic() {
        return memberPic;
    }

    public void setMemberPic(String memberPic) {
        this.memberPic = memberPic;
    }

    private Object loginTag = DEFAULT_LOGIN_TAG;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    public String getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(String memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }


    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }


    public String getMemberForm() {
        return memberForm;
    }

    public void setMemberForm(String memberForm) {
        this.memberForm = memberForm;
    }

    public String getMemberIntegral() {
        if (TextUtils.isEmpty(memberIntegral) || "null".equals(memberIntegral)) {
            return "0";
        }
        return memberIntegral;
    }

    public void setMemberIntegral(String memberIntegral) {
        if (!TextUtils.isEmpty(memberIntegral)) {
            this.memberIntegral = memberIntegral;
        }
    }


    public String getMemberMtime() {
        return memberMtime;
    }

    public void setMemberMtime(String memberMtime) {
        this.memberMtime = memberMtime;
    }


    public Object getLoginTag() {
        return loginTag;
    }

    public void setLoginTag(Object loginTag) {
        this.loginTag = loginTag;
    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(memberId);
    }

    public static void parseUserInfo(Context context, JSONObject userJson) {
        if (userJson != null) {
            UserInfo.getInstance().memberId = userJson.optString("member_id");
            UserInfo.getInstance().memberName = userJson.optString("member_name");
            UserInfo.getInstance().memberPhone = userJson.optString("member_phone");
            UserInfo.getInstance().memberRank = userJson.optString("member_rank");
            UserInfo.getInstance().memberSex = userJson.optString("member_sex");
            UserInfo.getInstance().memberMtime = userJson.optString("member_mtime");
            UserInfo.getInstance().memberIntegral = userJson.optString("member_integral");
            UserInfo.getInstance().memberForm = userJson.optString("member_form");
            UserInfo.getInstance().memberAddress = userJson.optString("member_address");
            UserInfo.getInstance().memberPic = userJson.optString("member_pic");
            UserInfo.getInstance().member_hxname = userJson.optString("member_hxname");
            UserInfo.getInstance().member_hxpw = userJson.optString("member_hxpw");
            if (TextUtils.isEmpty(UserInfo.getInstance().member_openid)) {
                UserInfo.getInstance().member_openid = userJson.optString("member_openid");
            }
            PreferenceUtils.setPrefString(context, USER_ID_FLAG, UserInfo.getInstance().memberId);
        }
    }

    /**
     * 登录环信
     */
    public static void loginHx() {
        ChatClient.getInstance().login(UserInfo.getInstance().member_hxname, UserInfo.getInstance().member_hxpw, new Callback() {
            @Override
            public void onSuccess() {
                Log.e("TAG", "环信登录成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.e("TAG", s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public static void openKeFu(Context context) {
        Intent intent = new IntentBuilder(context)
                .setServiceIMNumber(Constants.HX_IM_SERVICE_NUM)
                .setTargetClass(MyChatActivity.class)
                .build();
        context.startActivity(intent);
    }

    public void loginOut() {
        memberId = null;
        instance = null;
    }

    public void clearUserCache(Context context) {
        PreferenceUtils.setPrefString(context, USER_ID_FLAG, "");
    }

    public static boolean isCacheUserId(Context context) {
        return PreferenceUtils.hasKey(context, USER_ID_FLAG) && !TextUtils.isEmpty(PreferenceUtils.getPrefString(context, USER_ID_FLAG, ""));
    }

    public static String getUserIdFromCache(Context context) {
        return PreferenceUtils.getPrefString(context, USER_ID_FLAG, "");
    }
}
