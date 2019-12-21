package com.andlightlight.autowork.script;

import android.graphics.PointF;
import android.view.accessibility.AccessibilityEvent;

import com.andlightlight.autowork.GestureManager;
import com.andlightlight.autowork.ScriptInterface;
import com.andlightlight.autowork.ToolUtls;

import org.opencv.core.Point;

import java.text.BreakIterator;
import java.util.List;

public class MYSLScript extends ScriptInterface {
    @Override
    protected void startImp() throws InterruptedException {
        OpenApp("支付宝");
        click("蚂蚁森林");
        waitText(new String[]{"种树","合种"});
        clickCurrentUI("收集能量\\d+克", true);
        sleep(1000);
        clickCurrentUI("查看更多好友",false);
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
            slideY(-0.6f,500);
            sleep(600);
            if (isNodeInScreen(new String[]{"没有更多了"}))
                run = false;
        }
        back();
        click("合种");
        click("浇水");
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
