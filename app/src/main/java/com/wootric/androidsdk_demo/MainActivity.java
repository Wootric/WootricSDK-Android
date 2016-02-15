package com.wootric.androidsdk_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wootric.androidsdk.Wootric;
import com.wootric.androidsdk.objects.WootricCustomMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

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
