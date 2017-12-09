package com.android.ql.lf.electronicbusiness.data;

import java.util.List;

/**
 * Created by lf on 2017/12/9 0009.
 *
 * @author lf on 2017/12/9 0009
 */

public class CreateOrderSuccessBean {

    private String price;
    private String osn;
    private String paytime;
    private List<POrderBean> p_order;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOsn() {
        return osn;
    }

    public void setOsn(String osn) {
        this.osn = osn;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public List<POrderBean> getP_order() {
        return p_order;
    }

    public void setP_order(List<POrderBean> p_order) {
        this.p_order = p_order;
    }

    public static class POrderBean {

        private String order_gid;
        private String name;
        private String ktype;

        public String getOrder_gid() {
            return order_gid;
        }

        public void setOrder_gid(String order_gid) {
            this.order_gid = order_gid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKtype() {
            return ktype;
        }

        public void setKtype(String ktype) {
            this.ktype = ktype;
        }
    }
}
