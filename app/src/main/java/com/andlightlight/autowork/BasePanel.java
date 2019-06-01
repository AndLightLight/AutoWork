package com.andlightlight.autowork;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

abstract public class BasePanel {
    boolean mIsShow;
    boolean mIsCreate;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    View mRoot;
    Context mContext;
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
        }
        if (mRoot != null)
            mRoot.setVisibility(View.VISIBLE);
        onShow();
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
