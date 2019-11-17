package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

public abstract class ScriptInterface {
    HashMap<String, FloatPanelService.PrepareImage> mLoadPreImage = new HashMap<>();
    protected int mFeatureDetector = FeatureDetector.FAST;
    protected int mDescriptorExtractor = DescriptorExtractor.BRIEF;
    protected FeatureDetector mFd = FeatureDetector.create(mFeatureDetector);
    protected DescriptorExtractor mExtractor = DescriptorExtractor.create(mDescriptorExtractor);
    protected Bitmap mScreeShopImageCache;
    protected int mDefaultNeedNum = 4;
    protected Runnable mEndAction;

    protected interface FindNodeCallBack {
        boolean run(AccessibilityNodeInfo node) throws InterruptedException;
    }

    public void start(Runnable endAction) {
        mEndAction = endAction;
        mLoadPreImage.clear();
        File dir = new File(FloatPanelService.Instance.getExternalFilesDir(null) + "/");
        File[] files = dir.listFiles();
        for (File f : files) {
            Bitmap bp = BitmapFactory.decodeFile(f.getPath());
            if (bp != null) {
                String picnamewithdot = f.getName();
                String picname = picnamewithdot.substring(0, picnamewithdot.indexOf('.'));
                FloatPanelService.PrepareImage preimage = ToolUtls.prepareBitmap(bp, mFd, mExtractor);
                mLoadPreImage.put(picname, preimage);
            }
        }
        try {
            startImp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        end();
    }

    protected void end(){
        FloatPanelService.Instance.RemoveAllEvent();
        try {
            endImp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mEndAction != null)
            mEndAction.run();
    }


    protected void sleep(int time) throws InterruptedException {
        Thread.sleep(time);
    }

    protected void click(float x, float y) {
        click(new GestureManager.Point[]{new GestureManager.Point(x, y, 100)});
    }

    protected void click(GestureManager.Point[] points) {
        GestureManager.click(points);
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen() {
        return prepareSnapshotScreen(-1, -1, -1, -1);
    }

    protected FloatPanelService.PrepareImage prepareSnapshotScreen(int left, int top, int right, int down) {
        Bitmap ss = FloatPanelService.Instance.snapshotScreen();
        if (ss == null) {
            ss = mScreeShopImageCache;
        }
        mScreeShopImageCache = ss;
        if (left != -1 && top != -1 && right != -1 && down != -1)
            ss = ToolUtls.cropBitmap(ss, left, top, right - left, down - top);

        return ToolUtls.prepareBitmap(ss, mFd, mExtractor);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, FloatPanelService.PrepareImage subpic, int similar, int needNum) {
        return ToolUtls.findSubImageWithCV(orcpic, subpic, mFd, mExtractor, similar, needNum);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, String subpic, float similar) {
        return FindPic(orcpic, subpic, similar, mDefaultNeedNum);
    }

    protected FloatPanelService.MatchResult FindPic(FloatPanelService.PrepareImage orcpic, String subpic, float similar, int needNum) {
        return ToolUtls.findSubImageWithCV(orcpic, mLoadPreImage.get(subpic), mFd, mExtractor, similar, needNum);
    }

    protected FloatPanelService.MatchResult FindPic(int left, int top, int right, int down, String subpic, float similar) {
        return FindPic(left, top, right, down, subpic, similar, mDefaultNeedNum);
    }

    protected FloatPanelService.MatchResult FindPic(int left, int top, int right, int down, String subpic, float similar, int needNum) {
        return ToolUtls.findSubImageWithCV(prepareSnapshotScreen(left, top, right, down), mLoadPreImage.get(subpic), mFd, mExtractor, similar, needNum);
    }

    public String getPackageName(String appName) {
        PackageManager packageManager = FloatPanelService.Instance.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : installedApplications) {
            if (packageManager.getApplicationLabel(applicationInfo).toString().equals(appName)) {
                return applicationInfo.packageName;
            }
        }
        return null;
    }

    protected void OpenApp(String appName) {
        PackageManager packageManager = FloatPanelService.Instance.getPackageManager();
        FloatPanelService.Instance.startActivity(packageManager.getLaunchIntentForPackage(getPackageName(appName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    protected void waitText(final String[] uitxts) {
        final Semaphore semaphore = new Semaphore(0);
        RegisterEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, new Runnable() {
            @Override
            public void run() {
                try {
                    AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
                    boolean isfind = FindNode(rootNode, uitxts, null, new FindNodeCallBack() {
                        private Set<String> mNeedUI = new HashSet<String>(){{for (String txt: uitxts) add(txt);}};
                        @Override
                        public boolean run(AccessibilityNodeInfo node) {
                            mNeedUI.remove(node.getText().toString());
                            if (mNeedUI.isEmpty())
                                return true;
                            else
                                return false;
                        }
                    });
                    if (isfind) {
                        semaphore.release();
                        RemoveEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,this);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    protected void click(String txtPattern) {
        click(new String[]{txtPattern}, null, 1, true, false);
    }

    protected void clickCurrentUI(String txtPattern, final boolean isClickAll) throws InterruptedException {
        clickCurrentUI(new String[]{txtPattern}, null, isClickAll);
    }

    protected boolean clickCurrentUI(final String[] txtPatterns, final String classPattern, final boolean isClickAll) throws InterruptedException {
        AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
        boolean isfind = FindNode(rootNode, txtPatterns, classPattern, new FindNodeCallBack() {
            @Override
            public boolean run(AccessibilityNodeInfo node) throws InterruptedException {
//                if (node.isClickable())
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                else {
                    Rect b = new Rect();
                    node.getBoundsInScreen(b);
                    click(b.centerX(), b.centerY());
//                }
                if (!isClickAll)
                    return true;
                else {
                    sleep(300);
                    return false;
                }
            }
        });
        return isfind;
    }

    protected void click(final String[] txtPatterns, final String classPattern, final int times, boolean isBlock, final boolean isClickAll) {
        if (isClickAll)
            isBlock = false;
        final boolean finalisBlock = isBlock;
        final Semaphore semaphore = new Semaphore(0);
        RegisterEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, new Runnable() {
            private int runtimes = times;
            @Override
            public void run() {
                try {
                    if (runtimes > 0 || runtimes < 0) {
                        boolean isfind = clickCurrentUI(txtPatterns, classPattern, isClickAll);
                        if (isfind)
                            runtimes--;
                    }
                    if (runtimes == 0) {
                        RemoveEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, this);
                        if (finalisBlock)
                            semaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if (finalisBlock){
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean FindNode(AccessibilityNodeInfo node, String[] txtPatterns, String classPattern, FindNodeCallBack action) throws InterruptedException {
        if (node == null)
            node = FloatPanelService.Instance.getRootInActiveWindow();
        if (node != null) {
            boolean isMatchText = true;
            boolean isMatchClass = true;
            if (txtPatterns != null && txtPatterns.length > 0 ) {
                isMatchText = false;
                for (String txtPattern : txtPatterns){
                    CharSequence str = node.getText();
                    if (str == null? false : Pattern.matches(txtPattern, str)){
                        isMatchText = true;
                        break;
                    }
                }
            }
            if (classPattern != null && classPattern.compareTo("") != 0) {
                CharSequence str = node.getClassName();
                    isMatchClass = str == null? false : Pattern.matches(classPattern, str);
            }
            if (isMatchText && isMatchClass) {
                if (action.run(node))
                    return true;
            }
            if (node.getChildCount() == 0) {
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        boolean isfind = FindNode(node.getChild(i), txtPatterns, classPattern, action);
                        if (isfind) return true;
                    }
                }
            }
        }
        return false;
    }

    protected void RegisterEvent(int event, Runnable action) {
        FloatPanelService.Instance.RegisterEvent(event, action);
    }

    protected void RemoveEvent(int event, Runnable action) {
        FloatPanelService.Instance.RemoveEvent(event, action);
    }

    protected void back(){
        FloatPanelService.Instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    protected void home(){
        FloatPanelService.Instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    protected abstract void startImp() throws InterruptedException;
    protected abstract void endImp() throws InterruptedException;
}
