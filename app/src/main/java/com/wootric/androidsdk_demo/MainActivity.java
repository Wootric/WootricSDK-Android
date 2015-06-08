package com.wootric.androidsdk_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wootric.androidsdk.Wootric;

public class MainActivity extends Activity {

    private static final String CLIENT_ID = "CLIENT ID";
    private static final String CLIENT_SECRET = "CLIENT SECRET";
    private static final String ACCOUNT_TOKEN = "ACCOUNT TOKEN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wootric.with(this)
                .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
                .endUser("END USER EMAIL", "ORIGIN URL")
                .survey();
    }

    public void showSurvey(View view) {
        Wootric.with(this)
                .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
                .endUser("END USER EMAIL", "ORIGIN URL")
                .forceSurvey()
                .survey();
    }

    @Override
    protected void onStop() {
        Wootric.stop();
        super.onStop();
    }
}
