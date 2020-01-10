package com.andlightlight.autowork.script;

import android.graphics.PointF;

import com.andlightlight.autowork.ScriptInterface;
import com.andlightlight.autowork.ToolUtls;

import java.util.List;

public class DDSScript extends ScriptInterface {
    @Override
    protected void startImp() throws InterruptedException {
        while (true) {
            sleep(30);
            List<ToolUtls.ImgMatch> dspos = findPic("地鼠", 0.5f);
            while (!dspos.isEmpty()) {
                sleep(10);
                PointF p = dspos.get(0).point;
                click(p.x, p.y);
                dspos.remove(0);
            }
        }
    }

    @Override
    protected void endImp() throws InterruptedException {

    }
}
