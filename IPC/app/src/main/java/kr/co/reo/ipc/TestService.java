package kr.co.reo.ipc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class TestService extends Service {
    NotificationManager notificationManager;

    static final String NOTIFICATION_CHANNEL1_ID = "Service";
    static final String NOTIFICATION_CHANNEL1_NAME = "Service";

    static final int MESSAGE1ID = 10;

    private int value = 0;
    private boolean isRunning = false;

    //서비스에 접속하면 전달될 바인딩 객체
    private LocalBinder binder = new LocalBinder();

    public TestService() {
    }

    //외부에서 서비스에 접속할 때 전달하는 객체
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // 바인딩 객체를 전달한다.
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        addNotificationChannel(NOTIFICATION_CHANNEL1_ID, NOTIFICATION_CHANNEL1_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder1 = getNotificationBuilder(NOTIFICATION_CHANNEL1_ID);

            builder1.setSmallIcon(android.R.drawable.ic_menu_search);
            builder1.setContentTitle("서비스 가동");
            builder1.setContentText("서비스가 가동 중입니다");
            Notification notification = builder1.build();
            startForeground(MESSAGE1ID, notification);
        }
        isRunning = true;
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();
        return super.onStartCommand(intent, flags, startId);
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

    class ThreadClass extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRunning) {
                SystemClock.sleep(500);
                Log.d("test", "value : " + value);
                value++;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    //접속하는 Activity에서 서비스를 추출하기 위해 사용하는 객체
    class LocalBinder extends Binder {
        public TestService getService() {
            //현재 동작 중인 서비스 객체를 전달한다.
            return TestService.this;
        }
    }
    //변수의 값을 반환하는 메서드
    public int getNumber(){
        return value;
    }
}