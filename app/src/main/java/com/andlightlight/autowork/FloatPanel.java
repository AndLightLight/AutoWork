package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
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
    String mSaveFileName = "ScreenShot";

    FloatPanelService mFloatPanelService;

    static final int OVER_PANEL_OPEN_WIDTH = 500;
    static final int OVER_PANEL_OPEN_HEIGHT = 500;
    static final int OVER_PANEL_START_X = 300;
    static final int OVER_PANEL_START_Y = 300;
    static final int OVER_PANEL_COMPARE_WIDTH = 900;
    static final int OVER_PANEL_COMPARE_HEIGHT = 1600;

    public static FloatPanel Instance;


    LinearLayout linearLayoutRoot;
    Button bt0;
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt7;
    EditText et0;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;
    EditText et6;
    EditText et7;

    public FloatPanel(Context context) {
        super(context);
        mFloatPanelService = (FloatPanelService) context;
        Instance = this;
    }

    public String getSaveFilePath(){
        return mSaveFilePath = mFloatPanelService.getExternalFilesDir(null) + "/" + et6.getText().toString() + ".png";
    }

    @Override
    protected void onShow() {

    }

    @Override
    protected void onHide() {

    }

    View.OnTouchListener mMoveTouchListener = new View.OnTouchListener() {
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
    };

    View.OnClickListener mOpenClickListener = new View.OnClickListener() {
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
    };

    View.OnClickListener mFocuseClickListener = new View.OnClickListener() {
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
    };

    View.OnClickListener mRecordClickListener = new View.OnClickListener() {
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
    };

    View.OnClickListener mBoFangClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                GestureManager.click(new PointF[]{new PointF(568.970947f,1390.957031f),new PointF(523.102295f,1380.996704f),new PointF(476.471558f,1355.976563f),new PointF(412.959595f,1317.919922f),new PointF(379.456787f,1293.896484f),new PointF(361.329620f,1280.194702f),new PointF(354.968262f,1275.410156f),new PointF(339.955444f,1245.410156f),new PointF(330.463257f,1236.474609f),new PointF(327.252350f,1222.673828f),new PointF(316.966553f,1189.453125f),new PointF(312.231049f,1151.238525f),new PointF(309.468384f,1137.421875f),new PointF(310.968018f,1122.421875f),new PointF(310.968018f,1110.644653f),new PointF(310.968018f,1106.953125f),new PointF(321.465454f,1088.964844f),new PointF(336.068848f,1075.299194f),new PointF(361.861847f,1053.015015f),new PointF(430.460815f,1020.908203f),new PointF(452.865234f,1012.250061f),new PointF(471.461792f,1006.904297f),new PointF(494.887115f,1001.634338f),new PointF(507.963867f,999.462891f),new PointF(525.975952f,997.968750f),new PointF(547.465210f,997.968750f),new PointF(562.998169f,997.968750f),new PointF(631.891785f,1030.233398f),new PointF(644.463501f,1036.962891f),new PointF(654.975037f,1054.193726f),new PointF(680.591248f,1085.481079f),new PointF(686.964111f,1092.919922f),new PointF(691.463013f,1120.429688f),new PointF(697.461548f,1138.886719f),new PointF(695.961914f,1164.404297f),new PointF(695.961914f,1177.703491f),new PointF(695.961914f,1188.955078f),new PointF(691.640259f,1211.026733f),new PointF(681.792603f,1239.885132f),new PointF(677.966309f,1249.453125f),new PointF(671.939453f,1256.014038f),new PointF(662.576965f,1268.693359f),new PointF(653.972168f,1272.480469f),new PointF(650.351868f,1274.905518f),new PointF(633.966064f,1273.945313f),new PointF(620.596802f,1272.109375f),new PointF(609.971924f,1269.462891f),new PointF(588.318848f,1258.124878f),new PointF(547.721985f,1231.196289f),new PointF(503.850952f,1205.249268f),new PointF(466.244598f,1180.323608f),new PointF(438.469849f,1159.921875f),new PointF(439.969482f,1155.439453f),new PointF(439.969482f,1141.904297f),new PointF(439.969482f,1125.410156f),new PointF(454.965820f,1117.968750f),new PointF(463.963623f,1110.439453f),new PointF(478.959961f,1106.953125f),new PointF(488.216583f,1106.953125f),new PointF(501.035034f,1109.478027f),new PointF(504.964600f,1111.435547f),new PointF(534.932617f,1139.931641f),new PointF(538.467407f,1158.457031f),new PointF(539.967041f,1172.312744f),new PointF(539.967041f,1175.419922f),new PointF(539.967041f,1188.925781f),new PointF(539.967041f,1199.443359f),new PointF(539.967041f,1205.419922f),new PointF(539.967041f,1206.909180f),new PointF(536.967773f,1205.917969f),new PointF(529.469604f,1205.917969f),new PointF(530.969238f,1203.925781f)},1187);
            }
        }
    };

    View.OnClickListener mCompareClickListener = new View.OnClickListener() {
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
                Bitmap subimage = ToolUtls.getBitmapFromPath(getSaveFilePath());
                subimage = ToolUtls.scaleBitmap(subimage, 1f / mFloatPanelService.GetImageScale());
                Bitmap bitmap = mFloatPanelService.snapshotScreen();

                try {
                    int featureDetector = FeatureDetector.FAST;
                    int descriptorExtractor = DescriptorExtractor.BRIEF;
                    int findLeft = -1;
                    int findTop = -1;
                    int findRight = -1;
                    int findBottom = -1;
                    float simil = 0.9f;
                    try {
                        featureDetector = Integer.parseInt(et4.getText().toString());
                        descriptorExtractor = Integer.parseInt(et5.getText().toString());
                    }
                    catch (Exception e){}
                    try {
                        findLeft = Integer.parseInt(et0.getText().toString());
                        findTop = Integer.parseInt(et1.getText().toString());
                        findRight = Integer.parseInt(et2.getText().toString());
                        findBottom = Integer.parseInt(et3.getText().toString());
                    }
                    catch (Exception e){}
                    try {
                        simil = (float) Double.parseDouble(et7.getText().toString());
                    }
                    catch (Exception e){}

                    if (findLeft >= 0 && findTop >= 0 && findRight >= 0 && findBottom >= 0){
                        bitmap = ToolUtls.cropBitmap(bitmap,findLeft,findTop,findRight - findLeft,findBottom - findTop);
                    }
                    FloatPanelService.MatchResult mr = ToolUtls.findSubImageWithCV(bitmap, subimage, featureDetector, descriptorExtractor, simil,4);
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
    };

    View.OnClickListener mJieTuClickListener = new View.OnClickListener() {
        boolean mIsJieTu = false;
        ScreenShotPanel mPanel = new ScreenShotPanel(mContext);
        WindowManager.LayoutParams layoutParams2;

        @Override
        public void onClick(View v) {
            if (!mIsJieTu)
                mPanel.show();
            else
                mPanel.screenShot(getSaveFilePath());
            mIsJieTu = mIsJieTu == false;
        }
    };

    View.OnClickListener mRunClickListener = new View.OnClickListener() {
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
    };

    View.OnClickListener mCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FloatPanelService.Instance.disableSelf();
        }
    };

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

            linearLayoutRoot = mRoot.findViewById(R.id.linear1);
            bt0 = mRoot.findViewById(R.id.button0);
            bt1 = mRoot.findViewById(R.id.button1);
            bt2 = mRoot.findViewById(R.id.button2);
            bt3 = mRoot.findViewById(R.id.button3);
            bt4 = mRoot.findViewById(R.id.button4);
            bt5 = mRoot.findViewById(R.id.button5);
            bt6 = mRoot.findViewById(R.id.button6);
            bt7 = mRoot.findViewById(R.id.button7);
            et0 = mRoot.findViewById(R.id.editText0);
            et1 = mRoot.findViewById(R.id.editText1);
            et2 = mRoot.findViewById(R.id.editText2);
            et3 = mRoot.findViewById(R.id.editText3);
            et4 = mRoot.findViewById(R.id.editText4);
            et5 = mRoot.findViewById(R.id.editText5);
            et6 = mRoot.findViewById(R.id.editText6);
            et7 = mRoot.findViewById(R.id.editText7);
            bt0.setOnClickListener(mOpenClickListener);
            bt5.setOnClickListener(mFocuseClickListener);
            bt2.setOnClickListener(mRecordClickListener);
            bt4.setOnClickListener(mBoFangClickListener);
            bt1.setOnClickListener(mCompareClickListener);
            bt3.setOnClickListener(mJieTuClickListener);
            bt0.setOnTouchListener(mMoveTouchListener);
            bt6.setOnClickListener(mRunClickListener);
            bt7.setOnClickListener(mCloseClickListener);
        }
    }

    @Override
    protected void onDestroy() {

    }
}
