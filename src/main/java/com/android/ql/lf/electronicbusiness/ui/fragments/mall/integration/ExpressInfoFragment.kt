package com.android.ql.lf.electronicbusiness.ui.fragments.mall.integration

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_express_info_layout.*

/**
 * Created by lf on 2017/11/4 0004.
 * @author lf on 2017/11/4 0004
 */
class ExpressInfoFragment : BaseNetWorkingFragment(){

    private val stepList = mutableListOf<String>()

    override fun getLayoutId(): Int = R.layout.fragment_express_info_layout

    override fun initView(view: View?) {
        stepList.add("您已提交定单，等待系统确认\n2017-1-1 10:30")
        stepList.add("已发货\n商品已出库\n" +
                "2017-1-1 10:30")
        stepList.add("运输中\n商品到达济南市\n" +
                "2017-1-1 10:30")
        stepList.add("派件中 王先生正在派件王先生正在派件王先生正在派件王先生正在派件王先生正在派件\n" +
                "2017-1-1 10:30")
        stepList.add("已签收\n" +
                "2017-1-1 10:30")

        mVerticalStepView.setStepsViewIndicatorComplectingPosition(stepList.size - 2)//设置完成的步数
                .setTextSize(12)
                .setStepViewTexts(stepList)//总步骤
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#E6E6E6"))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#E6E6E6"))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(Color.parseColor("#666666"))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(mContext, R.drawable.default_icon))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(mContext, R.drawable.my_complted_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(mContext, R.drawable.default_icon))//设置StepsViewIndicator AttentionIcon
                .setLinePaddingProportion(2.0f)
    }
}