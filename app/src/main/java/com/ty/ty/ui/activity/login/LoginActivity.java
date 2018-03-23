package com.ty.ty.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ty.ty.R;
import com.ty.ty.base.BaseActivity;
import com.ty.ty.base.BaseBean;
import com.ty.ty.base.UserBean;
import com.ty.ty.constants.AppConstants;
import com.ty.ty.http.HttpClient;
import com.ty.ty.http.HttpParams;
import com.ty.ty.http.MyCallback;
import com.ty.ty.http.api.UserService;
import com.ty.ty.utils.ProgressUtils;
import com.ty.ty.utils.SPUtils;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc 登录页面
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_account)
    EditText mEdtAccount;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;
    private String mAccount;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 禁止滑动
        setSlideable(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {}

    @OnClick({R.id.btn_login, R.id.txt_now_register, R.id.txt_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login: //登录
                mAccount = mEdtAccount.getText().toString();
                mPassword = mEdtPassword.getText().toString();
                if (checkInput()){
                   login();
                }
                break;
            case R.id.txt_now_register: //注册
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.txt_forget_password: //忘记密码
                startActivity(new Intent(mContext, ForgetPasswordActivity.class));
                break;
        }
    }

    /**
     * 调用登录的接口
     */
    private void login() {
        ProgressUtils.show(mContext, UiUtils.getString(R.string.load_register));
        HttpClient.getIns().createService(UserService.class)
                .login(HttpParams.getIns().login(mAccount, mPassword))
                .equals(new MyCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
                        ProgressUtils.dismiss();
                        UserBean data = response.body().getData();
                        SPUtils.setUser(mContext, AppConstants.KEY_USER, data);
                        UiUtils.enterHomePage(mContext);
                    }

                    @Override
                    public void onFail(String message) {
                        ProgressUtils.dismiss();
                    }
                });
    }

    /**
     * 检查输入的账号密码是否正确
     * @return
     */
    private boolean checkInput(){
        if (TextUtils.isEmpty(mAccount)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_account));
            return false;
        }
        if (TextUtils.isEmpty(mPassword)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_password));
            return false;
        }
        return true;
    }
}
