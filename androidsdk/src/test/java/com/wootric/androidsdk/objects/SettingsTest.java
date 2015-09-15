package com.wootric.androidsdk.objects;

import com.wootric.androidsdk.Constants;

import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by maciejwitowski on 9/15/15.
 */

public class SettingsTest {

    /**
     * firstSurveyDelayPassed(long timeFrom)
     */
    @Test
    public void whenTimeFromIsNotSet_itReturnsTrue() throws Exception {
        Settings settings = new Settings();

        assertThat(settings.firstSurveyDelayPassed(Constants.NOT_SET)).isTrue();
    }

    @Test
    public void whenTimeFromIsBeforeFirstSurveyTime_itReturnsTrue() throws Exception {
        Settings settings = new Settings();

        long timeBeforeFirstSurvey = new Date().getTime() - 31L * Constants.DAY_IN_MILLIS;
        assertThat(settings.firstSurveyDelayPassed(timeBeforeFirstSurvey)).isTrue();
    }

    @Test
    public void whenTimeFromIsAfterFirstSurveyTime_itReturnsFalse() throws Exception {
        Settings settings = new Settings();

        long timeAfterFirstSurvey = new Date().getTime();
        assertThat(settings.firstSurveyDelayPassed(timeAfterFirstSurvey)).isFalse();
    }

    /**
     * mergeWithSurveyServerSettings(Settings settings)
     */
    @Test
    public void whenCustomMessageIsNull_overridesIt() throws Exception {
        Settings settings = new Settings();

        Settings settingsWithCustomMessages = new Settings();
        CustomMessage customMessage = new CustomMessage();
        settingsWithCustomMessages.setCustomMessage(customMessage);

        settings.mergeWithSurveyServerSettings(settingsWithCustomMessages);
        assertThat(settings.getCustomMessage()).isEqualTo(customMessage);
    }

    @Test
    public void whenCustomMessageIsNotNull_doesNotOverrideIt() throws Exception {
        Settings settings = new Settings();
        CustomMessage customMessage_1 = new CustomMessage();
        settings.setCustomMessage(customMessage_1);

        Settings surveyServerSettings = new Settings();
        CustomMessage customMessage_2 = new CustomMessage();
        surveyServerSettings.setCustomMessage(customMessage_2);

        settings.mergeWithSurveyServerSettings(surveyServerSettings);

        assertThat(settings.getCustomMessage()).isEqualTo(customMessage_1);
    }

    @Test
    public void setLocalizedTexts() throws Exception {
        Settings settings = new Settings();

        Settings surveyServerSettings = new Settings();
        LocalizedTexts localizedTexts = new LocalizedTexts();
        surveyServerSettings.setLocalizedTexts(localizedTexts);

        settings.mergeWithSurveyServerSettings(surveyServerSettings);
        assertThat(settings.getLocalizedTexts()).isEqualTo(localizedTexts);
    }
}
