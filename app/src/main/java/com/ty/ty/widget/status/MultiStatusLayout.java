package com.ty.ty.widget.status;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ty.ty.R;
import com.ty.ty.utils.NetUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author lx
 * @Date 2017/8/6 14:41
 * @Blog http://blog.csdn.net/airsaid
 * @Desc 多状态布局
 */
public class MultiStatusLayout extends FrameLayout {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_NO_NET = 2;
    public static final int STATUS_NO_DATA = 3;
    public static final int STATUS_LOAD_FAIL = 4;
    public static final int STATUS_HIDE = 5;
    private int mStatus = STATUS_LOADING;

    @IntDef({STATUS_LOADING, STATUS_NO_NET, STATUS_NO_DATA, STATUS_LOAD_FAIL, STATUS_HIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    private Context mContext;

    private TextView mTextView;
    private View mContainer;
    private View mLoading;

    public MultiStatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    private void initLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_multi_status, this);
        mTextView = findViewById(R.id.textView);
        mContainer = findViewById(R.id.container);
        mLoading = findViewById(R.id.loading);
        switchStatusLayout();
    }

    /**
     * 设置多状态布局状态
     * @param status @param status {@link Status}
     */
    public void setStatus(@Status int status){
        this.mStatus = status;
        switchStatusLayout();
    }

    /**
     * 设置多状态布局状态
     * @param status @param status {@link Status}
     * @param listener 重试点击监听
     */
    public void setStatus(@Status int status, OnClickListener listener){
        this.mStatus = status;
        switchStatusLayout();
        mTextView.setOnClickListener(listener);
    }

    private void switchStatusLayout() {
        switch (mStatus){
            case STATUS_LOADING:
                showLoading();
                break;
            case STATUS_NO_NET:
            case STATUS_NO_DATA:
            case STATUS_LOAD_FAIL:
                setLoadFail();
                break;
            case STATUS_HIDE:
                hide();
                break;
        }
    }

    private void showLoading(){
        show();
        mContainer.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void setLoadFail(){
        // 判断网络是否可用
        boolean isAvailable = NetUtils.networkIsAvailable(getContext());
        if(isAvailable){
            // 可用，显示数据加载错误
            mTextView.setText(mContext.getString(R.string.status_load_fail_reset));
            mTextView.setCompoundDrawablesWithIntrinsicBounds(null
                    , ContextCompat.getDrawable(mContext, R.mipmap.ic_load_fail), null, null);
        }else{
            // 不可用，显示网络错误
            mTextView.setText(mContext.getString(R.string.status_net_fail_reset));
            mTextView.setCompoundDrawablesWithIntrinsicBounds(null
                    , ContextCompat.getDrawable(mContext, R.mipmap.ic_load_fail), null, null);
        }
        show();
        mLoading.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    public void show(){
        setVisibility(View.VISIBLE);
    }

    public void hide(){
        setVisibility(View.GONE);
    }
}
