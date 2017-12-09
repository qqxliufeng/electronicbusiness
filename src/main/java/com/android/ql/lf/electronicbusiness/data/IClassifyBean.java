package com.android.ql.lf.electronicbusiness.data;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/1 0001.
 *
 * @author lf on 2017/12/1 0001
 */

public class IClassifyBean {

    private String jclassify_id;
    private String jclassify_title;
    private String jclassify_content;
    private String jclassify_pid;
    private String jclassify_path;
    private String jclassify_token;
    private String jclassify_url;
    private String jclassify_pic;
    private String jclassify_sort;
    private String jclassify_url1;
    private String jclassify_time;
    private String jclassify_sign;
    private ArrayList<IClassifySubItemBean> sub;

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getJclassify_id() {
        return jclassify_id;
    }

    public void setJclassify_id(String jclassify_id) {
        this.jclassify_id = jclassify_id;
    }

    public String getJclassify_title() {
        return jclassify_title;
    }

    public void setJclassify_title(String jclassify_title) {
        this.jclassify_title = jclassify_title;
    }

    public String getJclassify_content() {
        return jclassify_content;
    }

    public void setJclassify_content(String jclassify_content) {
        this.jclassify_content = jclassify_content;
    }

    public String getJclassify_pid() {
        return jclassify_pid;
    }

    public void setJclassify_pid(String jclassify_pid) {
        this.jclassify_pid = jclassify_pid;
    }

    public String getJclassify_path() {
        return jclassify_path;
    }

    public void setJclassify_path(String jclassify_path) {
        this.jclassify_path = jclassify_path;
    }

    public String getJclassify_token() {
        return jclassify_token;
    }

    public void setJclassify_token(String jclassify_token) {
        this.jclassify_token = jclassify_token;
    }

    public String getJclassify_url() {
        return jclassify_url;
    }

    public void setJclassify_url(String jclassify_url) {
        this.jclassify_url = jclassify_url;
    }

    public String getJclassify_pic() {
        return jclassify_pic;
    }

    public void setJclassify_pic(String jclassify_pic) {
        this.jclassify_pic = jclassify_pic;
    }

    public String getJclassify_sort() {
        return jclassify_sort;
    }

    public void setJclassify_sort(String jclassify_sort) {
        this.jclassify_sort = jclassify_sort;
    }

    public String getJclassify_url1() {
        return jclassify_url1;
    }

    public void setJclassify_url1(String jclassify_url1) {
        this.jclassify_url1 = jclassify_url1;
    }

    public String getJclassify_time() {
        return jclassify_time;
    }

    public void setJclassify_time(String jclassify_time) {
        this.jclassify_time = jclassify_time;
    }

    public String getJclassify_sign() {
        return jclassify_sign;
    }

    public void setJclassify_sign(String jclassify_sign) {
        this.jclassify_sign = jclassify_sign;
    }

    public ArrayList<IClassifySubItemBean> getSub() {
        return sub;
    }

    public void setSub(ArrayList<IClassifySubItemBean> sub) {
        this.sub = sub;
    }


    public static class IClassifySubItemBean {
        private String jclassify_id;
        private String jclassify_title;
        private String jclassify_content;
        private String jclassify_pid;
        private String jclassify_path;
        private String jclassify_token;
        private String jclassify_url;
        private String jclassify_pic;
        private String jclassify_sort;
        private String jclassify_url1;
        private String jclassify_time;
        private String jclassify_sign;

        public String getJclassify_id() {
            return jclassify_id;
        }

        public void setJclassify_id(String jclassify_id) {
            this.jclassify_id = jclassify_id;
        }

        public String getJclassify_title() {
            return jclassify_title;
        }

        public void setJclassify_title(String jclassify_title) {
            this.jclassify_title = jclassify_title;
        }

        public String getJclassify_content() {
            return jclassify_content;
        }

        public void setJclassify_content(String jclassify_content) {
            this.jclassify_content = jclassify_content;
        }

        public String getJclassify_pid() {
            return jclassify_pid;
        }

        public void setJclassify_pid(String jclassify_pid) {
            this.jclassify_pid = jclassify_pid;
        }

        public String getJclassify_path() {
            return jclassify_path;
        }

        public void setJclassify_path(String jclassify_path) {
            this.jclassify_path = jclassify_path;
        }

        public String getJclassify_token() {
            return jclassify_token;
        }

        public void setJclassify_token(String jclassify_token) {
            this.jclassify_token = jclassify_token;
        }

        public String getJclassify_url() {
            return jclassify_url;
        }

        public void setJclassify_url(String jclassify_url) {
            this.jclassify_url = jclassify_url;
        }

        public String getJclassify_pic() {
            return jclassify_pic;
        }

        public void setJclassify_pic(String jclassify_pic) {
            this.jclassify_pic = jclassify_pic;
        }

        public String getJclassify_sort() {
            return jclassify_sort;
        }

        public void setJclassify_sort(String jclassify_sort) {
            this.jclassify_sort = jclassify_sort;
        }

        public String getJclassify_url1() {
            return jclassify_url1;
        }

        public void setJclassify_url1(String jclassify_url1) {
            this.jclassify_url1 = jclassify_url1;
        }

        public String getJclassify_time() {
            return jclassify_time;
        }

        public void setJclassify_time(String jclassify_time) {
            this.jclassify_time = jclassify_time;
        }

        public String getJclassify_sign() {
            return jclassify_sign;
        }

        public void setJclassify_sign(String jclassify_sign) {
            this.jclassify_sign = jclassify_sign;
        }
    }
}