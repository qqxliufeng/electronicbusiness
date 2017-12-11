package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;

/**
 * Created by lf on 2017/12/11 0011.
 *
 * @author lf on 2017/12/11 0011
 */

public class SelectPayTypeView extends LinearLayout {

    public static final String WX_PAY = "wxpay";
    public static final String ALI_PAY = "alipay";

    private CheckBox cb_ali;
    private CheckBox cb_wx;
    private TextView tv_confirm;

    private OnConfirmClickListener onConfirmClickListener;

    public SelectPayTypeView(Context context) {
        this(context, null);
    }

    public SelectPayTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPayTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_pay_layout, null);
        cb_ali = contentView.findViewById(R.id.mCbALiPay);
        cb_wx = contentView.findViewById(R.id.mCbWX);
        tv_confirm = contentView.findViewById(R.id.mTvPayDialogConfirm);
        tv_confirm.setVisibility(GONE);
        contentView.findViewById(R.id.mRlSubmitOrderWXContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_wx.setChecked(true);
            }
        });
        contentView.findViewById(R.id.mRlSubmitOrderAliPayContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_ali.setChecked(true);
            }
        });
        cb_ali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cb_wx.setChecked(!isChecked);
            }
        });
        cb_wx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cb_ali.setChecked(!isChecked);
            }
        });
        addView(contentView);
    }

    public String getPayType() {
        if (cb_wx.isChecked()) {
            return WX_PAY;
        } else {
            return ALI_PAY;
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public void setShowConfirmView(int visible) {
        tv_confirm.setVisibility(visible);
        if (View.VISIBLE == visible) {
            tv_confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onConfirmClickListener != null) {
                        onConfirmClickListener.onConfirmClick();
                    }
                }
            });
        }
    }


    public interface OnConfirmClickListener {
        void onConfirmClick();
    }

}
