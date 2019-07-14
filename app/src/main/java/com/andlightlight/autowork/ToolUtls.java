package com.andlightlight.autowork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

final public class ToolUtls {

    static final String TAG = "FloatPanel";

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

    static class Segment {
        public int x;
        public int y;
        public int width;
        public int height;

        public Segment(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    static public List<Segment> findSubimage
            (
                    Bitmap orcimage,
                    Bitmap subimage,
                    Double similarity,
                    Boolean findAll,
                    Integer startX,
                    Integer startY
            ) {
        List<Segment> segments = new ArrayList<Segment>();
        if (subimage != null) {
            int subImagePixels = subimage.getWidth() * subimage.getHeight();
            boolean[][] processed = new boolean[orcimage.getWidth()][orcimage.getHeight()];

            int r1, g1, b1, r2, g2, b2;
            // Full image
            mainLoop:
            for (int y = startY; y < orcimage.getHeight(); y++) {
                for (int x = startX; x < orcimage.getWidth(); x++) {

                    if (processed[x][y]) {
                        continue;
                    }

                    int notMatched = 0;
                    boolean match = true;
                    // subimage
                    if (y + subimage.getHeight() < orcimage.getHeight() && x + subimage.getWidth() < orcimage.getWidth()) {


                        outerLoop:
                        for (int i = 0; i < subimage.getHeight(); i++) {
                            for (int j = 0; j < subimage.getWidth(); j++) {

                                if (processed[x + j][y + i]) {
                                    match = false;
                                    break outerLoop;
                                }

                                r1 = Color.red(orcimage.getPixel(x + j, y + i));
                                g1 = Color.green(orcimage.getPixel(x + j, y + i));
                                b1 = Color.blue(orcimage.getPixel(x + j, y + i));

                                r2 = Color.red(subimage.getPixel(j, i));
                                g2 = Color.green(subimage.getPixel(j, i));
                                b2 = Color.blue(subimage.getPixel(j, i));

                                if
                                (
                                        Math.abs(r1 - r2) > 50 ||
                                                Math.abs(g1 - g2) > 50 ||
                                                Math.abs(b1 - b2) > 50
                                ) {
                                    notMatched++;

                                    if (notMatched > (1 - similarity) * subImagePixels) {
                                        match = false;
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                    } else {
                        match = false;
                    }

                    if (match) {
                        segments.add(new Segment(x, y, subimage.getWidth(), subimage.getHeight()));

                        if (!findAll) {
                            break mainLoop;
                        }

                        for (int i = 0; i < subimage.getHeight(); i++) {
                            for (int j = 0; j < subimage.getWidth(); j++) {
                                processed[x + j][y + i] = true;
                            }
                        }

                    }
                }
            }
        }

        return segments;
    }

    static FloatPanelService.PrepareImage prepareBitmap(Bitmap image, FeatureDetector fd, DescriptorExtractor extractor){
        FloatPanelService.PrepareImage preImg = new FloatPanelService.PrepareImage();
        final MatOfKeyPoint keyPointsImage = new MatOfKeyPoint();
        Mat imageMat = new Mat();
        Utils.bitmapToMat(image, imageMat);
        fd.detect(imageMat, keyPointsImage);
        Log.d(TAG, "keyPoints.size() : " + keyPointsImage.size());
        Mat descriptors = new Mat();
        extractor.compute(imageMat, keyPointsImage, descriptors);
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
    static FloatPanelService.MatchResult findSubImageWithCV(FloatPanelService.PrepareImage orcpre,
                                     FloatPanelService.PrepareImage subpre,
                                     FeatureDetector fd,
                                     DescriptorExtractor extractor,
                                     float similar){

        FloatPanelService.MatchResult re = new FloatPanelService.MatchResult();
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

        int need = 4;
        if (sortList.size() >= need) {
            Log.d(TAG, "match found");
            for (DMatch dm : sortList){
                org.opencv.core.Point p = orcpre.keyPoints[dm.queryIdx].pt;
                re.rePoint.offset((float) p.x,(float) p.y);
            }
            re.rePoint.x /= sortList.size();
            re.rePoint.y /= sortList.size();
            return re;
        } else {
            return re;
        }
    }

    static FloatPanelService.MatchResult findSubImageWithCV(FloatPanelService.PrepareImage orcpre,
                                   FloatPanelService.PrepareImage subpre,
                                   int featureDetector,
                                   int descriptorExtractor,
                                   float similar){

        FeatureDetector fd = FeatureDetector.create(featureDetector);
        DescriptorExtractor extractor = DescriptorExtractor.create(descriptorExtractor);
        return findSubImageWithCV(orcpre,subpre,fd,extractor,similar);
    }

    static FloatPanelService.MatchResult findSubImageWithCV(Bitmap orcimage,
                                                            Bitmap subimage,
                                                            int featureDetector,
                                                            int descriptorExtractor,
                                                            float similar
    ) {
        FeatureDetector fd = FeatureDetector.create(featureDetector);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        DescriptorExtractor extractor = DescriptorExtractor.create(descriptorExtractor);

        FloatPanelService.PrepareImage orcpre = prepareBitmap(orcimage,fd,extractor);
        FloatPanelService.PrepareImage subpre = prepareBitmap(subimage,fd,extractor);
        return findSubImageWithCV(orcpre,subpre,fd,extractor,similar);
    }

}
