package com.ty.ty.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.ty.ty.R;
import com.ty.ty.base.BaseBean;
import com.ty.ty.base.BaseFragment;
import com.ty.ty.base.UserBean;
import com.ty.ty.constants.AppConstants;
import com.ty.ty.http.FileMapHelper;
import com.ty.ty.http.HttpClient;
import com.ty.ty.http.MyCallback;
import com.ty.ty.http.api.UserService;
import com.ty.ty.ui.activity.login.LoginActivity;
import com.ty.ty.ui.activity.my.ContactsActivity;
import com.ty.ty.ui.activity.my.RevisePasswordActivity;
import com.ty.ty.ui.activity.my.SettingActivity;
import com.ty.ty.utils.ActivityStack;
import com.ty.ty.utils.DialogUtils;
import com.ty.ty.utils.ImageLoader;
import com.ty.ty.utils.MPermissionUtils;
import com.ty.ty.utils.ProgressUtils;
import com.ty.ty.utils.SPUtils;
import com.ty.ty.utils.UiUtils;
import com.ty.ty.utils.UserUtils;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc 个人中心
 */

public class MyFragment extends BaseFragment {

    @BindView(R.id.img_icon)
    ImageView mImgIcon;
    @BindView(R.id.txt_service_phone)
    TextView mTxtServicePhone;
    @BindView(R.id.txt_name)
    TextView mTxtName;

    private UserBean mUser;

    @Override
    public View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    public void onCreateFragment(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = initToolbar(UiUtils.getString(R.string.title_my));
        toolbar.setNavigationIcon(null);
        mUser = SPUtils.getUser(mContext, AppConstants.KEY_USER, "");
        setInfo();
    }

    private void setInfo() {
        ImageLoader.getIns(mContext).loadIcon(mUser.getHeadimg(), mImgIcon);
        mTxtName.setText(mUser.getRname());
    }

    @OnClick({R.id.img_icon, R.id.llt_contacts, R.id.llt_revise_password,
            R.id.llt_setting, R.id.llt_service_phone, R.id.txt_out_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_icon:
                MPermissionUtils.requestPermissionsResult(getActivity(), 1,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                showUpdateIcon();
                            }

                            @Override
                            public void onPermissionDenied() {
                                MPermissionUtils.showTipsDialog(mContext);
                            }
                        });
                break;
            case R.id.llt_contacts: //通讯录
                MPermissionUtils.requestPermissionsResult(getActivity(), 1,
                        new String[]{Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                startActivity(new Intent(mContext, ContactsActivity.class));
                            }

                            @Override
                            public void onPermissionDenied() {
                                MPermissionUtils.showTipsDialog(mContext);
                            }
                        });
                break;
            case R.id.llt_revise_password: //修改密码
                startActivity(new Intent(mContext, RevisePasswordActivity.class));
                break;
            case R.id.llt_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.llt_service_phone:
                String servicePhone = mTxtServicePhone.getText().toString();
                callPhone(servicePhone);
                break;
            case R.id.txt_out_login:
                DialogUtils.showDialog(mContext, UiUtils.getString(R.string.dialog_title),
                        UiUtils.getString(R.string.dialog_confirm_out_login), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityStack.getInstance().popAllActivity();
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                break;
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        try{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 显示修改头像页面
     */
    public void showUpdateIcon() {
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setSelectMode(FunctionConfig.MODE_SINGLE)
                .setEnableCrop(true)
                .setCircularCut(true)
                .create();
        PictureConfig.getInstance().init(options).openPhoto(getActivity(), new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {
                if(list == null || list.size() < 1) return;
                String path;
                LocalMedia media = list.get(0);
                if (media.isCut() && !media.isCompressed()) {
                    // 裁剪过
                    path = media.getCutPath();
                } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = media.getCompressPath();
                } else {
                    // 原图地址
                    path = media.getPath();
                }
                Map params = new HashMap();
                FileMapHelper helper = new FileMapHelper(params);
                helper.putText("uid", UserUtils.getUid());
                helper.putPic("avator", path);
                ProgressUtils.show(mContext);
                HttpClient.getIns().createService(UserService.class)
                        .headimg(params)
                        .equals(new MyCallback<BaseBean<UserBean>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<UserBean>> response) {
                                ProgressUtils.dismiss();
                                UserBean data = response.body().getData();
                                SPUtils.setUser(mContext, AppConstants.KEY_USER, data);
                                ImageLoader.getIns(mContext).loadIcon(data.getHeadimg(), mImgIcon);
                            }

                            @Override
                            public void onFail(String message) {
                                ProgressUtils.dismiss();
                            }
                        });
            }
        });
    }
}
