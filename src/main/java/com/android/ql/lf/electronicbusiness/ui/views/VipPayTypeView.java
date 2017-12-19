package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.android.ql.lf.electronicbusiness.data.VipSelectPayTimeBean;

/**
 * Created by lf on 2017/12/19 0019.
 *
 * @author lf on 2017/12/19 0019
 */

public class VipPayTypeView extends LinearLayout {

    private LinearLayout ll_container;
    private TextView tv_title;
    private TextView tv_des;
    private TextView tv_price;
    private TextView tv_old_price;

    private VipSelectPayTimeBean vipSelectPayTimeBean;


    public VipPayTypeView(Context context) {
        this(context, null);
    }

    public VipPayTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VipPayTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
        View contentView = View.inflate(getContext(), R.layout.layout_vip_select_type_layout, null);
        ll_container = contentView.findViewById(R.id.mLlVipInfoSelectPayTime);
        tv_title = contentView.findViewById(R.id.mTvVipSelectTypeTitle);
        tv_des = contentView.findViewById(R.id.mTvVipSelectTypeDes);
        tv_price = contentView.findViewById(R.id.mTvVipSelectTypePrice);
        tv_old_price = contentView.findViewById(R.id.mTvVipSelectTypeOldPrice);
        tv_old_price.getPaint().setFlags(TextPaint.ANTI_ALIAS_FLAG);
        tv_old_price.getPaint().setFlags(TextPaint.STRIKE_THRU_TEXT_FLAG);
        addView(contentView);
    }

    public void bindData(VipSelectPayTimeBean vipSelectPayTimeBean) {
        if (vipSelectPayTimeBean != null) {
            this.vipSelectPayTimeBean = vipSelectPayTimeBean;
            tv_title.setText(vipSelectPayTimeBean.getM_p_name());
            tv_des.setText(vipSelectPayTimeBean.getM_p_content());
            tv_price.setText(vipSelectPayTimeBean.getM_p_price() + "元");
            tv_old_price.setText("原价:" + vipSelectPayTimeBean.getM_p_yprice());
        }
    }

    public VipSelectPayTimeBean getVipSelectPayTimeBean() {
        return vipSelectPayTimeBean;
    }

    public void onSelect(boolean isChecked){
        if (isChecked){
            ll_container.setBackgroundResource(R.drawable.img_vip_bottom_select);
        }else {
            ll_container.setBackgroundResource(R.drawable.img_vip_bottom);
        }
    }


}
