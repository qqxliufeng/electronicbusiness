package com.android.ql.lf.electronicbusiness.ui.activities

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.MessageInfo
import com.android.ql.lf.electronicbusiness.ui.adapters.im.ChatAdapter
import com.android.ql.lf.electronicbusiness.ui.adapters.im.CommonFragmentPagerAdapter
import com.android.ql.lf.electronicbusiness.ui.views.EmotionInputDetector
import com.android.ql.lf.electronicbusiness.utils.im.Constants
import kotlinx.android.synthetic.main.chat_activity_layout.*
import kotlinx.android.synthetic.main.im_include_reply_layout.*
import rx.Subscription
import com.android.ql.lf.electronicbusiness.ui.fragments.im.ChatFunctionFragment
import com.android.ql.lf.electronicbusiness.ui.fragments.im.ChatEmotionFragment
import com.android.ql.lf.electronicbusiness.utils.RxBus
import com.android.ql.lf.electronicbusiness.utils.im.GlobalOnItemClickManagerUtils
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import com.android.ql.lf.electronicbusiness.utils.im.MediaManager


/**
 * Created by lf on 2017/11/16 0016.
 * @author lf on 2017/11/16 0016
 */
class ChatActivity : BaseActivity() {

    private var mDetector: EmotionInputDetector? = null
    private var chatAdapter: ChatAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var messageInfos: ArrayList<MessageInfo>

    private lateinit var chatEmotionFragment: ChatEmotionFragment
    private lateinit var chatFunctionFragment: ChatFunctionFragment
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var commonFragmentAdapter: CommonFragmentPagerAdapter

    private lateinit var subscription: Subscription

    //录音相关
    var animationRes = 0
    var res = 0
    private lateinit var animationDrawable: AnimationDrawable
    private var animView: ImageView? = null

    override fun getLayoutId() = R.layout.chat_activity_layout

    override fun initView() {
        subscription = RxBus.getDefault().toObservable(MessageInfo::class.java).subscribe { messageInfo ->
            messageInfo.header = "http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg"
            messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT)
            messageInfo.sendState = Constants.CHAT_ITEM_SENDING
            messageInfos.add(messageInfo)
            mChatList.scrollToPosition(chatAdapter!!.itemCount - 1)
            Handler().postDelayed({
                messageInfo.sendState = Constants.CHAT_ITEM_SEND_SUCCESS
                chatAdapter!!.notifyDataSetChanged()
            }, 2000)
        }
        initWidget()
    }

    private fun initWidget() {
        messageInfos = ArrayList()
        (0..10).forEach {
            val messageInfo = MessageInfo()
            if (it % 2 == 0) {
                messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT)
            } else {
                messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT)
            }
            messageInfo.content = "item $it"
            messageInfos?.add(messageInfo)
        }
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatAdapter = ChatAdapter(messageInfos)
        mChatList.layoutManager = layoutManager
        mChatList.adapter = chatAdapter
        chatEmotionFragment = ChatEmotionFragment()
        chatFunctionFragment = ChatFunctionFragment()
        fragments = ArrayList()
        fragments.add(chatEmotionFragment)
        fragments.add(chatFunctionFragment)
        commonFragmentAdapter = CommonFragmentPagerAdapter(supportFragmentManager, fragments)
        mIMViewpager.adapter = commonFragmentAdapter
        mIMViewpager.currentItem = 0
        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(mIMEmotionLayout)
                .setViewPager(mIMViewpager)
                .bindToContent(mChatList)
                .bindToEditText(mIMEditText)
                .bindToEmotionButton(mIMEmotionButton)
                .bindToAddButton(mIMEmotionAdd)
                .bindToSendButton(mIMSend)
                .bindToVoiceButton(mIMEmotionVoice)
                .bindToVoiceText(mIMVoiceText)
                .build()
        val globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this)
        globalOnItemClickListener.attachToEditText(mIMEditText)
        mChatList.smoothScrollToPosition(messageInfos.size - 1)
        mChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        chatAdapter!!.notifyDataSetChanged()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        mDetector!!.hideEmotionLayout(false)
                        mDetector!!.hideSoftInput()
                    }
                    else -> {
                    }
                }
            }
        })
        mChatList.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (view?.id) {
                    R.id.chat_item_layout_content -> {

                    }
                    R.id.chat_item_layout_content_send -> {
                        val imageViewVoice = view.findViewById<ImageView>(R.id.chat_item_voice_send)
                        if (animView != null) {
                            animView!!.setImageResource(res)
                            animView = null
                        }
//                        when (messageInfos[position].getType()) {
//                            1 -> {
//                                animationRes = R.drawable.voice_left
//                                res = R.mipmap.icon_voice_left3
//                            }
//                            2 -> {
//
//                            }
//                        }
                        animationRes = R.drawable.voice_right
                        res = R.mipmap.icon_voice_right3
                        animView = imageViewVoice
                        animView!!.setImageResource(animationRes)
                        animationDrawable = imageViewVoice.drawable as AnimationDrawable
                        animationDrawable.start()
                        MediaManager.playSound(messageInfos[position].filepath) { animView!!.setImageResource(res) }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (!mDetector!!.interceptBackPress()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

}