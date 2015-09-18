package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wootric.androidsdk.Constants;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings implements Parcelable {

    @SerializedName("first_survey")
    private Long firstSurvey = 31L;

    @SerializedName("time_delay")
    private int adminPanelTimeDelay = Constants.NOT_SET;

    @SerializedName("localized_texts")
    private LocalizedTexts localizedTexts;

    @SerializedName("messages")
    private CustomMessage adminPanelCustomMessage;

    private CustomMessage localCustomMessage;

    private int timeDelay = Constants.NOT_SET;

    private boolean surveyImmediately;

    private Integer dailyResponseCap;
    private Integer registeredPercent;
    private Integer visitorPercent;
    private Integer resurveyThrottle;

    private String languageCode;
    private String productName;
    private String recommendTarget;

    // Sets values from the argument settings only if they are not provided yet
    public void mergeWithSurveyServerSettings(Settings settings) {
        if(settings == null) {
            return;
        }

        this.adminPanelCustomMessage = settings.adminPanelCustomMessage;
        this.adminPanelTimeDelay = settings.adminPanelTimeDelay;
        this.localizedTexts = settings.localizedTexts;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {
        return timeFrom == Constants.NOT_SET ||
                new Date().getTime() - firstSurvey * Constants.DAY_IN_MILLIS >= timeFrom;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public int getTimeDelayInMillis() {
        int time;

        if(timeDelay != Constants.NOT_SET) {
            time = timeDelay;
        } else if(adminPanelTimeDelay != Constants.NOT_SET) {
            time = adminPanelTimeDelay;
        } else {
            time = 0;
        }

        return time*1000;
    }

    public LocalizedTexts getLocalizedTexts() {
        return localizedTexts;
    }

    public String getNpsQuestion() {
        return localizedTexts.getNpsQuestion();
    }

    public String getAnchorLikely() {
        return localizedTexts.getAnchorLikely();
    }

    public String getAnchorNotLikely() {
        return localizedTexts.getAnchorNotLikely();
    }

    public String getBtnSubmit() {
        return localizedTexts.getSubmit().toUpperCase();
    }

    public String getBtnDismiss() {
        return localizedTexts.getDismiss().toUpperCase();
    }

    public String getFollowupQuestion(int score) {
        String followupQuestion = null;

        if(localCustomMessage != null) {
            followupQuestion = localCustomMessage.getFollowupQuestionForScore(score);
        }

        if(followupQuestion == null && adminPanelCustomMessage != null) {
            followupQuestion =  adminPanelCustomMessage.getFollowupQuestionForScore(score);
        }

        if(followupQuestion == null) {
            followupQuestion = localizedTexts.getFollowupQuestion();
        }

        return followupQuestion;
    }

    public String getFollowupPlaceholder(int score) {
        String followupPlaceholder = null;

        if(localCustomMessage != null) {
            followupPlaceholder = localCustomMessage.getPlaceholderForScore(score);
        }

        if(followupPlaceholder == null && adminPanelCustomMessage != null) {
            followupPlaceholder =  adminPanelCustomMessage.getPlaceholderForScore(score);
        }

        if(followupPlaceholder == null) {
            followupPlaceholder = localizedTexts.getFollowupPlaceholder();
        }

        return followupPlaceholder;
    }

    public String getFinalThankYou() {
        return localizedTexts.getFinalThankYou();
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void setFirstSurveyDelay(long firstSurvey) {
        this.firstSurvey = firstSurvey;
    }

    public Long getFirstSurveyDelay() {
        return firstSurvey;
    }

    public void setLocalCustomMessage(CustomMessage customMessage) {
        this.localCustomMessage = customMessage;
    }

    public CustomMessage getLocalCustomMessage() {
        return localCustomMessage;
    }

    public void setLocalizedTexts(LocalizedTexts localizedTexts) {
        this.localizedTexts = localizedTexts;
    }

    public void setAdminPanelCustomMessage(CustomMessage adminPanelCustomMessage) {
        this.adminPanelCustomMessage = adminPanelCustomMessage;
    }

    public CustomMessage getAdminPanelCustomMessage() {
        return adminPanelCustomMessage;
    }

    public void setAdminPanelTimeDelay(int adminPanelTimeDelay) {
        this.adminPanelTimeDelay = adminPanelTimeDelay;
    }

    public int getAdminPanelTimeDelay() {
        return adminPanelTimeDelay;
    }

    public void setDailyResponseCap(int dailyResponseCap) {
        this.dailyResponseCap = dailyResponseCap;
    }

    public void setRegisteredPercent(Integer registeredPercent) {
        this.registeredPercent = registeredPercent;
    }

    public void setVisitorPercent(Integer visitorPercent) {
        this.visitorPercent = visitorPercent;
    }

    public void setResurveyThrottle(Integer resurveyThrottle) {
        this.resurveyThrottle = resurveyThrottle;
    }

    public Integer getDailyResponseCap() {
        return dailyResponseCap;
    }

    public Integer getRegisteredPercent() {
        return registeredPercent;
    }

    public Integer getVisitorPercent() {
        return visitorPercent;
    }

    public Integer getResurveyThrottle() {
        return resurveyThrottle;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRecommendTarget() {
        return recommendTarget;
    }

    public void setRecommendTarget(String recommendTarget) {
        this.recommendTarget = recommendTarget;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.firstSurvey);
        dest.writeInt(this.adminPanelTimeDelay);
        dest.writeParcelable(this.localizedTexts, 0);
        dest.writeParcelable(this.adminPanelCustomMessage, 0);
        dest.writeParcelable(this.localCustomMessage, 0);
        dest.writeByte(surveyImmediately ? (byte) 1 : (byte) 0);
    }

    public Settings() {
    }

    private Settings(Parcel in) {
        this.firstSurvey = in.readLong();
        this.adminPanelTimeDelay = in.readInt();
        this.localizedTexts = in.readParcelable(LocalizedTexts.class.getClassLoader());
        this.adminPanelCustomMessage = in.readParcelable(CustomMessage.class.getClassLoader());
        this.localCustomMessage = in.readParcelable(CustomMessage.class.getClassLoader());
        this.surveyImmediately = in.readByte() != 0;
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