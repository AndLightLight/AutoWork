package com.andlightlight.autowork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
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

import com.andlightlight.autowork.script.DDSScript;
import com.andlightlight.autowork.script.MYSLScript;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class FloatPanel extends BasePanel{

    static final String TAG = "FloatPanel";

    String mSaveFilePath;
    boolean mMax = true;

    FloatPanelService mFloatPanelService;

    static final int OVER_PANEL_OPEN_WIDTH = 500;
    static final int OVER_PANEL_OPEN_HEIGHT = 500;
    static final int OVER_PANEL_START_X = 300;
    static final int OVER_PANEL_START_Y = 300;
    static final int OVER_PANEL_COMPARE_WIDTH = 900;
    static final int OVER_PANEL_COMPARE_HEIGHT = 1600;

    public static FloatPanel Instance;

    @UIMake(R.id.linear1)
    LinearLayout linearLayoutRoot;
    @UIMake(value = R.id.button0,click = "mOpenClickListener",touch = "mMoveTouchListener")
    Button bt0;
    @UIMake(value = R.id.button1,click = "mCompareClickListener")
    Button bt1;
    @UIMake(value = R.id.button2,click = "mRecordClickListener")
    Button bt2;
    @UIMake(value = R.id.button3,click = "mJieTuClickListener")
    Button bt3;
    @UIMake(value = R.id.button4,click = "mBoFangClickListener")
    Button bt4;
    @UIMake(value = R.id.button5,click = "mFocuseClickListener")
    Button bt5;
    @UIMake(value = R.id.button6,click = "mRunClickListener")
    Button bt6;
    @UIMake(value = R.id.button7,click = "mCloseClickListener")
    Button bt7;
    @UIMake(value = R.id.button8,click = "mPixelClickListener")
    Button bt8;
    @UIMake(value = R.id.editText0)
    EditText et0;
    @UIMake(value = R.id.editText1)
    EditText et1;
    @UIMake(value = R.id.editText2)
    EditText et2;
    @UIMake(value = R.id.editText3)
    EditText et3;
    @UIMake(value = R.id.editText4)
    EditText et4;
    @UIMake(value = R.id.editText5)
    EditText et5;
    @UIMake(value = R.id.editText6)
    EditText et6;
    @UIMake(value = R.id.editText7)
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

    View.OnTouchListener mMoveTouchListener = new PosTouchListener(new PosTouchListener.PosChangeCB() {
        @Override
        public void run(int dx, int dy) {
            mLayoutParams.x = mLayoutParams.x + dx;
            mLayoutParams.y = mLayoutParams.y + dy;
            mWindowManager.updateViewLayout(mRoot, mLayoutParams);
        }
    });

    View.OnClickListener mOpenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (FloatPanel.this.mMax) {
                min();
            } else {
                max();
            }
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

        }
    };

    View.OnClickListener mCompareClickListener = new View.OnClickListener() {
        boolean mIsCompare = true;
        ImageView mCompareResult;

        @Override
        public void onClick(View v) {
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
                    Bitmap outmap = null;
                    if (featureDetector <= 0){
                        //List<ToolUtls.Match> reslutlist = ToolUtls.findColors(bitmap, "#1DA06D", new ToolUtls.ColorPos[]{new ToolUtls.ColorPos(50, 44, "#1DA06D"),new ToolUtls.ColorPos(50, 38, "#1DA06D"),new ToolUtls.ColorPos(20,10, "#FFFFFF"),new ToolUtls.ColorPos(50,32, "#FFFFFF"),new ToolUtls.ColorPos(30,32, "#FFFFFF")},0.9f,null);
                        List<ToolUtls.ImgMatch> reslutlist = ToolUtls.findSubImage(bitmap, subimage, Imgproc.TM_CCOEFF_NORMED, 0.9f, ToolUtls.MAX_LEVEL_AUTO);
                        outmap = Bitmap.createBitmap(bitmap.getWidth() + subimage.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas cnvs = new Canvas(outmap);
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.STROKE);
                        cnvs.drawBitmap(bitmap,0,0,paint);
                        cnvs.drawBitmap(subimage,bitmap.getWidth(),0,paint);
                        for (ToolUtls.Match r : reslutlist){
                            cnvs.drawRect(new Rect((int)(r.point.x),(int)(r.point.y),(int)(r.point.x + subimage.getWidth()), (int)(r.point.y + subimage.getHeight())),paint);
                        }
                    }
                    else{
                        ToolUtls.ImgFtMatch mr = ToolUtls.findSubImageWithFeature(bitmap, subimage, featureDetector, descriptorExtractor, simil,4);
                        Mat largeImageRgb = new Mat();
                        Mat smallImageRgb = new Mat();
                        Imgproc.cvtColor(mr.largeImage, largeImageRgb, Imgproc.COLOR_RGBA2RGB, 1);
                        Imgproc.cvtColor(mr.smallImage, smallImageRgb, Imgproc.COLOR_RGBA2RGB, 1);
                        Mat outmapM = new Mat();
                        Features2d.drawMatches(largeImageRgb, mr.keyPointsLarge, smallImageRgb, mr.keyPointsSmall, mr.matchesFiltered, outmapM);
                        outmap = Bitmap.createBitmap(outmapM.width(), outmapM.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(outmapM, outmap);
                    }

                    if (mCompareResult == null) {
                        mCompareResult = new ImageView(mContext);
                        linearLayoutRoot.addView(mCompareResult);
                    }

                    mLayoutParams.width = OVER_PANEL_COMPARE_WIDTH;
                    mLayoutParams.height = OVER_PANEL_COMPARE_HEIGHT;
                    mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                    mCompareResult.setImageBitmap(outmap);
                } catch (Exception e) {
                    Log.e(TAG, "FindSubImageWithCV error: " + ToolUtls.getExceptionAllinformation(e));
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
        ScriptInterface script;
        @Override
        public void onClick(View v) {
            if (mIsStart == false){
                script = new DDSScript();
                script.start(new Runnable() {
                    @Override
                    public void run() {
                        mIsStart = false;
                    }
                });
            }
            else{
                script.interrupt();
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

    View.OnClickListener mPixelClickListener = new View.OnClickListener() {
        boolean mIsStart = false;
        CatchPointPanel mPanel = new CatchPointPanel(mContext);
        @Override
        public void onClick(View v) {
            if (mIsStart == false)
                mPanel.show();
            else
                mPanel.hide();
            mIsStart = mIsStart == false;
        }
    };

    public void min(){
        mRoot.post(new Runnable() {
            @Override
            public void run() {
                mLayoutParams.width = bt0.getWidth();
                mLayoutParams.height = bt0.getHeight();
                mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                mMax = false;
            }
        });
    }

    public void max(){
        mRoot.post(new Runnable() {
            @Override
            public void run() {
                mLayoutParams.width = OVER_PANEL_OPEN_WIDTH;
                mLayoutParams.height = OVER_PANEL_OPEN_HEIGHT;
                mWindowManager.updateViewLayout(mRoot, mLayoutParams);
                mMax = true;
            }
        });
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
        }
    }

    @Override
    protected void onDestroy() {

    }
}
