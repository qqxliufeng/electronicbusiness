package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageBean
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_message_info_layout.*

/**
 * Created by lf on 2017/12/21 0021.
 * @author lf on 2017/12/21 0021
 */
class MessageInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val MESSAGE_BEAN_FLAG = "message_bean_flag"
    }

    private val messageBean: MessageBean by lazy {
        arguments.classLoader = this.javaClass.classLoader
        arguments.getParcelable<MessageBean>(MESSAGE_BEAN_FLAG)
    }

    override fun getLayoutId() = R.layout.fragment_message_info_layout

    override fun initView(view: View?) {
        mTvMessageInfoTitle.text = messageBean.message_title
        mTvMessageInfoTime.text = messageBean.message_time
        mTvMessageInfoContent.text = messageBean.message_content
    }
}