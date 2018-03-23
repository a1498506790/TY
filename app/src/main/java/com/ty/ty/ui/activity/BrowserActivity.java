package com.ty.ty.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.ty.ty.R;
import com.ty.ty.base.BaseActivity;
import com.ty.ty.constants.AppConstants;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;
import com.ty.ty.widget.webview.ProgressWebView;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;


/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 浏览页 Activity
 */
public class BrowserActivity extends BaseActivity {

    @BindView(R.id.webView)
    ProgressWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 避免播放视频时出现闪烁情况
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_browser;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar("");

        if(getIntent().getExtras() == null){
            showLoadFail();
            return;
        }

        // 设置标题
        String title = getIntent().getExtras().getString(Intent.EXTRA_TITLE, "");
        if(!TextUtils.isEmpty(title)){
            setTitle(title);
        }else{
            mWebView.setOnReceivedTitleListener(new ProgressWebView.OnReceivedTitleListener() {
                @Override
                public void onReceivedTitle(String title) {
                    setTitle(title);
                }
            });
        }

        // 设置加载 url
        String urlStr = getIntent().getExtras().getString(AppConstants.EXTRA_URL, "");
        if (!TextUtils.isEmpty(urlStr)) {
            try {
                URL url = new URL(urlStr);
                mWebView.loadUrl(url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                showLoadFail();
            }
        }else{
            showLoadFail();
        }
    }

    /**
     * 显示加载失败
     */
    private void showLoadFail(){
        mWebView.loadUrl("about:blank");
        setTitle(UiUtils.getString(R.string.title_url_fail));
        ToastUtils.show(mContext, UiUtils.getString(R.string.toast_load_fail));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }
}
