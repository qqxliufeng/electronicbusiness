package com.android.ql.lf.electronicbusiness.data

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by lf on 2017/11/10 0010.
 *
 * @author lf on 2017/11/10 0010
 */

class IndexAskInfoBean : MultiItemEntity {

    companion object {
        val LARGE_IMAGE = 0
        val MULTI_IMAGE = 1
    }

    override fun getItemType(): Int = if (quiz_pic.size > 1) MULTI_IMAGE else LARGE_IMAGE

    lateinit var quiz_uid: String
    lateinit var quiz_id: String
    lateinit var quiz_title: String
    lateinit var quiz_token: String
    lateinit var quiz_type: ArrayList<String>
    var quiz_click: String? = null
    lateinit var quiz_content: String
    lateinit var quiz_time: String
    lateinit var quiz_num: String
    lateinit var quiz_pic: ArrayList<String>
    lateinit var quiz_concerm:String

    var isExpand:Boolean = false
    var maxLines = 3

}
