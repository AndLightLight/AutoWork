package com.andlightlight.autowork;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class BasePanel {
    boolean mIsShow;
    boolean mIsCreate;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    View mRoot;
    Context mContext;

    public @interface UIMake {
        int value() default -1;
        String click() default "";
        String touch() default "";
    }

    public BasePanel(Context context){
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
    }

    public void show(){
        if (mIsShow) return;
        mIsShow = true;
        if (!mIsCreate){
            onCreate();
            mIsCreate = true;

            for (Field field : this.getClass().getFields()){
                UIMake uiMake = field.getAnnotation(UIMake.class);
                if (uiMake != null){
                    int value = uiMake.value();
                    String click = uiMake.click();
                    String touch = uiMake.touch();
                    if (value == -1)
                        value = mContext.getResources().getIdentifier(field.getName(), "layout", mContext.getPackageName());
                    try {
                        field.setAccessible(true);
                        field.set(this,mRoot.findViewById(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    fieldInvoke(field,"setOnClickListener",click);
                    fieldInvoke(field,"setOnTouchListener",touch);
                }
            }
        }
        if (mRoot != null)
            mRoot.setVisibility(View.VISIBLE);
        onShow();
    }

    private void fieldInvoke(Field field, String methodName, String paramFieldName){
        if (paramFieldName == "")
            return;
        Class c = field.getType();
        try {
            Field classfield = this.getClass().getField(paramFieldName);
            if (classfield != null){
                Method method = c.getMethod("setOnClickListener", View.OnClickListener.class);
                method.setAccessible(true);
                method.invoke(field.get(this),classfield.get(this));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void hide(){
        mIsShow = false;
        if (mRoot != null)
            mRoot.setVisibility(View.INVISIBLE);
        onHide();
    }

    public void destroy(){
        mIsShow = false;
        mIsCreate = false;
        if (mWindowManager != null && mRoot != null){
            mWindowManager.removeViewImmediate(mRoot);
        }
        onDestroy();
    }

    abstract protected void onShow();
    abstract protected void onHide();
    abstract protected void onCreate();
    abstract protected void onDestroy();
}
