package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings implements Parcelable {

    private int firstSurvey = Constants.NOT_SET;
    private int language = Constants.NOT_SET;

    private CustomMessage customMessage;

    private String productName;
    private int timeDelay = Constants.NOT_SET;

    public static Settings fromJson(JSONObject json) throws JSONException {
        Settings settings = new Settings();

        if(json == null) {
            return settings;
        }

        if(json.has("first_survey")) {
            settings.firstSurvey = json.getInt("first_survey");
        }

        if(json.has("language")) {
            settings.language = json.getInt("language");
        }

        if(json.has("time_delay")) {
            settings.timeDelay = json.getInt("time_delay");
        }

        if(json.has("product_name")) {
            settings.productName = json.getString("product_name");
        }

        if(json.has("messages")) {
            settings.customMessage = CustomMessage.fromJson(json.getJSONObject("messages"));
        }

        return settings;
    }

    public void setFirstSurvey(int firstSurvey) {
        this.firstSurvey = firstSurvey;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setCustomMessage(CustomMessage customMessage) {
        this.customMessage = customMessage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public int getFirstSurvey() {
        return firstSurvey;
    }

    public int getLanguage() {
        return language;
    }

    public CustomMessage getCustomMessage() {
        return customMessage;
    }

    public String getProductName() {
        return productName;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    // Sets values from the argument settings only if they are not provided yet
    public void merge(Settings settings) {
        if(settings == null) {
            return;
        }

        if(this.productName == null) {
            this.productName = settings.productName;
        }

        if(this.customMessage == null) {
            this.customMessage = settings.customMessage;
        }

        if(this.firstSurvey == Constants.NOT_SET) {
            this.firstSurvey = settings.firstSurvey;
        }

        if(this.language == Constants.NOT_SET) {
            this.language = settings.language;
        }

        if(this.timeDelay == Constants.NOT_SET) {
            this.timeDelay = settings.timeDelay;
        }
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {
        if(firstSurvey == Constants.NOT_SET || timeFrom == Constants.NOT_SET) {
            return true;
        }

        return new Date().getTime() - firstSurvey *  Constants.DAY_IN_MILLIS > timeFrom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.firstSurvey);
        dest.writeInt(this.language);
        dest.writeParcelable(this.customMessage, 0);
        dest.writeString(this.productName);
        dest.writeInt(this.timeDelay);
    }

    public Settings() {
    }

    private Settings(Parcel in) {
        this.firstSurvey = in.readInt();
        this.language = in.readInt();
        this.customMessage = in.readParcelable(CustomMessage.class.getClassLoader());
        this.productName = in.readString();
        this.timeDelay = in.readInt();
    }

    public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>() {
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };
}