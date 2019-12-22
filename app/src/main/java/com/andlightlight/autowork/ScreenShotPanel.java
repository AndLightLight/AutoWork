package com.andlightlight.autowork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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

    public ScreenShotPanel(Context context) {
        super(context);
        mFloatPanelService = (FloatPanelService) context;
    }

    @Override
    protected void onShow() {
        if (mRoot != null)
            mRoot.setAlpha(ALPHA);
        mLayoutParams.width = START_WIDTH;
        mLayoutParams.height = START_HEIGHT;
        mLayoutParams.x = START_X;
        mLayoutParams.y = START_Y;
        mWindowManager.updateViewLayout(mRoot,mLayoutParams);
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
        mLayoutParams.width = START_WIDTH;
        mLayoutParams.height = START_HEIGHT;
        mLayoutParams.x = START_X;
        mLayoutParams.y = START_Y;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRoot = inflater.inflate(R.layout.functionpanel, null);
        mWindowManager.addView(mRoot, mLayoutParams);









        if (true) return;
        mRoot = new FrameLayout(mContext);
        mWindowManager.addView(mRoot, mLayoutParams);

        mRoot.setBackgroundColor(Color.GREEN);
        mRoot.setAlpha(ALPHA);

        mRoot.setOnTouchListener(new View.OnTouchListener() {
            private int x;
            private int y;
            private int bound = 0;

            public int GetTouchBound(View v, MotionEvent e){
                int height = v.getHeight();
                int width = v.getWidth();
                float posx = e.getX();
                float posy = e.getY();
                int xb = v.getWidth()/5;
                int yb = v.getWidth()/5;

                if (posx < xb){
                    if (posy <  height - yb){
                        return 4;
                    }
                    else{
                        return 3;
                    }
                }
                else if (posx > width - xb){
                    if ( posy > yb){
                        return 2;
                    }
                    else{
                        return 1;
                    }
                }
                else{
                    if (posy < yb){
                        return 1;
                    }
                    else if (posy > height - yb){
                        return 3;
                    }
                    else{
                        return 0;
                    }
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        bound = GetTouchBound(v,event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;

                        int reHeight = mLayoutParams.height;
                        int reWidth = mLayoutParams.width;
                        if (bound == 0){

                        }
                        else if (bound == 1){
                            reHeight = reHeight - movedY;
                            movedX = 0;
                        }
                        else if (bound == 2){
                            reWidth = reWidth + movedX;
                            movedX = 0;
                            movedY = 0;
                        }
                        else if (bound == 3){
                            reHeight = reHeight + movedY;
                            movedY = 0;
                            movedX = 0;
                        }
                        else if (bound == 4){
                            reWidth = reWidth - movedX;
                            movedY = 0;
                        }
                        Log.e(TAG, "onTouch: " + bound + " nowY:" + nowY + " reHeight:" + reHeight );
                        mLayoutParams.height = reHeight;
                        mLayoutParams.width = reWidth;
                        mLayoutParams.x = mLayoutParams.x + movedX;
                        mLayoutParams.y = mLayoutParams.y + movedY;
                        mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    public void screenShot(final String path){
        int []location=new int[2];
        mRoot.getLocationOnScreen(location);
        final int x = location[0];
        final int y = location[1];
        final int w = mRoot.getWidth();
        final int h = mRoot.getHeight();
        mRoot.setAlpha(0);
        mRoot.postDelayed(new Runnable() {
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
