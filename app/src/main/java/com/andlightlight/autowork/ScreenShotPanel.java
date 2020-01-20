package com.andlightlight.autowork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

public class ScreenShotPanel extends BasePanel {

    static final String TAG = "ScreenShotPanel";

    FloatPanelService mFloatPanelService;

    static final int START_WIDTH = 768;
    static final int START_HEIGHT = 1024;
    static final int START_X = 300;
    static final int START_Y= 300;
    static final float ALPHA= 0.1f;

    @UIMake(value = R.id.buttonup,touch = "mButtonUpListener")
    Button buttonup;
    @UIMake(value = R.id.buttondown,touch = "mButtonDownListener")
    Button buttondown;
    @UIMake(value = R.id.buttonleft,touch = "mButtonLeftListener")
    Button buttonleft;
    @UIMake(value = R.id.buttonright,touch = "mButtonRightListener")
    Button buttonright;
    @UIMake(value = R.id.mainlayout,touch = "mMainLayoutListener")
    ConstraintLayout mainlayout;

    public ScreenShotPanel(Context context) {
        super(context);
        mFloatPanelService = (FloatPanelService) context;
    }

    @Override
    protected void onShow() {
        if (mainlayout != null)
            mainlayout.setAlpha(ALPHA);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
        params.leftMargin = START_X;
        params.topMargin = START_Y;
        params.width = START_WIDTH;
        params.height = START_HEIGHT;
        mainlayout.requestLayout();
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
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRoot = inflater.inflate(R.layout.functionpanel, null);
        mWindowManager.addView(mRoot, mLayoutParams);
    }

    PosTouchListener mMainLayoutListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
            params.leftMargin += dx;
            params.topMargin += dy;
            mainlayout.requestLayout();
        }
    });

    PosTouchListener mButtonUpListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
            params.topMargin += dy;
            params.height -= dy;
            mainlayout.requestLayout();
        }
    });

    PosTouchListener mButtonDownListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
            params.height += dy;
            mainlayout.requestLayout();
        }
    });

    PosTouchListener mButtonLeftListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
            params.leftMargin += dx;
            params.width -= dx;
            mainlayout.requestLayout();
        }
    });

    PosTouchListener mButtonRightListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainlayout.getLayoutParams();
            params.width += dx;
            mainlayout.requestLayout();
        }
    });

    @Override
    protected void onAfterCreate() {
        mainlayout.setClickable(true);
        mainlayout.setBackgroundColor(Color.GREEN);
        mainlayout.setAlpha(ALPHA);
    }

    public void screenShot(final String path){
        int []location=new int[2];
        mainlayout.getLocationOnScreen(location);
        final int x = location[0];
        final int y = location[1];
        final int w = mainlayout.getWidth();
        final int h = mainlayout.getHeight();
        mainlayout.setAlpha(0);
        mainlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap ss = mFloatPanelService.snapshotScreen();
                ss = ToolUtls.cropBitmap(ss,x,y,w,h);
                try {
                    ToolUtls.saveBitmap(ss, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hide();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {

    }
}
