package com.andlightlight.autowork;

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
import java.util.List;
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
        void run(AccessibilityNodeInfo node);
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

    protected void OpenActivity(String packageName, String ActivityName) {
        Intent intent = new Intent().setComponent(new ComponentName(packageName, ActivityName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FloatPanelService.Instance.startActivity(intent);
    }

    protected void OpenActivity(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        FloatPanelService.Instance.startActivity(intent);
    }



    protected void click(String txtPattern) {
        click(txtPattern, "", 1, true);
    }

    protected void click(final String txtPattern, final String classPattern, final int times, final boolean isBlock) {
        final Semaphore semaphore = new Semaphore(1);
        if (isBlock){
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RegisterEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, new Runnable() {
            private int runtimes = times;
            @Override
            public void run() {
                if (runtimes > 0 || runtimes < 0) {
                    AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
                    boolean isfind = FindNode(rootNode, txtPattern, classPattern, new FindNodeCallBack() {
                        @Override
                        public void run(AccessibilityNodeInfo node) {
                            if (node.isClickable())
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            else {
                                Rect b = new Rect();
                                node.getBoundsInScreen(b);
                                click(b.centerX(), b.centerY());
                            }
                        }
                    });
                    if (isfind)
                        runtimes--;
                }
                if (runtimes == 0) {
                    RemoveEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, this);
                    if (isBlock)
                        semaphore.release();
                }
            }
        });
        if (isBlock){
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean FindNode(AccessibilityNodeInfo node, String txtPattern, String classPattern, FindNodeCallBack action) {
        if (node == null)
            node = FloatPanelService.Instance.getRootInActiveWindow();
        if (node != null) {
            boolean isMatchText = true;
            boolean isMatchClass = true;
            if (txtPattern != null && txtPattern.compareTo("") != 0) {
                CharSequence str = node.getText();
                    isMatchText = str == null? false : Pattern.matches(txtPattern, str);
            }
            if (classPattern != null && classPattern.compareTo("") != 0) {
                CharSequence str = node.getClassName();
                    isMatchClass = str == null? false : Pattern.matches(classPattern, str);
            }
            if (isMatchText && isMatchClass) {
                action.run(node);
                return true;
            }
            if (node.getChildCount() == 0) {
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        boolean isfind = FindNode(node.getChild(i), txtPattern, classPattern, action);
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

    protected abstract void startImp() throws InterruptedException;
    protected abstract void endImp() throws InterruptedException;
}
