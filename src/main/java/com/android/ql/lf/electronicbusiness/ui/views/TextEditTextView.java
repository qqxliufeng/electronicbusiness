package com.android.ql.lf.electronicbusiness.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import org.jetbrains.anko.ToastsKt;

/**
 * @author Administrator
 */
public class TextEditTextView extends AppCompatEditText {
    public TextEditTextView(Context context) {
        super(context);
    }

    public TextEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.e("TAG", "键盘向下");
        super.onKeyPreIme(keyCode, event);
        return super.onKeyPreIme(keyCode, event);
    }
}