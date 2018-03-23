package com.ty.ty.http.api;


import com.ty.ty.base.BaseBean;
import com.ty.ty.base.UserBean;
import com.ty.ty.bean.HomeBean;
import com.ty.ty.bean.ListBean;
import com.ty.ty.constants.Api;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * @author lx
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc user service interface
 */
public interface UserService {

    @GET(Api.REGISTER)
    Callback<BaseBean<UserBean>> register(@QueryMap Map<String,String> params);

    @GET(Api.LOGIN)
    Callback<BaseBean<UserBean>> login(@QueryMap Map<String,String> params);

    @Multipart
    @POST(Api.HEADIMG)
    Callback<BaseBean<UserBean>> headimg(@PartMap Map<String, RequestBody> params);

    @GET(Api.FORGETPASSWORD)
    Callback<BaseBean<String>> forgetPassword(@QueryMap Map<String,String> params);

    @GET(Api.EDITPASSWORD)
    Callback<BaseBean<String>> editPassword(@QueryMap Map<String,String> params);

    @GET(Api.HOME)
    Callback<BaseBean<ListBean<HomeBean>>> home(@QueryMap Map<String,String> params);

    @Multipart
    @POST(Api.COMMUNICATION)
    Callback<BaseBean<String>> communication(@PartMap Map<String, RequestBody> params);

}
