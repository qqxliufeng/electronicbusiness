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

    private GetDataFromNetPresent present;

    public OrderPresent(GetDataFromNetPresent present) {
        this.present = present;
    }

    public OrderPresent(){}


    /**
     * 检查网络代理是否为空
     */
    private void checkNull(){
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
    public static void notifyRefreshOrderNum(){
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(MainMineFragment.Companion.getREFRESH_QBADGE_VIEW_FLAG());
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }

    public static void notifyRefreshOrderList(){

    }




    public void destroy() {
        present = null;
    }

}
