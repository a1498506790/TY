package com.ty.ty.widget.webview;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 自定义带加载进度条的 WebView
 */
public class ProgressWebView extends X5WebView {

    private WebViewProgressBar progressBar; // 进度条
    private Handler handler;

    public ProgressWebView(Context context){
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 实例化进度条
        progressBar = new WebViewProgressBar(context);
        // 设置进度条的 size
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // 刚开始时候进度条不可见
        progressBar.setVisibility(GONE);
        // 把进度条添加到 WebView 里面
        addView(progressBar);
        // 初始化 handle
        handler = new Handler();
        setWebViewClient(new MyWebClient());
        setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setProgress(100);
                handler.postDelayed(runnable, 200); // 0.2 秒后隐藏进度条
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            // 设置初始进度 10
            if (newProgress < 10) {
                newProgress = 10;
            }
            // 不断更新进度
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            if(mOnReceivedTitleListener != null)
                mOnReceivedTitleListener.onReceivedTitle(s);
        }
    }

    private class MyWebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            ProgressWebView.this.requestFocus();
            ProgressWebView.this.requestFocusFromTouch();
        }
    }

    /**
     * 刷新界面（此处为加载完成后进度消失）
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void destroy() {
        handler.removeCallbacks(runnable);
        super.destroy();
    }

    private OnReceivedTitleListener mOnReceivedTitleListener;

    public interface OnReceivedTitleListener{
        void onReceivedTitle(String title);
    }

    public void setOnReceivedTitleListener(OnReceivedTitleListener listener){
        this.mOnReceivedTitleListener = listener;
    }
}