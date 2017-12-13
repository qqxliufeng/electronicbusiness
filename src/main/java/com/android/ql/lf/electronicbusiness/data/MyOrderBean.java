package com.android.ql.lf.electronicbusiness.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by liufeng on 2017/12/2.
 */

public class MyOrderBean implements Parcelable {
    private String product_id;
    private String product_name;
    private String product_md;
    private ArrayList<String> product_pic;
    private String product_hname;
    private String product_price;
    private String product_ktype;
    private String product_mdprice;
    private String product_endstatus;
    private String order_specification;
    private String order_num;
    private String order_sn;
    private String order_oprice;
    private String order_fc;
    private String order_token;
    private String order_id;
    private String order_ctime;
    private String order_ftime;
    private String order_htime;
    private String order_fintime;
    private String order_tn;
    private String order_mdprice;

    public String getOrder_mdprice() {
        return order_mdprice;
    }

    public void setOrder_mdprice(String order_mdprice) {
        this.order_mdprice = order_mdprice;
    }

    public String getProduct_endstatus() {
        return product_endstatus;
    }

    public void setProduct_endstatus(String product_endstatus) {
        this.product_endstatus = product_endstatus;
    }

    public String getOrder_tn() {
        return order_tn;
    }

    public void setOrder_tn(String order_tn) {
        this.order_tn = order_tn;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_md() {
        return product_md;
    }

    public void setProduct_md(String product_md) {
        this.product_md = product_md;
    }

    public ArrayList<String> getProduct_pic() {
        return product_pic;
    }

    public void setProduct_pic(ArrayList<String> product_pic) {
        this.product_pic = product_pic;
    }

    public String getProduct_hname() {
        return product_hname;
    }

    public void setProduct_hname(String product_hname) {
        this.product_hname = product_hname;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_ktype() {
        return product_ktype;
    }

    public void setProduct_ktype(String product_ktype) {
        this.product_ktype = product_ktype;
    }

    public String getProduct_mdprice() {
        return product_mdprice;
    }

    public void setProduct_mdprice(String product_mdprice) {
        this.product_mdprice = product_mdprice;
    }

    public String getOrder_specification() {
        return order_specification;
    }

    public void setOrder_specification(String order_specification) {
        this.order_specification = order_specification;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_oprice() {
        return order_oprice;
    }

    public void setOrder_oprice(String order_oprice) {
        this.order_oprice = order_oprice;
    }

    public String getOrder_fc() {
        return order_fc;
    }

    public void setOrder_fc(String order_fc) {
        this.order_fc = order_fc;
    }

    public String getOrder_token() {
        return order_token;
    }

    public void setOrder_token(String order_token) {
        this.order_token = order_token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_ctime() {
        return order_ctime;
    }

    public void setOrder_ctime(String order_ctime) {
        this.order_ctime = order_ctime;
    }

    public String getOrder_ftime() {
        return order_ftime;
    }

    public void setOrder_ftime(String order_ftime) {
        this.order_ftime = order_ftime;
    }

    public String getOrder_htime() {
        return order_htime;
    }

    public void setOrder_htime(String order_htime) {
        this.order_htime = order_htime;
    }

    public String getOrder_fintime() {
        return order_fintime;
    }

    public void setOrder_fintime(String order_fintime) {
        this.order_fintime = order_fintime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.product_name);
        dest.writeString(this.product_md);
        dest.writeStringList(this.product_pic);
        dest.writeString(this.product_hname);
        dest.writeString(this.product_price);
        dest.writeString(this.product_ktype);
        dest.writeString(this.product_mdprice);
        dest.writeString(this.product_endstatus);
        dest.writeString(this.order_specification);
        dest.writeString(this.order_num);
        dest.writeString(this.order_sn);
        dest.writeString(this.order_oprice);
        dest.writeString(this.order_fc);
        dest.writeString(this.order_token);
        dest.writeString(this.order_id);
        dest.writeString(this.order_ctime);
        dest.writeString(this.order_ftime);
        dest.writeString(this.order_htime);
        dest.writeString(this.order_fintime);
        dest.writeString(this.order_tn);
    }

    public MyOrderBean() {
    }

    protected MyOrderBean(Parcel in) {
        this.product_id = in.readString();
        this.product_name = in.readString();
        this.product_md = in.readString();
        this.product_pic = in.createStringArrayList();
        this.product_hname = in.readString();
        this.product_price = in.readString();
        this.product_ktype = in.readString();
        this.product_mdprice = in.readString();
        this.product_endstatus = in.readString();
        this.order_specification = in.readString();
        this.order_num = in.readString();
        this.order_sn = in.readString();
        this.order_oprice = in.readString();
        this.order_fc = in.readString();
        this.order_token = in.readString();
        this.order_id = in.readString();
        this.order_ctime = in.readString();
        this.order_ftime = in.readString();
        this.order_htime = in.readString();
        this.order_fintime = in.readString();
        this.order_tn = in.readString();
    }

    public static final Parcelable.Creator<MyOrderBean> CREATOR = new Parcelable.Creator<MyOrderBean>() {
        @Override
        public MyOrderBean createFromParcel(Parcel source) {
            return new MyOrderBean(source);
        }

        @Override
        public MyOrderBean[] newArray(int size) {
            return new MyOrderBean[size];
        }
    };
}
