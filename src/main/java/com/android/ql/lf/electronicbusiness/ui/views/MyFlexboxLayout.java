package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.electronicbusiness.R;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/4 0004.
 *
 * @author lf on 2017/12/4 0004
 */

public class MyFlexboxLayout extends LinearLayout {

    private TextView tvTitle;

    private FlexboxLayout flexboxLayout;

    private OnItemClickListener onItemClickListener;

    public MyFlexboxLayout(Context context) {
        this(context, null);
    }

    public MyFlexboxLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFlexboxLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void init() {
        setOrientation(VERTICAL);
        tvTitle = new TextView(getContext());
        tvTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        tvTitle.setPadding(padding, padding, padding, padding);
        tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.black_tv_color));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sp_16));
        tvTitle.setText("标题");
        addView(tvTitle);

        flexboxLayout = new FlexboxLayout(getContext());
        flexboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void addItems(final ArrayList<String> items) {
        if (items != null && !items.isEmpty()) {
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            for (final String item : items) {
                final CheckedTextView tv = new CheckedTextView(flexboxLayout.getContext());
                tv.setText(item);
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(margin, margin, margin, margin);
                tv.setLayoutParams(params);
                tv.setBackgroundResource(R.drawable.selector_tv_bg1);
                tv.setTextColor(ContextCompat.getColor(getContext(),R.color.select_tv_color1));
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv.isChecked()) {
                            tv.setChecked(false);
                        } else {
                            if (onItemClickListener != null) {
                                for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
                                    CheckedTextView childAt = (CheckedTextView) flexboxLayout.getChildAt(i);
                                    childAt.setChecked(childAt == tv);
                                }
                                tv.setChecked(true);
                                onItemClickListener.onItemClick(items.indexOf(item));
                            }
                        }
                    }
                });
                flexboxLayout.addView(tv);
            }
            addView(flexboxLayout);
        }
    }

    public TextView getTitleView() {
        return tvTitle;
    }


    public interface OnItemClickListener {
        public void onItemClick(int index);
    }

}
