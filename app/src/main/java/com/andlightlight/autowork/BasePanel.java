package com.andlightlight.autowork;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

    @Retention(RetentionPolicy.RUNTIME)
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

            for (Field field : this.getClass().getDeclaredFields()){
                field.setAccessible(true);
                UIMake uiMake = field.getAnnotation(UIMake.class);
                if (uiMake != null){
                    try {
                        int value = uiMake.value();
                        String click = uiMake.click();
                        String touch = uiMake.touch();
                        if (value == -1) {
                            for (Field idfield : R.id.class.getDeclaredFields()){
                                idfield.setAccessible(true);
                                if (idfield.getName().compareTo(field.getName()) == 0){
                                    value = (int) idfield.get(new R.id());
                                    break;
                                }
                            }
                        }

                        field.setAccessible(true);
                        field.set(this,mRoot.findViewById(value));
                        fieldInvoke(field,"setOnClickListener",View.OnClickListener.class, click);
                        fieldInvoke(field,"setOnTouchListener",View.OnTouchListener.class, touch);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            onAfterCreate();
        }
        if (mRoot != null)
            mRoot.setVisibility(View.VISIBLE);
        onShow();
    }

    protected void onAfterCreate(){};

    private void fieldInvoke(Field field, String methodName, Class methodClass, String paramFieldName){
        if (paramFieldName == "")
            return;
        Class c = field.getType();
        try {
            Field classfield = this.getClass().getDeclaredField(paramFieldName);
            classfield.setAccessible(true);
            if (classfield != null){
                Method method = c.getMethod(methodName, methodClass);
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
