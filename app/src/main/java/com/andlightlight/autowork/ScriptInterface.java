package com.andlightlight.autowork;

import android.accessibilityservice.GestureDescription;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import java.sql.PreparedStatement;

public abstract class ScriptInterface {
    protected int mFeatureDetector = FeatureDetector.FAST;
    protected int mDescriptorExtractor = DescriptorExtractor.ORB;
    protected FeatureDetector mFd = FeatureDetector.create(mFeatureDetector);
    protected DescriptorExtractor mExtractor = DescriptorExtractor.create(mDescriptorExtractor);
    public void start(){
        try {
            startImp();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
    protected void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }
    protected void click(PointF[] points, long duration){
        Path clickPath = new Path();
        clickPath.moveTo(points[0].x, points[0].y);
        for (PointF p : points){
            clickPath.lineTo(p.x, p.y);
        }
        GestureDescription.StrokeDescription clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, duration);
        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
        GestureDescription gd = clickBuilder.addStroke(clickStroke).build();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean result = FloatPanelService.Instance.dispatchGesture(gd, null, null);
        }
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen(){
        return prepareSnapshotScreen(-1,-1,-1,-1);
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen(int x, int y, int w, int h){
        Bitmap ss = FloatPanelService.Instance.snapshotScreen();
        if (x != -1 && y != -1 && w != -1 && h != -1)
            ss = ToolUtls.cropBitmap(ss,x,y,w,h);
        return ToolUtls.prepareBitmap(ss,mFd,mExtractor);
    }
    protected abstract void startImp() throws InterruptedException;
}
