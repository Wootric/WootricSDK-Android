package com.wootric.androidsdk.objects;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings {

    private static final int NOT_SET = -1;
    private static final long DAY_IN_MILLIS = 1000L *60L *60L *24L;

    @SerializedName("first_survey")
    private int firstSurvey = NOT_SET;

    @SerializedName("time_delay")
    private int timeDelay = 0;

    @SerializedName("localized_texts")
    private LocalizedTexts localizedTexts;

    private CustomMessage customMessage;

    private boolean surveyImmediately;

    public static Settings fromJson(JSONObject json) throws JSONException {
        Settings settings = new Settings();

        if(json == null) {
            return settings;
        }

        if(json.has("first_survey")) {
            settings.firstSurvey = json.getInt("first_survey");
        }

        if(json.has("time_delay")) {
            settings.timeDelay = json.getInt("time_delay");
        }

        if(json.has("messages")) {
            settings.customMessage = CustomMessage.fromJson(json.getJSONObject("messages"));
        }

        if(json.has("localized_texts")) {
            settings.localizedTexts = LocalizedTexts.fromJson(json.getJSONObject("localized_texts"));
        }

        return settings;
    }


    // Sets values from the argument settings only if they are not provided yet
    public void merge(Settings settings) {
        if(settings == null) {
            return;
        }

        if(this.customMessage == null) {
            this.customMessage = settings.customMessage;
        }

        if(this.timeDelay == NOT_SET) {
            this.timeDelay = settings.timeDelay;
        }

        this.localizedTexts = settings.localizedTexts;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {
        return firstSurvey == NOT_SET ||
                timeFrom == NOT_SET ||
                new Date().getTime() - firstSurvey * DAY_IN_MILLIS > timeFrom;

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
}