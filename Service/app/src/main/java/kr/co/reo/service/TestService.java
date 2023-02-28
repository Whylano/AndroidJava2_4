package kr.co.reo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class TestService extends Service {

    NotificationManager notificationManager;

    static final String NOTIFICATION_CHANNEL1_ID = "Service";
    static final String NOTIFICATION_CHANNEL1_NAME = "Service";

    static final int MESSAGE1_ID = 10;
    boolean isRunning = false;

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    // 서비스가 가동되면 호출되는 메서드

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("test", "서비스 가동");

        addNotificationChannel(NOTIFICATION_CHANNEL1_ID, NOTIFICATION_CHANNEL1_NAME);

        // 안드로이드 8.0 이상 부터..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder1 = getNotificationBuilder(NOTIFICATION_CHANNEL1_ID);

            builder1.setSmallIcon(android.R.drawable.ic_menu_search);
            builder1.setContentTitle("서비스 가동");
            builder1.setContentText("서비스가 가동 중입니다");
            Notification notification = builder1.build();
            //알림 메시지를 Foreground 서비스를 위해 표시 한다.
            startForeground(MESSAGE1_ID, notification);
        }
        //쓰래드 가동
        isRunning = true;
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();

        return super.onStartCommand(intent, flags, startId);
    }
    //서비스가 중지되고 소멸될 때 호출되는 메서드

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "서비스 중지");

        isRunning = false;
    }

    public void addNotificationChannel(String id, String name) {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(id);

            if (channel == null) {
                channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public NotificationCompat.Builder getNotificationBuilder(String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
            return builder;
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            return builder;
        }
    }

    // Thread Class
    class ThreadClass extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRunning) {
                SystemClock.sleep(500);
                long now = System.currentTimeMillis();
                Log.d("test", "Service : " + now);
            }
        }
    }
}