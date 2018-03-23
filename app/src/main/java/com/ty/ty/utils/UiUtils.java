package com.ty.ty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ty.ty.MainActivity;
import com.ty.ty.R;
import com.ty.ty.base.App;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * Created by zhouyou on 2016/6/27.
 * Class desc: ui 操作相关封装
 */
public class UiUtils {

    /**
     * 获取上下文
     */
    public static Context getContext() {
        return App.getContext();
    }

    /**
     * 获取资源操作类
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取字符串资源
     *
     * @param id 资源id
     * @return 字符串
     */
    public static String getString(int id) {
        return getResources().getString(id);
    }

    /**
     * 获取字符串数组资源
     *
     * @param id 资源id
     * @return 字符串数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取颜色资源
     */
    public static int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * 获取颜色资源
     *
     * @param id    资源id
     * @param theme 样式
     * @return
     */
    public static int getColor(int id, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, theme);
        }
        return getResources().getColor(id);
    }

    /**
     * 获取drawable资源
     *
     * @param id 资源id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 通过图片名称获取图片资源 id
     * @param imageName 图片名称
     * @return 图片资源 id
     */
    public static int getImageResIdByName(String imageName){
        return getResources().getIdentifier(imageName, "mipmap"
                , AppUtils.getPackageName());
    }

    /**
     * 加载布局（使用View方式）
     *
     * @param resource 布局资源id
     * @return View
     */
    public static View inflate(int resource) {
        return View.inflate(getContext(), resource, null);
    }

    /**
     * 检查输入的内容是否为空
     */
    public static boolean checkEmpty(EditText editText) {
        if(TextUtils.isEmpty(editText.getText().toString())){
            ToastUtils.show(UiUtils.getContext(), UiUtils.getString(R.string.toast_input_not_empty));
            return true;
        }
        return false;
    }

    /**
     * 设置透明状态栏
     * @param activity
     */
    public static  void setStatusBar(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 对于4.4以上5.0以下版本，设置透明状态栏
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // 5.0及以上版本，设置透明状态栏
            Window window = activity.getWindow();
            // 清理4.4Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // 添加标志位
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置为透明
            window.setStatusBarColor(0);
        }
    }


    /**
     * 获取列表为空时显示的 Empty View
     * @return 默认 Empty View
     */
    public static View getEmptyView(Context context, RecyclerView recyclerView){
        return getEmptyView(context, recyclerView, null, -1);
    }

    /**
     * 获取列表为空时显示的 Empty View
     * @param emptyText 提示文字
     * @return Empty View
     */
    public static View getEmptyView(Context context, RecyclerView recyclerView, String emptyText){
        return getEmptyView(context, recyclerView, emptyText, -1);
    }

    /**
     * 获取列表为空时显示的 Empty View
     * @param emptyText  提示文字
     * @param emptyImgId 图片
     * @return Empty View
     */
    public static View getEmptyView(Context context, RecyclerView recyclerView, String emptyText, int emptyImgId){
        View emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, (ViewGroup) recyclerView.getParent(), false);
        if(emptyText != null){
            ((TextView)emptyView.findViewById(R.id.txt_empty)).setText(emptyText);
        }
        if(emptyImgId != -1){
            ((ImageView)emptyView.findViewById(R.id.img_empty)).setImageResource(emptyImgId);
        }
        return emptyView;
    }

    /** 显示不带 null 的字符 */
    public static String show(String text){
        return text != null ? text : "";
    }

    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (char curchar : input) {
                if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else {
                    output += java.lang.Character.toString(curchar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 根据名字查找电话
     */
    public static String nameFindCall(Context context, String name) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(contactName)) {
                Cursor phone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                if (phone.moveToNext()) {
                    String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    return phoneNumber;
                }
            }
        }
        return null;
    }

    /**
     * 进入首页
     */
    public static void enterHomePage(Activity context){
        ActivityStack.getInstance().popAllActivity();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.finish();
    }

}
