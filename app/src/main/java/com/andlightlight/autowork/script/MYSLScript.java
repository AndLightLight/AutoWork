package com.andlightlight.autowork.script;

import android.view.accessibility.AccessibilityEvent;

import com.andlightlight.autowork.ScriptInterface;

public class MYSLScript extends ScriptInterface {
    @Override
    protected void startImp() throws InterruptedException {
        //OpenActivity("alipay://platformapi/startapp?appId=60000002");
        OpenApp("支付宝");
        click("蚂蚁森林");
        int dfdf = 1;
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
