

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
        startSurvey();
    }

    public void showSurvey(View view) {
        startSurvey();
    }

    private void startSurvey() {

        Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setEndUserEmail("nps@example.com");
        wootric.setSurveyImmediately(true);
        wootric.survey();
    }
}