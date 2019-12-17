package com.andlightlight.autowork;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.ArraySet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

public abstract class ScriptInterface extends Thread {
    HashMap<String, ToolUtls.PrepareImage> mLoadPreImage = new HashMap<>();
    protected int mFeatureDetector = FeatureDetector.FAST;
    protected int mDescriptorExtractor = DescriptorExtractor.BRIEF;
    protected FeatureDetector mFd = FeatureDetector.create(mFeatureDetector);
    protected DescriptorExtractor mExtractor = DescriptorExtractor.create(mDescriptorExtractor);
    protected Bitmap mScreeShopImageCache;
    protected int mDefaultNeedNum = 4;
    protected Runnable mEndAction;
    protected Set<Semaphore> mSemaphoreSet = new ArraySet<>();
    protected boolean mInterrupt = false;

    protected interface FindNodeCallBack {
        boolean run(AccessibilityNodeInfo node) throws InterruptedException;
    }

    @Override
    public void run() {
        minPanel();
        mLoadPreImage.clear();
        File dir = new File(FloatPanelService.Instance.getExternalFilesDir(null) + "/");
        File[] files = dir.listFiles();
        for (File f : files) {
            Bitmap bp = BitmapFactory.decodeFile(f.getPath());
            if (bp != null) {
                String picnamewithdot = f.getName();
                String picname = picnamewithdot.substring(0, picnamewithdot.indexOf('.'));
                ToolUtls.PrepareImage preimage = ToolUtls.prepareBitmap(bp, mFd, mExtractor);
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

    @Override
    public synchronized void start(){
        start(null);
    }

    @Override
    public void interrupt() {
        mInterrupt = true;
        cleanSemaphore();
        super.interrupt();
    }

    @Override
    public boolean isInterrupted(){
        return super.isInterrupted() || mInterrupt;
    }

    public synchronized void start(Runnable endAction) {
        super.start();
        mEndAction = endAction;
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

    protected Semaphore createSemaphore(int permits) throws InterruptedException {
        if (isInterrupted()) {
            throw new InterruptedException();
        }
        Semaphore sp = new Semaphore(permits);
        mSemaphoreSet.add(sp);
        return sp;
    }

    protected void cleanSemaphore(){
        for (Semaphore sp : mSemaphoreSet)
            sp.release();
    }

    protected void sleep(int time) throws InterruptedException {
        if (isInterrupted()) {
            throw new InterruptedException();
        }
        Thread.sleep(time);
    }

    protected void click(float x, float y) {
        slide(new GestureManager.Point[]{new GestureManager.Point(x, y, 100)});
    }

    protected void slide(GestureManager.Point[] points) {
        GestureManager.click(points);
    }

    protected void slideX(float dx, int duration) {
        slide(dx,duration,true);
    }

    protected void slideY(float dx, int duration) {
        slide(dx,duration,false);
    }

    protected void slide(float d, int duration, boolean isX) {
        Rect ssize = getScreenSize();
        int centerx = ssize.width()/2;
        int centery = ssize.height()/2;
        int dirl = isX ? ssize.width() : ssize.height();
        int slidelength = (int) (d * dirl);
        int fromx = isX ? centerx - slidelength/2 : centerx;
        int fromy = isX ? centery : centery - slidelength/2;
        int endx = isX ? centerx + slidelength/2 : centerx;
        int endy = isX ? centery : centery + slidelength/2;
        slide(new GestureManager.Point[]{new GestureManager.Point(fromx,fromy,10),new GestureManager.Point(endx, endy, duration)});
    }

    protected Rect getScreenSize(){
        return FloatPanelService.Instance.getScreenSize();
    }

    protected Bitmap snapshotScreen(int left, int top, int right, int down){
        Bitmap ss =  FloatPanelService.Instance.snapshotScreen();
        if (ss == null) {
            ss = mScreeShopImageCache;
        }
        mScreeShopImageCache = ss;
        if (left != -1 && top != -1 && right != -1 && down != -1)
            ss = ToolUtls.cropBitmap(ss, left, top, right - left, down - top);
        return ss;
    }

    protected ToolUtls.PrepareImage prepareSnapshotScreen() {
        return prepareSnapshotScreen(-1, -1, -1, -1);
    }

    protected ToolUtls.PrepareImage prepareSnapshotScreen(int left, int top, int right, int down) {
        Bitmap ss = snapshotScreen(left, top, right, down);
        return ToolUtls.prepareBitmap(ss, mFd, mExtractor);
    }

    protected ToolUtls.ImgFtMatch findPicWithFeature(ToolUtls.PrepareImage orcpic, ToolUtls.PrepareImage subpic, int similar, int needNum) {
        return ToolUtls.findSubImageWithFeature(orcpic, subpic, mFd, mExtractor, similar, needNum);
    }

    protected ToolUtls.ImgFtMatch findPicWithFeature(ToolUtls.PrepareImage orcpic, String subpic, float similar) {
        return findPicWithFeature(orcpic, subpic, similar, mDefaultNeedNum);
    }

    protected ToolUtls.ImgFtMatch findPicWithFeature(ToolUtls.PrepareImage orcpic, String subpic, float similar, int needNum) {
        return ToolUtls.findSubImageWithFeature(orcpic, mLoadPreImage.get(subpic), mFd, mExtractor, similar, needNum);
    }

    protected ToolUtls.ImgFtMatch findPicWithFeature(int left, int top, int right, int down, String subpic, float similar) {
        return findPicWithFeature(left, top, right, down, subpic, similar, mDefaultNeedNum);
    }

    protected ToolUtls.ImgFtMatch findPicWithFeature(int left, int top, int right, int down, String subpic, float similar, int needNum) {
        return ToolUtls.findSubImageWithFeature(prepareSnapshotScreen(left, top, right, down), mLoadPreImage.get(subpic), mFd, mExtractor, similar, needNum);
    }

    protected List<ToolUtls.ImgMatch> findPic(int left, int top, int right, int down, String subpic, float similar) {
        return findPic(left, top, right, down,subpic, Imgproc.TM_CCOEFF_NORMED, similar, ToolUtls.MAX_LEVEL_AUTO);
    }

    protected List<ToolUtls.ImgMatch> findPic(int left, int top, int right, int down, String subpic, int matchMethod, float similar, int maxLevel) {
        return ToolUtls.findSubImage(snapshotScreen(left, top, right, down), mLoadPreImage.get(subpic).imageBitmap, matchMethod, similar, maxLevel);
    }

    protected List<ToolUtls.Match> findColors(String firstColor, ToolUtls.ColorPos[] points) {
        return ToolUtls.findColors(snapshotScreen(-1, -1, -1, -1), firstColor, points, 0.9f, null);
    }

    protected List<ToolUtls.Match> findColors(String firstColor, ToolUtls.ColorPos[] points, float similar) {
        return ToolUtls.findColors(snapshotScreen(-1, -1, -1, -1), firstColor, points, similar, null);
    }

    protected List<ToolUtls.Match> findColors(Bitmap orcimage, String firstColor, ToolUtls.ColorPos[] points, float similar, org.opencv.core.Rect rect) {
        return ToolUtls.findColors(orcimage, firstColor, points, similar, rect);
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

    protected void waitText(final String[] uitxts) throws InterruptedException {
        final Semaphore semaphore = createSemaphore(0);
        registerEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, new Runnable() {
            @Override
            public void run() {
                try {
                    AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
                    boolean isfind = findNode(rootNode, uitxts, null, new FindNodeCallBack() {
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
                        removeEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,this);
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

    protected void click(String txtPattern) throws InterruptedException {
        click(new String[]{txtPattern}, null, 1, true, false);
    }

    protected void clickCurrentUI(String txtPattern, final boolean isClickAll) throws InterruptedException {
        clickCurrentUI(new String[]{txtPattern}, null, isClickAll);
    }

    protected boolean clickCurrentUI(final String[] txtPatterns, final String classPattern, final boolean isClickAll) throws InterruptedException {
        AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
        boolean isfind = findNode(rootNode, txtPatterns, classPattern, new FindNodeCallBack() {
            @Override
            public boolean run(AccessibilityNodeInfo node) throws InterruptedException {
//                if (node.isClickable())
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                else {
                    Rect b = new Rect();
                    node.getBoundsInScreen(b);
                    if (b.bottom < FloatPanelService.Instance.getScreenSize().height())
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

    protected void click(final String[] txtPatterns, final String classPattern, final int times, boolean isBlock, final boolean isClickAll) throws InterruptedException {
        if (isClickAll)
            isBlock = false;
        final boolean finalisBlock = isBlock;
        final Semaphore semaphore = createSemaphore(0);
        registerEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, new Runnable() {
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
                        removeEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, this);
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

    protected boolean findNode(AccessibilityNodeInfo node, String[] txtPatterns, String classPattern, FindNodeCallBack action) throws InterruptedException {
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
                        boolean isfind = findNode(node.getChild(i), txtPatterns, classPattern, action);
                        if (isfind) return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isNodeInScreen(String[] uitxts){
        AccessibilityNodeInfo rootNode = FloatPanelService.Instance.getRootInActiveWindow();
        try {
            return findNode(rootNode, uitxts, null, new FindNodeCallBack() {
                @Override
                public boolean run(AccessibilityNodeInfo node) throws InterruptedException {
                    Rect b = new Rect();
                    node.getBoundsInScreen(b);
                    if (b.bottom < FloatPanelService.Instance.getScreenSize().height())
                        return true;
                    return false;
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void registerEvent(int event, Runnable action) {
        FloatPanelService.Instance.RegisterEvent(event, action);
    }

    protected void removeEvent(int event, Runnable action) {
        FloatPanelService.Instance.RemoveEvent(event, action);
    }

    protected void back(){
        FloatPanelService.Instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    protected void home(){
        FloatPanelService.Instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    protected void minPanel(){
        FloatPanelService.Instance.getPanel().min();
    }

    protected void maxPanel(){
        FloatPanelService.Instance.getPanel().max();
    }

    protected abstract void startImp() throws InterruptedException;
    protected abstract void endImp() throws InterruptedException;
}
