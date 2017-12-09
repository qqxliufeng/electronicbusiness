package com.android.ql.lf.electronicbusiness.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lf on 2017/11/29 0029.
 *
 * @author lf on 2017/11/29 0029
 */

public class AnswerBean implements Parcelable {
    private int maxLines = 3;
    private boolean isExpand = false;

    private String answer_id;
    private String answer_content;
    private ArrayList<String> answer_pic;
    private String answer_title;
    private String answer_qid;
    private String answer_uid;
    private String answer_num;
    private String answer_click;
    private String answer_pnum;
    private String answer_time;
    private String member_id;
    private String member_name;
    private String member_pic;
    private String ask_title;
    private String ask_num;

    public String getAsk_title() {
        return ask_title;
    }

    public void setAsk_title(String ask_title) {
        this.ask_title = ask_title;
    }

    public String getAsk_num() {
        return ask_num;
    }

    public void setAsk_num(String ask_num) {
        this.ask_num = ask_num;
    }

    public String getAnswer_num() {
        return answer_num;
    }

    public void setAnswer_num(String answer_num) {
        this.answer_num = answer_num;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public ArrayList<String> getAnswer_pic() {
        return answer_pic;
    }

    public void setAnswer_pic(ArrayList<String> answer_pic) {
        this.answer_pic = answer_pic;
    }

    public String getAnswer_title() {
        return answer_title;
    }

    public void setAnswer_title(String answer_title) {
        this.answer_title = answer_title;
    }

    public String getAnswer_qid() {
        return answer_qid;
    }

    public void setAnswer_qid(String answer_qid) {
        this.answer_qid = answer_qid;
    }

    public String getAnswer_uid() {
        return answer_uid;
    }

    public void setAnswer_uid(String answer_uid) {
        this.answer_uid = answer_uid;
    }

    public String getAnswer_click() {
        return answer_click;
    }

    public void setAnswer_click(String answer_click) {
        this.answer_click = answer_click;
    }

    public String getAnswer_pnum() {
        return answer_pnum;
    }

    public void setAnswer_pnum(String answer_pnum) {
        this.answer_pnum = answer_pnum;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_pic() {
        return member_pic;
    }

    public void setMember_pic(String member_pic) {
        this.member_pic = member_pic;
    }

    public AnswerBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxLines);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
        dest.writeString(this.answer_id);
        dest.writeString(this.answer_content);
        dest.writeStringList(this.answer_pic);
        dest.writeString(this.answer_title);
        dest.writeString(this.answer_qid);
        dest.writeString(this.answer_uid);
        dest.writeString(this.answer_num);
        dest.writeString(this.answer_click);
        dest.writeString(this.answer_pnum);
        dest.writeString(this.answer_time);
        dest.writeString(this.member_id);
        dest.writeString(this.member_name);
        dest.writeString(this.member_pic);
        dest.writeString(this.ask_title);
        dest.writeString(this.ask_num);
    }

    protected AnswerBean(Parcel in) {
        this.maxLines = in.readInt();
        this.isExpand = in.readByte() != 0;
        this.answer_id = in.readString();
        this.answer_content = in.readString();
        this.answer_pic = in.createStringArrayList();
        this.answer_title = in.readString();
        this.answer_qid = in.readString();
        this.answer_uid = in.readString();
        this.answer_num = in.readString();
        this.answer_click = in.readString();
        this.answer_pnum = in.readString();
        this.answer_time = in.readString();
        this.member_id = in.readString();
        this.member_name = in.readString();
        this.member_pic = in.readString();
        this.ask_title = in.readString();
        this.ask_num = in.readString();
    }

    public static final Creator<AnswerBean> CREATOR = new Creator<AnswerBean>() {
        @Override
        public AnswerBean createFromParcel(Parcel source) {
            return new AnswerBean(source);
        }

        @Override
        public AnswerBean[] newArray(int size) {
            return new AnswerBean[size];
        }
    };
}
