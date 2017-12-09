package com.android.ql.lf.electronicbusiness.utils

import android.text.TextUtils
import java.util.regex.Pattern


/**
 * Created by lf on 2017/11/25 0025.
 * @author lf on 2017/11/25 0025
 */
fun String.replaceBlank(): String {
    var des = ""
    if (!TextUtils.isEmpty(this)) {
        val p = Pattern.compile("\\s*|\t|\r|\n")
        val m = p.matcher(this)
        des = m.replaceAll("")
    }
    return des
}