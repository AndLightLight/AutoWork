package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

public class FloatPanel extends BasePanel{

    static final String TAG = "FloatPanel";

    String mSaveFilePath;

    FloatPanelService mFloatPanelService;

    static final int OVER_PANEL_OPEN_WIDTH = 500;
    static final int OVER_PANEL_OPEN_HEIGHT = 500;
    static final int OVER_PANEL_START_X = 300;
    static final int OVER_PANEL_START_Y = 300;
    static final int OVER_PANEL_COMPARE_WIDTH = 900;
    static final int OVER_PANEL_COMPARE_HEIGHT = 1600;

    public FloatPanel(Context context) {
        super(context);
        mFloatPanelService = (FloatPanelService) context;
        mSaveFilePath = mFloatPanelService.getExternalFilesDir(null) + "/" + "test1.png";
    }

    // callback invoked either when the gesture has been completed or cancelled
    AccessibilityService.GestureResultCallback callback = new AccessibilityService.GestureResultCallback() {
        @Override
        public void onCompleted(GestureDescription gestureDescription) {
            super.onCompleted(gestureDescription);
            Log.d(TAG, "gesture completed");
        }

        @Override
        public void onCancelled(GestureDescription gestureDescription) {
            super.onCancelled(gestureDescription);
            Log.d(TAG, "gesture cancelled");
        }
    };

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    @Override
    protected void onCreate() {
        if (Settings.canDrawOverlays(mContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mLayoutParams.width = OVER_PANEL_OPEN_WIDTH;
            mLayoutParams.height = OVER_PANEL_OPEN_HEIGHT;
            mLayoutParams.x = OVER_PANEL_START_X;
            mLayoutParams.y = OVER_PANEL_START_Y;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            mRoot = inflater.inflate(R.layout.floatpanel_main, null);
            mWindowManager.addView(mRoot, mLayoutParams);

            final LinearLayout linearLayoutRoot = mRoot.findViewById(R.id.linear1);
            final Button bt0 = mRoot.findViewById(R.id.button0);
            final Button bt1 = mRoot.findViewById(R.id.button1);
            final Button bt2 = mRoot.findViewById(R.id.button2);
            final Button bt3 = mRoot.findViewById(R.id.button3);
            final Button bt4 = mRoot.findViewById(R.id.button4);
            final Button bt5 = mRoot.findViewById(R.id.button5);
            final Button bt6 = mRoot.findViewById(R.id.button6);
            final EditText et0 = mRoot.findViewById(R.id.editText0);
            final EditText et1 = mRoot.findViewById(R.id.editText1);
            bt0.setOnClickListener(new View.OnClickListener() {
                boolean isOpen = true;

                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        mLayoutParams.width = bt0.getWidth();
                        mLayoutParams.height = bt0.getHeight();
                    } else {
                        mLayoutParams.width = OVER_PANEL_OPEN_WIDTH;
                        mLayoutParams.height = OVER_PANEL_OPEN_HEIGHT;
                    }
                    mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                    isOpen = isOpen == false;
                }
            });
            bt5.setOnClickListener(new View.OnClickListener() {
                boolean isFocuse = false;

                @Override
                public void onClick(View v) {
                    if (isFocuse) {
                        mLayoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                    } else {
                        mLayoutParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                    }
                    isFocuse = isFocuse == false;
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                boolean mIsRecord = false;
                GestureRecordPanel mPanel = new GestureRecordPanel(mContext);

                @Override
                public void onClick(View v) {
                    if (!mIsRecord)
                        mPanel.show();
                    else
                        mPanel.hide();
                    mIsRecord = mIsRecord == false;
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        boolean result = mFloatPanelService.dispatchGesture(GestureManager.RengQiu(), callback, null);
                    }
                }
            });
            bt1.setOnClickListener(new View.OnClickListener() {
                boolean mIsCompare = true;
                ImageView mCompareResult;

                @Override
                public void onClick(View v) {
                    //example
//                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//                    recycle(rootNode);
//                    boolean isc = rootNode.isClickable();
//                    rootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//
//
//
//                    TimerTask task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            boolean result = dispatchGesture(createClick(1080/4, 2160*2/(2*3)), callback, null);
//                        }
//                    };
//                    Timer timer = new Timer(true);
//                    timer.schedule(task,strToDateLong("2019-05-15 20:00:00"));

                    if (mIsCompare) {
                        Bitmap subimage = ToolUtls.getBitmapFromPath(mSaveFilePath);
                        subimage = ToolUtls.scaleBitmap(subimage, 1f / mFloatPanelService.GetImageScale());
                        Bitmap bitmap = mFloatPanelService.snapshotScreen();
                        try {
                            int featureDetector = FeatureDetector.FAST;
                            int descriptorExtractor = DescriptorExtractor.ORB;
                            try {
                                featureDetector = Integer.parseInt(et0.getText().toString());
                                descriptorExtractor = Integer.parseInt(et1.getText().toString());
                            } finally {
                                FloatPanelService.MatchResult mr = new FloatPanelService.MatchResult();
                                boolean find = ToolUtls.findSubImageWithCV(bitmap, subimage, featureDetector, descriptorExtractor, mr);


                                Mat largeImageRgb = new Mat();
                                Mat smallImageRgb = new Mat();
                                Imgproc.cvtColor(mr.largeImage, largeImageRgb, Imgproc.COLOR_RGBA2RGB, 1);
                                Imgproc.cvtColor(mr.smallImage, smallImageRgb, Imgproc.COLOR_RGBA2RGB, 1);
                                Mat outmapM = new Mat();
                                Features2d.drawMatches(largeImageRgb, mr.keyPointsLarge, smallImageRgb, mr.keyPointsSmall, mr.matchesFiltered, outmapM);
                                Bitmap outmap = Bitmap.createBitmap(outmapM.width(), outmapM.height(), Bitmap.Config.ARGB_8888);
                                Utils.matToBitmap(outmapM, outmap);

                                if (mCompareResult == null) {
                                    mCompareResult = new ImageView(mContext);
                                    linearLayoutRoot.addView(mCompareResult);
                                }

                                mLayoutParams.width = OVER_PANEL_COMPARE_WIDTH;
                                mLayoutParams.height = OVER_PANEL_COMPARE_HEIGHT;
                                mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                                mCompareResult.setImageBitmap(outmap);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "FindSubImageWithCV ");
                        }
                    } else {
                        mLayoutParams.width = OVER_PANEL_OPEN_WIDTH;
                        mLayoutParams.height = OVER_PANEL_OPEN_HEIGHT;
                        mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                    }
                    mIsCompare = mIsCompare == false;
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                boolean mIsJieTu = false;
                ScreenShotPanel mPanel = new ScreenShotPanel(mContext);
                WindowManager.LayoutParams layoutParams2;

                @Override
                public void onClick(View v) {
                    if (!mIsJieTu)
                        mPanel.show();
                    else
                        mPanel.screenShot(mSaveFilePath);
                    mIsJieTu = mIsJieTu == false;
                }
            });

            bt0.setOnTouchListener(new View.OnTouchListener() {
                private int x;
                private int y;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = (int) event.getRawX();
                            y = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int nowX = (int) event.getRawX();
                            int nowY = (int) event.getRawY();
                            int movedX = nowX - x;
                            int movedY = nowY - y;
                            x = nowX;
                            y = nowY;
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

            bt6.setOnClickListener(new View.OnClickListener() {
                boolean mIsStart = false;
                Thread thread;
                @Override
                public void onClick(View v) {
                    if (mIsStart == false){
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new ScriptMain().start();
                            }
                        });
                        thread.start();
                    }
                    else{
                        thread.interrupt();
                    }
                    mIsStart = mIsStart == false;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {

    }
}
