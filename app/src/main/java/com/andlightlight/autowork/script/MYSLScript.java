package com.andlightlight.autowork.script;

import android.view.accessibility.AccessibilityEvent;

import com.andlightlight.autowork.ScriptInterface;

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
        click("可收取");
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(2000);
        click("可收取");
        back();
        clickCurrentUI("收集能量\\d+克", true);
        sleep(20000000);
        int dfdf = 1;
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
