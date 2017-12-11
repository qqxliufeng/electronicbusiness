package com.android.ql.lf.electronicbusiness.data;

/**
 * Created by lf on 2017/12/11 0011.
 *
 * @author lf on 2017/12/11 0011
 */

public class SelectImageItemBean {

    private String path;
    private int resId;

    public SelectImageItemBean(String path, int resId) {
        this.path = path;
        this.resId = resId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
