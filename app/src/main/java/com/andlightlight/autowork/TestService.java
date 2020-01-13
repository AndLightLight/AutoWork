package com.andlightlight.autowork;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TestService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Notification notification = new Notification(android.R.drawable.sym_def_app_icon,
                "my_service_name",
                System.currentTimeMillis());
        PendingIntent p_intent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        startForeground(0x1982, notification); // notification ID: 0x1982, you can name it as you will.
        //new FloatPanel(this).show();
        return START_STICKY;
    }
}
