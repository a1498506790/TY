package com.ty.ty.constants;

import android.os.Environment;

import java.io.File;


/**
 * @author airsaid
 *
 * app config info store.
 */
public class AppConfig {

    /** SD 卡目录 */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + File.separator;

    /** 缓存目录名字 */
    public static final String CACHE_DIR_NAME = "cache";

    /** 缓存目录路径 */
    public static final String CACHE_PATH = SDCARD_PATH.concat(CACHE_DIR_NAME.concat(File.separator));

    /** 文件缓存目录路径 */
    public static final String CACHE_FILE_PATH = CACHE_PATH.concat("file");

    /** 图片缓存目录路径 */
    public static final String CACHE_IMAGE_PATH =  CACHE_PATH.concat("image");

    /** 缓存大小（100 M） */
    public static final int CACHE_SIZE = 104857600;

    /** 缓存时间（一天） */
    public static final int CACHE_DATE = 60 * 60 * 24;

}
