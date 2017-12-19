package com.android.ql.lf.electronicbusiness.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufeng on 2017/12/19.
 */

public class VipGoodsInfoBean {

    private String code;
    private String msg;
    private ResultBean result;
    private ArrBean arr;
    private String arr1;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public ArrBean getArr() {
        return arr;
    }

    public void setArr(ArrBean arr) {
        this.arr = arr;
    }

    public String getArr1() {
        return arr1;
    }

    public void setArr1(String arr1) {
        this.arr1 = arr1;
    }

    public static class ResultBean {

        private DetailBean detail;
        private String kprice;
        private String nextprice;
        private String resnum;
        private String endtime;
        private ShareBean share;
        private List<KindBean> kind;

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public String getKprice() {
            return kprice;
        }

        public void setKprice(String kprice) {
            this.kprice = kprice;
        }

        public String getNextprice() {
            return nextprice;
        }

        public void setNextprice(String nextprice) {
            this.nextprice = nextprice;
        }

        public String getResnum() {
            return resnum;
        }

        public void setResnum(String resnum) {
            this.resnum = resnum;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public List<KindBean> getKind() {
            return kind;
        }

        public void setKind(List<KindBean> kind) {
            this.kind = kind;
        }

        public static class DetailBean {
            private String product_id;
            private String product_stype;
            private String product_ptype;
            private String product_type;
            private String product_content;
            private String product_name;
            private String product_price;
            private String product_endtime;
            private String product_minus;
            private String product_batch;
            private String product_address;
            private String product_yprice;
            private String product_title;
            private String product_ms;
            private String product_endprice;
            private String product_token;
            private String product_time;
            private String product_ktype;
            private String product_dknum;
            private String product_hname;
            private String product_entrepot;
            private String product_knum;
            private String product_stoken;
            private String product_colloct;
            private String product_orderid;
            private String product_sv;
            private String product_video;
            private String product_vurl;
            private String product_md;
            private String product_mdprice;
            private String product_bprice;
            private String product_jstatus;
            private String product_endstatus;
            private String product_jtoken;
            private String product_integral;
            private ArrayList<String> product_pic;
            private ArrayList<SpecificationBean> product_specification;

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getProduct_stype() {
                return product_stype;
            }

            public void setProduct_stype(String product_stype) {
                this.product_stype = product_stype;
            }

            public String getProduct_ptype() {
                return product_ptype;
            }

            public void setProduct_ptype(String product_ptype) {
                this.product_ptype = product_ptype;
            }

            public String getProduct_type() {
                return product_type;
            }

            public void setProduct_type(String product_type) {
                this.product_type = product_type;
            }

            public String getProduct_content() {
                return product_content;
            }

            public void setProduct_content(String product_content) {
                this.product_content = product_content;
            }

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

            public String getProduct_endtime() {
                return product_endtime;
            }

            public void setProduct_endtime(String product_endtime) {
                this.product_endtime = product_endtime;
            }

            public String getProduct_minus() {
                return product_minus;
            }

            public void setProduct_minus(String product_minus) {
                this.product_minus = product_minus;
            }

            public String getProduct_batch() {
                return product_batch;
            }

            public void setProduct_batch(String product_batch) {
                this.product_batch = product_batch;
            }

            public String getProduct_address() {
                return product_address;
            }

            public void setProduct_address(String product_address) {
                this.product_address = product_address;
            }

            public String getProduct_yprice() {
                return product_yprice;
            }

            public void setProduct_yprice(String product_yprice) {
                this.product_yprice = product_yprice;
            }

            public String getProduct_title() {
                return product_title;
            }

            public void setProduct_title(String product_title) {
                this.product_title = product_title;
            }

            public String getProduct_ms() {
                return product_ms;
            }

            public void setProduct_ms(String product_ms) {
                this.product_ms = product_ms;
            }

            public String getProduct_endprice() {
                return product_endprice;
            }

            public void setProduct_endprice(String product_endprice) {
                this.product_endprice = product_endprice;
            }

            public String getProduct_token() {
                return product_token;
            }

            public void setProduct_token(String product_token) {
                this.product_token = product_token;
            }

            public String getProduct_time() {
                return product_time;
            }

            public void setProduct_time(String product_time) {
                this.product_time = product_time;
            }

            public String getProduct_ktype() {
                return product_ktype;
            }

            public void setProduct_ktype(String product_ktype) {
                this.product_ktype = product_ktype;
            }

            public String getProduct_dknum() {
                return product_dknum;
            }

            public void setProduct_dknum(String product_dknum) {
                this.product_dknum = product_dknum;
            }

            public String getProduct_hname() {
                return product_hname;
            }

            public void setProduct_hname(String product_hname) {
                this.product_hname = product_hname;
            }

            public String getProduct_entrepot() {
                return product_entrepot;
            }

            public void setProduct_entrepot(String product_entrepot) {
                this.product_entrepot = product_entrepot;
            }

            public String getProduct_knum() {
                return product_knum;
            }

            public void setProduct_knum(String product_knum) {
                this.product_knum = product_knum;
            }

            public String getProduct_stoken() {
                return product_stoken;
            }

            public void setProduct_stoken(String product_stoken) {
                this.product_stoken = product_stoken;
            }

            public String getProduct_colloct() {
                return product_colloct;
            }

            public void setProduct_colloct(String product_colloct) {
                this.product_colloct = product_colloct;
            }

            public String getProduct_orderid() {
                return product_orderid;
            }

            public void setProduct_orderid(String product_orderid) {
                this.product_orderid = product_orderid;
            }

            public String getProduct_sv() {
                return product_sv;
            }

            public void setProduct_sv(String product_sv) {
                this.product_sv = product_sv;
            }

            public String getProduct_video() {
                return product_video;
            }

            public void setProduct_video(String product_video) {
                this.product_video = product_video;
            }

            public String getProduct_vurl() {
                return product_vurl;
            }

            public void setProduct_vurl(String product_vurl) {
                this.product_vurl = product_vurl;
            }

            public String getProduct_md() {
                return product_md;
            }

            public void setProduct_md(String product_md) {
                this.product_md = product_md;
            }

            public String getProduct_mdprice() {
                return product_mdprice;
            }

            public void setProduct_mdprice(String product_mdprice) {
                this.product_mdprice = product_mdprice;
            }

            public String getProduct_bprice() {
                return product_bprice;
            }

            public void setProduct_bprice(String product_bprice) {
                this.product_bprice = product_bprice;
            }

            public String getProduct_jstatus() {
                return product_jstatus;
            }

            public void setProduct_jstatus(String product_jstatus) {
                this.product_jstatus = product_jstatus;
            }

            public String getProduct_endstatus() {
                return product_endstatus;
            }

            public void setProduct_endstatus(String product_endstatus) {
                this.product_endstatus = product_endstatus;
            }

            public String getProduct_jtoken() {
                return product_jtoken;
            }

            public void setProduct_jtoken(String product_jtoken) {
                this.product_jtoken = product_jtoken;
            }

            public String getProduct_integral() {
                return product_integral;
            }

            public void setProduct_integral(String product_integral) {
                this.product_integral = product_integral;
            }

            public ArrayList<String> getProduct_pic() {
                return product_pic;
            }

            public void setProduct_pic(ArrayList<String> product_pic) {
                this.product_pic = product_pic;
            }

            public ArrayList<SpecificationBean> getProduct_specification() {
                return product_specification;
            }

            public void setProduct_specification(ArrayList<SpecificationBean> product_specification) {
                this.product_specification = product_specification;
            }
        }

        public static class ShareBean {
            /**
             * url : /index.php/home/index/share?gid=32
             * pic : /Public/Uploads/images/2017-12-19/small_5a387bb706f7a.jpg
             * ms : <p>妈妈棉衣短款中老年羽绒棉服女40岁到50岁加厚外套中年人新款冬中长</p>
             * title : 中老年羽绒40岁加厚棉服新款
             */

            private String url;
            private String pic;
            private String ms;
            private String title;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class KindBean {

            private String product_id;
            private String product_name;
            private String product_price;
            private String product_minus;
            private String product_knum;
            private String product_time;
            private List<String> product_pic;

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

            public List<String> getProduct_pic() {
                return product_pic;
            }

            public void setProduct_pic(List<String> product_pic) {
                this.product_pic = product_pic;
            }
        }
    }

    public static class ArrBean {
        private String count;
        private List<CommentForGoodsBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<CommentForGoodsBean> getList() {
            return list;
        }

        public void setList(List<CommentForGoodsBean> list) {
            this.list = list;
        }
    }
}
