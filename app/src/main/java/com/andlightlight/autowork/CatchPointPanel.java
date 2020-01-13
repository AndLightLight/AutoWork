package com.andlightlight.autowork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.IOException;

public class CatchPointPanel extends BasePanel {

    static final String TAG = "CatchPointPanel";

    FloatPanelService mFloatPanelService;

    static final float ALPHA= 0.0f;

    public CatchPointPanel(Context context) {
        super(context);
        //mFloatPanelService = (FloatPanelService) context;
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
        mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;

        mRoot = new FrameLayout(mContext);
        mWindowManager.addView(mRoot, mLayoutParams);

        mRoot.setBackgroundColor(Color.GREEN);
        mRoot.setAlpha(ALPHA);

        mRoot.setOnTouchListener(new View.OnTouchListener() {
            private int x;
            private int y;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case  MotionEvent.ACTION_UP:
                        Bitmap ss = mFloatPanelService.snapshotScreen();
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        int color = ss.getPixel(x,y);
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));
                        Log.i(TAG,String.format("Point.x:%d,y:%d,color:%s",x,y,hexColor));
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
