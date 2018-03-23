package com.ty.ty.http;

import com.ty.ty.utils.UserUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 请求参数封装类
 */
public class HttpParams {

	private static HttpParams mInstance;
	private static Map<String, String> mParams;

	public static HttpParams getIns() {
		if(mParams == null){
			mParams = new HashMap<>();
		}else{
			mParams.clear();
		}

		if (mInstance == null) {
			mInstance = new HttpParams();
		}
		return mInstance;
	}

	/**
	 * 请求用户 id 和 code
	 * @param uid	用户 id
	 * @param ucode	用户 code
     */
	public Map<String, String> putUserInfo(String uid, String ucode){
		mParams.put("uid", uid);
		mParams.put("ucode", ucode);
		return mParams;
	}

	/**
	 * 请求不带用户信息的分页数据
	 * @param page	页码
	 */
	public Map<String, String> putPage(int page){
		mParams.put("page", String.valueOf(page));
		return mParams;
	}

	/**
	 * 请求带用户信息的分页数据
	 * @param page 页码
	 */
	public Map<String, String> putPage(String uid, String ucode, int page){
		putUserInfo(uid, ucode);
		mParams.put("page", String.valueOf(page));
		return mParams;
	}

	public Map<String, String> putPage(String uid, int page){
		mParams.put("uid", uid);
		mParams.put("page", String.valueOf(page));
		return mParams;
	}

	/**
	 * 注册
	 * @param phone
	 * @param rname
	 * @param idcard
	 * @return
	 */
	public Map<String, String> register(String phone, String rname, String idcard){
		mParams.put("phone", phone);
		mParams.put("rname", rname);
		mParams.put("idcard", idcard);
		return mParams;
	}

	/**
	 * 登陆
	 * @param account
	 * @param password
	 * @return
	 */
	public Map<String, String> login(String account, String password){
		mParams.put("account", account);
		mParams.put("password", password);
		return mParams;
	}

	/**
	 * 忘记密码
	 * @param phone
	 * @param rname
	 * @param idcard
	 * @param newpwd
	 * @param confirmpwd
	 * @return
	 */
	public Map<String, String> forgetPassword(String phone, String rname, String idcard, String newpwd, String confirmpwd){
		mParams.put("uid", UserUtils.getUid());
		mParams.put("phone", phone);
		mParams.put("rname", rname);
		mParams.put("idcard", idcard);
		mParams.put("newpwd", newpwd);
		mParams.put("confirmpwd", confirmpwd);
		return mParams;
	}

	/**
	 * 修改密码
	 * @param oldpwd
	 * @param newpwd
	 * @param confirmpwd
	 * @return
	 */
	public Map<String, String> editPassword(String oldpwd, String newpwd, String confirmpwd){
		mParams.put("uid", UserUtils.getUid());
		mParams.put("oldpwd", oldpwd);
		mParams.put("newpwd", newpwd);
		mParams.put("confirmpwd", confirmpwd);
		return mParams;
	}

}
