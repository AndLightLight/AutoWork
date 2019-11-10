package com.andlightlight.autowork;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Date;

public class GestureRecordPanel extends BasePanel{
    static final String TAG = "GestureRecordPanel";
    StringBuilder mCode = new StringBuilder();

    public GestureRecordPanel(Context context) {
        super(context);
    }

    @Override
    protected void onShow() {
        mCode.setLength(0);
        mCode.append("Sart Record:\n");
    }

    @Override
    protected void onHide() {
        GestureManager.LogLong(TAG, String.valueOf(mCode));
    }

    @Override
    protected void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mRoot = new FrameLayout(mContext);
        mWindowManager.addView(mRoot, mLayoutParams);

        mRoot.setBackgroundColor(Color.GREEN);
        mRoot.setAlpha(0.1f);

        mRoot.setOnTouchListener(new View.OnTouchListener() {
            boolean mIsStart = false;
            boolean mIsFirstMove = true;
            Date mStartDate;
            Date mLastPointDate;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsStart = true;
                        mIsFirstMove = true;
                        if (mStartDate != null){
                            Date now = new Date();
                            long diff = now.getTime() - mStartDate.getTime();
                            mCode.append(String.format("sleep(%d);\n",diff));
                        }
                        mCode.append(String.format(
                                "click(new GestureManager.Point[]{new GestureManager.Point(%ff,%ff,%d)",event.getRawX(),event.getRawY(), 10));
                        mStartDate = new Date();
                        mLastPointDate = new Date();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mIsStart){
                            if (!mIsFirstMove) {
                                Date now = new Date();
                                long diff = now.getTime() - mLastPointDate.getTime();
                                mLastPointDate = now;
                                mCode.append(String.format(",new GestureManager.Point(%ff,%ff,%d)", event.getRawX(), event.getRawY(), diff));
                            }
                            mIsFirstMove = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsStart = false;
                        mCode.append(String.format("});\n"));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        GestureManager.LogLong(TAG, String.valueOf(mCode));
    }
}
