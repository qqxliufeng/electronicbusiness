package com.android.ql.lf.electronicbusiness.data

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class CodeBean {
    var code: String = ""
    var status: String = ""
    var time: String = ""
    var data: Data? = null

    public class Data {
        var returnstatus: String = ""
        var message: String = ""
        var remainpoint: String = ""
        var taskID: String = ""
        var successCounts: String = ""
    }

}