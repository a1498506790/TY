package com.ty.ty.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.ty.ty.utils.RegexUtils;
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
 * @desc 注册界面
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edt_phone)
    EditText mEdtPhone;
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_id)
    EditText mEdtId;
    private String mPhone;
    private String mId;
    private String mName;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    public void onCreateActivity(@Nullable Bundle savedInstanceState) {
        initToolbar(UiUtils.getString(R.string.title_register));
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() { //注册
        mPhone = mEdtPhone.getText().toString();
        mName = mEdtName.getText().toString();
        mId = mEdtId.getText().toString();
        if (checkInput()) {
            register();
        }
    }

    /**
     * 请求注册接口
     */
    private void register() {
        ProgressUtils.show(mContext, UiUtils.getString(R.string.load_register));
        HttpClient.getIns().createService(UserService.class)
                .register(HttpParams.getIns().register(mPhone, mName, mId))
                .equals(new MyCallback<BaseBean<UserBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<UserBean>> response) {
                        ProgressUtils.dismiss();
                        ToastUtils.show(mContext, UiUtils.getString(R.string.dialog_register_success));
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
     * 检验输入的信息是否正确
     * @return
     */
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
        return true;
    }
}
