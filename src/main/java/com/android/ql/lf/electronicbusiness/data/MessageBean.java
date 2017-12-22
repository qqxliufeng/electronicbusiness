package com.android.ql.lf.electronicbusiness.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lf on 2017/12/21 0021.
 *
 * @author lf on 2017/12/21 0021
 */

public class MessageBean implements Parcelable {
    private String message_id;
    private String message_title;
    private String message_content;
    private String message_time;
    private String message_uid;
    private String message_dtime;
    private String message_status; // 0是未读 1 是已读
    private String message_token;
    private String message_qid;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_uid() {
        return message_uid;
    }

    public void setMessage_uid(String message_uid) {
        this.message_uid = message_uid;
    }

    public String getMessage_dtime() {
        return message_dtime;
    }

    public void setMessage_dtime(String message_dtime) {
        this.message_dtime = message_dtime;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getMessage_token() {
        return message_token;
    }

    public void setMessage_token(String message_token) {
        this.message_token = message_token;
    }

    public String getMessage_qid() {
        return message_qid;
    }

    public void setMessage_qid(String message_qid) {
        this.message_qid = message_qid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message_id);
        dest.writeString(this.message_title);
        dest.writeString(this.message_content);
        dest.writeString(this.message_time);
        dest.writeString(this.message_uid);
        dest.writeString(this.message_dtime);
        dest.writeString(this.message_status);
        dest.writeString(this.message_token);
        dest.writeString(this.message_qid);
    }

    public MessageBean() {
    }

    protected MessageBean(Parcel in) {
        this.message_id = in.readString();
        this.message_title = in.readString();
        this.message_content = in.readString();
        this.message_time = in.readString();
        this.message_uid = in.readString();
        this.message_dtime = in.readString();
        this.message_status = in.readString();
        this.message_token = in.readString();
        this.message_qid = in.readString();
    }

    public static final Parcelable.Creator<MessageBean> CREATOR = new Parcelable.Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel source) {
            return new MessageBean(source);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };
}
