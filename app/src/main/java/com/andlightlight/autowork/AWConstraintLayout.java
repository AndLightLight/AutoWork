package com.andlightlight.autowork;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AWConstraintLayout extends ConstraintLayout {
    private DispatchTouchEventListener mDispatchTouchEventListener;

    public AWConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    interface DispatchTouchEventListener{
        void OnDispatchTouch(View view, MotionEvent ev);
    }

    public void setDispatchTouchEventListener(DispatchTouchEventListener dispatchTouchEventListener){
        mDispatchTouchEventListener = dispatchTouchEventListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean re = super.dispatchTouchEvent(ev);
        if (mDispatchTouchEventListener != null)
            mDispatchTouchEventListener.OnDispatchTouch(this, ev);
        return re;
    }
}
