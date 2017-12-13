package com.android.ql.lf.electronicbusiness.data;

import java.util.List;

/**
 * Created by liufeng on 2017/12/13.
 */

public class IntegrationExChangeRecordBean {

    private String jproduct_id;
    private String jproduct_name;
    private String jproduct_price;
    private String order_id;
    private String order_oprice;
    private String order_num;
    private String order_ctime;
    private List<String> jproduct_pic;

    public String getJproduct_id() {
        return jproduct_id;
    }

    public void setJproduct_id(String jproduct_id) {
        this.jproduct_id = jproduct_id;
    }

    public String getJproduct_name() {
        return jproduct_name;
    }

    public void setJproduct_name(String jproduct_name) {
        this.jproduct_name = jproduct_name;
    }

    public String getJproduct_price() {
        return jproduct_price;
    }

    public void setJproduct_price(String jproduct_price) {
        this.jproduct_price = jproduct_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_oprice() {
        return order_oprice;
    }

    public void setOrder_oprice(String order_oprice) {
        this.order_oprice = order_oprice;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_ctime() {
        return order_ctime;
    }

    public void setOrder_ctime(String order_ctime) {
        this.order_ctime = order_ctime;
    }

    public List<String> getJproduct_pic() {
        return jproduct_pic;
    }

    public void setJproduct_pic(List<String> jproduct_pic) {
        this.jproduct_pic = jproduct_pic;
    }
}
