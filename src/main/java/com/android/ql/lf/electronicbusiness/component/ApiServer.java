package com.android.ql.lf.electronicbusiness.component;

import com.android.ql.lf.electronicbusiness.utils.ApiParams;
import com.android.ql.lf.electronicbusiness.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/16 0016.
 *
 * @author lf
 */

public interface ApiServer {

    /**
     * 以post的请求方式，请求网络数据
     *
     * @param params 请求参数
     * @return 请求结果
     */
    @FormUrlEncoded
    @POST(Constants.BASE_IP_POSTFIX)
    Observable<String> getDataByPost(@Path("postfix1") String postfix1, @Path("postfix2") String postfix2, @FieldMap ApiParams params, @Field("token") String token);

    /**
     * 以post的请求方式，请求网络数据
     * @return 请求结果
     */
    @FormUrlEncoded
    @POST(Constants.BASE_IP_POSTFIX)
    Observable<String> getDataByPost(@Path("postfix1") String postfix1, @Path("postfix2") String postfix2, @Field("token") String token);

    /**
     * 以get的请求方式，请求网络数据
     *
     * @return 请求结果
     */
    @GET(Constants.BASE_IP_POSTFIX)
    Observable<String> getDataByGet(@Path("postfix1") String postfix1, @Path("postfix2") String postfix2, @QueryMap ApiParams params);

    /**
     * 以get的请求方式，请求网络数据
     *
     * @param map 请求参数
     * @return 请求结果
     */
    @GET(Constants.BASE_IP_POSTFIX)
    Observable<String> getDataByGet(@QueryMap HashMap<String, String> map, @Query("token") String token);

    /**
     * 上传图片
     *
     * @param postfix1
     * @param postfix2
     * @param partList
     * @return
     */
    @Multipart
    @POST(Constants.BASE_IP_POSTFIX)
    Observable<String> uploadFiles(
            @Path("postfix1") String postfix1,
            @Path("postfix2") String postfix2,
            @Part List<MultipartBody.Part> partList);
}
