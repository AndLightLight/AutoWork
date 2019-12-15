package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
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
import android.view.accessibility.AccessibilityWindowInfo;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.KeyPoint;

import java.nio.ByteBuffer;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FloatPanelService extends AccessibilityService {

    static public boolean isStarted;

    int mScale = 1;
    MediaProjection mMediaProjection;
    private int mResultCode;
    private Intent mResultData;
    ImageReader mImageReader;
    VirtualDisplay mVirtualDisplay;
    FloatPanel mFloatPanel;

    HashMap<Integer, Set<Runnable>> mEventMap = new HashMap<>();

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

    public Rect getScreenSize(){
        WindowManager window = (WindowManager) getSystemService(this.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getRealMetrics(dm);
        return new Rect(0,0,dm.widthPixels,dm.heightPixels);
    }

    public FloatPanel getPanel(){
        return mFloatPanel;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mResultCode = intent.getIntExtra("code", -1);
        mResultData = intent.getParcelableExtra("data");

        mMediaProjection = ((MediaProjectionManager) getSystemService(this.MEDIA_PROJECTION_SERVICE)).getMediaProjection(mResultCode, mResultData);
        int width = getScreenSize().width() / mScale;
        int height = getScreenSize().height() / mScale;

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

    void recycle(AccessibilityNodeInfo node){
        if (node != null) {
            Log.i("test2", String.format("class:%s, text:%s, Resource:%s, ContentDes:%s, Paneltxt:%s", node.getClassName(), node.getText(), node.getViewIdResourceName(), node.getContentDescription(), node.getPaneTitle()));
            if (node.getChildCount() == 0) {
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        recycle(node.getChild(i));
                    }
                }
            }
        }
    }

    public void RegisterEvent(int event, final Runnable action){
        Set v = mEventMap.get(event);
        if (v == null)
            mEventMap.put(event,new HashSet<Runnable>(){{add(action);}});
        else
            v.add(action);
    }

    public void RemoveEvent(int event, final Runnable action){
        Set v = mEventMap.get(event);
        if (v != null)
            v.remove(action);
    }

    public void RemoveAllEvent(){
        mEventMap.clear();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
       Set v = mEventMap.get(event.getEventType());
        if (v != null) {
            for (Object a : v) {
                Runnable action = (Runnable) a;
                action.run();
            }
        }
        Log.i("test2",String.format("event:%s, package:%s, class:%s", event.toString(), event.getPackageName(), event.getClassName()));
        int eventType = event.getEventType();
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    //example
//                findAndClickButtonByString(event, "立即购买");
//                findAndClickButtonByString(event, "确定");
//                findAndClickButtonByString(event, "提交订单");

//                List<AccessibilityWindowInfo> windowList = getWindows();
//                boolean isc = rootNode.isClickable();
//                rootNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);



//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    boolean result = dispatchGesture(createClick(1080/4, 2160*2/(2*3)), callback, null);
//                }
//            };
//            Timer timer = new Timer(true);
//            timer.schedule(task,strToDateLong("2019-05-15 20:00:00"));
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
