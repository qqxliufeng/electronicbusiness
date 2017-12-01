package com.android.ql.lf.electronicbusiness.data;

/**
 * Created by lf on 2017/11/30 0030.
 *
 * @author lf on 2017/11/30 0030
 */

public class TabItemBean {
    //积分中的Tab字段
    private String jclassify_id;
    private String jclassify_title;

    //砍价 专享Tab的字段
    private String classify_id;
    private String classify_title;

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getClassify_title() {
        return classify_title;
    }

    public void setClassify_title(String classify_title) {
        this.classify_title = classify_title;
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
}
