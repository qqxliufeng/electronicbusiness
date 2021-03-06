package com.android.ql.lf.electronicbusiness.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lf on 2017/11/30 0030.
 *
 * @author lf on 2017/11/30 0030
 */

public class IMallGoodsItemBean implements Parcelable {
    private String jproduct_id;
    private String jproduct_price;
    private ArrayList<String> jproduct_pic;
    private String jproduct_name;
    private String jproduct_content;
    private String jproduct_entrepot;
    private String jproduct_type;
    private String jproduct_time;
    private String jproduct_ptype;
    private String jproduct_stype;
    private String jproduct_token;
    private String jproduct_yprice;

    public String getJproduct_id() {
        return jproduct_id;
    }

    public void setJproduct_id(String jproduct_id) {
        this.jproduct_id = jproduct_id;
    }

    public String getJproduct_price() {
        return jproduct_price;
    }

    public void setJproduct_price(String jproduct_price) {
        this.jproduct_price = jproduct_price;
    }

    public ArrayList<String> getJproduct_pic() {
        return jproduct_pic;
    }

    public void setJproduct_pic(ArrayList<String> jproduct_pic) {
        this.jproduct_pic = jproduct_pic;
    }

    public String getJproduct_name() {
        return jproduct_name;
    }

    public void setJproduct_name(String jproduct_name) {
        this.jproduct_name = jproduct_name;
    }

    public String getJproduct_content() {
        return jproduct_content;
    }

    public void setJproduct_content(String jproduct_content) {
        this.jproduct_content = jproduct_content;
    }

    public String getJproduct_entrepot() {
        return jproduct_entrepot;
    }

    public void setJproduct_entrepot(String jproduct_entrepot) {
        this.jproduct_entrepot = jproduct_entrepot;
    }

    public String getJproduct_type() {
        return jproduct_type;
    }

    public void setJproduct_type(String jproduct_type) {
        this.jproduct_type = jproduct_type;
    }

    public String getJproduct_time() {
        return jproduct_time;
    }

    public void setJproduct_time(String jproduct_time) {
        this.jproduct_time = jproduct_time;
    }

    public String getJproduct_ptype() {
        return jproduct_ptype;
    }

    public void setJproduct_ptype(String jproduct_ptype) {
        this.jproduct_ptype = jproduct_ptype;
    }

    public String getJproduct_stype() {
        return jproduct_stype;
    }

    public void setJproduct_stype(String jproduct_stype) {
        this.jproduct_stype = jproduct_stype;
    }

    public String getJproduct_token() {
        return jproduct_token;
    }

    public void setJproduct_token(String jproduct_token) {
        this.jproduct_token = jproduct_token;
    }

    public String getJproduct_yprice() {
        return jproduct_yprice;
    }

    public void setJproduct_yprice(String jproduct_yprice) {
        this.jproduct_yprice = jproduct_yprice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jproduct_id);
        dest.writeString(this.jproduct_price);
        dest.writeStringList(this.jproduct_pic);
        dest.writeString(this.jproduct_name);
        dest.writeString(this.jproduct_content);
        dest.writeString(this.jproduct_entrepot);
        dest.writeString(this.jproduct_type);
        dest.writeString(this.jproduct_time);
        dest.writeString(this.jproduct_ptype);
        dest.writeString(this.jproduct_stype);
        dest.writeString(this.jproduct_token);
        dest.writeString(this.jproduct_yprice);
    }

    public IMallGoodsItemBean() {
    }

    protected IMallGoodsItemBean(Parcel in) {
        this.jproduct_id = in.readString();
        this.jproduct_price = in.readString();
        this.jproduct_pic = in.createStringArrayList();
        this.jproduct_name = in.readString();
        this.jproduct_content = in.readString();
        this.jproduct_entrepot = in.readString();
        this.jproduct_type = in.readString();
        this.jproduct_time = in.readString();
        this.jproduct_ptype = in.readString();
        this.jproduct_stype = in.readString();
        this.jproduct_token = in.readString();
        this.jproduct_yprice = in.readString();
    }

    public static final Parcelable.Creator<IMallGoodsItemBean> CREATOR = new Parcelable.Creator<IMallGoodsItemBean>() {
        @Override
        public IMallGoodsItemBean createFromParcel(Parcel source) {
            return new IMallGoodsItemBean(source);
        }

        @Override
        public IMallGoodsItemBean[] newArray(int size) {
            return new IMallGoodsItemBean[size];
        }
    };
}
