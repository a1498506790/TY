package com.ty.ty.base;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseBean<T> implements Parcelable {

    public static final String STATUS = "status";
    public static final String MSG    = "msg";

    public T data;          // 数据
    public int status;      // 状态码
    public String msg;      // 请求结果

    protected BaseBean(Parcel in) {
        status = in.readInt();
        msg = in.readString();
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel in) {
            return new BaseBean(in);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "data=" + data +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(status);
        parcel.writeString(msg);
        parcel.writeValue(data);
    }
}
