package com.android.ql.lf.electronicbusiness.data;

/**
 * Created by lf on 2017/12/6 0006.
 *
 * @author lf on 2017/12/6 0006
 */

public class OrderBean {
    private String gid;
    private String cid;
    private String price;
    private String address;
    private String num;
    private String ktype;
    private String mdtype;
    private String mliuyan;
    private String specification;
    private String mdprice;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBbs() {
        return bbs;
    }

    public void setBbs(String bbs) {
        this.bbs = bbs;
    }

    private String bbs;

    public String getMdprice() {
        return mdprice;
    }

    public void setMdprice(String mdprice) {
        this.mdprice = mdprice;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getKtype() {
        return ktype;
    }

    public void setKtype(String ktype) {
        this.ktype = ktype;
    }

    public String getMdtype() {
        return mdtype;
    }

    public void setMdtype(String mdtype) {
        this.mdtype = mdtype;
    }

    public String getMliuyan() {
        return mliuyan;
    }

    public void setMliuyan(String mliuyan) {
        this.mliuyan = mliuyan;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
