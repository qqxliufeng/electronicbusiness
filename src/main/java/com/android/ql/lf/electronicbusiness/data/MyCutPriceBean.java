package com.android.ql.lf.electronicbusiness.data;

import java.util.List;

/**
 * Created by lf on 2017/12/13 0013.
 *
 * @author lf on 2017/12/13 0013
 */

public class MyCutPriceBean {

    private String product_name;
    private String product_price;
    private String kprice_id;
    private Object kprice_time;
    private String kprice_uid;
    private List<String> product_pic;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getKprice_id() {
        return kprice_id;
    }

    public void setKprice_id(String kprice_id) {
        this.kprice_id = kprice_id;
    }

    public Object getKprice_time() {
        return kprice_time;
    }

    public void setKprice_time(Object kprice_time) {
        this.kprice_time = kprice_time;
    }

    public String getKprice_uid() {
        return kprice_uid;
    }

    public void setKprice_uid(String kprice_uid) {
        this.kprice_uid = kprice_uid;
    }

    public List<String> getProduct_pic() {
        return product_pic;
    }

    public void setProduct_pic(List<String> product_pic) {
        this.product_pic = product_pic;
    }
}
