package com.ty.ty.utils;

import android.content.Context;

import com.ty.ty.base.UserBean;
import com.ty.ty.constants.AppConstants;


/**
 * Created by lx on 2017/5/26.
 */
public class UserUtils {

    /**
     * 校验用户是否登录
     * @return true: 登录 false: 未登录
     */
    public static boolean checkUserLogin(Context context){
        UserBean user = SPUtils.getUser(context, AppConstants.KEY_USER, "");
        if(user == null){
            return false;
        }
        return true;
    }

    /**
     * 获取 user 信息
     * @return user
     */
    public static UserBean getUser(){
        return SPUtils.getUser(UiUtils.getContext(), AppConstants.KEY_USER, "");
    }

    /**
     * 获取 uid
     * @return uid
     */
    public static String getUid(){
        UserBean user = SPUtils.getUser(UiUtils.getContext(), AppConstants.KEY_USER, "");
        if(user != null)
            return user.getUid();
        return null;
    }
}
