package com.wootric.androidsdk.objects;

import com.wootric.androidsdk.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings {

    private int firstSurvey = Constants.NOT_SET;

    private int timeDelay = 0;

    private CustomMessage customMessage;
    private LocalizedTexts localizedTexts;

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

        if(this.timeDelay == Constants.NOT_SET) {
            this.timeDelay = settings.timeDelay;
        }

        this.localizedTexts = settings.localizedTexts;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {
        if(firstSurvey == Constants.NOT_SET || timeFrom == Constants.NOT_SET) {
            return true;
        }

        return new Date().getTime() - firstSurvey *  Constants.DAY_IN_MILLIS > timeFrom;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    public CustomMessage getCustomMessage() {
        return customMessage;
    }

    public LocalizedTexts getLocalizedTexts() {
        return localizedTexts;
    }
}