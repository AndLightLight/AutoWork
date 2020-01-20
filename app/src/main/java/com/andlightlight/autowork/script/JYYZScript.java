package com.andlightlight.autowork.script;

import com.andlightlight.autowork.GestureManager;
import com.andlightlight.autowork.ScriptInterface;
import com.andlightlight.autowork.ToolUtls;

import java.util.Date;
import java.util.List;

public class JYYZScript extends ScriptInterface {
    Date LastDate = new Date();
    boolean xiala = false;
    boolean houzi = false;
    @Override
    protected void startImp() throws InterruptedException {
        while (true){
            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("挑战首领", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                    LastDate = new Date();
                }
            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("挑战首领", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                    LastDate = new Date();
                }
            }

            if (xiala == false){

                sleep(200);
                {
                    List<ToolUtls.ImgMatch> poss = findPic("下拉", 0.6f);
                    if (poss.isEmpty() == false) {
                        List<ToolUtls.ImgMatch> poss2 = findPic("火女", 0.6f);
                        if (poss2.isEmpty() == false) {
                            slide(new GestureManager.Point[]{new GestureManager.Point(poss2.get(0).point.x,poss2.get(0).point.y,10), new GestureManager.Point(poss2.get(0).point.x + 10,poss2.get(0).point.y + 250,2000)});
                            xiala = true;
                        }
                    }
                }
            }

            if (houzi == false) {

                sleep(200);
                {
                    List<ToolUtls.ImgMatch> poss = findPic("猴子", 0.6f);
                    if (poss.isEmpty() == false) {
                        click(poss.get(0).point.x, poss.get(0).point.y);
                    }
                }

                sleep(1000);
                {
                    List<ToolUtls.ImgMatch> poss = findPic("免费领取", 0.6f);
                    if (poss.isEmpty() == false) {
                        click(poss.get(0).point.x, poss.get(0).point.y);
                    }
                }

                sleep(200);
                {
                    List<ToolUtls.ImgMatch> poss = findPic("猴子2", 0.6f);
                    if (poss.isEmpty() == false) {
                        click(poss.get(0).point.x, poss.get(0).point.y);
                        houzi = true;
                    }
                }

            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("战斗", 0.3f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                    LastDate = new Date();
                }
            }
            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("点击屏幕", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                }
            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("点击任意", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                }
            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("点击任意2", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                }
            }

            sleep(200);

//            {
//                List<ToolUtls.ImgMatch> poss = findPic("叹号", 0.3f);
//                while (poss.isEmpty() == false) {
//                    click(poss.get(0).point.x, poss.get(0).point.y);
//                    sleep(200);
//
//                    {
//                        List<ToolUtls.ImgMatch> poss2 = findPic("领取", 0.6f);
//                        while (poss2.isEmpty() == false) {
//                            click(poss2.get(0).point.x, poss2.get(0).point.y);
//                            sleep(200);
//                            {
//                                List<ToolUtls.ImgMatch> poss3 = findPic("宝箱", 0.9f);
//                                if (poss3.isEmpty() == false) {
//                                    click(poss3.get(0).point.x, poss3.get(0).point.y);
//                                }
//                            }
//                            sleep(200);
//                            {
//                                List<ToolUtls.ImgMatch> poss3 = findPic("获得奖励", 0.9f);
//                                if (poss3.isEmpty() == false) {
//                                    click(poss3.get(0).point.x, poss3.get(0).point.y);
//                                }
//                            }
//
//                            poss2 = findPic("领取", 0.6f);
//                        }
//
//                    }
//
//                    sleep(200);
//                    {
//                        List<ToolUtls.ImgMatch> poss2 = findPic("叹号", 0.3f);
//                        while (poss2.isEmpty() == false) {
//                            click(poss2.get(0).point.x, poss2.get(0).point.y);
//                            sleep(200);
//                        }
//                    }
//
//                    sleep(200);
//                    {
//                        List<ToolUtls.ImgMatch> poss2 = findPic("返回", 0.9f);
//                        if (poss2.isEmpty() == false) {
//                            click(poss2.get(0).point.x, poss2.get(0).point.y);
//                        }
//                    }
//
//
//                    poss = findPic("叹号", 0.3f);
//                }
//            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("手指", 0.6f);
                while (poss.isEmpty() == false) {
                    click(poss.get(0).point.x + 150, poss.get(0).point.y + 100);
                    sleep(200);
                    poss = findPic("手指", 0.6f);
                    LastDate = new Date();
                }
            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("半个手指", 0.6f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x + 150, poss.get(0).point.y + 100);
                }
            }

            sleep(200);
            {

                Date NowRengQiuDate = new Date();
                long DateOffset = NowRengQiuDate.getTime() - LastDate.getTime();
                if (DateOffset >= 10000) {
                    List<ToolUtls.ImgMatch> poss = findPic("返回", 0.9f);
                    while (poss.isEmpty() == false) {
                        click(poss.get(0).point.x, poss.get(0).point.y);
                        sleep(200);
                        poss = findPic("返回", 0.9f);
                    }
                }
            }

            sleep(200);
            {
                List<ToolUtls.ImgMatch> poss = findPic("战役", 0.9f);
                if (poss.isEmpty() == false) {
                    click(poss.get(0).point.x, poss.get(0).point.y);
                }
            }
        }
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
