package kr.co.reo.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import java.util.List;

import kr.co.reo.ipc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    //접속한 서비스 객체
    TestService ipcService;

    //서비스 접속을 관리하는 객체
    ServiceConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        //서비스가 가동중인지 확인한다.
        boolean chk = isServiceRunning("kr.co.reo.ipc.TestService");
        Intent serviceIntent = new Intent(this, TestService.class);

        //서비스가 가동중이 아니면 서비스를 가동한다.
        if (chk == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
        //서비스에 접속한다.
        connection = new ServiceConnectionClass();
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

        Button1ClickListener listener1 = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(listener1);
    }

    // 서비스가 실행되고 있는지 확인하는 메서드
    public boolean isServiceRunning(String name) {
        //서비스 관리자를 추출한다.
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //현재 동작 중인 모든 서비스를 관리하는 객체를 추출한다.
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        // 가져온 서비스 정보 객체의 수 만큼 반복한다.
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceList) {
            //현재 서비스의 이름이 원하는 이름인가...
            if (serviceInfo.service.getClassName() == name) {
                return true;
            }
        }
        return false;
    }

    //Activity의 서비스 접속을 관리하는 클래스
    class ServiceConnectionClass implements ServiceConnection {
        //서비스에 접속이 성공했을 때
        // 두 번째 : 서비스의 onBind 메서드가 반환하는 바인딩 객체
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TestService.LocalBinder binder = (TestService.LocalBinder) service;
            //서비스를 추출한다.
            ipcService = binder.getService();
        }

        //서비스 접속이 해제 되었을 때
        @Override
        public void onServiceDisconnected(ComponentName name) {
            ipcService = null;
        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //서비스의 메서드를 호출해서 값을 가져온다.
            int value = ipcService.getNumber();
            activityMainBinding.textView.setText("value : " + value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 서비스에 접속을 해제한다.
        unbindService(connection);
    }
}