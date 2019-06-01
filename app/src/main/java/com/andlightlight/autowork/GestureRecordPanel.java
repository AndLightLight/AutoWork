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

    public GestureRecordPanel(Context context) {
        super(context);
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

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
            StringBuilder mCode = new StringBuilder();
            Date mStartDate;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsStart = true;
                        mStartDate = new Date();
                        mCode.setLength(0);
                        mCode.append(String.format("\nPath clickPath = new Path();\nclickPath.moveTo(%ff, %ff);\n",event.getRawX(),event.getRawY()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mIsStart){
                            mCode.append(String.format("clickPath.lineTo(%ff, %ff);\n",event.getRawX(),event.getRawY()));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsStart = false;
                        Date now = new Date();
                        long diff = now.getTime() - mStartDate.getTime();
                        mCode.append(String.format("GestureDescription.StrokeDescription clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, %d);\n" +
                                "GestureDescription.Builder clickBuilder = new GestureDescription.Builder();\nclickBuilder.addStroke(clickStroke).build();\n",diff));
                        GestureManager.LogLong(TAG, String.valueOf(mCode));
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

    }
}
