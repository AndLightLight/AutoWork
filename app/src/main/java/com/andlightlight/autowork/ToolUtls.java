package com.andlightlight.autowork;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

final public class ToolUtls {

    static final String TAG = "ToolUtls";
    public static final int MAX_LEVEL_AUTO = -1;

    public static class PrepareImage{
        Bitmap imageBitmap;
        Mat imageMat;
        Mat imageDesMat;
        MatOfKeyPoint keyPointsMat;
        KeyPoint[] keyPoints;
    }

    public static class Match{
        public PointF point = new PointF();
    }

    public static class ImgMatch extends Match {
        public double similarity;
        public ImgMatch(PointF point, double similarity) {
            this.point = point;
            this.similarity = similarity;
        }
    }

    public static class ImgFtMatch extends Match {
        Mat largeImage;
        Mat smallImage;
        MatOfKeyPoint keyPointsLarge;
        MatOfKeyPoint keyPointsSmall;
        MatOfDMatch matchesFiltered;
    }

    public static class ColorMatch extends Match {

    }

    public static class ColorPos{
        public int color;
        public int x;
        public int y;
        public ColorPos(int x, int y, String color){
            this.color = Color.parseColor(color);
            this.x = x;
            this.y = y;
        }
    }


    public static String getExceptionAllinformation(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }

    public static String getPackageName(Context context, String appName) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : installedApplications) {
            if (packageManager.getApplicationLabel(applicationInfo).toString().equals(appName)) {
                return applicationInfo.packageName;
            }
        }
        return null;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    static public Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    static public Bitmap cropBitmap(Bitmap bitmap, int startX, int startY, int width, int height) {
        if ((startX + width) <= bitmap.getWidth() && (startY + height) <= bitmap.getHeight()){
            return Bitmap.createBitmap(bitmap, startX, startY, width, height, null, false);
        }
        return bitmap;
    }

    static public void saveBitmap(Bitmap bitmap,String path) throws IOException
    {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static public Bitmap getBitmapFromPath(String path) {
        if (!new File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists");
            return null;
        }

        byte[] buf = new byte[1024 * 1024];// 1M
        Bitmap bitmap = null;

        try {

            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err
                        .println("path: " + path + "  could not be decode!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return bitmap;
    }

    /**
     * string类型时间转换为date
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    static PrepareImage prepareBitmap(Bitmap image, FeatureDetector fd, DescriptorExtractor extractor){
        PrepareImage preImg = new PrepareImage();
        final MatOfKeyPoint keyPointsImage = new MatOfKeyPoint();
        Mat imageMat = new Mat();
        Utils.bitmapToMat(image, imageMat);
        fd.detect(imageMat, keyPointsImage);
        Log.d(TAG, "keyPoints.size() : " + keyPointsImage.size());
        Mat descriptors = new Mat();
        extractor.compute(imageMat, keyPointsImage, descriptors);
        preImg.imageBitmap = image;
        preImg.imageMat = imageMat;
        preImg.imageDesMat = descriptors;
        preImg.keyPointsMat = keyPointsImage;
        preImg.keyPoints = keyPointsImage.toArray();
        return preImg;
    }
//        能用的FD：
//        public static final int FAST = 1 - >3;
//        public static final int STAR = 2 - >3;
//        public static final int ORB = 5;
//        public static final int MSER = 6;
//        public static final int GFTT = 7;
//        public static final int HARRIS = 8;
//        public static final int SIMPLEBLOB = 9;
//        public static final int DENSE = 10;
//        public static final int BRISK = 11;
//        能用的DE：
//        public static final int ORB = 3;
//        public static final int BRIEF = 4;
//        推荐使用排名：
//        1 - >4
//        1 - >3
//        10 - >4
//        5 - >3
    static ImgFtMatch findSubImageWithFeature(PrepareImage orcpre,
                                                                 PrepareImage subpre,
                                                                 FeatureDetector fd,
                                                                 DescriptorExtractor extractor,
                                                                 float similar,
                                                                 int needNum

    ){

        ImgFtMatch re = new ImgFtMatch();
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        Log.d(TAG, "descriptorsA.size() : " + orcpre.imageDesMat.size());
        Log.d(TAG, "descriptorsB.size() : " + subpre.imageDesMat.size());

        MatOfDMatch matches = new MatOfDMatch();


        if ((subpre.imageDesMat.size().width + subpre.imageDesMat.size().height) <= 0 || (orcpre.imageDesMat.size().width + orcpre.imageDesMat.size().height) <= 0)
            return re;

        matcher.match(orcpre.imageDesMat, subpre.imageDesMat, matches);

        Log.d(TAG, "matches.size() : " + matches.size());

        List<DMatch> matchesList = matches.toList();
        List<DMatch> bestMatches = new ArrayList<DMatch>();

        Double max_dist = 0.0;
        Double min_dist = 100.0;
        int min_idx = -100;
        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;

            if (dist < min_dist && dist != 0) {
                min_idx = i;
                min_dist = dist;
            }

            if (dist > max_dist) {
                max_dist = dist;
            }

        }

        Log.d(TAG, "max_dist : " + max_dist);
        Log.d(TAG, "min_dist : " + min_dist);

        if (min_dist > 50) {
            Log.d(TAG, "No match found");
            Log.d(TAG, "Just return ");
            return re;
        }

        double threshold = 15 + (1-similar)*50;
//        double threshold = 3 * min_dist;
//        double threshold2 = 2 * min_dist;

//        if (threshold > 75) {
//            threshold = 75;
//        } else if (threshold2 >= max_dist) {
//            threshold = min_dist * 1.1;
//        } else if (threshold >= max_dist) {
//            threshold = threshold2 * 1.4;
//        }

        Log.d(TAG, "Threshold : " + threshold);

        ArrayList<DMatch> sortList = new ArrayList<>();

        for (int i = 0; i < matchesList.size(); i++) {
            DMatch dm = matchesList.get(i);
            Double dist = (double) dm.distance;

            if (dist < threshold) {
                bestMatches.add(dm);
                sortList.add(dm);
                Log.d(TAG,String.format(i + " best match added : %s", dist));
            }
        }

        sortList.sort(new Comparator<DMatch>() {
            @Override
            public int compare(DMatch o1, DMatch o2) {
                return o1.distance > o2.distance? 1: (o1.distance == o2.distance?0:-1);
            }
        });

        sortList = new ArrayList<>(sortList.subList(0,sortList.size()>=4?4:sortList.size()));

        MatOfDMatch matchesFiltered = new MatOfDMatch();

        matchesFiltered.fromList(sortList);

        Log.d(TAG, "matchesFiltered.size() : " + matchesFiltered.size());

        re.largeImage = orcpre.imageMat;
        re.smallImage = subpre.imageMat;
        re.keyPointsLarge = orcpre.keyPointsMat;
        re.keyPointsSmall = subpre.keyPointsMat;
        re.matchesFiltered = matchesFiltered;

        Log.d(TAG, "sortList.size() : " + sortList.size());

        if (sortList.size() >= needNum) {
            Log.d(TAG, "match found");
            for (DMatch dm : sortList){
                org.opencv.core.Point p = orcpre.keyPoints[dm.queryIdx].pt;
                re.point.x += (float) p.x;
                re.point.y += (float) p.y;
            }
            re.point.x /= sortList.size();
            re.point.y /= sortList.size();
            return re;
        } else {
            return re;
        }
    }

    static ImgFtMatch findSubImageWithFeature(PrepareImage orcpre,
                                                                 PrepareImage subpre,
                                                                 int featureDetector,
                                                                 int descriptorExtractor,
                                                                 float similar,
                                                                 int needNum
    ){

        FeatureDetector fd = FeatureDetector.create(featureDetector);
        DescriptorExtractor extractor = DescriptorExtractor.create(descriptorExtractor);
        return findSubImageWithFeature(orcpre,subpre,fd,extractor,similar,needNum);
    }

    static ImgFtMatch findSubImageWithFeature(Bitmap orcimage,
                                                                 Bitmap subimage,
                                                                 int featureDetector,
                                                                 int descriptorExtractor,
                                                                 float similar,
                                                                 int needNum
    ) {
        FeatureDetector fd = FeatureDetector.create(featureDetector);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        DescriptorExtractor extractor = DescriptorExtractor.create(descriptorExtractor);

        PrepareImage orcpre = prepareBitmap(orcimage,fd,extractor);
        PrepareImage subpre = prepareBitmap(subimage,fd,extractor);
        return findSubImageWithFeature(orcpre,subpre,fd,extractor,similar,needNum);
    }

    private static int selectPyramidLevel(Mat img, Mat template) {
        int minDim = Math.min(Math.min(img.rows(), img.cols()), Math.min(template.rows(), template.cols()));
        //这里选取16为图像缩小后的最小宽高，从而用log(2, minDim / 16)得到最多可以经过几次缩小。
        int maxLevel = (int) (Math.log(minDim / 16) / Math.log(2));
        if (maxLevel < 0) {
            return 0;
        }
        //上限为6
        return Math.min(6, maxLevel);
    }

    private static Mat getPyramidDownAtLevel(Mat m, int level) {
        if (level == 0) {
            return m;
        }
        int cols = m.cols();
        int rows = m.rows();
        for (int i = 0; i < level; i++) {
            cols = (cols + 1) / 2;
            rows = (rows + 1) / 2;
        }
        Mat r = new Mat(rows, cols, m.type());
        Imgproc.resize(m, r, new Size(cols, rows));
        return r;
    }

    private static boolean shouldContinueMatching(int level, int maxLevel) {
        if (level == maxLevel && level != 0) {
            return true;
        }
        if (maxLevel <= 2) {
            return false;
        }
        return level == maxLevel - 1;
    }

    private static Mat matchTemplate(Mat img, Mat temp, int match_method) {
        int result_cols = img.cols() - temp.cols() + 1;
        int result_rows = img.rows() - temp.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(img, temp, result, match_method);
        return result;
    }

    private static ImgMatch getBestMatched(Mat tmResult, int matchMethod, float weakThreshold, Rect rect) {
        Core.MinMaxLocResult mmr = Core.minMaxLoc(tmResult);
        double value;
        Point pos;
        if (matchMethod == Imgproc.TM_SQDIFF || matchMethod == Imgproc.TM_SQDIFF_NORMED) {
            pos = mmr.minLoc;
            value = -mmr.minVal;
        } else {
            pos = mmr.maxLoc;
            value = mmr.maxVal;
        }
        if (value < weakThreshold) {
            return null;
        }
        if (rect != null) {
            pos.x += rect.x;
            pos.y += rect.y;
        }
        return new ImgMatch(new PointF((float) pos.x,(float) pos.y), value);
    }

    private static Rect getROI(Point p, Mat src, Mat currentTemplate) {
        int x = (int) (p.x * 2 - currentTemplate.cols() / 4);
        x = Math.max(0, x);
        int y = (int) (p.y * 2 - currentTemplate.rows() / 4);
        y = Math.max(0, y);
        int w = (int) (currentTemplate.cols() * 1.5);
        int h = (int) (currentTemplate.rows() * 1.5);
        if (x + w >= src.cols()) {
            w = src.cols() - x - 1;
        }
        if (y + h >= src.rows()) {
            h = src.rows() - y - 1;
        }
        return new Rect(x, y, w, h);
    }

    private static void getBestMatched(Mat tmResult, Mat template, int matchMethod, float weakThreshold, List<ImgMatch> outResult, int limit, Rect rect) {
        for (int i = 0; i < limit; i++) {
            ImgMatch bestMatched = getBestMatched(tmResult, matchMethod, weakThreshold, rect);
            if (bestMatched == null) {
                break;
            }
            outResult.add(bestMatched);
            Point start = new Point(Math.max(0, bestMatched.point.x - template.width() + 1),
                    Math.max(0, bestMatched.point.y - template.height() + 1));
            Point end = new Point(Math.min(tmResult.width(), bestMatched.point.x + template.width()),
                    Math.min(tmResult.height(), bestMatched.point.y + template.height()));
            //Imgproc.rectangle(tmResult, start, end, new Scalar(0, 255, 0), -1);
        }
    }

    private static void pyrUp(Point p, int level) {
        for (int i = 0; i < level; i++) {
            p.x *= 2;
            p.y *= 2;
        }
    }

    static List<ImgMatch> findSubImage(Bitmap orcimage,
                                    Bitmap subimage,
                                    int matchMethod,
                                    float similar,
                                    int maxLevel
    ) {
        Mat orcMat = new Mat();
        Utils.bitmapToMat(orcimage, orcMat);
        Mat subMat = new Mat();
        Utils.bitmapToMat(subimage, subMat);

        if (maxLevel == MAX_LEVEL_AUTO) {
            //自动选取金字塔层数
            maxLevel = selectPyramidLevel(orcMat, subMat);
        }
        //保存每一轮匹配到模板图片在原图片的位置
        List<ImgMatch> finalMatchResult = new ArrayList<>();
        List<ImgMatch> previousMatchResult = Collections.emptyList();
        boolean isFirstMatching = true;
        for (int level = maxLevel; level >= 0; level--) {
            // 放缩图片
            List<ImgMatch> currentMatchResult = new ArrayList<>();
            Mat src = getPyramidDownAtLevel(orcMat, level);
            Mat currentTemplate = getPyramidDownAtLevel(subMat, level);
            // 如果在上一轮中没有匹配到图片，则考虑是否退出匹配
            if (previousMatchResult.isEmpty()) {
                // 如果不是第一次匹配，并且不满足shouldContinueMatching的条件，则直接退出匹配
                if (!isFirstMatching && !shouldContinueMatching(level, maxLevel)) {
                    break;
                }
                Mat matchResult = matchTemplate(src, currentTemplate, matchMethod);
                getBestMatched(matchResult, currentTemplate, matchMethod, 0.7f, currentMatchResult, 1, null);
            } else {
                for (Match match : previousMatchResult) {
                    // 根据上一轮的匹配点，计算本次匹配的区域
                    Rect r = getROI(new Point(match.point.x,match.point.y), src, currentTemplate);
                    Mat m = new Mat(src, r);
                    Mat matchResult = matchTemplate(m, currentTemplate, matchMethod);
                    getBestMatched(matchResult, currentTemplate, matchMethod, 0.7f, currentMatchResult, 1, r);
                }
            }

            // 把满足强阈值的点找出来，加到最终结果列表
            if (!currentMatchResult.isEmpty()) {
                Iterator<ImgMatch> iterator = currentMatchResult.iterator();
                while (iterator.hasNext()) {
                    ImgMatch match = iterator.next();
                    if (match.similarity >= similar) {
                        pyrUp(new Point(match.point.x,match.point.y), level);
                        finalMatchResult.add(match);
                        iterator.remove();
                    }
                }
                // 如果所有结果都满足强阈值，则退出循环，返回最终结果
                if (currentMatchResult.isEmpty()) {
                    break;
                }
            }
            isFirstMatching = false;
            previousMatchResult = currentMatchResult;
        }
        return finalMatchResult;
    }


    static private boolean checksPath(Bitmap image, Point startingPoint, int threshold, Rect rect, int[] points) {
        for (int i = 0; i < points.length; i += 3) {
            int x = points[i];
            int y = points[i + 1];
            int color = points[i + 2];
            ColorDetector colorDetector = new ColorDetector.DifferenceDetector(color, threshold);
            x += startingPoint.x;
            y += startingPoint.y;
            if (x >= image.getWidth() || y >= image.getHeight()
                    || x < 0 || y < 0) {
                return false;
            }
            int c = image.getPixel(x, y);
            if (!colorDetector.detectsColor(Color.red(c), Color.green(c), Color.blue(c))) {
                return false;
            }
        }
        return true;
    }

    static List<Match> findColors(Bitmap orcimage, String firstColor, ColorPos[] points, float similar, Rect rect) {
        int[] list = new int[points.length * 3];
        for (int i = 0; i < points.length; i++) {
            ColorPos p = points[i];
            list[i * 3] = p.x;
            list[i * 3 + 1] = p.y;
            list[i * 3 + 2] = p.color;
        }
        return findColors(orcimage, Color.parseColor(firstColor), similar, rect, list, -1);
    }

    static List<Match> findColors(Bitmap orcimage, int firstColor, float similar, Rect rect, int[] points, int findnum) {
        int threshold = (int) ((1 - similar) * 255);
        Mat orcMat = new Mat();
        Utils.bitmapToMat(orcimage, orcMat);
        Mat bi = new Mat();
        Scalar lowerBound = new Scalar(Color.red(firstColor) - threshold, Color.green(firstColor) - threshold,
                Color.blue(firstColor) - threshold, 255);
        Scalar upperBound = new Scalar(Color.red(firstColor) + threshold, Color.green(firstColor) + threshold,
                Color.blue(firstColor) + threshold, 255);
        if (rect != null) {
            Mat m = new Mat(orcMat, rect);
            Core.inRange(m, lowerBound, upperBound, bi);
        } else {
            Core.inRange(orcMat, lowerBound, upperBound, bi);
        }
        Mat nonZeroPos = new Mat();
        Core.findNonZero(bi, nonZeroPos);
        MatOfPoint matpoint;
        if (nonZeroPos.rows() == 0 || nonZeroPos.cols() == 0) {
            matpoint = null;
        } else {
            matpoint = new MatOfPoint(nonZeroPos);
        }

        List<Match> result = new ArrayList<>();
        if (matpoint != null){
            Point[] repoints = matpoint.toArray();
            if (rect != null) {
                for (int i = 0; i < repoints.length; i++) {
                    repoints[i].x = repoints[i].x + rect.x;
                    repoints[i].y = repoints[i].y + rect.y;
                }
            }

            int rangemaxx = -99999;
            int rangemaxy = -99999;
            int rangeminx = 99999;
            int rangeminy = 99999;
            for (int i = 0; i < points.length; i += 3) {
                int x = points[i];
                int y = points[i + 1];
                if (x > rangemaxx)
                    rangemaxx = x;
                if (y > rangemaxy)
                    rangemaxy = y;
                if (x < rangeminx)
                    rangeminx = x;
                if (y < rangeminy)
                    rangeminy = y;
            }

            int rangex = Math.max(Math.max(Math.abs(rangemaxx - rangeminx),Math.abs(rangemaxx)),Math.abs(rangeminx));
            int rangey = Math.max(Math.max(Math.abs(rangemaxy - rangeminy),Math.abs(rangemaxy)),Math.abs(rangeminy));

            Point lastPoint = null;
            for (Point p : repoints) {
                if (p == null)
                    continue;
                if (checksPath(orcimage, p, threshold, rect, points)) {
                    if (lastPoint != null){
                        if (Math.abs(p.x - lastPoint.x) < rangex && Math.abs(p.y - lastPoint.y) < rangey)
                            continue;
                    }
                    lastPoint = p;
                    result.add(new ImgMatch(new PointF((float)p.x,(float)p.y),similar));
                    findnum --;
                    if (findnum == 0)
                        return result;
                }
            }
        }
        return result;
    }

}
