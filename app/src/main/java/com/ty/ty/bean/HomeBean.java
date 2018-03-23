package com.ty.ty.bean;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-21
 * @desc
 */

public class HomeBean {

    /**
     * a_id : 文章id
     * a_title : 文章标题
     * picurl : 图片链接地址
     * is_outside : 外部链接  0：否  1：是
     * a_from : url跳转链接
     */

    private String a_id;
    private String a_title;
    private String picurl;
    private String is_outside;
    private String a_from;

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getA_title() {
        return a_title;
    }

    public void setA_title(String a_title) {
        this.a_title = a_title;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getIs_outside() {
        return is_outside;
    }

    public void setIs_outside(String is_outside) {
        this.is_outside = is_outside;
    }

    public String getA_from() {
        return a_from;
    }

    public void setA_from(String a_from) {
        this.a_from = a_from;
    }
}
