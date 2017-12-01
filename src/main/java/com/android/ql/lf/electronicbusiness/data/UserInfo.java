package com.android.ql.lf.electronicbusiness.data;

import android.text.TextUtils;

/**
 * @author Administrator
 * @date 2017/10/21 0021
 */

public class UserInfo {

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

    public String getMemberPic() {
        return memberPic;
    }

    public void setMemberPic(String memberPic) {
        this.memberPic = memberPic;
    }

    private int loginTag = DEFAULT_LOGIN_TAG;

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
        return memberIntegral;
    }

    public void setMemberIntegral(String memberIntegral) {
        this.memberIntegral = memberIntegral;
    }


    public String getMemberMtime() {
        return memberMtime;
    }

    public void setMemberMtime(String memberMtime) {
        this.memberMtime = memberMtime;
    }


    public int getLoginTag() {
        return loginTag;
    }

    public void setLoginTag(int loginTag) {
        this.loginTag = loginTag;
    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(memberId);
    }


    public void loginOut() {
        memberId = null;
        instance = null;
    }
}
