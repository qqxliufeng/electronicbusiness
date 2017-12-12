package com.android.ql.lf.electronicbusiness.present;

import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper;
import com.android.ql.lf.electronicbusiness.data.RefreshData;
import com.android.ql.lf.electronicbusiness.ui.fragments.main.MainMineFragment;
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper;
import com.android.ql.lf.electronicbusiness.utils.RxBus;

/**
 * Created by lf on 2017/12/9 0009.
 *
 * @author lf on 2017/12/9 0009
 */

public class OrderPresent {

    // 0 待付款 1 待发货 2 待收货 3 待评价 4 完成 5 已取消 6 已退款
    public static class OrderStatus {
        public static int STATUS_OF_DFK = 0;
        public static int STATUS_OF_DFH = 1;
        public static int STATUS_OF_DSH = 2;
        public static int STATUS_OF_DPJ = 3;
        public static int STATUS_OF_FINISH = 4;
        public static int STATUS_OF_CANCEL = 5;
        public static int STATUS_OF_BACK = 6;
    }

    // "1" 个人砍  "2" 团体砍 "3" 会员专享
    public static class GoodsType {
        public static String VIP_GOODS = "3";
        public static String PERSONAL_CUT_GOODS = "1";
        public static String TEAM_CUT_GOODS = "2";
    }

    private GetDataFromNetPresent present;

    public OrderPresent(GetDataFromNetPresent present) {
        this.present = present;
    }

    public OrderPresent() {
    }


    /**
     * 检查网络代理是否为空
     */
    private void checkNull() {
        if (present == null) {
            throw new NullPointerException("present is null , please init ");
        }
    }

    /**
     * 取消订单
     */
    public void cancelOrder(int requestId, String orderId) {
        checkNull();
        present.getDataByPost(requestId,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_EDIT_ORDER_STATUS(),
                RequestParamsHelper.Companion.getEditOrderStatusParam(orderId, "4"));
    }

    /**
     * 确认收货
     */
    public void confirmGoods(int requestId, String orderId) {
        checkNull();
        present.getDataByPost(0x4,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_EDIT_ORDER_STATUS(),
                RequestParamsHelper.Companion.getEditOrderStatusParam(orderId, "3"));
    }

    /**
     * 支付
     */
    public void payOrder() {
        checkNull();
    }

    /**
     * 删除订单
     */
    public void deleteOrder(int requestId, String orderId) {
        checkNull();
        present.getDataByPost(requestId,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_DEL_DETAIL(),
                RequestParamsHelper.Companion.getDelDetailParam(orderId));
    }

    /**
     * 查看订单快递
     */
    public void expressOrder() {
        checkNull();
    }

    /**
     * 通知卖家发货
     */
    public void notifySendOrder() {
        checkNull();
    }

    /**
     * 确认订单
     */
    public void confirmOrder() {
        checkNull();
    }

    /**
     * 刷新MainMineFragment中的各个状态的数字
     */
    public static void notifyRefreshOrderNum() {
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(MainMineFragment.Companion.getREFRESH_QBADGE_VIEW_FLAG());
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }

    /**
     * 通知刷新订单列表
     */
    public static void notifyRefreshOrderList() {

    }

    /**
     * 获取订单状态
     *
     * @return 订单状态
     */
    public static int getOrderStatus(String token) {
        switch (token) {
            case "0": {
                return OrderStatus.STATUS_OF_DFK;
            }
            case "1": {
                return OrderStatus.STATUS_OF_DFH;
            }
            case "2": {
                return OrderStatus.STATUS_OF_DSH;
            }
            case "3": {
                return OrderStatus.STATUS_OF_DPJ;
            }
            case "4": {
                return OrderStatus.STATUS_OF_FINISH;
            }
            case "5": {
                return OrderStatus.STATUS_OF_CANCEL;
            }
            case "6": {
                return OrderStatus.STATUS_OF_BACK;
            }
            default: {
                return -1;
            }
        }
    }


    public void destroy() {
        present = null;
    }

}
