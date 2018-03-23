package com.ty.ty.http;


import com.ty.ty.base.App;
import com.ty.ty.constants.Api;
import com.ty.ty.constants.AppConfig;
import com.ty.ty.http.gson.CustomGsonConverterFactory;
import com.ty.ty.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 对 retrofit 进行二次封装
 */
public class HttpClient {

    /** 是否设置缓存 */
    private static final boolean mIsSetCache = false;

    private static HttpClient mInstance = null;
    private Retrofit mRetrofit = null;

    /**
     * 获取 HttpClient 实例（单例，使用默认服务器地址）
     * @return HttpClient 对象
     */
    public static HttpClient getIns(){
        return getIns(null);
    }

    /**
     * 获取 HttpClient 实例
     * @param baseUrl 服务器地址
     * @return
     */
    public static HttpClient getIns(String baseUrl){
        if (mInstance == null) {
            synchronized (HttpClient.class){
                if(mInstance == null){
                    mInstance = new HttpClient(baseUrl);
                }
            }
        }
        return mInstance;
    }

    private HttpClient(String baseUrl){
        if(baseUrl != null){
            configRetrofit(baseUrl);
        }else{
            configRetrofit(Api.SERVER_URL);
        }
    }

    /**
     * 配置Retrofit
     * @param baseUrl 服务器地址
     */
    private void configRetrofit(String baseUrl){
        // 创建 OKHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 设置缓存
        if(mIsSetCache){
            File cacheFile = new File(AppConfig.CACHE_PATH);
            Cache cache = new Cache(cacheFile, AppConfig.CACHE_DATE);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetUtils.networkIsAvailable(App.getContext())) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (NetUtils.networkIsAvailable(App.getContext())) {
                        int maxAge = 0;
                        // 有网络时 设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("nyn")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("nyn")
                                .build();
                    }
                    return response;
                }
            };
            builder.cache(cache).addInterceptor(cacheInterceptor);
        }

        // 设置公共参数
        // 设置头
        // 设置Log信息拦截器
        // 设置Cookie

        // 设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        // 设置错误重连
        builder.retryOnConnectionFailure(true);

        // 构建Retrofit
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * 创建 api接口
     */
    public <T> T createService(Class<T> clz){
        return mRetrofit.create(clz);
    }

}
