package com.ty.ty.http;


import com.ty.ty.R;
import com.ty.ty.base.BaseBean;
import com.ty.ty.utils.LogUtils;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 对 retrofit 的 callback 进行二次封装
 */
public abstract class MyCallback<T extends BaseBean> implements Callback<T> {

    private static final String TAG = "MyCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        LogUtils.e(TAG, "request success: \n" + response.raw().toString());

        if (response.raw().code() == 200) {
            // 在这里对服务器规定的状态码进行相应判断
            if (response.body().status == 0) {
                onSuccess(response);
            } else {
                ToastUtils.show(UiUtils.getContext(), response.body().msg);
                onFail(response.body().msg);
            }
        } else {
            onFailure(call, new RuntimeException("response error, detail=" + response.raw().toString()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        LogUtils.e(TAG, "request failure:  \n" + call.request().toString());
        LogUtils.e(TAG, "request failure:  \n" + t.getMessage() + " ==== " + t.toString());

        // 对不同网络错误做不同处理
        if (t instanceof SocketTimeoutException) { // 连接超时
            ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.request_time_out_fail));
        } else if (t instanceof ConnectException) {// 网络连接错误
            ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.request_connect_fail));
        } else if (t instanceof UnknownHostException) {// DNS解析错误
            ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.request_unknown_host_fail));
        } else if (t instanceof IllegalStateException) {// 参数解析失败
            ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.request_illegal_state_fail));
        } else if (t instanceof RuntimeException) {// 运行时异常
            String message = t.getMessage();
            if(message.contains("404") || message.contains("505")){
                ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.request_server_busy_fail));
            }else{
                ToastUtils.show(UiUtils.getContext(), message);
            }
        }
        onFail(t.getMessage() + " ==== " + t.toString());
    }

    /**
     * 请求成功回调
     */
    public abstract void onSuccess(Response<T> response);

    /**
     * 请求失败回调
     */
    public abstract void onFail(String message);

}
