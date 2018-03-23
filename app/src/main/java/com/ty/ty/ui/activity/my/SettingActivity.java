package com.ty.ty.ui.activity.my;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.ty.ty.R;
import com.ty.ty.base.BaseActivity;
import com.ty.ty.utils.AppUtils;
import com.ty.ty.utils.ProgressUtils;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-22
 * @desc 设置
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.txt_current_version)
    TextView mTxtCurrentVersion;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar(UiUtils.getString(R.string.title_setting));
        mTxtCurrentVersion.setText(TextUtils.isEmpty(AppUtils.getAppVersionName()) ? "V:1.0.0" : "V" + AppUtils.getAppVersionName());
    }

    @OnClick(R.id.llt_clear_cache)
    public void onViewClicked() {
        ProgressUtils.show(mContext, UiUtils.getString(R.string.load_clear_cache));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressUtils.dismiss();
                ToastUtils.show(mContext, UiUtils.getString(R.string.toast_clear_cache_success));
            }
        }, 2000);
    }
}
