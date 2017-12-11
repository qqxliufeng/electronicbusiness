package com.android.ql.lf.electronicbusiness.utils

import com.android.ql.lf.electronicbusiness.data.UserInfo

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class RequestParamsHelper {

    companion object {

        private fun getBaseParams(): ApiParams {
            val params = ApiParams()
            params.addParam("pt", "android")
            return params
        }

        private fun getWithIdParams(): ApiParams {
            val params = getBaseParams()
            params.addParam("uid", UserInfo.getInstance().memberId)
            return params
        }

        private fun getWithPageParams(page: Int, pageSize: Int = 10): ApiParams {
            val param = if (UserInfo.getInstance().isLogin) {
                getWithIdParams()
            } else {
                getBaseParams()
            }
            param.addParam("page", page)
            param.addParam("pagesize", pageSize)
            return param
        }

        /**              login model  start           **/
        val LOGIN_MODEL = "login"


        val ACT_REGISTER = "Register"
        val ACT_CODE = "getcode"
        val ACT_LOGIN = "Login"
        val ACT_FORGETPW = "forgetpw"
        val ACT_WX_PERFECT = "wx_perfect"

        fun getCodeParams(tel: String = ""): ApiParams {
            val params = getBaseParams()
            return params.addParam("tel", tel)
        }

        fun getRegisterParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw)
            return params
        }

        fun getLoginParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw)
            return params
        }

        fun getForgetPWParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw)
            return params
        }


        fun getWXCompleteDataParam(phone: String): ApiParams {
            val params = getBaseParams()
            params.addParam("phone", phone)
            return params
        }

        /**              login model  end           **/


        /**              member model  start           **/

        val MEMBER_MODEL = "member"

        val ACT_PERSONAL = "personal"
        val ACT_EDIT_PERSONAL = "edit_personal"
        val ACT_MSG = "mymsg"
        val ACT_RESET_PW = "edit_pw"
        val ACT_ADDRESS_LIST = "address"
        val ACT_ADD_ADDRESS = "add_address"
        val ACT_DEFAULT_ADDRESS = "default_address"
        val ACT_DEL_ADDRESS = "del_address"
        val ACT_SIGNIN = "signin"
        val ACT_INTEGRAL = "integral"
        val ACT_PTGG = "ptgg" //积分规则

        val ACT_PROVINCE = "province"
        val ACT_CITY = "city"
        val ACT_PROVINCE_CITY_AREA = "province_city_area"

        fun getDefaultAddress() = getWithIdParams()

        fun getProvinceParam() = getBaseParams()

        fun getCityParam(pid: String): ApiParams {
            val param = getBaseParams()
            param.addParam("pid", pid)
            return param
        }

        fun getProvinceCityAreaParam(cid: String): ApiParams {
            val param = getBaseParams()
            param.addParam("cid", cid)
            return param
        }

        fun getPersonal() = getWithIdParams()

        fun getEditPersonalParams(nickName: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("account", nickName.replaceBlank())
            return params
        }

        fun getMyMessageList(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        fun getResetPassword(oldPassword: String = "", newPassword: String = ""): ApiParams {
            val params = getWithIdParams()
            params.addParam("pw", oldPassword.replaceBlank())
                    .addParam("newpw", newPassword.replaceBlank())
            return params
        }

        fun getAddressListParams() = getWithIdParams()

        fun getAddAddressListParams(aid: String? = "", name: String = "", phone: String = "", addressInfo: String = "", code: String = "", detail: String = ""): ApiParams {
            val params = getWithIdParams()
            params.addParam("aid", aid)
            params.addParam("name", name.replaceBlank())
            params.addParam("phone", phone.replaceBlank())
            params.addParam("address", addressInfo.replaceBlank())
            params.addParam("detail", detail.replaceBlank())
            params.addParam("postcode", code.replaceBlank())
            return params
        }

        fun getDefaultAddressParams(topAid: String, setAid: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("topaid", topAid)
            params.addParam("aid", setAid)
            return params
        }

        fun getDelAddressParams(aid: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("aid", aid)
            return params
        }

        fun getSigninParams() = getWithIdParams()

        fun getIntegralParams(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        fun getPtggParam(pid: String): ApiParams {
            val param = getBaseParams()
            param.addParam("pid", pid)
            return param
        }

        //获取用户所有的订单
        val ACT_MYORDER = "myorder"

        fun getMyOrderParams(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        //订单详情
        val ACT_ORDER_DETAIL = "order_detail"

        fun getOrderDetailParam(oid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("oid", oid)
            return param
        }

        //加入购物车
        val ACT_ADD_SHOPCART = "add_shopcart"

        fun getAddShopCartParam(gid: String, specification: String, num: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("gid", gid)
            param.addParam("specification", specification)
            param.addParam("num", num)
            return param
        }

        //购物车
        val ACT_SHOPCART = "shopcart"

        fun getShopcartParam(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        //删除购物车商品
        val ACT_DEL_SHOPCART = "del_shopcart"

        fun getDelShopcartParam(cid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("cid", cid)
            return param
        }

        //修改商品数量
        val ACT_UPDATE_SHOPCART = "update_shopcart"

        fun getUpdateShopcart(cid: String, num: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("cid", cid)
            param.addParam("num", num)
            return param
        }


        /**
         * 根据不同的状态获取不同的订单信息
         * 0 待付款 1 待发货 2 待收货 3 待评价 4 完成 5 已取消 6 已退款
         */
        val ACT_MYORDER_STATUS = "myorder_status"

        fun getMyorderStatusParam(status: String, page: Int, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("status", status)
            return param
        }

        /**
         * 修改订单状态
         *   0 待付款  1 待发货  2 已发货/待收货 3 已收货/订单完成 4 取消订单 5 申请退款 6 已退款
         */
        val ACT_EDIT_ORDER_STATUS = "edit_order_status"

        fun getEditOrderStatusParam(oid: String, status: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("oid", oid)
            param.addParam("status", status)
            return param
        }


        //意见反馈
        val ACT_ADD_IDEA = "add_idea"

        fun getAddIdeaParam(content: String, phone: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("content", content)
            param.addParam("phone", phone)
            return param
        }

        //删除订单
        val ACT_DEL_DETAIL = "del_detail"

        fun getDelDetailParam(oid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("oid", oid)
            return param
        }

        //评价商品
        val ACT_EVALUATE = "evaluate"

        fun getEvaluateParam(oid:String,gid:String,content:String,f:String):ApiParams{
            val param = getWithIdParams()
            param.addParam("oid",oid)
            param.addParam("gid",gid)
            param.addParam("content",content)
            param.addParam("f",f)
            return param
        }

        //快递查询
        val ACT_GETLOGISTICS = "getlogistics"

        fun getGetlogisticsParam(num: String):ApiParams{
            val param = getWithIdParams()
            param.addParam("nu",num)
            return param
        }

        /**              member model  end           **/


        /**              qaa model  start           **/

        val QAA_MODEL = "qaa"

        val ACT_TAG = "tag"
        val ACT_QUIZ_TYPE_SEARCH = "quiz_type_search"
        //我的关注
        val ACT_GET_MYCONCERM = "get_myconcerm"

        //是否有敏感词
        val ACT_ISQUIZ = "isquiz"

        fun getQuizTypeSearch(type: String, page: Int = 0, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("type", type)
            return param
        }

        fun getMyFocus(page: Int = 0, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        fun getIsquizParams(title: String): ApiParams {
            val param = getBaseParams()
            param.addParam("title", title)
            return param
        }

        val ACT_ADD_QUIZ = "add_quiz"

        fun getAddQuizParams(title: String = "", content: String = "", type: String = ""): ApiParams {
            val params = getWithIdParams()
            params.addParam("title", title)
            params.addParam("content", content)
            params.addParam("type", type)
            return params
        }

        val ACT_QUIZ_DETAIL = "quiz_detail"

        fun getQuizDetailParams(qid: String, page: Int, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("qid", qid)
            return param
        }

        val ACT_PRAISE = "praise"
        fun getPraise(aid: String = ""): ApiParams {
            val params = getWithIdParams()
            params.addParam("aid", aid)
            return params
        }

        val ACT_ANSWER_DETAIL = "answer_detail"

        fun getAnswerDetailParams(aid: String, page: Int, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("aid", aid)
            return param
        }

        val ACT_GET_MYQUIZ = "get_myquiz"
        fun getMyQuizParams(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        val ACT_GET_MY_ANSWER = "get_myanswer"
        fun getMyAnswerParams(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        /**
         * 关注问题
         */
        val ACT_CONCERM = "concerm"

        fun getConcermParams(qid: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("qid", qid)
            return params
        }

        val ACT_ADD_ANSWER = "add_answer"
        fun getAddAnswerParams(qid: String, content: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("qid", qid)
            param.addParam("content", content)
            return param
        }

        val ACT_H_REPLY = "h_reply"
        fun getHReply(aid: String, rid: String, content: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("aid", aid)
            param.addParam("rid", rid)
            param.addParam("content", content)
            return param
        }

        //会员中心
        val ACT_BBS = "bbs"

        fun getBBSParam(payType: String, money: String, numtime: String = "6"): ApiParams {
            val param = getWithIdParams()
            param.addParam("paytype", payType)
            param.addParam("price", money)
            param.addParam("numtime", numtime)
            return param
        }

        /**              qaa model  end           **/

        /**              product model  end           **/
        val PRODUCT_MODEL = "product"

        //条目
        val ACT_JPRODUCT_TYPE = "jproduct_type"

        fun getJProductTypeParams(pid: Int = 0): ApiParams {
            val param = getBaseParams()
            param.addParam("pid", pid)
            return param
        }

        //全部商品
        val ACT_JPRODUCT = "jproduct"

        fun getJproductParam(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)


        //商品分类
        val ACT_JPRODUCT_SEARCH = "jproduct_search"

        fun getJproductSearchParam(type_id: String, page: Int, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("type_id", type_id)
            return param
        }

        //商品详情
        val ACT_JPRODUCT_DETAIL = "jproduct_detail"

        fun getJproductDetail(gid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("gid", gid)
            return param
        }


        val ACT_PRODUCT_TYPE = "product_type"

        fun getProductTypeParams(ktype: String, pid: String = "0"): ApiParams {
            val param = getBaseParams()
            param.addParam("ktype", ktype)
            param.addParam("pid", pid)
            return param
        }

        //砍价商品分类查询
        val ACT_PRODUCT_TYPE_SEARCH = "product_type_search"

        fun getProductTypeSearchParams(type_id: String, stype_id: String, ktype: String,sort: String, page: Int, pagesize: Int = 10): ApiParams {
            val params = getWithPageParams(page, pagesize)
            params.addParam("stype_id", stype_id)
            params.addParam("type_id", type_id)
            params.addParam("ktype", ktype)
            params.addParam("sort", sort)
            return params
        }

        //砍价商品查询
        val ACT_PRODUCT = "product"

        fun getProductParams(ktype: String, sort: String, page: Int, pagesize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pagesize)
            param.addParam("ktype", ktype)
            param.addParam("sort", sort)
            return param
        }

        //砍价商品详情
        val ACT_PRODUCT_DETAIL = "product_detail"

        fun getProductDetailParam(gid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("gid", gid)
            return param
        }


        //个人砍
        val ACT_ONEBARGAIN = "onebargain"

        fun getOnebargainParam(gid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("gid", gid)
            return param
        }

        //团体砍
        val ACT_MOREBARGAIN = "morebargain"

        fun getMorebargainParam(gid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("gid", gid)
            return param
        }

        //轮播图 1 砍价 2 会员
        val ACT_PRODUCT_LUNBO = "product_lunbo"

        fun getLunBoParam(ktype: String): ApiParams {
            val param = getBaseParams()
            param.addParam("ktype", ktype)
            return param
        }

        //全部评价
        val ACT_ALL_COMMENT = "all_comment"

        fun getAllCommentParam(gid: String, page: Int, pageSize: Int = 10): ApiParams {
            val param = getWithPageParams(page, pageSize)
            param.addParam("gid", gid)
            return param
        }

        /**              product model  end           **/

        /**              order model  end           **/

        val ORDER_MODEL = "order"

        val ACT_ADD_ORDER = "add_order"

        /**
         * 下订单
         * @param gid 商品ID
         * @param price 商品价格
         * @param addressId 收货地址ID
         * @param num 商品数量
         * @param ktype 活动类型 (1 个人砍 2 团体砍 3 会员专享)
         * @param mdtype 配送类型
         * @param mliuyan 留言
         * @param specification 商品规格
         */
        fun getAddOrderParams(payType: String, orderData: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("paytype", payType)
            param.addParam("post_data", orderData)
            return param
        }

        val ACT_REFUND = "refund"
        fun getRefundParam(name: String, phone: String, oid: String, content: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("name", name)
            param.addParam("phone", phone)
            param.addParam("oid", oid)
            param.addParam("content", content)
            return param
        }

        /**              order model  end           **/

    }


}