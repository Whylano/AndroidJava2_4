package kr.co.reo.systembr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //BR이 동작할 때 사용한 이름을 가져온다
        String action = intent.getAction();

        //  분기한다.
        switch (action) {
            case "android.intent.action.BOOT_COMPLETED":
                Toast t1 = Toast.makeText(context, "부팅완료", Toast.LENGTH_SHORT);
                t1.show();
                break;
            case "android.provider.Telephony.SMS_RECEIVED":
                // 수신 문자 데이터를 가지고 있는 객체를 추출한다.
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    //문자 메시지 정보 객체를 추출한다.
                    Object[] obj = (Object[]) bundle.get("pdus");
                    // 문자 메시지 양식 객체를 추출한다.
                    String format = bundle.getString("format");

                    //문자 메시지 수 만큼 반복한다.
                    for (int i = 0; i < obj.length; i++) {
                        SmsMessage currentSMS = SmsMessage.createFromPdu((byte[]) obj[i], format);

                        // 문자를 보낸 단말기의 전화번호를 추출한다.
                        String str1 = currentSMS.getDisplayOriginatingAddress();

                        // 문자 내용을 가져온다.
                        String str2 = currentSMS.getDisplayMessageBody();
                        String str3 = "전화번호 : " + str1 + "\n내용 : " + str2;

                        Toast t2 = Toast.makeText(context, str3, Toast.LENGTH_SHORT);
                        t2.show();

                    }
                }

        }
    }
}