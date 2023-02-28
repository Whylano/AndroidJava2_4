package kr.co.reo.handlermessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import kr.co.reo.handlermessage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    boolean isRunning = false;
    HandlerClass handlerClass;

    int value1 = 100;
    int value2 = 200;
    String value3 = "문자열";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener listener1 = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(listener1);

        //Handler 객체 생성
        handlerClass = new HandlerClass();

        // 오래 걸리는 작업을 처리하기 위해
        // 새로운 쓰래드를 발생시켰다.
        isRunning = true;
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            long now = System.currentTimeMillis();
            activityMainBinding.textView.setText("버튼 클릭 : " + now);
        }
    }

    class ThreadClass extends Thread {
        @Override
        public void run() {
            super.run();

            while (isRunning) {
                SystemClock.sleep(500);
                // 핸들러에게 작업을 요청한다.
                handlerClass.sendEmptyMessage(0);

                SystemClock.sleep(500);
                handlerClass.sendEmptyMessage(1);

                SystemClock.sleep(500);
                //데이터를 담을 Message 객체
                Message msg = new Message();
                msg.what = 2;
                value1 = value1 + 1;
                value2 = value2 + 1;
                value3 = value3 + 1;

                msg.arg1 = value1;
                msg.arg2 = value2;
                msg.obj = value3;

                handlerClass.sendMessage(msg);
//                long now2 = System.currentTimeMillis();
//                //Log.d("test","오래 걸리는 작업1"+now2);
//                activityMainBinding.textView2.setText("오래 걸리는 작업1 : "+ now2);
//                SystemClock.sleep(500);
//
//                long now3 = System.currentTimeMillis();
//                //Log.d("test","오래 걸리는 작업2" + now3);
//                activityMainBinding.textView2.setText("오래 걸리는 작업2 : " +now3);
//                SystemClock.sleep(500);
            }
        }
    }

    // MainThread가 처리해야할 작업을 구현하는 핸들러
    class HandlerClass extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //what 값으로 분기한다.
            switch (msg.what) {
                case 0:
                    activityMainBinding.textView2.setText("Handler : 0");
                    break;
                case 1:
                    activityMainBinding.textView2.setText("Handler : 1");
                    break;
                case 2:
                    activityMainBinding.textView2.setText("Handler : 2\n");
                    activityMainBinding.textView2.append("arg1 : " + msg.arg1 + "\n");
                    activityMainBinding.textView2.append("arg1 : " + msg.arg2 + "\n");
                    activityMainBinding.textView2.append("arg1 : " + msg.obj);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}