package com.wootric.androidsdk_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wootric.androidsdk.Wootric;

public class MainActivity extends Activity {

    private static final String CLIENT_ID = "8ce33d3c0e6fa71752e411a9a7209f4e0fdb44a472923a71a8d94ca45c050906";
    private static final String CLIENT_SECRET = "8969ac12e7e8dccfaad2bbc1d45f583e3d6811dd18a5d91a07c5f30d6e62c3dd";
    private static final String ACCOUNT_TOKEN = "NPS-5a38583e";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setEndUserEmail("nps@example.com");
        wootric.setSurveyImmediately(true);
        wootric.survey();
    }

    public void showSurvey(View view) {
        Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setSurveyImmediately(true);
        wootric.survey();
    }
}
