package com.ty.ty.ui.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.ty.ty.R;
import com.ty.ty.base.BaseActivity;
import com.ty.ty.base.BaseBean;
import com.ty.ty.http.HttpClient;
import com.ty.ty.http.HttpParams;
import com.ty.ty.http.MyCallback;
import com.ty.ty.http.api.UserService;
import com.ty.ty.utils.ProgressUtils;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-22
 * @desc 修改密码
 */

public class
RevisePasswordActivity extends BaseActivity {

    @BindView(R.id.edt_old_password)
    EditText mEdtOldPassword;
    @BindView(R.id.edt_new_password)
    EditText mEdtNewPassword;
    @BindView(R.id.edt_again_new_password)
    EditText mEdtAgainNewPassword;
    private String mOldPassWord;
    private String mNewPassWord;
    private String mAgainNewPassWord;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_revise_password;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar(UiUtils.getString(R.string.title_revise_password));
    }

    @OnClick(R.id.btn_affirm_revise)
    public void onViewClicked() {
        mOldPassWord = mEdtOldPassword.getText().toString();
        mNewPassWord = mEdtNewPassword.getText().toString();
        mAgainNewPassWord = mEdtAgainNewPassword.getText().toString();
        if (checkInput()) {
            revisePassword();
        }
    }

    /**
     * 请求接口 修改密码
     */
    private void revisePassword() {
        ProgressUtils.show(mContext, UiUtils.getString(R.string.load_update));
        HttpClient.getIns().createService(UserService.class)
                .editPassword(HttpParams.getIns().editPassword(mOldPassWord, mNewPassWord, mAgainNewPassWord))
                .equals(new MyCallback<BaseBean<String>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<String>> response) {
                        ProgressUtils.dismiss();
                        ToastUtils.show(mContext, response.body().getMsg());
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        ProgressUtils.dismiss();
                    }
                });
    }

    /**
     * 检查输入的信息
     * @return
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(mOldPassWord)){
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_please_input_old_password));
            return false;
        }
        if (TextUtils.isEmpty(mNewPassWord)){
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_please_input_new_password));
            return false;
        }
        if (mNewPassWord.length() < 6 || mNewPassWord.length() > 12){
            ToastUtils.show(mContext, "请输入6到12的密码");
            return false;
        }
        if (TextUtils.isEmpty(mAgainNewPassWord)){
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_please_again_input_new_password));
            return false;
        }
        if (!mNewPassWord.equals(mAgainNewPassWord)) {
            ToastUtils.show(mContext, "两次密码输入不一致");
            return false;
        }
        return true;
    }
}
