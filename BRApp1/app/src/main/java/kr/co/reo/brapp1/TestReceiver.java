package kr.co.reo.brapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TestReceiver extends BroadcastReceiver {
    //OS에 의해서 BR이 동작하게 되면 자동으로 호출되는 메서드
    @Override
    public void onReceive(Context context, Intent intent) {
        String str1 = "Broad Cast Receiver가 동작하였습니다";
        Toast t1 = Toast.makeText(context, str1, Toast.LENGTH_SHORT);
        t1.show();
    }
}