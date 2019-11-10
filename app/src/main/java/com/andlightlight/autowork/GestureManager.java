package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestureManager {
    public static void LogLong(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    public static class Point{
        public float x;
        public float y;
        public long duration;
        public Point(float x, float y, long duration) {
            this.x = x;
            this.y = y;
            this.duration = duration;
        }
    }

    public static void click(final Point[] points){
        GestureDescription.StrokeDescription clickStroke = null;
        final List clickStrokeList = new ArrayList();
        Path clickPath = new Path();
        clickPath.moveTo(points[0].x, points[0].y);
        for (int i = 0;i < points.length; ++ i){
            Point p = points[i];
            clickPath.lineTo(p.x, p.y);

            if (clickStroke == null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, p.duration, (i < points.length - 1)?true:false);
                else
                    clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, p.duration);
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    clickStroke = clickStroke.continueStroke(clickPath,0,p.duration,(i < points.length - 1)?true:false);
                else
                    clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, p.duration);
            clickStrokeList.add(clickStroke);

            if (i < points.length - 1){
                clickPath = new Path();
                clickPath.moveTo(points[i].x, points[i].y);
            }
        }

        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
        final GestureDescription gd = clickBuilder.addStroke((GestureDescription.StrokeDescription) clickStrokeList.get(0)).build();
        AccessibilityService.GestureResultCallback callback = new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                clickStrokeList.remove(0);
                if (clickStrokeList.size() > 0)
                    FloatPanelService.Instance.dispatchGesture(new GestureDescription.Builder().addStroke((GestureDescription.StrokeDescription) clickStrokeList.get(0)).build(), this, null);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        };
        boolean result = FloatPanelService.Instance.dispatchGesture(gd, callback, null);
    }

    protected void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}
