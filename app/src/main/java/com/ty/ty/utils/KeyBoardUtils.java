package com.ty.ty.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by zhouyou on 2016/6/24.
 * Class desc:
 *
 * 软键盘封装工具类
 */
public class KeyBoardUtils {

    private KeyBoardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 显示软键盘.
     * @param context 上下文.
     * @param view    对应要输入的 EditText.
     */
    public static void show(Context context, EditText view){
        if(view.requestFocus()){
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 判断软键盘是否弹起
     */
    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
}