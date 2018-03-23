package com.ty.ty.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.ty.ty.BuildConfig;
import com.ty.ty.cockroach.Cockroach;

/**
 * @author airsaid
 */
public class App extends Application {

    public static final String TAG = "App";

    private static Context mContext;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initX5WebView();
//        installCockroach();
    }

    /**
     * 获取上下文
     * @return Context
     */
    public static Context getContext(){
        return mContext;
    }

    /**
     * 初始化 x5 内核
     */
    private void initX5WebView() {
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d(TAG, "onDownloadFinish is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d(TAG, "onInstallFinish is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d(TAG, "onDownloadProgress:"+i);
            }
        });
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, "onCoreInitFinished: 初始化完成");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.d(TAG, "onCoreInitFinished: 初始化失败 " + b);
            }
        });
    }

    private void installCockroach() {
        Cockroach.install(new Cockroach.ExceptionHandler() {
            // handlerException 内部建议手动 try{  你的异常处理逻辑  }catch(Throwable e){ }
            // 以防handlerException 内部再次抛出异常，导致循环调用 handlerException
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                // 开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                // 由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                // 所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                // new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (BuildConfig.DEBUG) {
                                // 打印异常并 toast
                                Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
