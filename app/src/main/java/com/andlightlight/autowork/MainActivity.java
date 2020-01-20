package com.andlightlight.autowork;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import static com.andlightlight.autowork.ActivityResult.*;

class ActionBroadcastName {
    static final public String FloatPanelServiceOnCreate = "FloatPanelService OnCreate";
}

class ActivityResult{
    static final public int OverLay = 1;
    static final public int ScreenCapture = 2;
}

public class MainActivity extends AppCompatActivity {
    int mMediaProjectionResultCode;
    Intent mMediaProjectionData;

    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
//        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ActionBroadcastName.FloatPanelServiceOnCreate)){
                    Intent backintent = new Intent(getApplicationContext(), FloatPanelService.class);
                    backintent.putExtra("data",mMediaProjectionData);
                    backintent.putExtra("code",mMediaProjectionResultCode);
                    startService(backintent);
                }
            }
        },new IntentFilter(ActionBroadcastName.FloatPanelServiceOnCreate));

    }

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ActivityResult.OverLay:
                if (mMediaProjectionData == null)
                    startScreenCaptureIntentActivity();
                else
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case ScreenCapture:
                mMediaProjectionResultCode = resultCode;
                mMediaProjectionData = data;
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
        }
    }


    public void startFloatPanleService(View view){
        if (FloatPanelService.isStarted) {
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), OverLay);
        } else {
            if (mMediaProjectionData == null)
                startScreenCaptureIntentActivity();
            else
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
//        Intent backintent = new Intent(getApplicationContext(), TestService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(backintent);
//        }
    }

    private void startScreenCaptureIntentActivity(){
        @SuppressLint("WrongConstant") MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(this.MEDIA_PROJECTION_SERVICE);

        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                ScreenCapture);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
