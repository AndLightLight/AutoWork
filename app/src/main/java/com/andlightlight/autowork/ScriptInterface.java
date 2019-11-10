package com.andlightlight.autowork;

import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class ScriptInterface {
    HashMap<String, FloatPanelService.PrepareImage> mLoadPreImage = new HashMap<>();
    protected int mFeatureDetector = FeatureDetector.FAST;
    protected int mDescriptorExtractor = DescriptorExtractor.BRIEF;
    protected FeatureDetector mFd = FeatureDetector.create(mFeatureDetector);
    protected DescriptorExtractor mExtractor = DescriptorExtractor.create(mDescriptorExtractor);
    protected Bitmap mScreeShopImageCache;
    protected int mDefaultNeedNum = 4;

    protected interface FindNodeCallBack{
        void run(AccessibilityNodeInfo node);
    }

    public void start(){
        mLoadPreImage.clear();
        File dir = new File(FloatPanelService.Instance.getExternalFilesDir(null) + "/");
        File[] files = dir.listFiles();
        for (File f : files){
            Bitmap bp = BitmapFactory.decodeFile(f.getPath());
            if (bp != null){
                String picnamewithdot = f.getName();
                String picname = picnamewithdot.substring(0,picnamewithdot.indexOf('.'));
                FloatPanelService.PrepareImage preimage = ToolUtls.prepareBitmap(bp,mFd,mExtractor);
                mLoadPreImage.put(picname,preimage);
            }
        }
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

    protected void click(float x, float y){
        click(new GestureManager.Point[]{new GestureManager.Point(x,y,100)});
    }

    protected void click(GestureManager.Point[] points){
        GestureManager.click(points);
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen(){
        return prepareSnapshotScreen(-1,-1,-1,-1);
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen(int left, int top, int right, int down){
        Bitmap ss = FloatPanelService.Instance.snapshotScreen();
        if (ss == null){
            ss = mScreeShopImageCache;
        }
        mScreeShopImageCache = ss;
        if (left != -1 && top != -1 && right != -1 && down != -1)
            ss = ToolUtls.cropBitmap(ss,left,top,right - left,down - top);

        return ToolUtls.prepareBitmap(ss,mFd,mExtractor);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, FloatPanelService.PrepareImage subpic, int similar, int needNum){
        return ToolUtls.findSubImageWithCV(orcpic,subpic,mFd,mExtractor,similar,needNum);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, String subpic, float similar){
        return FindPic(orcpic,subpic,similar,mDefaultNeedNum);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, String subpic, float similar, int needNum){
        return ToolUtls.findSubImageWithCV(orcpic,mLoadPreImage.get(subpic),mFd,mExtractor,similar,needNum);
    }

    protected FloatPanelService.MatchResult FindPic(int left, int top, int right, int down, String subpic, float similar){
        return FindPic(left, top, right, down, subpic, similar,mDefaultNeedNum);
    }

    protected FloatPanelService.MatchResult FindPic(int left, int top, int right, int down, String subpic, float similar, int needNum){
        return ToolUtls.findSubImageWithCV(prepareSnapshotScreen(left,top,right,down),mLoadPreImage.get(subpic),mFd,mExtractor,similar,needNum);
    }

    protected void OpenActivity(String packageName, String ActivityName){
        Intent intent = new Intent().setComponent(new ComponentName(packageName,ActivityName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FloatPanelService.Instance.startActivity(intent);
    }

    protected void ClickButton(String txtPattern){
        AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
        FindNode(rootNode, txtPattern, "android.widget.Button", new FindNodeCallBack() {
            @Override
            public void run(AccessibilityNodeInfo node) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        });
    }

    protected void ClickText(String txtPattern){
        AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
        FindNode(rootNode, txtPattern, "android.widget.TextView", new FindNodeCallBack() {
            @Override
            public void run(AccessibilityNodeInfo node) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        });
    }

    protected void FindNode(AccessibilityNodeInfo node, String txtPattern, String classPattern, FindNodeCallBack action){
        if (node == null)
            node = FloatPanelService.Instance.getRootInActiveWindow();
        if (node != null) {
            boolean isMatchText = true;
            boolean isMatchClass = true;
            if (txtPattern != null && txtPattern.compareTo("") != 0)
                isMatchText = Pattern.matches(txtPattern, node.getText());
            if (classPattern != null && classPattern.compareTo("") != 0)
                isMatchClass = Pattern.matches(classPattern, node.getClassName());
            if (isMatchText && isMatchClass)
                action.run(node);
            if (node.getChildCount() == 0) {
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        FindNode(node.getChild(i), txtPattern, classPattern, action);
                    }
                }
            }
        }
    }

    protected void RegisterEvent(int event, String packageName, Runnable action){
        FloatPanelService.Instance.RegisterEvent(event,packageName,action);
    }

    protected abstract void startImp() throws InterruptedException;
}
