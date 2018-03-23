package com.ty.ty.ui.activity.login;

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
import com.ty.ty.utils.RegexUtils;
import com.ty.ty.utils.ToastUtils;
import com.ty.ty.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-22
 * @desc 忘记密码
 */

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.edt_phone)
    EditText mEdtPhone;
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_id)
    EditText mEdtId;
    @BindView(R.id.edt_new_password)
    EditText mEdtNewPassword;
    @BindView(R.id.edt_again_new_password)
    EditText mEdtAgainNewPassword;
    private String mPhone;
    private String mName;
    private String mId;
    private String mNewPassword;
    private String mAgainNewPassword;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar(UiUtils.getString(R.string.title_forget_password));
    }

    @OnClick(R.id.btn_affirm_alter)
    public void onViewClicked() {
        //确认修改
        mPhone = mEdtPhone.getText().toString();
        mName = mEdtName.getText().toString();
        mId = mEdtId.getText().toString();
        mNewPassword = mEdtNewPassword.getText().toString();
        mAgainNewPassword = mEdtAgainNewPassword.getText().toString();
        if (checkInput()) {
            affirmAlter();
        }
    }

    private void affirmAlter() {
        ProgressUtils.show(mContext);
        HttpClient.getIns().createService(UserService.class)
                .forgetPassword(HttpParams.getIns().forgetPassword(mPhone, mName, mId, mNewPassword, mAgainNewPassword))
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

    private boolean checkInput() {
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_phone));
            return false;
        }
        if (!RegexUtils.checkPhone(mPhone)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_correct_phone));
            return false;
        }
        if (TextUtils.isEmpty(mId)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_id));
            return false;
        }
        if (!RegexUtils.isIdCard(mId)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_correct_id));
            return false;
        }
        if (TextUtils.isEmpty(mName)) {
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_input_name));
            return false;
        }
        if (TextUtils.isEmpty(mNewPassword)){
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_please_input_new_password));
            return false;
        }
        if (mNewPassword.length() < 6 || mNewPassword.length() > 12){
            ToastUtils.show(mContext, "请输入6到12的密码");
            return false;
        }
        if (TextUtils.isEmpty(mAgainNewPassword)){
            ToastUtils.show(mContext, UiUtils.getString(R.string.toast_please_again_input_new_password));
            return false;
        }
        if (!mNewPassword.equals(mAgainNewPassword)) {
            ToastUtils.show(mContext, "两次密码输入不一致");
            return false;
        }
        return true;
    }
}
