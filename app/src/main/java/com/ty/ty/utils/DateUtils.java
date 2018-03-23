package com.ty.ty.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Airsaid
 * @github https://github.com/airsaid
 * @date 2017/5/22
 * @desc 日期操作相关工具类
 */
public class DateUtils {


    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将时间戳，格式化为指定日期格式的日期字符串。
     * @param time      时间戳
     * @param format    日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(long time, String format){
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeInMillis(time * 1000);
        return getDateText(c, format);
    }

    /**
     * 将日期对象，格式化为指定日期格式的日期字符串。
     * @param date      日期对象
     * @param format    日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(Date date, String format){
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.CHINA);
        return f.format(date);
    }

    /**
     * 将日期对象，格式化为指定日期格式的日期字符串。
     * @param c         日期对象
     * @param format    日期格式
     * @return 根据指定日期格式格式话后的字符串，如：2017-04-07
     */
    public static String getDateText(Calendar c, String format){
        SimpleDateFormat f = new SimpleDateFormat(format != null ? format : FORMAT, Locale.CHINA);
        return f.format(c.getTime());
    }


}
