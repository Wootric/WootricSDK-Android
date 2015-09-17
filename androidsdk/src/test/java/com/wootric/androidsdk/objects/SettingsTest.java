package com.wootric.androidsdk.objects;

import com.google.gson.Gson;
import com.wootric.androidsdk.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Created by maciejwitowski on 9/15/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SettingsTest {

    @Mock
    LocalizedTexts mockLocalizedTexts;

    @Mock
    CustomMessage mockCustomMessage;

    @Test
    public void settingsAreCorrectlyPassedFromJson() {
        Settings settings = new Gson().fromJson(SETTINGS_JSON, Settings.class);

        assertThat(settings.getFirstSurveyDelay()).isEqualTo(30);
        assertThat(settings.getLocalizedTexts()).isNotNull();
        assertThat(settings.getNpsQuestion()).isEqualTo("How likely are you to recommend this product or service to a friend or co-worker?");
        assertThat(settings.getAdminPanelCustomMessage()).isNotNull();
        assertThat(settings.getAdminPanelCustomMessage().getFollowupQuestion()).isEqualTo("Followup text");
        assertThat(settings.getTimeDelayInMillis()).isEqualTo(5 * 1000);
    }

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
    public void setsAdminPanelCustomMessage() throws Exception {
        Settings settings = new Settings();

        Settings settingsWithCustomMessages = new Settings();
        CustomMessage customMessage = new CustomMessage();
        settingsWithCustomMessages.setAdminPanelCustomMessage(customMessage);

        settings.mergeWithSurveyServerSettings(settingsWithCustomMessages);
        assertThat(settings.getAdminPanelCustomMessage()).isEqualTo(customMessage);
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

    @Test
    public void setsAdminTimeDelay() {
        Settings settings = new Settings();

        Settings surveyServerSettings = new Settings();
        int timeDelay = 10;
        surveyServerSettings.setAdminPanelTimeDelay(timeDelay);

        settings.mergeWithSurveyServerSettings(surveyServerSettings);

        assertThat(settings.getAdminPanelTimeDelay()).isEqualTo(10);
    }

    /**
     * getTimeDelayInMillis()
     */
    @Test
    public void whenLocalTimeDelaySet_returnsLocalTimeDelayInMillis() {
        Settings settings = new Settings();
        settings.setTimeDelay(10);
        settings.setAdminPanelTimeDelay(20);

        assertThat(settings.getTimeDelayInMillis()).isEqualTo(10*1000);
    }

    @Test
    public void whenLocalTimeDelayIsNotSet_returnsAdminTimeDelayInMillis() {
        Settings settings = new Settings();
        settings.setAdminPanelTimeDelay(20);

        assertThat(settings.getTimeDelayInMillis()).isEqualTo(20*1000);
    }

    @Test
    public void whenAdminTimeDelayIsNotSet_returns0() {
        Settings settings = new Settings();
        assertThat(settings.getTimeDelayInMillis()).isEqualTo(0);
    }

    /**
     * Methods delegated to localized texts
     */
    @Test
    public void delegatesMethodsToLocalizedTexts() {
        mockLocalizedTexts();

        Settings settings = new Settings();
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getNpsQuestion()).isEqualTo("How likely");
        assertThat(settings.getAnchorLikely()).isEqualTo("likely");
        assertThat(settings.getAnchorNotLikely()).isEqualTo("not likely");
        assertThat(settings.getBtnSubmit()).isEqualTo("SUBMIT");
        assertThat(settings.getBtnDismiss()).isEqualTo("DISMISS");
    }

    /**
     * getFollowupQuestion(int score)
     */
    @Test
    public void whenLocalCustomMessageIsSet_returnsLocalCustomMessageQuestion() {
        doReturn("promoter").when(mockCustomMessage).getFollowupQuestionForScore(10);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(mockCustomMessage);

        assertThat(settings.getFollowupQuestion(10)).isEqualTo("promoter");
    }

    @Test
    public void whenAdminPanelCustomMessageIsSet_returnsAdminPanelCustomMessageQuestion() {
        doReturn("promoter").when(mockCustomMessage).getFollowupQuestionForScore(10);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(mockCustomMessage);

        assertThat(settings.getFollowupQuestion(10)).isEqualTo("promoter");
    }

    @Test
    public void whenCustomMessageIsNotSet_returnsLocalizedTextFollowupQuestion() {
        mockLocalizedTexts();

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(null);
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getFollowupQuestion(10)).isEqualTo("followup question");
    }

    /**
     * getFollowupPlaceholder(int score)
     */
    @Test
    public void whenLocalCustomMessageIsSet_returnsLocalCustomMessagePlaceholder() {
        doReturn("promoter").when(mockCustomMessage).getPlaceholderForScore(10);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(mockCustomMessage);

        assertThat(settings.getFollowupPlaceholder(10)).isEqualTo("promoter");
    }

    @Test
    public void whenAdminPanelCustomMessageIsSet_returnsAdminPanelCustomMessagePlaceholder() {
        doReturn("promoter").when(mockCustomMessage).getPlaceholderForScore(10);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(mockCustomMessage);

        assertThat(settings.getFollowupPlaceholder(10)).isEqualTo("promoter");
    }

    @Test
    public void whenCustomMessageIsNotSet_returnsLocalizedTextFollowupQuestionPlaceholder() {
        mockLocalizedTexts();

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(null);
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getFollowupPlaceholder(10)).isEqualTo("followup placeholder");
    }


    private static final String SETTINGS_JSON = "{" +
            "\"first_survey\":30," +
            "\"localized_texts\":{" +
                "\"nps_question\":\"How likely are you to recommend this product or service to a friend or co-worker?\"" +
            "}," +
            "\"messages\":{" +
                "\"followup_question\":\"Followup text\"" +
            "}," +
            "\"time_delay\":5" +
            "}";

    private void mockLocalizedTexts() {
        doReturn("How likely").when(mockLocalizedTexts).getNpsQuestion();
        doReturn("likely").when(mockLocalizedTexts).getAnchorLikely();
        doReturn("not likely").when(mockLocalizedTexts).getAnchorNotLikely();
        doReturn("dismiss").when(mockLocalizedTexts).getDismiss();
        doReturn("submit").when(mockLocalizedTexts).getSubmit();
        doReturn("followup question").when(mockLocalizedTexts).getFollowupQuestion();
        doReturn("followup placeholder").when(mockLocalizedTexts).getFollowupPlaceholder();
        doReturn("social question").when(mockLocalizedTexts).getSocialShareQuestion();
        doReturn("social decline").when(mockLocalizedTexts).getSocialShareDecline();
    }
}
