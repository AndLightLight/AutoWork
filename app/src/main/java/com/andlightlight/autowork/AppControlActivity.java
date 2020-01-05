package com.andlightlight.autowork;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class AppControlActivity extends AppCompatActivity {
    public enum ControlAction{
        NONE,
        OPEN,
        CLOSE
    }
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        PackageManager packageManager = getPackageManager();
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        Intent intent = getIntent();
        String appName = intent.getStringExtra("appName");
        ControlAction action = (ControlAction) intent.getSerializableExtra("action");
        switch (action){
            case OPEN:
                startActivity(packageManager.getLaunchIntentForPackage(ToolUtls.getPackageName(this,appName))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case CLOSE:
                activityManager.killBackgroundProcesses(ToolUtls.getPackageName(this,appName));
                break;
        }


        finish();
    }
}
