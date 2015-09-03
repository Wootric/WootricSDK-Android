package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.Settings;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 5/5/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SettingsTest {

    Settings settings;

    @Before
    public void setup() {
        settings = new Settings();
    }

    @Test
    public void fromJson_buildsSurveyObject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("first_survey", 10);
        json.put("language", 2);
        json.put("time_delay", 100);
        json.put("product_name", "test product");

        JSONObject messages = new JSONObject();
        messages.put("wootric_recommend_target", "test target");
        json.put("messages", messages);

        Settings settings = Settings.fromJson(json);

        assertThat(settings.getFirstSurvey()).isEqualTo(10);
        assertThat(settings.getLanguage()).isEqualTo(2);
        assertThat(settings.getTimeDelay()).isEqualTo(100);
        assertThat(settings.getProductName()).isEqualTo("test product");
        assertThat(settings.getCustomMessage().getRecommendTarget()).isEqualTo("test target");
    }

    @Test
    public void merge_assignsAttributes_ifNotSet(){
        Settings otherSettings = buildOtherSettings();

        settings.merge(otherSettings);

        assertThat(settings.getFirstSurvey()).isEqualTo(100);
        assertThat(settings.getLanguage()).isEqualTo(200);
        assertThat(settings.getTimeDelay()).isEqualTo(300);
        assertThat(settings.getProductName()).isEqualTo("test product");

        assertThat(settings.getCustomMessage()).isEqualTo(otherSettings.getCustomMessage());
    }

    @Test
    public void merge_doesNotAssignAttributes_ifSet() {
        settings.setFirstSurvey(10);
        settings.setLanguage(20);
        settings.setProductName("original product");
        settings.setTimeDelay(30);


        CustomMessage originalMessage = CustomMessage.create();
        settings.setCustomMessage(originalMessage);

        Settings otherSettings = buildOtherSettings();

        settings.merge(otherSettings);

        assertThat(settings.getFirstSurvey()).isEqualTo(10);
        assertThat(settings.getLanguage()).isEqualTo(20);
        assertThat(settings.getProductName()).isEqualTo("original product");
        assertThat(settings.getTimeDelay()).isEqualTo(30);
        assertThat(settings.getCustomMessage()).isEqualTo(originalMessage);
    }

    @Test
    public void firstSurveyDelayPassed_returnsTrue_ifPassed() {
        settings.setFirstSurvey(10);

        long timeFrom = new Date().getTime() - Constants.DAY_IN_MILLIS*11;

        assertThat(settings.firstSurveyDelayPassed(timeFrom)).isTrue();
    }

    @Test
    public void firstSurveyDelayPassed_returnsFalse_ifNotPassed() {
        settings.setFirstSurvey(10);

        long timeFrom = new Date().getTime() - Constants.DAY_IN_MILLIS * 9;

        assertThat(settings.firstSurveyDelayPassed(timeFrom)).isFalse();
    }

    private Settings buildOtherSettings() {
        Settings otherSettings = new Settings();
        otherSettings.setFirstSurvey(100);
        otherSettings.setLanguage(200);
        otherSettings.setTimeDelay(300);
        otherSettings.setProductName("test product");


        CustomMessage customMessage = CustomMessage.create();
        otherSettings.setCustomMessage(customMessage);

        return otherSettings;
    }
}
