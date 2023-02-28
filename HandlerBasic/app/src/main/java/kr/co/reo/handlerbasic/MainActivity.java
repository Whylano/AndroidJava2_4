package kr.co.reo.handlerbasic;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.reo.handlerbasic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    // MainThread에게 작업을 요청하기 위해 사용할 Handler
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener listener1 = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(listener1);

        handler = new Handler();

        // Handler를 통해 작업을 요청한다.
        ThreadClass threadClass = new ThreadClass();
        // handler.post(threadClass);
        handler.postDelayed(threadClass,100);

    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            long now = System.currentTimeMillis();
            activityMainBinding.textView.setText("버튼 클릭 : " + now);
        }
    }

    // Handler에게 작업을 요청할 Thread 클래스
    // run 메서드에 구현한 코드는 별도의 Thread가 발생해서 처리 되는 것이 아닌
    // MainThread가 가져다 처리한다.
    // 오래 걸리는 작업을 하면 안된다.
    // 화면 처리가 가능하다.
    class ThreadClass extends Thread {
        @Override
        public void run() {
            super.run();

            long now2 = System.currentTimeMillis();
            activityMainBinding.textView2.setText("Handler : " + now2);

            // 반복 작업을 위해 요청한다.
            //handler.post(this);
            handler.postDelayed(this,100);
        }
    }
}


