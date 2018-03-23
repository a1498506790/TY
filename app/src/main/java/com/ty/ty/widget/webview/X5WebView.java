package com.ty.ty.widget.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.ty.ty.constants.AppConfig;


/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 腾讯 X5 内核的 WebView，用于替代原生 WebView
 */
public class X5WebView extends WebView {

	public X5WebView(Context context){
		this(context, null);
	}

	public X5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getView().setClickable(true);
		initWebViewSettings();
	}

	/**
	 * 初始化 WebView 设置
	 */
	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(false);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(AppConfig.CACHE_PATH);
		webSetting.setDatabasePath(AppConfig.CACHE_PATH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setGeolocationDatabasePath(AppConfig.CACHE_PATH);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
	}
}
