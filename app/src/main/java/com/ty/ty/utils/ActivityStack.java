package com.ty.ty.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ListIterator;
import java.util.Stack;


/**
 * @author airsaid
 *
 * activity manager stack.
 */
public class ActivityStack {

    private static ActivityStack sInstance = new ActivityStack();
    private Stack<WeakReference<Activity>> mStack = new Stack<>();

    private ActivityStack() {}

    public static ActivityStack getInstance() {
        return sInstance;
    }

    /**
     * 将指定 activity 推入栈中.
     * @param act 要入栈的 activity.
     */
    public void push(Activity act) {
        mStack.push(new WeakReference<>(act));
    }

    /**
     * 将栈顶 activity 出栈.
     */
    public void pop() {
        if(!mStack.isEmpty()){
            mStack.pop();
        }
    }

    /**
     * 清空栈.
     */
    public void clear() {
        mStack.clear();
    }

    /**
     * 退出栈中所有 activity，并 finish 掉.
     */
    public void popAllActivity() {
        popAllActivity(null);
    }

    /**
     * 退出栈中除指定的 activity 外的所有 activity.
     * @param cls 指定不出栈的 activity class.
     */
    public void popAllActivity(Class cls) {
        ListIterator<WeakReference<Activity>> iterator = mStack.listIterator(mStack.size());
        while (iterator.hasPrevious()){
            WeakReference<Activity> weakAct = iterator.previous();
            if(weakAct != null && mStack.contains(weakAct)){
                Activity act = weakAct.get();
                if(act != null && !act.getClass().equals(cls)){
                    iterator.remove();
                    if(!act.isFinishing()){
                        act.finish();
                    }
                }
            }
        }
    }
}