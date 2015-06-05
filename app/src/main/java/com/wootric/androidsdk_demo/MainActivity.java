package com.wootric.androidsdk_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.wootric.androidsdk.Wootric;

public class MainActivity extends Activity {

    private static final String CLIENT_ID = "627ff892837d5762946f4699529642e8cb9833cbaec693f3f9b9f42904c7fd9b";
    private static final String CLIENT_SECRET = "79a0fdf8ffc56dcc498826f948204ac3506d028d12938fcec49ef2b8db1615ee";
    private static final String ACCOUNT_TOKEN = "NPS-7768d9aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wootric.with(this)
                .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
                .endUser("junkuits@gmail.com", "ORIGIN URL")
                .survey();
    }

    public void showSurvey(View view) {
        Wootric.with(this)
                .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
                .endUser("junkuits@gmail.com", "ORIGIN URL")
                .forceSurvey()
                .survey();
    }

    @Override
    protected void onStop() {
        Wootric.stop();
        super.onStop();
    }
}
