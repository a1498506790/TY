package com.ty.ty.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author by lx
 * @github https://github.com/a1498506790
 * @data 2018-03-15
 * @desc
 */

public class TelContactBean implements MultiItemEntity{

    private String name;
    private String telNumber;
    private int type = 1;

    public TelContactBean(String name) {
        this.name = name;
    }

    public TelContactBean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public TelContactBean(String name, String telNumber, int type) {
        this.name = name;
        this.telNumber = telNumber;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public String toString() {
        return "TelContactBean{" +
                "name='" + name + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", type=" + type +
                '}';
    }
}
