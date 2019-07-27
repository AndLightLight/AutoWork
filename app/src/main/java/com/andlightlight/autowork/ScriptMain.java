package com.andlightlight.autowork;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.Date;

public class ScriptMain extends ScriptInterface {
    Date LastRengQiuDate;
    @Override
    protected void startImp() throws InterruptedException{
        LastRengQiuDate = new Date();
        while (true){
            int DDelay = 1000;
            RoundClick();
            sleep(DDelay);
            RunUp();
            sleep(DDelay);
            RoundClick();
            sleep(DDelay);
            RunUp();
            sleep(DDelay);
            RoundClick();
            sleep(DDelay);
            RunUp();
            sleep(DDelay);
            RoundClick();
            RunUp();
            sleep(DDelay);
            RoundClick();
            sleep(DDelay);
            RunDown();
            sleep(DDelay);
            RoundClick();
            sleep(DDelay);
            RunDown();
            sleep(DDelay);
            RoundClick();
            sleep(DDelay);
            RunDown();
            sleep(DDelay);
            RoundClick();
        }
    }

    void RoundClick() throws InterruptedException {
        int allDelay = 2000;
        click(new PointF[]{new PointF(449.000000f,1179.000000f)},48);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(597.000000f,1185.000000f)},45);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(670.000000f,1197.000000f)},50);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(665.000000f,1276.000000f)},27);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(653.000000f,1360.000000f)},51);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(581.000000f,1386.000000f)},50);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(483.000000f,1380.000000f)},47);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(434.000000f,1374.000000f)},65);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(449.000000f,1289.000000f)},49);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(451.000000f,1221.000000f)},49);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(495.000000f,1293.000000f)},36);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(578.000000f,1248.000000f)},52);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(537.000000f,1322.000000f)},35);
        sleep(allDelay);
        CheckAndRengQiu();
        click(new PointF[]{new PointF(530.000000f,1219.000000f)},10);
    }

    void RunUp() throws InterruptedException {
        FloatPanelService.MatchResult re = FindPic(prepareSnapshotScreen(),"10",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            float startX = re.rePoint.x - 60;
            float startY = re.rePoint.y = 220;
            float endX = startX;
            float endY = startY - 100;
            click(new PointF[]{new PointF(startX,startY),new PointF(endX,endY)},2000,true);
            sleep(5000);
            click(endX,endY);
        }
    }

    void RunDown() throws InterruptedException {
        FloatPanelService.MatchResult re = FindPic(prepareSnapshotScreen(),"10",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            float startX = re.rePoint.x - 60;
            float startY = re.rePoint.y = 220;
            float endX = startX;
            float endY = startY + 100;
            click(new PointF[]{new PointF(startX,startY),new PointF(endX,endY)},2000,true);
            sleep(5000);
            click(endX,endY);
        }
    }

    public void CheckAndRengQiu() throws InterruptedException {
        Date NowRengQiuDate = new Date();
        long DateOffset = NowRengQiuDate.getTime() - LastRengQiuDate.getTime();
        if (DateOffset > 600) {
//            while (true) {
//            Call Plugin.Media.Beep(784, 300)
//            Call Plugin.Media.Beep(578, 300)
//            Call Plugin.Media.Beep(988, 300)
//            Call Plugin.Media.Beep(440, 300)
//            Call Plugin.Media.Beep(1760, 300)
//            Call Plugin.Media.Beep(1397, 300)
//            Call Plugin.Media.Beep(698, 300)
//            Call Plugin.Media.Beep(523, 300)
//            Call Plugin.Media.Beep(1046, 300)
//            Call Plugin.Media.Beep(262, 300)
//            Call Plugin.Media.Beep(349, 300)
//            Call Plugin.Media.Beep(784, 300)
//            Call Plugin.Media.Beep(880, 300)
//            Call Plugin.Media.Beep(880, 300)
//            }
        }

        int rengqiuDelay = 3000;
        FloatPanelService.MatchResult re = FindPic(307, 1623, 780, 2141, "1", 1.0f);
        while (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            RengQiu();
            LastRengQiuDate = new Date();
            sleep(rengqiuDelay);
            re = FindPic(307, 1623, 780, 2141, "1", 0.9f);
        }
        FloatPanelService.PrepareImage pre = prepareSnapshotScreen();
        re = FindPic(pre,"2",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"4",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"5",0.9f);
        while (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
            pre = prepareSnapshotScreen();
            re = FindPic(pre,"5",0.9f);
        }
        re = FindPic(pre,"6",0.9f,1);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"13",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"7",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"9",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
        re = FindPic(pre,"3",0.9f);
        if (re.rePoint.x > 0f && re.rePoint.y > 0f) {
            MoveAndClick(re.rePoint.x,re.rePoint.y,rengqiuDelay);
        }
    }

    void RengQiu() throws InterruptedException {
        click(new PointF[]{new PointF(531.000000f,1855.000000f),new PointF(531.000000f,1848.500000f),new PointF(525.500000f,1816.000000f),new PointF(519.447327f,1774.854980f),new PointF(516.000000f,1749.000000f),new PointF(508.000000f,1695.000000f),new PointF(502.500000f,1664.000000f),new PointF(499.000000f,1631.000000f),new PointF(493.500000f,1593.000000f),new PointF(488.500000f,1555.500000f),new PointF(478.500000f,1472.500000f),new PointF(471.000000f,1383.500000f),new PointF(469.155212f,1333.557983f),new PointF(466.500000f,1295.500000f),new PointF(465.000000f,1255.500000f),new PointF(461.500000f,1219.000000f)},150);
    }

    void MoveAndClick(float x, float y, int delay) throws InterruptedException {
        click(x,y);
        sleep(delay);
    }
}
