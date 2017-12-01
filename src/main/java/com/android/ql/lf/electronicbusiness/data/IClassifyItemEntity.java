package com.android.ql.lf.electronicbusiness.data;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by lf on 2017/12/1 0001.
 *
 * @author lf on 2017/12/1 0001
 */

public class IClassifyItemEntity extends SectionEntity<IClassifyBean.IClassifySubItemBean>{

    public IClassifyItemEntity(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public IClassifyItemEntity(IClassifyBean.IClassifySubItemBean iClassifySubItemBean) {
        super(iClassifySubItemBean);
    }

}
