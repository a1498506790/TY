package com.ty.ty.http;

import android.text.TextUtils;
import android.widget.Toast;

import com.ty.ty.base.App;
import com.ty.ty.constants.AppConstants;
import com.ty.ty.utils.ImageUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zhouyou on 2016/6/22.
 * Class desc: 上传文件帮助类
 */
public class FileMapHelper {

    private Map mMap;

    public FileMapHelper(Map map){
        this.mMap = map;
    }

    /**
     * 上传普通文本
     * @param key 字段
     * @param value 文本
     */
    public void putText(String key, String value){
        if(!TextUtils.isEmpty(value)){
            RequestBody requestBody = RequestBody.create(MediaType.parse(AppConstants.CONTENT_TYPE_TEXT), value);
            mMap.put(key, requestBody);
        }
    }

    /**
     * 上传多张图片
     * @param key 字段
     * @param values 图片地址集合
     */
    public void putPics(String key, List<String> values){
        if(values == null) return;
        for (int i = 0; i < values.size(); i++){
            // 压缩图片
            String path = ImageUtils.compressImage(values.get(i));
            File file = new File(path);
            RequestBody requestBody;
            requestBody = RequestBody.create(MediaType.parse(AppConstants.CONTENT_TYPE_IMAGE), file);
            mMap.put(key + "\";filename=\"" + file.getName(), requestBody);
        }
    }

    /**
     * 上传单张图片
     * @param key      字段
     * @param value    图片地址
     */
    public void putPic(String key, String value){
        // 压缩图片
        String path = ImageUtils.compressImage(value);
        File file = new File(path);
        RequestBody requestBody;
        if(file.getName().endsWith(".jpg")){
            requestBody = RequestBody.create(MediaType.parse(AppConstants.CONTENT_TYPE_JPG), file);
            mMap.put(key + "\";filename=\"" + file.getName(), requestBody);
        }else if(file.getName().endsWith(".png")){
            requestBody = RequestBody.create(MediaType.parse(AppConstants.CONTENT_TYPE_PNG), file);
            mMap.put(key + "\";filename=\"" + file.getName(), requestBody);
        }else{
            Toast.makeText(App.getContext(), "图片格式不正确", Toast.LENGTH_SHORT).show();
        }
    }
}
