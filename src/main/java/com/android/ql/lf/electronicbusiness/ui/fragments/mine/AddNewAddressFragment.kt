package com.android.ql.lf.electronicbusiness.ui.fragments.mine

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.electronicbusiness.R
import com.android.ql.lf.electronicbusiness.data.AddressBean
import com.android.ql.lf.electronicbusiness.data.RefreshData
import com.android.ql.lf.electronicbusiness.ui.activities.FragmentContainerActivity
import com.android.ql.lf.electronicbusiness.ui.activities.MainActivity
import com.android.ql.lf.electronicbusiness.ui.activities.SelectAddressActivity
import com.android.ql.lf.electronicbusiness.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.electronicbusiness.ui.views.MyProgressDialog
import com.android.ql.lf.electronicbusiness.utils.RequestParamsHelper
import com.android.ql.lf.electronicbusiness.utils.RxBus
import kotlinx.android.synthetic.main.fragment_add_new_address_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import rx.Subscription
import java.util.regex.Pattern

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AddNewAddressFragment : BaseNetWorkingFragment() {

    companion object {
        val ADDRESS_BEAN_FLAG = "address_bean_flag"
    }

    private var addressInfo: String? = null

    private var addressBean: AddressBean? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }


    override fun getLayoutId(): Int = R.layout.fragment_add_new_address_layout

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(SelectAddressActivity.SelectAddressItemBean::class.java).subscribe {
            addressInfo = it.name
            mEtAddNewAddressAddress.text = addressInfo!!
        }
        mEtAddNewAddressAddress.setOnClickListener {
            startActivity(Intent(mContext, SelectAddressActivity::class.java))
            (mContext as FragmentContainerActivity).overridePendingTransition(R.anim.activity_open, 0)
        }

        if (arguments != null) {
            arguments.classLoader = this.javaClass.classLoader
            addressBean = arguments.getParcelable(ADDRESS_BEAN_FLAG)
            mEtAddNewAddressName.setText(addressBean!!.address_name)
            mEtAddNewAddressPhone.setText(addressBean!!.address_phone)
            addressInfo = addressBean!!.address_addres
            mEtAddNewAddressAddress.text = addressBean!!.address_addres
            mEtAddNewAddressCode.setText(addressBean!!.address_postcode)
            mEtAddNewAddressAddressDetail.setText(addressBean!!.address_detail)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.save_new_address_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuAddNewAddressSave) {
            if (TextUtils.isEmpty(mEtAddNewAddressName.text.toString())) {
                toast("收货人姓名不能为空")
                return false
            }
            if (TextUtils.isEmpty(mEtAddNewAddressPhone.text.toString())) {
                toast("收货人手机号不能为空")
                return false
            }
            if (!Pattern.matches(RegisterFragment.REGEX_MOBILE, mEtAddNewAddressPhone.text.toString())) {
                toast("请输入合法的手机号吗")
                return false
            }
            if (addressInfo == null) {
                toast("请选择省市区")
                return false
            }
            if (TextUtils.isEmpty(mEtAddNewAddressCode.text.toString())) {
                toast("请输入邮政编码")
                return false
            }
            if (TextUtils.isEmpty(mEtAddNewAddressAddressDetail.text.toString())) {
                toast("请输入详细地址")
                return false
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_ADD_ADDRESS,
                    RequestParamsHelper.getAddAddressListParams(
                            addressBean?.address_id ?: "",
                            name = mEtAddNewAddressName.text.toString().trim(),
                            phone = mEtAddNewAddressPhone.text.toString().trim(),
                            addressInfo = addressInfo!!,
                            code = mEtAddNewAddressCode.text.toString().trim(),
                            detail = mEtAddNewAddressAddressDetail.text.toString().trim()))
        }
        return true
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        progressDialog = MyProgressDialog(mContext, "正在提交……")
        progressDialog.show()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (result != null) {
            val jsonObject = JSONObject(result.toString())
            if ("200" == jsonObject.optString("code")) {
                toast("${jsonObject.optString("msg")}")
                RefreshData.isRefresh = true
                RefreshData.any = "添加地址"
                RxBus.getDefault().post(RefreshData)
                finish()
            } else {
                toast("${jsonObject.optString("msg")}")
            }
        }
    }
}