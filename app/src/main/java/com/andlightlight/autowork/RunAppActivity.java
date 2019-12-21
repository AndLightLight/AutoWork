package com.andlightlight.autowork;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class RunAppActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        PackageManager packageManager = getPackageManager();
        startActivity(packageManager.getLaunchIntentForPackage(ToolUtls.getPackageName(this,intent.getStringExtra("appName")))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
