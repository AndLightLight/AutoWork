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
        //OpenActivity("alipay://platformapi/startapp?appId=60000002");
        OpenApp("支付宝");
        click("蚂蚁森林");
        waitText(new String[]{"种树","合种"});
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        clickCurrentUI("查看更多好友",false);
        sleep(2000);
        while (true){
            List<ToolUtls.Match> handpos = findColors("#1DA06D", new ToolUtls.ColorPos[]{new ToolUtls.ColorPos(50, 44, "#1DA06D"),new ToolUtls.ColorPos(50,32, "#FFFFFF")});
            while (!handpos.isEmpty()){
                sleep(1000);
                PointF p = handpos.get(0).point;
                click(p.x,p.y);
                handpos.remove(0);
                waitText(new String[]{"浇水"});
                clickCurrentUI("收集能量\\d+克", true);
                sleep(5000);
                back();
            }
            slideY(-0.6f,500);
            sleep(2000);
            if (isNodeInScreen(new String[]{"没有更多了"}))
                break;
        }
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
