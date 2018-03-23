package com.ty.ty.base;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-22
 * @desc
 */

public class UserBean {


    /**
     * uid : 用户id
     * phone : 手机号
     * rname : 真实姓名
     * headimg : 头像
     * idcard : 身份证号
     */

    private String uid;
    private String phone;
    private String rname;
    private String headimg;
    private String idcard;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
