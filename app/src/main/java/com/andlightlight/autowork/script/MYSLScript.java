package com.andlightlight.autowork.script;
import android.graphics.PointF;
import android.util.Log;

import com.andlightlight.autowork.ScriptInterface;
import com.andlightlight.autowork.ToolUtls;
import java.util.List;

public class MYSLScript extends ScriptInterface {
    @Override
    protected void startImp() throws InterruptedException {
        openApp("支付宝");
        click("蚂蚁森林");
        waitText(new String[]{"种树","合种"});
        sleep(300);
        clickCurrentUI("收集能量\\d+克", true);
        sleep(1000);
        clickCurrentUI("查看更多好友");
        sleep(1000);
        boolean run = true;
        while (true){
            List<ToolUtls.Match> handpos = findColors("#1DA06D", new ToolUtls.ColorPos[]{new ToolUtls.ColorPos(50, 44, "#1DA06D"),new ToolUtls.ColorPos(50, 38, "#1DA06D"),new ToolUtls.ColorPos(20,10, "#FFFFFF"),new ToolUtls.ColorPos(50,32, "#FFFFFF"),new ToolUtls.ColorPos(30,32, "#FFFFFF")});
            while (!handpos.isEmpty()){
                sleep(1000);
                PointF p = handpos.get(0).point;
                click(p.x,p.y);
                handpos.remove(0);
                waitText(new String[]{"浇水"});
                clickCurrentUI("收集能量\\d+克", true);
                sleep(300);
                back();
            }
            if (run == false)
                break;
            slideY(-0.6f,300);
            sleep(600);
            if (isNodeInScreen(new String[]{"没有更多了"})) {
                run = false;
            }
        }
        sleep(1000);
        back();
        click("合种");
        sleep(2000);
        List<ToolUtls.ImgMatch> picpos = findPic("浇水",0.9f);
        if (picpos.size() > 0 && picpos.get(0).point.x > 0f && picpos.get(0).point.y > 0f) {
            click(picpos.get(0).point.x,picpos.get(0).point.y);
        }
        click("\\+");
        int clicktimes = 0;
        while (++clicktimes < 20)
            clickCurrentUI("\\+");
        sleep(2000);
        clickCurrentUI("浇水");
        sleep(1000);
        back();
        sleep(1000);
        back();
        sleep(1000);
        back();
        sleep(1000);
        back();
        sleep(1000);
        back();
        closeApp("支付宝");
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
