/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.FontRes;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by maciejwitowski on 5/5/15.
 */
public class Settings implements Parcelable {

    private Long firstSurvey = -1L;
    private int adminPanelTimeDelay = Constants.NOT_SET;
    private LocalizedTexts localizedTexts;

    private Long userID;
    private Long accountID;

    private WootricCustomMessage adminPanelCustomMessage;
    private WootricCustomMessage localCustomMessage;

    private int timeDelay = Constants.NOT_SET;

    private boolean surveyImmediately;
    private boolean skipFollowupScreenForPromoters;

    private Integer dailyResponseCap;
    private Integer registeredPercent;
    private Integer visitorPercent;
    private Integer resurveyThrottle;
    private Integer declineResurveyThrottle;

    private String languageCode;
    private String productName;
    private String recommendTarget;
    private String facebookPageId;
    private String twitterPage;
    private String surveyType;

    private WootricCustomThankYou customThankYou;

    private @FontRes int surveyDefaultFontResId = Constants.NOT_SET;
    private @FontRes int surveyTitleFontResId = Constants.NOT_SET;
    private @FontRes int surveyBtnFontResId = Constants.NOT_SET;

    private @ColorRes int surveyTitleBackgroundColor = Constants.NOT_SET;
    private @ColorRes int surveyTitleTextColor = Constants.NOT_SET;
    private @ColorRes int surveyScoreColor = Constants.NOT_SET;
    private @ColorRes int surveyBtnColor = Constants.NOT_SET;

    private @ColorRes int thankYouButtonBackgroundColor = Constants.NOT_SET;
    private @ColorRes int socialSharingColor = Constants.NOT_SET;

    // Sets values from the argument settings only if they are not provided yet
    public void mergeWithSurveyServerSettings(Settings settings) {
        if(settings == null) {
            return;
        }

        this.adminPanelCustomMessage = settings.adminPanelCustomMessage;
        this.adminPanelTimeDelay = settings.adminPanelTimeDelay;
        this.localizedTexts = settings.localizedTexts;
        this.userID = settings.userID;
        this.accountID = settings.accountID;
        this.resurveyThrottle = settings.resurveyThrottle;
        this.declineResurveyThrottle = settings.declineResurveyThrottle;
        this.surveyType = settings.surveyType;
    }

    public boolean firstSurveyDelayPassed(long timeFrom) {

        Boolean firstCriteria = timeFrom == Constants.NOT_SET;
        Boolean secondCriteria;
        if (getFirstSurveyDelay() != null) {
            secondCriteria = new Date().getTime() - (firstSurvey * Constants.DAY_IN_MILLIS) >= timeFrom;
        } else {
            secondCriteria = new Date().getTime() - (31L * Constants.DAY_IN_MILLIS) >= timeFrom;
        }

        if (firstCriteria || secondCriteria) {
            return true;
        }
        return  false;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public void setSkipFollowupScreenForPromoters(boolean skipFollowupScreenForPromoters) {
        this.skipFollowupScreenForPromoters = skipFollowupScreenForPromoters;
    }

    public boolean shouldSkipFollowupScreenForPromoters() {
        return skipFollowupScreenForPromoters;
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

    public String getSurveyQuestion() {
        return localizedTexts.getSurveyQuestion();
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

    public String getBtnEditScore() {
        return localizedTexts.getEditScore().toUpperCase();
    }

    public String getFollowupQuestion(int score) {
        String followupQuestion = null;

        if(localCustomMessage != null) {
            followupQuestion = localCustomMessage.getFollowupQuestionForScore(score, surveyType);
        }

        if(followupQuestion == null && adminPanelCustomMessage != null) {
            followupQuestion =  adminPanelCustomMessage.getFollowupQuestionForScore(score, surveyType);
        }

        if(followupQuestion == null) {
            followupQuestion = localizedTexts.getFollowupQuestion();
        }

        return followupQuestion;
    }

    public String getFollowupPlaceholder(int score) {
        String followupPlaceholder = null;

        if(localCustomMessage != null) {
            followupPlaceholder = localCustomMessage.getPlaceholderForScore(score, surveyType);
        }

        if(followupPlaceholder == null && adminPanelCustomMessage != null) {
            followupPlaceholder =  adminPanelCustomMessage.getPlaceholderForScore(score, surveyType);
        }

        if(followupPlaceholder == null) {
            followupPlaceholder = localizedTexts.getFollowupPlaceholder();
        }

        return followupPlaceholder;
    }

    public String getCustomThankYouMessage(int score) {
        String thankYouMessage = null;

        if(customThankYou != null) {
            thankYouMessage = customThankYou.getTextForScore(score, surveyType);
        }

        return thankYouMessage;
    }

    public String getThankYouMessage() {
        return localizedTexts.getFinalThankYou();
    }

    public String getThankYouLinkText(int score) {
        return (customThankYou == null) ?
                null : customThankYou.getLinkTextForScore(score, surveyType);
    }

    public Uri getThankYouLinkUri(int score, String comment) {
        return (customThankYou == null) ?
                null :  customThankYou.getLinkUri(score, comment, surveyType);
    }

    public boolean isThankYouActionConfigured(int score, String comment) {
        return customThankYou != null &&
                customThankYou.getLinkTextForScore(score, surveyType) != null &&
                customThankYou.getLinkUri(score, comment, surveyType) != null;
    }

    public String getSocialShareQuestion() {
        return localizedTexts.getSocialShareQuestion();
    }

    public String getSocialShareDecline() {
        return localizedTexts.getSocialShareDecline();
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
        return firstSurvey == -1L? null : firstSurvey;
    }

    public void setUserID(long userID) { this.userID = userID; }

    public Long getUserID() { return userID; }

    public void setAccountID(long accountID) { this.accountID = accountID; }

    public Long getAccountID() { return accountID; }

    public void setLocalCustomMessage(WootricCustomMessage customMessage) {
        this.localCustomMessage = customMessage;
    }

    public WootricCustomMessage getLocalCustomMessage() {
        return localCustomMessage;
    }

    public void setLocalizedTexts(LocalizedTexts localizedTexts) {
        this.localizedTexts = localizedTexts;
    }

    public void setAdminPanelCustomMessage(WootricCustomMessage adminPanelCustomMessage) {
        this.adminPanelCustomMessage = adminPanelCustomMessage;
    }

    public WootricCustomMessage getAdminPanelCustomMessage() {
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

    public void setDeclineResurveyThrottle(Integer declineResurveyThrottle) {
        this.declineResurveyThrottle = declineResurveyThrottle;
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

    public Integer getDeclineResurveyThrottle() {
        return declineResurveyThrottle;
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

    public String getFacebookPageId() {
        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    public String getTwitterPage() {
        return twitterPage;
    }

    public void setTwitterPage(String twitterPage) {
        this.twitterPage = twitterPage;
    }

    public String getSurveyType() { return surveyType; }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public void setCustomThankYou(WootricCustomThankYou customThankYou) {
        this.customThankYou = customThankYou;
    }

    public int getSurveyScoreColor() {
        if (surveyScoreColor != Constants.NOT_SET){
            return surveyScoreColor;
        }
        return R.color.wootric_score_color;
    }

    public void setSurveyScoreColorResId(int surveyScoreColor) {
        this.surveyScoreColor = surveyScoreColor;
    }

    public int getSurveyBtnColor() {
        if (surveyBtnColor != Constants.NOT_SET){
            return surveyBtnColor;
        }
        return R.color.wootric_survey_layout_header_background;
    }

    public void setSurveyBtnColor(@ColorRes int surveyBtnColor) {
        this.surveyBtnColor = surveyBtnColor;
    }

    public int getThankYouButtonBackgroundColorResId() {
        if (thankYouButtonBackgroundColor != Constants.NOT_SET) {
            return thankYouButtonBackgroundColor;
        }
        return R.color.wootric_score_color;
    }

    public void setThankYouButtonBackgroundColorResId(int thankYouButtonBackgroundColor) {
        this.thankYouButtonBackgroundColor = thankYouButtonBackgroundColor;
    }

    public int getSocialSharingColorResId() {
        if (socialSharingColor != Constants.NOT_SET) {
            return socialSharingColor;
        }
        return R.color.wootric_social;
    }

    public void setSocialSharingColorResId(@ColorRes int socialSharingColor) {
        this.socialSharingColor = socialSharingColor;
    }

    public int getSurveyDefaultFontResId(){
        if (surveyDefaultFontResId != Constants.NOT_SET) {
            return surveyDefaultFontResId;
        }
        return Constants.NOT_SET;
    }

    public void setSurveyDefaultFontResId(@FontRes int surveyDefaultFontResId){
        this.surveyDefaultFontResId = surveyDefaultFontResId;
    }

    public int getSurveyTitleFontResId(){
        if (surveyTitleFontResId != Constants.NOT_SET) {
           return surveyTitleFontResId;
        }
        return Constants.NOT_SET;
    }

    public void setSurveyTitleFontResId(@FontRes int surveyTitleFontResId){
        this.surveyTitleFontResId = surveyTitleFontResId;
    }

    public int getSurveyBtnFontResId(){
        if (surveyBtnFontResId != Constants.NOT_SET) {
            return surveyBtnFontResId;
        }
        return Constants.NOT_SET;
    }

    public void setSurveyBtnFontResId(@FontRes int surveyBtnFontResId){
        this.surveyBtnFontResId = surveyBtnFontResId;
    }

    public int getSurveyTitleBackgroundColorResId(){
        if (surveyTitleBackgroundColor != Constants.NOT_SET) {
            return surveyTitleBackgroundColor;
        }
        return R.color.wootric_survey_layout_header_background;
    }

    public void setSurveyTitleBackgroundColorResId(@ColorRes int surveyTitleBackgroundColor){
        this.surveyTitleBackgroundColor = surveyTitleBackgroundColor;
    }

    public int getSurveyTitleTextColorResId(){
        if (surveyTitleTextColor != Constants.NOT_SET) {
            return surveyTitleTextColor;
        }
        return R.color.wootric_survey_title_header_text;
    }

    public void setSurveyTitleTextColorResId(@ColorRes int surveyTitleTextColor){
        this.surveyTitleTextColor = surveyTitleTextColor;
    }

    public Settings() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.firstSurvey);
        dest.writeValue(this.userID);
        dest.writeValue(this.accountID);
        dest.writeInt(this.adminPanelTimeDelay);
        dest.writeParcelable(this.localizedTexts, 0);
        dest.writeParcelable(this.adminPanelCustomMessage, 0);
        dest.writeParcelable(this.localCustomMessage, 0);
        dest.writeInt(this.timeDelay);
        dest.writeByte(surveyImmediately ? (byte) 1 : (byte) 0);
        dest.writeValue(this.dailyResponseCap);
        dest.writeValue(this.registeredPercent);
        dest.writeValue(this.visitorPercent);
        dest.writeValue(this.resurveyThrottle);
        dest.writeValue(this.declineResurveyThrottle);
        dest.writeString(this.languageCode);
        dest.writeString(this.productName);
        dest.writeString(this.recommendTarget);
        dest.writeString(this.facebookPageId);
        dest.writeString(this.twitterPage);
        dest.writeString(this.surveyType);
        dest.writeParcelable(this.customThankYou, 0);
    }

    private Settings(Parcel in) {
        this.firstSurvey = (Long) in.readValue(Long.class.getClassLoader());
        this.userID = (Long) in.readValue(Long.class.getClassLoader());
        this.accountID = (Long) in.readValue(Long.class.getClassLoader());
        this.adminPanelTimeDelay = in.readInt();
        this.localizedTexts = in.readParcelable(LocalizedTexts.class.getClassLoader());
        this.adminPanelCustomMessage = in.readParcelable(WootricCustomMessage.class.getClassLoader());
        this.localCustomMessage = in.readParcelable(WootricCustomMessage.class.getClassLoader());
        this.timeDelay = in.readInt();
        this.surveyImmediately = in.readByte() != 0;
        this.dailyResponseCap = (Integer) in.readValue(Integer.class.getClassLoader());
        this.registeredPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visitorPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resurveyThrottle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.declineResurveyThrottle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.languageCode = in.readString();
        this.productName = in.readString();
        this.recommendTarget = in.readString();
        this.facebookPageId = in.readString();
        this.twitterPage = in.readString();
        this.surveyType = in.readString();
        this.customThankYou = in.readParcelable(WootricCustomThankYou.class.getClassLoader());
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    public static Settings fromJson(JSONObject settingsObject) throws JSONException {
        Settings settings = new Settings();
        settings.setSurveyType(settingsObject.optString("survey_type", "NPS"));
        settings.firstSurvey = settingsObject.optLong("first_survey");
        settings.timeDelay = settingsObject.optInt("time_delay");
        if (settingsObject.has("account_id")) {
            settings.accountID = settingsObject.optLong("account_id");
        } else {
            settings.accountID = (long) Constants.NOT_SET;
        }
        if (settingsObject.has("user_id")) {
            settings.userID = settingsObject.optLong("user_id");
        } else {
            settings.userID = (long) Constants.NOT_SET;
        }
        if (settingsObject.has("resurvey_throttle")) {
            settings.setResurveyThrottle(settingsObject.optInt("resurvey_throttle"));
        }
        if (settingsObject.has("decline_resurvey_throttle")) {
            settings.setDeclineResurveyThrottle(settingsObject.optInt("decline_resurvey_throttle"));
        }

        JSONObject localizedTextsJson = settingsObject.optJSONObject("localized_texts");
        settings.localizedTexts = LocalizedTexts.fromJson(localizedTextsJson, settings.getSurveyType());

        JSONObject customMessagesJson = settingsObject.optJSONObject("messages");
        settings.adminPanelCustomMessage = WootricCustomMessage.fromJson(customMessagesJson);

        return settings;
    }
}