package com.wootric.androidsdk_demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;


import com.wootric.androidsdk.Wootric;

public class MainActivity extends FragmentActivity {

    private static final String ACCOUNT_TOKEN = "NPS-3dbd275b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startSurvey();
    }

    public void showSurvey(View view) {
        startSurvey();
    }

    public void showAnotherActivity(View view) {
        finish();
        startActivity(new Intent(this, AnotherActivity.class));
    }

    private void startSurvey() {
        Wootric wootric = Wootric.init(this, ACCOUNT_TOKEN);
        wootric.setEndUserEmail("demoday@inmoment.com");
        wootric.setSurveyImmediately(true);
        wootric.survey();
    }
}
