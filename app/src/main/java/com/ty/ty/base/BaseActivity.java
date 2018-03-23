package com.ty.ty.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ty.ty.R;
import com.ty.ty.utils.ActivityStack;
import com.ty.ty.utils.MPermissionUtils;
import com.ty.ty.widget.slideback.SlideBackActivity;
import com.ty.ty.widget.status.MultiStatusLayout;

import butterknife.ButterKnife;

/**
 * @author airsaid
 */
public abstract class BaseActivity extends SlideBackActivity {

    protected static String TAG;
    protected Activity mContext;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置 Activity 屏幕方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 隐藏 ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置 TAG
        TAG = this.getClass().getSimpleName();

        super.onCreate(savedInstanceState);
        this.mContext = this;

        // 设置布局
        setContentView(getLayoutRes());

//        StatusBarUtil.setColor(this, UiUtils.getColor(R.color.white));

        // 绑定依赖注入框架
        ButterKnife.bind(this);

        onCreateActivity(savedInstanceState);

        // 将 Activity 推入栈中
        ActivityStack.getInstance().push(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化标题栏
     */
    public Toolbar initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 取消原有左侧标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
        // 设置标题
        TextView txtTitle = (TextView) findViewById(R.id.txt_title_title);
        txtTitle.setText(title);
        // 设置左侧图标
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        return mToolbar;
    }

    /**
     * 设置标题栏标题
     */
    public void setTitle(String title){
        if(mToolbar != null){
            TextView txtTitle = (TextView) findViewById(R.id.txt_title_title);
            txtTitle.setText(title);
        }
    }

    /**
     * 设置标题栏标题颜色
     */
    public void setTitleTextColor(int id) {
        if (mToolbar != null) {
            TextView txtTitle = (TextView) findViewById(R.id.txt_title_title);
            txtTitle.setTextColor(id);
        }
    }

    /**
     * 设置左侧标题
     */
    public void setLeftTitle(String leftTitle){
        if(mToolbar != null && leftTitle != null){
            mToolbar.setNavigationIcon(null);
            TextView txtLeftTitle = (TextView) findViewById(R.id.txt_title_left);
            txtLeftTitle.setVisibility(View.VISIBLE);
            txtLeftTitle.setText(leftTitle);
            txtLeftTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBack();
                }
            });
        }
    }

    /**
     * 设置左侧标题颜色
     */
    public void setLeftTitleColor(int id){
        if(mToolbar != null){
            TextView txtLeftTitle = (TextView) findViewById(R.id.txt_title_left);
            txtLeftTitle.setTextColor(id);
        }
    }

    /**
     * 设置左侧标题字体大小
     */
    public void setLeftTitleSize(float size){
        if(mToolbar != null){
            TextView txtLeftTitle = (TextView) findViewById(R.id.txt_title_left);
            txtLeftTitle.setTextSize(size);
        }
    }

    /**
     * 显示加载中布局
     */
    public void showLoading(){
        MultiStatusLayout multiStatusLayout = (MultiStatusLayout) findViewById(R.id.layout_multi_status);
        if(multiStatusLayout != null){
            multiStatusLayout.setStatus(MultiStatusLayout.STATUS_LOADING);
        }
    }

    /**
     * 显示加载失败布局
     */
    public void showFail(View.OnClickListener listener){
        MultiStatusLayout multiStatusLayout = (MultiStatusLayout) findViewById(R.id.layout_multi_status);
        if(multiStatusLayout != null){
            multiStatusLayout.setStatus(MultiStatusLayout.STATUS_LOAD_FAIL, listener);
        }
    }

    /**
     * 显示内容
     */
    public void showContent(){
        MultiStatusLayout multiStatusLayout = (MultiStatusLayout) findViewById(R.id.layout_multi_status);
        if(multiStatusLayout != null){
            multiStatusLayout.setStatus(MultiStatusLayout.STATUS_HIDE);
        }
    }

    /**
     * 返回，默认退出当前 activity
     */
    protected void onBack() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 获取布局
     */
    public abstract int getLayoutRes();

    public abstract void onCreateActivity(@Nullable Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将 Activity 弹出栈
        ActivityStack.getInstance().pop();
    }
}
