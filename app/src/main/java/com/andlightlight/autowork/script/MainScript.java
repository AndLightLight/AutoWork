package com.andlightlight.autowork.script;

import com.andlightlight.autowork.FloatPanelService;
import com.andlightlight.autowork.GestureManager;
import com.andlightlight.autowork.ScriptInterface;
import com.andlightlight.autowork.ToolUtls;

import java.util.Date;

public class MainScript extends ScriptInterface {
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

    @Override
    protected void endImp() throws InterruptedException {

    }

    void RoundClick() throws InterruptedException {
        int allDelay = 2000;
        slide(new GestureManager.Point[]{new GestureManager.Point(449.000000f,1179.000000f, 48)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(597.000000f,1185.000000f, 45)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(670.000000f,1197.000000f, 50)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(665.000000f,1276.000000f, 27)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(653.000000f,1360.000000f, 51)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(581.000000f,1386.000000f, 50)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(483.000000f,1380.000000f, 47)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(434.000000f,1374.000000f, 65)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(449.000000f,1289.000000f, 49)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(451.000000f,1221.000000f, 49)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(495.000000f,1293.000000f, 36)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(578.000000f,1248.000000f, 52)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(537.000000f,1322.000000f, 35)});
        sleep(allDelay);
        CheckAndRengQiu();
        slide(new GestureManager.Point[]{new GestureManager.Point(530.000000f,1219.000000f, 10)});
    }

    void RunUp() throws InterruptedException {
        ToolUtls.ImgFtMatch re = findPicWithFeature(prepareSnapshotScreen(),"10",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            float startX = re.point.x - 60;
            float startY = re.point.y = 220;
            float endX = startX;
            float endY = startY - 100;
            slide(new GestureManager.Point[]{new GestureManager.Point(startX,startY,10),new GestureManager.Point(endX,endY, 2000)});
            sleep(5000);
            click(endX,endY);
        }
    }

    void RunDown() throws InterruptedException {
        ToolUtls.ImgFtMatch re = findPicWithFeature(prepareSnapshotScreen(),"10",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            float startX = re.point.x - 60;
            float startY = re.point.y = 220;
            float endX = startX;
            float endY = startY + 100;
            slide(new GestureManager.Point[]{new GestureManager.Point(startX,startY,10),new GestureManager.Point(endX,endY, 2000)});
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
        ToolUtls.ImgFtMatch re = findPicWithFeature(307, 1623, 780, 2141, "1", 1.0f);
        while (re.point.x > 0f && re.point.y > 0f) {
            RengQiu();
            LastRengQiuDate = new Date();
            sleep(rengqiuDelay);
            re = findPicWithFeature(307, 1623, 780, 2141, "1", 0.9f);
        }
        ToolUtls.PrepareImage pre = prepareSnapshotScreen();
        re = findPicWithFeature(pre,"2",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"4",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"5",0.9f);
        while (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
            pre = prepareSnapshotScreen();
            re = findPicWithFeature(pre,"5",0.9f);
        }
        re = findPicWithFeature(pre,"6",0.9f,1);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"13",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"7",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"9",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
        re = findPicWithFeature(pre,"3",0.9f);
        if (re.point.x > 0f && re.point.y > 0f) {
            MoveAndClick(re.point.x,re.point.y,rengqiuDelay);
        }
    }

    void RengQiu() throws InterruptedException {
        slide(new GestureManager.Point[]{new GestureManager.Point(516.961670f,1575.937500f,10),new GestureManager.Point(498.207764f,1585.280151f,104),new GestureManager.Point(440.102448f,1587.949219f,34),new GestureManager.Point(392.974213f,1586.122803f,18),new GestureManager.Point(374.957886f,1583.378906f,16),new GestureManager.Point(352.662659f,1574.421021f,10),new GestureManager.Point(341.471558f,1569.960938f,20),new GestureManager.Point(324.464722f,1547.402344f,15),new GestureManager.Point(316.744934f,1524.790039f,2),new GestureManager.Point(316.966553f,1501.904297f,32),new GestureManager.Point(318.065094f,1477.553589f,10),new GestureManager.Point(321.465454f,1463.935547f,17),new GestureManager.Point(330.463257f,1452.949219f,18),new GestureManager.Point(347.913391f,1442.667847f,1),new GestureManager.Point(358.972778f,1437.949219f,16),new GestureManager.Point(382.437256f,1424.206421f,10),new GestureManager.Point(393.958740f,1418.466797f,18),new GestureManager.Point(429.393280f,1405.795532f,1),new GestureManager.Point(489.473877f,1392.451172f,33),new GestureManager.Point(551.944092f,1402.929321f,18),new GestureManager.Point(563.961182f,1408.974609f,16),new GestureManager.Point(567.282837f,1419.237305f,1),new GestureManager.Point(571.475830f,1428.457031f,16),new GestureManager.Point(574.433655f,1439.325317f,1),new GestureManager.Point(576.469116f,1445.449219f,16),new GestureManager.Point(574.969482f,1461.545898f,1),new GestureManager.Point(574.969482f,1496.519775f,18),new GestureManager.Point(561.456299f,1526.923828f,33),new GestureManager.Point(553.663391f,1534.524048f,1),new GestureManager.Point(550.464478f,1540.898438f,16),new GestureManager.Point(541.466675f,1540.898438f,18),new GestureManager.Point(512.957153f,1536.416016f,17),new GestureManager.Point(492.967529f,1515.410156f,18),new GestureManager.Point(480.970459f,1498.916016f,3),new GestureManager.Point(455.520569f,1441.469849f,15),new GestureManager.Point(442.968750f,1390.927734f,34),new GestureManager.Point(442.968750f,1375.745239f,10),new GestureManager.Point(442.968750f,1365.410156f,17),new GestureManager.Point(462.463989f,1351.406250f,17),new GestureManager.Point(476.649109f,1350.300293f,10),new GestureManager.Point(483.475342f,1348.447266f,17),new GestureManager.Point(500.465698f,1349.941406f,10),new GestureManager.Point(511.420502f,1349.941406f,1),new GestureManager.Point(566.960449f,1369.453125f,34),new GestureManager.Point(635.465698f,1438.916016f,35),new GestureManager.Point(633.966064f,1460.449219f,17),new GestureManager.Point(629.467163f,1472.460938f,18),new GestureManager.Point(617.470093f,1469.941406f,16),new GestureManager.Point(594.644897f,1476.180908f,2),new GestureManager.Point(510.192108f,1464.815918f,34),new GestureManager.Point(469.200378f,1437.521851f,17),new GestureManager.Point(440.036743f,1401.531494f,17),new GestureManager.Point(430.971680f,1387.939453f,17),new GestureManager.Point(426.159119f,1363.135986f,3),new GestureManager.Point(424.462280f,1352.958984f,13),new GestureManager.Point(425.961914f,1339.798584f,2),new GestureManager.Point(440.974731f,1319.414063f,33),new GestureManager.Point(475.091949f,1313.688110f,1),new GestureManager.Point(496.972046f,1310.419922f,16),new GestureManager.Point(522.644836f,1311.914063f,1),new GestureManager.Point(546.970825f,1311.914063f,16),new GestureManager.Point(576.735962f,1324.421143f,2),new GestureManager.Point(620.250366f,1351.413940f,16),new GestureManager.Point(630.472412f,1360.429688f,18),new GestureManager.Point(650.462036f,1393.417969f,18),new GestureManager.Point(659.476318f,1429.892578f,17),new GestureManager.Point(655.471802f,1516.406250f,33),new GestureManager.Point(638.464966f,1548.398438f,17),new GestureManager.Point(608.472290f,1589.414063f,18),new GestureManager.Point(580.957092f,1604.948120f,10),new GestureManager.Point(530.607300f,1614.278320f,19),new GestureManager.Point(473.890594f,1610.005005f,16),new GestureManager.Point(356.962280f,1541.396484f,34), new GestureManager.Point(253.965454f,1470.439453f,19),new GestureManager.Point(201.644714f,1421.254150f,4),new GestureManager.Point(133.806030f,1332.488892f,12),new GestureManager.Point(118.454590f,1310.888672f,17),new GestureManager.Point(105.039223f,1251.919189f,10),new GestureManager.Point(95.465698f,1177.412109f,36),new GestureManager.Point(120.992432f,1086.416016f,15),new GestureManager.Point(131.456909f,1064.970703f,1),new GestureManager.Point(152.962646f,1037.929688f,16),new GestureManager.Point(196.452026f,1004.472656f,18),new GestureManager.Point(218.089264f,985.175415f,10),new GestureManager.Point(228.471680f,978.486328f,17),new GestureManager.Point(255.959473f,962.929688f,1)});
    }

    void MoveAndClick(float x, float y, int delay) throws InterruptedException {
        click(x,y);
        sleep(delay);
    }
}
