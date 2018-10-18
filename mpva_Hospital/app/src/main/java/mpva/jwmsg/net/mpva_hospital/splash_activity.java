package mpva.jwmsg.net.mpva_hospital;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class splash_activity extends Activity {
    /** 로딩 화면이 떠있는 시간(밀리초단위)  **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    /** 처음 액티비티가 생성될때 불려진다. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splish_page);
        initialize();
        /* SPLASH_DISPLAY_LENGTH 뒤에 메뉴 액티비티를 실행시키고 종료한다.*/
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
//                Intent mainIntent = new Intent(splash_activity.this, MainActivity.class);
//                splash_activity.this.startActivity(mainIntent);
//                splash_activity.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
    }
    private void initialize()
    {
        Handler handler =    new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                finish();    // 액티비티 종료
            }
        };

        handler.sendEmptyMessageDelayed(0, 3000);    // ms, 3초후 종료시킴
    }
}
