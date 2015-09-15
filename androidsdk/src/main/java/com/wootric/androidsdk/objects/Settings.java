package com.wootric.androidsdk.objects;

import com.google.gson.annotations.SerializedName;
import com.wootric.androidsdk.Constants;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings {

    @SerializedName("first_survey")
    private long firstSurvey = 31L;

    @SerializedName("time_delay")
    private int timeDelay = 0;

    @SerializedName("localized_texts")
    private LocalizedTexts localizedTexts;

    private CustomMessage customMessage;

    private boolean surveyImmediately;

    // Sets values from the argument settings only if they are not provided yet
    public void mergeWithSurveyServerSettings(Settings settings) {
        if(settings == null) {
            return;
        }

        if(this.customMessage == null) {
            this.customMessage = settings.customMessage;
        }

        if(this.timeDelay == Constants.TIME_NOT_SET) {
            this.timeDelay = settings.timeDelay;
        }

        this.localizedTexts = settings.localizedTexts;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {
        return timeFrom == Constants.TIME_NOT_SET ||
                new Date().getTime() - firstSurvey * Constants.DAY_IN_MILLIS >= timeFrom;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public int getTimeDelayInMillis() {
        return timeDelay * 1000;
    }

    public CustomMessage getCustomMessage() {
        return customMessage;
    }

    public LocalizedTexts getLocalizedTexts() {
        return localizedTexts;
    }

    public long getFirstSurveyDelay() {
        return firstSurvey;
    }

    public void setCustomMessage(CustomMessage customMessage) {
        this.customMessage = customMessage;
    }
}