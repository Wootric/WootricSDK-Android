package com.wootric.androidsdk.objects;

import com.wootric.androidsdk.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class SettingsTest {

    @Mock
    LocalizedTexts mockLocalizedTexts;

    @Mock
    WootricCustomMessage mockCustomMessage;

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

    @Test
    public void setsAdminPanelCustomMessage() throws Exception {
        Settings settings = new Settings();

        Settings settingsWithCustomMessages = new Settings();
        WootricCustomMessage customMessage = new WootricCustomMessage();
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

    @Test
    public void delegatesMethodsToLocalizedTexts() {
        mockLocalizedTexts();

        Settings settings = new Settings();
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getSurveyQuestion()).isEqualTo("How likely");
        assertThat(settings.getAnchorLikely()).isEqualTo("likely");
        assertThat(settings.getAnchorNotLikely()).isEqualTo("not likely");
        assertThat(settings.getBtnSubmit()).isEqualTo("SUBMIT");
        assertThat(settings.getBtnDismiss()).isEqualTo("DISMISS");
        assertThat(settings.getFinalThankYou()).isEqualTo("Thank you!");
    }

    @Test
    public void whenLocalCustomMessageIsSet_returnsLocalCustomMessageQuestion() {
        doReturn("promoter").when(mockCustomMessage).getFollowupQuestionForScore(10, "NPS", 0);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(mockCustomMessage);
        settings.setSurveyType("NPS");

        assertThat(settings.getFollowupQuestion(10)).isEqualTo("promoter");
    }

    @Test
    public void whenAdminPanelCustomMessageIsSet_returnsAdminPanelCustomMessageQuestion() {
        doReturn("promoter").when(mockCustomMessage).getFollowupQuestionForScore(10, "NPS", 0);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(mockCustomMessage);
        settings.setSurveyType("NPS");

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

    @Test
    public void whenLocalCustomMessageIsSet_returnsLocalCustomMessagePlaceholder() {
        doReturn("promoter").when(mockCustomMessage).getPlaceholderForScore(10, "NPS", 0);

        Settings settings = new Settings();
        settings.setLocalCustomMessage(mockCustomMessage);
        settings.setSurveyType("NPS");

        assertThat(settings.getFollowupPlaceholder(10)).isEqualTo("promoter");
    }

    @Test
    public void whenAdminPanelCustomMessageIsSet_returnsAdminPanelCustomMessagePlaceholder() {
        doReturn("promoter").when(mockCustomMessage).getPlaceholderForScore(10, "NPS", 0    );

        Settings settings = new Settings();
        settings.setLocalCustomMessage(null);
        settings.setAdminPanelCustomMessage(mockCustomMessage);
        settings.setSurveyType("NPS");

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

    @Test
    public void whenCustomThankYouIsSet_returnsCustomThankYou() {
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText("thank you");
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo("thank you");
    }

    @Test
    public void whenCustomThankYouIsNotSet_returnsNull() {
        Settings settings = new Settings();
        mockLocalizedTexts();
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(null);
    }

    @Test
    public void whenCustomThankYouLinkIsNotSet_returnsNull() {
        Settings settings = new Settings();
        assertThat(settings.getThankYouLinkText(10)).isNull();
    }

    @Test
    public void whenCustomThankYouLinkIsSet_returnsIt() {
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText("Link text");
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkText(10)).isEqualTo("Link text");
    }

    @Test
    public void whenCustomThankYouLinkUriIsNotSet_returnsNull() {
        Settings settings = new Settings();
        assertThat(settings.getThankYouLinkUri(10, "feedback")).isNull();
    }

    private void mockLocalizedTexts() {
        doReturn("How likely").when(mockLocalizedTexts).getSurveyQuestion();
        doReturn("likely").when(mockLocalizedTexts).getAnchorLikely();
        doReturn("not likely").when(mockLocalizedTexts).getAnchorNotLikely();
        doReturn("dismiss").when(mockLocalizedTexts).getDismiss();
        doReturn("submit").when(mockLocalizedTexts).getSubmit();
        doReturn("Thank you!").when(mockLocalizedTexts).getFinalThankYou();
        doReturn("followup question").when(mockLocalizedTexts).getFollowupQuestion();
        doReturn("followup placeholder").when(mockLocalizedTexts).getFollowupPlaceholder();
        doReturn("social question").when(mockLocalizedTexts).getSocialShareQuestion();
        doReturn("social decline").when(mockLocalizedTexts).getSocialShareDecline();
    }
}
