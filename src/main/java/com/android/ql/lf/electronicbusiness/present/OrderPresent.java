package com.android.ql.lf.electronicbusiness.present;

import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper;

/**
 * Created by lf on 2017/12/9 0009.
 *
 * @author lf on 2017/12/9 0009
 */

public class OrderPresent {

    private GetDataFromNetPresent present;

    public OrderPresent(GetDataFromNetPresent present) {
        this.present = present;
    }

    /**
     * 取消订单
     */
    public void cancelOrder(int requestId, String orderId) {
        present.getDataByPost(requestId,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_EDIT_ORDER_STATUS(),
                RequestParamsHelper.Companion.getEditOrderStatusParam(orderId, "4"));
    }

    /**
     * 支付
     */
    public void payOrder() {
    }

    /**
     * 删除订单
     */
    public void deleteOrder(int requestId, String orderId) {
        present.getDataByPost(requestId,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_DEL_DETAIL(),
                RequestParamsHelper.Companion.getDelDetailParam(orderId));
    }

    /**
     * 查看订单快递
     */
    public void expressOrder() {
    }

    /**
     * 通知卖家发货
     */
    public void notifySendOrder() {
    }

    /**
     * 确认订单
     */
    public void confirmOrder() {
    }

    public void destroy() {
        present = null;
    }

}
