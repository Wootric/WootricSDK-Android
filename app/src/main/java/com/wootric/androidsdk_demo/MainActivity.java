package com.wootric.androidsdk_demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.wootric.androidsdk.Wootric;
import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.CustomThankYou;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String CLIENT_ID = "CLIENT ID";
    private static final String CLIENT_SECRET = "CLIENT SECRET";
    private static final String ACCOUNT_TOKEN = "ACCOUNT TOKEN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setEndUserEmail("nps@example.com");
        wootric.setOriginUrl("http://www.wootric.com");
        wootric.setSurveyImmediately(true);

        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        wootric.setProperties(properties);

        wootric.setDailyResponseCap(10);
        wootric.setProductName("Wootric");
        wootric.setRecommendTarget("Best Friend");
        wootric.setLanguageCode("PL");
        wootric.setFacebookPageId("123456");
        wootric.setTwitterPage("wootric");

        CustomMessage customMessage = new CustomMessage();
        customMessage.setFollowupQuestion("custom followup");
        customMessage.setDetractorFollowupQuestion("custom detractor");
        customMessage.setPassiveFollowupQuestion("custom passive");
        customMessage.setPromoterFollowupQuestion("custom promoter");
        customMessage.setPlaceholderText("custom placeholder");
        customMessage.setDetractorPlaceholderText("custom detractor placeholder");
        customMessage.setPassivePlaceholderText("custom passive placeholder");
        customMessage.setPromoterPlaceholderText("custom promoter placeholder");

        wootric.setCustomMessage(customMessage);

        CustomThankYou customThankYou = new CustomThankYou();
        customThankYou.setText("THANK YOU!");
        customThankYou.setDetractorText("Detractor thank you");
        customThankYou.setPassiveText("Passive thank you");
        customThankYou.setPromoterText("Promoter thank you");
        customThankYou.setLinkText("CLICK");
        customThankYou.setDetractorLinkText("Detractor click");
        customThankYou.setPassiveLinkText("Passive click");
        customThankYou.setPromoterLinkText("Promoter click");
        customThankYou.setLinkUri(Uri.parse("http://wootric.com/thank_you"));

        customThankYou.setDetractorLinkUri(Uri.parse("http://wootric.com/detractor_thank_you"));
        customThankYou.setPassiveLinkUri(Uri.parse("http://wootric.com/passive_thank_you"));
        customThankYou.setPromoterLinkUri(Uri.parse("http://wootric.com/promoter_thank_you"));
        customThankYou.setScoreInUrl(true);
        customThankYou.setCommentInUrl(true);
        wootric.setCustomThankYou(customThankYou);

        wootric.survey();
    }

    public void showSurvey(View view) {
        Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setEndUserEmail("nps@example.com");
        wootric.setOriginUrl("http://www.wootric.com");
        wootric.setSurveyImmediately(true);
        wootric.survey();
    }
}
