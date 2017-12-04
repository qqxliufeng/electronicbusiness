package com.android.ql.lf.electronicbusiness.data;

import java.util.ArrayList;

/**
 * Created by liufeng on 2017/12/4.
 */

public class SpecificationBean {

    private String  name;
    private ArrayList<String> item;
    private ArrayList<String> pic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItem() {
        return item;
    }

    public void setItem(ArrayList<String> item) {
        this.item = item;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }
}
