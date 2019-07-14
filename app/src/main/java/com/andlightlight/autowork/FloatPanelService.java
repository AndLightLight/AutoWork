package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.KeyPoint;

import java.nio.ByteBuffer;
import java.util.List;

public class FloatPanelService extends AccessibilityService {

    static public boolean isStarted;

    int mScale = 1;
    MediaProjection mMediaProjection;
    private int mResultCode;
    private Intent mResultData;
    ImageReader mImageReader;
    VirtualDisplay mVirtualDisplay;
    FloatPanel mFloatPanel;

    static class MatchResult{
        Mat largeImage;
        Mat smallImage;
        MatOfKeyPoint keyPointsLarge;
        MatOfKeyPoint keyPointsSmall;
        MatOfDMatch matchesFiltered;
        PointF rePoint = new PointF();
    }

    static class PrepareImage{
        Mat imageMat;
        Mat imageDesMat;
        MatOfKeyPoint keyPointsMat;
        KeyPoint[] keyPoints;
    }

    public static FloatPanelService Instance;


    String TAG = "FloatPanelService";

    public FloatPanelService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        Intent toA = new Intent(ActionBroadcastName.FloatPanelServiceOnCreate);
        sendBroadcast(toA);
    }

    public Bitmap snapshotScreen(){
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            Log.e(TAG, "image is null.");
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap;
    }

    public int GetImageScale() {
        return mScale;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mResultCode = intent.getIntExtra("code", -1);
        mResultData = intent.getParcelableExtra("data");

        mMediaProjection = ((MediaProjectionManager) getSystemService(this.MEDIA_PROJECTION_SERVICE)).getMediaProjection(mResultCode, mResultData);


        WindowManager window = (WindowManager) getSystemService(this.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels / mScale;
        int height = dm.heightPixels / mScale;

        if (mImageReader == null) {
            mImageReader = ImageReader.newInstance(width, height, 0x1, 2);
        }

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                width, height, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        isStarted = true;
        Log.d(TAG, "onServiceConnected: FloatPanelService" );

        if (mFloatPanel == null)
            mFloatPanel = new FloatPanel(this);
        mFloatPanel.show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    //example
//                findAndClickButtonByString(event, "立即购买");
//                findAndClickButtonByString(event, "确定");
//                findAndClickButtonByString(event, "提交订单");
            break;
            default:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        mFloatPanel.destroy();
        mFloatPanel = null;
        return super.onUnbind(intent);
    }

    public void findAndClickButtonByString(AccessibilityEvent event, String tx) {
        if (event.getSource() != null) {
            List<AccessibilityNodeInfo> list = event.getSource().findAccessibilityNodeInfosByText(tx);
            if (null != list) {
                for (AccessibilityNodeInfo info : list) {
                    //info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
}
