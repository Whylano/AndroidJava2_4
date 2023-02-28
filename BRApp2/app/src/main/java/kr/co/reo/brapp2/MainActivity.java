package kr.co.reo.brapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kr.co.reo.brapp2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener listener1 = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(listener1);
    }
    class Button1ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // 다른 애플리케이션의 BR 실행 요청
            Intent intent = new Intent("kr.co.softcampus.testbr");
            sendBroadcast(intent);
        }
    }
}