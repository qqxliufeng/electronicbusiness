package com.android.ql.lf.electronicbusiness.data;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/6 0006.
 *
 * @author lf on 2017/12/6 0006
 */

public class GoodsItemBean {

    private String product_id;
    private ArrayList<String> product_pic;
    private String product_price;
    private String product_minus;
    private String product_knum;
    private String product_time;
    private String product_sv;
    private String product_name;
    private String product_ms;
    private String product_content;
    private String product_endstatus; //1 活动结束 0 活动继续
    private String product_ptype; // 1  已经砍到最底价  0 未到最底价

    public String getProduct_endstatus() {
        return product_endstatus;
    }

    public void setProduct_endstatus(String product_endstatus) {
        this.product_endstatus = product_endstatus;
    }

    public String getProduct_ptype() {
        return product_ptype;
    }

    public void setProduct_ptype(String product_ptype) {
        this.product_ptype = product_ptype;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public ArrayList<String> getProduct_pic() {
        return product_pic;
    }

    public void setProduct_pic(ArrayList<String> product_pic) {
        this.product_pic = product_pic;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_minus() {
        return product_minus;
    }

    public void setProduct_minus(String product_minus) {
        this.product_minus = product_minus;
    }

    public String getProduct_knum() {
        return product_knum;
    }

    public void setProduct_knum(String product_knum) {
        this.product_knum = product_knum;
    }

    public String getProduct_time() {
        return product_time;
    }

    public void setProduct_time(String product_time) {
        this.product_time = product_time;
    }

    public String getProduct_sv() {
        return product_sv;
    }

    public void setProduct_sv(String product_sv) {
        this.product_sv = product_sv;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_ms() {
        return product_ms;
    }

    public void setProduct_ms(String product_ms) {
        this.product_ms = product_ms;
    }

    public String getProduct_content() {
        return product_content;
    }

    public void setProduct_content(String product_content) {
        this.product_content = product_content;
    }
}
