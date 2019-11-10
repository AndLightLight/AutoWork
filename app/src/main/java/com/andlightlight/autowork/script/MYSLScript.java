package com.andlightlight.autowork.script;

import android.view.accessibility.AccessibilityEvent;

import com.andlightlight.autowork.ScriptInterface;

public class MYSLScript extends ScriptInterface {
    @Override
    protected void startImp() throws InterruptedException {
        OpenActivity("package:com.eg.android.AlipayGphone","com.eg.android.AlipayGphone.AlipayLogin");
        RegisterEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED, "package:com.eg.android.AlipayGphone", new Runnable() {
            @Override
            public void run() {
                ClickText("蚂蚁森林");
            }
        });
    }
}
