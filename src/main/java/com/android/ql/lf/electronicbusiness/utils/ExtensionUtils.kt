package com.android.ql.lf.electronicbusiness.utils

import android.content.Context

/**
 * Created by liufeng on 2017/12/11.
 */

fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun ArrayList<String>.getFirstPic(): String {
    return if (isEmpty()) "" else get(0)
}
