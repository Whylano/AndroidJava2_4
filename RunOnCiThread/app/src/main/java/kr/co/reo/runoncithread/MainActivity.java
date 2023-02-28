package kr.co.reo.runoncithread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import kr.co.reo.runoncithread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener listener1 = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(listener1);

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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView2.setText("runOnCiThread 1");
                    }
                });

                SystemClock.sleep(500);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView2.setText("runOnCiThread 2");
                    }
                });


                SystemClock.sleep(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView2.setText("runOnCiThread 3");
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}