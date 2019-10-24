package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.util.Log;

import com.wootric.androidsdk.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
public class SettingsTest {

    public static final String LOCALIZED_FINAL_THANK_YOU = "Localized final thank you!";
    public static final String LOCALIZED_THANK_YOU_SETUP = "Localized thank you setup";

    public static final String EMAIL = "nps@test.com";
    
    @Mock
    LocalizedTexts mockLocalizedTexts;

    @Mock
    WootricCustomMessage mockCustomMessage;

    @Mock
    WootricCustomThankYou mockCustomThankYou;

    @Before
    public void setUpMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDownMockito() {
        Mockito.validateMockitoUsage();
    }

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
        assertThat(settings.getFinalThankYou(0)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
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
    public void whenFinalThankYouIsNotSet_returnsLocalizedText() {
        mockLocalizedTexts();

        Settings settings = new Settings();
        settings.setLocalCustomThankYou(null);
        settings.setAdminPanelCustomThankYou(null);
        settings.setLocalizedTexts(mockLocalizedTexts);

        assertThat(settings.getFinalThankYou(10)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
    }

    @Test
    public void whenFinalThankYouIsSet_returnsLocalFinalThankYou() {
        String finalThankYou = "Local final thank you";

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setFinalThankYou(finalThankYou);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(finalThankYou);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(finalThankYou);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(finalThankYou);
    }

    @Test
    public void whenPromoterFinalThankYouIsSet_returnsLocalPromoterFinalThankYou() {
        mockLocalizedTexts();
        String promoterFinalThankYou = "Local promoter final thank you";

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterFinalThankYou(promoterFinalThankYou);
        settings.setCustomThankYou(customThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(promoterFinalThankYou);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(promoterFinalThankYou);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(promoterFinalThankYou);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
    }

    @Test
    public void whenPassiveFinalThankYouIsSet_returnsLocalPassiveFinalThankYou() {
        mockLocalizedTexts();
        String passiveFinalThankYou = "Local passive final thank you";

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveFinalThankYou(passiveFinalThankYou);
        settings.setCustomThankYou(customThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(passiveFinalThankYou);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(passiveFinalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(passiveFinalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
    }

    @Test
    public void whenDetractorFinalThankYouIsSet_returnsLocalDetractorFinalThankYou() {
        mockLocalizedTexts();
        String detractorFinalThankYou = "Local detractor final thank you";

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorFinalThankYou(detractorFinalThankYou);
        settings.setCustomThankYou(customThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(detractorFinalThankYou);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(detractorFinalThankYou);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(detractorFinalThankYou);
    }

    @Test
    public void whenFinalThankYouIsNotSet_returnsAdminPanelFinalThankYou() {
        mockLocalizedTexts();
        String finalThankYou = "Admin panel final thank you";
        doReturn(finalThankYou).when(mockCustomThankYou).getFinalThankYouForScore(10, "NPS", 0);
        doReturn(finalThankYou).when(mockCustomThankYou).getFinalThankYouForScore(3, "CSAT", 0);
        doReturn(finalThankYou).when(mockCustomThankYou).getFinalThankYouForScore(1, "CES", 0);

        Settings settings = new Settings();
        settings.setLocalCustomThankYou(null);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(LOCALIZED_FINAL_THANK_YOU);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(finalThankYou);
    }

    @Test
    public void whenFinalThankYouIsSetAndAdminPanelIsSet_returnsLocalFinalThankYou() {
        mockLocalizedTexts();
        String finalThankYou = "Local final thank you";
        doReturn("admin panel").when(mockCustomThankYou).getFinalThankYouForScore(10, "NPS", 0);
        doReturn("admin panel").when(mockCustomThankYou).getFinalThankYouForScore(3, "CSAT", 0);
        doReturn("admin panel").when(mockCustomThankYou).getFinalThankYouForScore(1, "CES", 0);

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setFinalThankYou(finalThankYou);
        settings.setCustomThankYou(customThankYou);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getFinalThankYou(10)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(8)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(0)).isEqualTo(finalThankYou);

        settings.setSurveyType("CSAT");

        assertThat(settings.getFinalThankYou(5)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(3)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(finalThankYou);

        settings.setSurveyType("CES");

        assertThat(settings.getFinalThankYou(7)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(5)).isEqualTo(finalThankYou);
        assertThat(settings.getFinalThankYou(1)).isEqualTo(finalThankYou);
    }

    @Test
    public void whenCustomThankYouTextIsNotSetPromoter_returnsLocalizedText() {
        mockLocalizedTexts();
        doReturn(true).when(mockCustomThankYou).getUniqueByScore();

        Settings settings = new Settings();
        settings.setLocalCustomThankYou(null);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(LOCALIZED_THANK_YOU_SETUP);
        assertThat(settings.getCustomThankYouMessage(8)).isNull();
        assertThat(settings.getCustomThankYouMessage(0)).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(LOCALIZED_THANK_YOU_SETUP);
        assertThat(settings.getCustomThankYouMessage(3)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isEqualTo(LOCALIZED_THANK_YOU_SETUP);
        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isNull();
    }

    @Test
    public void whenCustomThankYouTextIsSet_returnsCustomThankYouText() {
        Settings settings = new Settings();
        String customText = "Local custom text";
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText(customText);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(8)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(0)).isEqualTo(customText);

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(3)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(customText);

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(customText);
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(customText);
    }

    @Test
    public void whenCustomPromoterThankYouTextIsSet_returnsCustomPromoterThankYouText() {
        Settings settings = new Settings();
        String promoterText = "Local promoter custom text";
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPromoterText(promoterText);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(promoterText);
        assertThat(settings.getCustomThankYouMessage(8)).isNull();
        assertThat(settings.getCustomThankYouMessage(0)).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(promoterText);
        assertThat(settings.getCustomThankYouMessage(3)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isEqualTo(promoterText);
        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isNull();
    }

    @Test
    public void whenCustomPassiveThankYouTextIsSet_returnsCustomPassiveThankYouText() {
        Settings settings = new Settings();
        String passiveText = "Local passive custom text";
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setPassiveText(passiveText);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isNull();
        assertThat(settings.getCustomThankYouMessage(8)).isEqualTo(passiveText);
        assertThat(settings.getCustomThankYouMessage(0)).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(3)).isEqualTo(passiveText);
        assertThat(settings.getCustomThankYouMessage(1)).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isNull();
        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(passiveText);
        assertThat(settings.getCustomThankYouMessage(1)).isNull();
    }

    @Test
    public void whenCustomDetractorThankYouTextIsSet_returnsCustomDetractorThankYouText() {
        Settings settings = new Settings();
        String detractorText = "Local detractor custom text";
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setDetractorText(detractorText);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isNull();
        assertThat(settings.getCustomThankYouMessage(8)).isNull();
        assertThat(settings.getCustomThankYouMessage(0)).isEqualTo(detractorText);

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(3)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(detractorText);

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isNull();
        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(detractorText);
    }

    @Test
    public void whenCustomThankYouTextIsNotSet_returnsAdminPanel() {
        mockLocalizedTexts();
        String adminPanelText = "Admin panel text";
        doReturn(adminPanelText).when(mockCustomThankYou).getTextForScore(10, "NPS", 0);
        doReturn(adminPanelText).when(mockCustomThankYou).getTextForScore(3, "CSAT", 0);
        doReturn(adminPanelText).when(mockCustomThankYou).getTextForScore(1, "CES", 0);

        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(adminPanelText);
        assertThat(settings.getCustomThankYouMessage(8)).isNull();
        assertThat(settings.getCustomThankYouMessage(0)).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(3)).isEqualTo(adminPanelText);
        assertThat(settings.getCustomThankYouMessage(1)).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isNull();
        assertThat(settings.getCustomThankYouMessage(5)).isNull();
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(adminPanelText);

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(null);
    }

    @Test
    public void whenCustomThankYouTextIsSetAndAdminPanelIsSet_returnsLocalCustomThankYouText() {
        mockLocalizedTexts();
        String finalText = "Local final text";
        doReturn("admin panel text").when(mockCustomThankYou).getTextForScore(10, "NPS", 0);
        doReturn("admin panel text").when(mockCustomThankYou).getTextForScore(3, "CSAT", 0);
        doReturn("admin panel text").when(mockCustomThankYou).getTextForScore(1, "CES", 0);

        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setText(finalText);
        settings.setCustomThankYou(customThankYou);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setLocalizedTexts(mockLocalizedTexts);
        settings.setSurveyType("NPS");

        assertThat(settings.getCustomThankYouMessage(10)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(8)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(0)).isEqualTo(finalText);

        settings.setSurveyType("CSAT");

        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(3)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(finalText);

        settings.setSurveyType("CES");

        assertThat(settings.getCustomThankYouMessage(7)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(5)).isEqualTo(finalText);
        assertThat(settings.getCustomThankYouMessage(1)).isEqualTo(finalText);
    }

    @Test
    public void whenCustomThankYouLinkTextIsNotSet_returnsNull() {
        Settings settings = new Settings();
        assertThat(settings.getThankYouLinkText(10)).isNull();
    }

    @Test
    public void whenCustomThankYouLinkTextIsSet_returnsLocalThankYouLinkText() {
        String linkText = "Local link text";
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText(linkText);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkText(10)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(8)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(0)).isEqualTo(linkText);

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkText(5)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(3)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(1)).isEqualTo(linkText);

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkText(7)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(5)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(1)).isEqualTo(linkText);
    }

    @Test
    public void whenCustomThankYouLinkTextIsNotSetAndAdminPanelIsSet_returnsAdminPanelCustomThankYouLinkText() {
        String adminPanelLinkText = "Admin panel link text";
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(10, "NPS", 0);
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(3, "CSAT", 0);
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(1, "CES", 0);

        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkText(10)).isEqualTo(adminPanelLinkText);
        assertThat(settings.getThankYouLinkText(8)).isNull();
        assertThat(settings.getThankYouLinkText(0)).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkText(5)).isNull();
        assertThat(settings.getThankYouLinkText(3)).isEqualTo(adminPanelLinkText);
        assertThat(settings.getThankYouLinkText(1)).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkText(7)).isNull();
        assertThat(settings.getThankYouLinkText(5)).isNull();
        assertThat(settings.getThankYouLinkText(1)).isEqualTo(adminPanelLinkText);
    }

    @Test
    public void whenCustomThankYouLinkTextIsSetAndAdminPanelIsSet_returnsLocalCustomThankYouLinkText() {
        String adminPanelLinkText = "Admin panel link text";
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(10, "NPS", 0);
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(3, "CSAT", 0);
        doReturn(adminPanelLinkText).when(mockCustomThankYou).getLinkTextForScore(1, "CES", 0);

        String linkText = "Local link text";
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkText(linkText);
        settings.setCustomThankYou(customThankYou);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkText(10)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(8)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(0)).isEqualTo(linkText);

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkText(5)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(3)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(1)).isEqualTo(linkText);

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkText(7)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(5)).isEqualTo(linkText);
        assertThat(settings.getThankYouLinkText(1)).isEqualTo(linkText);
    }

    @Test
    public void whenCustomThankYouLinkUriIsNotSet_returnsNull() {
        Settings settings = new Settings();
        assertThat(settings.getThankYouLinkUri("",10, "")).isNull();
    }

    @Test
    public void whenCustomThankYouLinkUriIsSet_returnsLocalThankYouLinkUri() {
        Uri linkUri = Uri.parse("http://wootric.com/");
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkUri(linkUri);
        settings.setCustomThankYou(customThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isEqualTo(linkUri);

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(linkUri);

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(linkUri);
    }

    @Test
    public void whenCustomThankYouLinkUriIsNotSetAndAdminPanelIsSet_returnsAdminPanelCustomThankYouLinkUri() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1, "CES", 0);

        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(adminPanelLinkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(adminPanelLinkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(adminPanelLinkUri);
    }

    @Test
    public void whenCustomThankYouLinkUriIsSetAndAdminPanelIsSet_returnsLocalCustomThankYouLinkUri() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1, "CES", 0);

        Uri linkUri = Uri.parse("http://wootric.com/");
        Settings settings = new Settings();
        WootricCustomThankYou customThankYou = new WootricCustomThankYou();
        customThankYou.setLinkUri(linkUri);
        settings.setCustomThankYou(customThankYou);
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isEqualTo(linkUri);

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(linkUri);

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isEqualTo(linkUri);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(linkUri);
    }

    @Test
    public void whenAddEmailParamToUriIsTrue_returnsUriWithEmailParam() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(1, "CES", 0);


        Uri result = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com");
        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(result);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(result);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(result);
    }

    @Test
    public void whenAddScoreParamToUriIsTrue_returnsUriWithScoreParam() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_score=10");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_score=3");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_score=1");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(1, "CES", 0);


        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(resultCES);
    }

    @Test
    public void whenAddCommentParamToUriIsTrue_returnsUriWithCommentParam() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_comment=abc");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_comment=bac");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_comment=cab");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(1, "CES", 0);


        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "abc")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "abc")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "bac")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "bac")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "bac")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "cab")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "cab")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "cab")).isEqualTo(resultCES);
    }

    @Test
    public void whenAddEmailScoreParamToUriIsTrue_returnsUriWithEmailScoreParams() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(10, "NPS", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(3, "CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(1, "CES", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(1, "CES", 0);

        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=10");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=3");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=1");
        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "")).isEqualTo(resultCES);
    }

    @Test
    public void whenAddEmailCommentToUriIsTrue_returnsUriWithEmailCommentParams() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(10, "NPS", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(3, "CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(1, "CES", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(1, "CES", 0);

        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_comment=abc");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_comment=abc");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_comment=abc");
        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "abc")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "abc")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "abc")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isEqualTo(resultCES);
    }

    @Test
    public void whenAddScoreCommentToUriIsTrue_returnsUriWithScoreCommentParams() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(10, "NPS", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(3, "CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(1, "CES", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(1, "CES", 0);

        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_score=10&wootric_comment=abc");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_score=3&wootric_comment=abc");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_score=1&wootric_comment=abc");
        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "abc")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "abc")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "abc")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isEqualTo(resultCES);
    }

    @Test
    public void whenAddEmailScoreCommentToUriIsTrue_returnsUriWithEmailScoreCommentParams() {
        Uri adminPanelLinkUri = Uri.parse("http://wootric.com/admin");
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(10,"NPS", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(10, "NPS", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(10, "NPS", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(10, "NPS", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(3,"CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(3, "CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(3, "CSAT", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(3, "CSAT", 0);
        doReturn(adminPanelLinkUri).when(mockCustomThankYou).getLinkUri(1,"CES", 0);
        doReturn(true).when(mockCustomThankYou).getEmailInUri(1, "CES", 0);
        doReturn(true).when(mockCustomThankYou).getScoreInUri(1, "CES", 0);
        doReturn(true).when(mockCustomThankYou).getCommentInUri(1, "CES", 0);

        Uri resultNPS = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=10&wootric_comment=abc");
        Uri resultCSAT = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=3&wootric_comment=abc");
        Uri resultCES = Uri.parse("http://wootric.com/admin?wootric_email=nps%40test.com&wootric_score=1&wootric_comment=abc");
        Settings settings = new Settings();
        settings.setAdminPanelCustomThankYou(mockCustomThankYou);
        settings.setSurveyType("NPS");

        assertThat(settings.getThankYouLinkUri(EMAIL,10, "abc")).isEqualTo(resultNPS);
        assertThat(settings.getThankYouLinkUri(EMAIL,8, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,0, "abc")).isNull();

        settings.setSurveyType("CSAT");

        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,3, "abc")).isEqualTo(resultCSAT);
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isNull();

        settings.setSurveyType("CES");

        assertThat(settings.getThankYouLinkUri(EMAIL,7, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,5, "abc")).isNull();
        assertThat(settings.getThankYouLinkUri(EMAIL,1, "abc")).isEqualTo(resultCES);
    }


    private void mockLocalizedTexts() {
        doReturn("How likely").when(mockLocalizedTexts).getSurveyQuestion();
        doReturn("likely").when(mockLocalizedTexts).getAnchorLikely();
        doReturn("not likely").when(mockLocalizedTexts).getAnchorNotLikely();
        doReturn("dismiss").when(mockLocalizedTexts).getDismiss();
        doReturn("submit").when(mockLocalizedTexts).getSubmit();
        doReturn(LOCALIZED_FINAL_THANK_YOU).when(mockLocalizedTexts).getFinalThankYou();
        doReturn("followup question").when(mockLocalizedTexts).getFollowupQuestion();
        doReturn("followup placeholder").when(mockLocalizedTexts).getFollowupPlaceholder();
        doReturn(LOCALIZED_THANK_YOU_SETUP).when(mockLocalizedTexts).getSocialShareQuestion();
        doReturn("social decline").when(mockLocalizedTexts).getSocialShareDecline();
    }
}
