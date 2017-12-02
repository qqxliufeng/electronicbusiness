package com.android.ql.lf.electronicbusiness.utils

import android.os.CountDownTimer

/**
 * Created by lf on 2017/11/28 0028.
 * @author lf on 2017/11/28 0028
 */
class CounterHelper(private var millisInFuture: Long = 60 * 1000, private var countDownInterval: Long = 1000) {

    var onFinish: (() -> Unit)? = null
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null

    private val counterTimer = object : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            cancel()
            this@CounterHelper.onFinish?.invoke()
        }

        override fun onTick(millisUntilFinished: Long) {
            this@CounterHelper.onTick?.invoke(millisUntilFinished)
        }
    }

    fun start() {
        counterTimer.start()
    }

    fun stop() {
        counterTimer.cancel()
    }


}