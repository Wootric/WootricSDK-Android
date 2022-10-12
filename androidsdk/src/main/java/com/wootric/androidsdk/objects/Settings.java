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

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.wootric.androidsdk.utils.Utils.isBlank;

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

    private WootricSocial adminPanelSocial;
    private WootricSocial localSocial;

    private WootricCustomThankYou localCustomThankYou;
    private WootricCustomThankYou adminPanelCustomThankYou;

    private JSONObject driverPicklist;

    private int timeDelay = Constants.NOT_SET;

    private boolean surveyedDefault = true;
    private boolean surveyImmediately;
    private boolean showOptOut;
    private boolean skipFollowupScreenForPromoters;
    private boolean skipFeedbackScreen;

    private Integer dailyResponseCap;
    private Integer registeredPercent;
    private Integer visitorPercent;
    private Integer resurveyThrottle;
    private Integer declineResurveyThrottle;

    private String languageCode;
    private String productName;
    private String recommendTarget;
    private String surveyType;
    private String eventName;

    private int surveyColor = Constants.NOT_SET;
    private int scoreColor = Constants.NOT_SET;
    private int thankYouButtonBackgroundColor = Constants.NOT_SET;
    private int socialSharingColor = Constants.NOT_SET;
    private int surveyTypeScale = 0;

    private String clientID;
    private String accountToken;

    public Settings(Settings settings) {
        this.firstSurvey = settings.firstSurvey;
        this.adminPanelTimeDelay = settings.getAdminPanelTimeDelay();
        this.localizedTexts = new LocalizedTexts(settings.getLocalizedTexts());
        this.userID = settings.getUserID();
        this.accountID = settings.getAccountID();
        this.adminPanelCustomMessage = new WootricCustomMessage(settings.getAdminPanelCustomMessage());
        this.localCustomMessage = new WootricCustomMessage(settings.getLocalCustomMessage());
        this.adminPanelSocial = new WootricSocial(settings.adminPanelSocial);
        this.localSocial = new WootricSocial(settings.localSocial);
        this.localCustomThankYou = new WootricCustomThankYou(settings.localCustomThankYou);
        this.adminPanelCustomThankYou = new WootricCustomThankYou(settings.adminPanelCustomThankYou);
//        this.driverPicklist = new WootricDriverPicklist(settings.driverPicklist);
        this.timeDelay = settings.timeDelay;
        this.surveyedDefault = settings.surveyedDefault;
        this.surveyImmediately = settings.surveyImmediately;
        this.showOptOut = settings.showOptOut;
        this.skipFollowupScreenForPromoters = settings.skipFollowupScreenForPromoters;
        this.skipFeedbackScreen = settings.skipFeedbackScreen;
        this.dailyResponseCap = settings.dailyResponseCap;
        this.registeredPercent = settings.registeredPercent;
        this.visitorPercent = settings.visitorPercent;
        this.resurveyThrottle = settings.resurveyThrottle;
        this.declineResurveyThrottle = settings.declineResurveyThrottle;
        this.languageCode = settings.languageCode;
        this.productName = settings.productName;
        this.recommendTarget = settings.recommendTarget;
        this.surveyType = settings.surveyType;
        this.eventName = settings.eventName;
        this.surveyColor = settings.surveyColor;
        this.scoreColor = settings.scoreColor;
        this.thankYouButtonBackgroundColor = settings.thankYouButtonBackgroundColor;
        this.socialSharingColor = settings.socialSharingColor;
        this.surveyTypeScale = settings.surveyTypeScale;
    }

    public Settings() {
        this.localSocial = new WootricSocial();
    }

    // Sets values from the argument settings only if they are not provided yet
    public void mergeWithSurveyServerSettings(Settings settings) {
        if (settings == null) {
            return;
        }

        this.adminPanelCustomMessage = settings.adminPanelCustomMessage;
        this.adminPanelTimeDelay = settings.adminPanelTimeDelay;
        this.adminPanelCustomThankYou = settings.adminPanelCustomThankYou;
        this.adminPanelSocial = settings.adminPanelSocial;
        this.driverPicklist = settings.driverPicklist;
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

    public void setSurveyedDefault(boolean surveyedDefault) {
        this.surveyedDefault = surveyedDefault;
    }

    public void setShowOptOut(boolean showOptOut) {
        this.showOptOut = showOptOut;
    }

    public boolean isShowOptOut() { return showOptOut; }

    public boolean isSurveyImmediately() {
        return surveyImmediately;
    }

    public boolean isSurveyedDefault() {
        return surveyedDefault;
    }

    public void setSkipFollowupScreenForPromoters(boolean skipFollowupScreenForPromoters) {
        this.skipFollowupScreenForPromoters = skipFollowupScreenForPromoters;
    }

    public boolean shouldSkipFollowupScreenForPromoters() {
        return skipFollowupScreenForPromoters;
    }

    public void setSkipFeedbackScreen(boolean skipFeedbackScreen) {
        this.skipFeedbackScreen = skipFeedbackScreen;
    }

    public boolean skipFeedbackScreen() { return skipFeedbackScreen; }

    public int getTimeDelayInMillis() {
        int time;

        if (timeDelay != Constants.NOT_SET) {
            time = timeDelay;

        } else if (adminPanelTimeDelay != Constants.NOT_SET) {
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

    public String getBtnOptOut() { return localizedTexts.getOptOut().toUpperCase(); }

    public JSONObject getDriverPicklist(int score) throws JSONException {
        JSONObject dpl = new JSONObject();
        if (adminPanelCustomMessage != null) {
            dpl =  adminPanelCustomMessage.getDriverPicklistForScore(score, surveyType, surveyTypeScale);
        }
        return dpl;
    }

    public String getFollowupQuestion(int score) {
        String followupQuestion = null;

        if (localCustomMessage != null) {
            followupQuestion = localCustomMessage.getFollowupQuestionForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(followupQuestion) && adminPanelCustomMessage != null) {
            followupQuestion =  adminPanelCustomMessage.getFollowupQuestionForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(followupQuestion)) {
            followupQuestion = localizedTexts.getFollowupQuestion();
        }

        return followupQuestion;
    }

    public String getFollowupPlaceholder(int score) {
        String followupPlaceholder = null;

        if (localCustomMessage != null) {
            followupPlaceholder = localCustomMessage.getPlaceholderForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(followupPlaceholder) && adminPanelCustomMessage != null) {
            followupPlaceholder =  adminPanelCustomMessage.getPlaceholderForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(followupPlaceholder)) {
            followupPlaceholder = localizedTexts.getFollowupPlaceholder();
        }

        return followupPlaceholder;
    }

    public String getCustomThankYouMessage(int score) {
        String thankYouMessage = null;

        if (localCustomThankYou != null) {
            thankYouMessage = localCustomThankYou.getTextForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(thankYouMessage) && adminPanelCustomThankYou != null) {
            thankYouMessage =  adminPanelCustomThankYou.getTextForScore(score, surveyType, surveyTypeScale);

            if (isBlank(thankYouMessage)) {
                Score mScore = new Score(score, this.getSurveyType(), this.getSurveyTypeScale());
                if (mScore.isPromoter() && (adminPanelCustomThankYou.getUniqueByScore() || isTwitterEnabled() || isFacebookEnabled())) {
                    thankYouMessage = localizedTexts.getSocialShareQuestion();
                }
            }
        }

        return thankYouMessage;
    }

    public String getFinalThankYou(int score) {
        String finalThankYou = null;

        if (localCustomThankYou != null) {
            finalThankYou = localCustomThankYou.getFinalThankYouForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(finalThankYou) && adminPanelCustomThankYou != null) {
            finalThankYou =  adminPanelCustomThankYou.getFinalThankYouForScore(score, surveyType, surveyTypeScale);
        }

        if (isBlank(finalThankYou) && localizedTexts != null) {
            finalThankYou = localizedTexts.getFinalThankYou();
        }

        return finalThankYou;
    }

    public String getThankYouLinkText(int score) {
        String thankYouLinkText = null;

        if (localCustomThankYou != null) {
            thankYouLinkText = localCustomThankYou.getLinkTextForScore(score, surveyType, surveyTypeScale);
        }

        if (adminPanelCustomThankYou != null && thankYouLinkText == null) {
            thankYouLinkText = adminPanelCustomThankYou.getLinkTextForScore(score, surveyType, surveyTypeScale);
        }

        return thankYouLinkText;
    }

    public Uri getThankYouLinkUri(String email, int score, String comment) {
        Uri thankYouLinkUri = null;

        if (localCustomThankYou != null) {
            thankYouLinkUri = localCustomThankYou.getLinkUri(score, surveyType, surveyTypeScale);

        }

        if (adminPanelCustomThankYou != null && thankYouLinkUri == null) {
            thankYouLinkUri = adminPanelCustomThankYou.getLinkUri(score, surveyType, surveyTypeScale);
        }

        if (thankYouLinkUri != null) {
            thankYouLinkUri = addEmail(thankYouLinkUri, email, score);
            thankYouLinkUri = addScore(thankYouLinkUri, score);
            thankYouLinkUri = addComment(thankYouLinkUri, comment, score);
        }

        return thankYouLinkUri;
    }

    private Uri addEmail(Uri link, String email, int score) {
        Boolean localEmailInUri = null;
        Boolean adminEmailInUri = null;
        boolean addEmail = false;

        if (localCustomThankYou != null) {
            localEmailInUri = localCustomThankYou.getEmailInUri(score, surveyType, surveyTypeScale);
        }

        if (adminPanelCustomThankYou != null) {
            adminEmailInUri = adminPanelCustomThankYou.getEmailInUri(score, surveyType, surveyTypeScale);
        }

        if (adminEmailInUri != null) {
            addEmail = adminEmailInUri;
        }

        if (localEmailInUri != null) {
            addEmail = localEmailInUri;
        }

        if (addEmail){
            link = link.buildUpon()
                    .appendQueryParameter("wootric_email", email)
                    .build();
        }

        return link;
    }

    private Uri addScore(Uri link, int score) {
        Boolean localScoreInUri = null;
        Boolean adminScoreInUri = null;
        boolean addScore = false;

        if (localCustomThankYou != null) {
            localScoreInUri = localCustomThankYou.getScoreInUri(score, surveyType, surveyTypeScale);
        }

        if (adminPanelCustomThankYou != null) {
            adminScoreInUri = adminPanelCustomThankYou.getScoreInUri(score, surveyType, surveyTypeScale);
        }

        if (adminScoreInUri != null) {
            addScore = adminScoreInUri;
        }

        if (localScoreInUri != null) {
            addScore = localScoreInUri;
        }

        if (addScore){
            link = link.buildUpon()
                    .appendQueryParameter("wootric_score", String.valueOf(score))
                    .build();
        }

        return link;
    }

    private Uri addComment(Uri link, String comment, int score) {
        Boolean localCommentInUri = null;
        Boolean adminCommentInUri = null;
        boolean addComment = false;

        if (localCustomThankYou != null) {
            localCommentInUri = localCustomThankYou.getCommentInUri(score, surveyType, surveyTypeScale);
        }

        if (adminPanelCustomThankYou != null) {
            adminCommentInUri = adminPanelCustomThankYou.getCommentInUri(score, surveyType, surveyTypeScale);
        }

        if (adminCommentInUri != null) {
            addComment = adminCommentInUri;
        }

        if (localCommentInUri != null) {
            addComment = localCommentInUri;
        }

        if (addComment){
            link = link.buildUpon()
                    .appendQueryParameter("wootric_comment", comment)
                    .build();
        }

        return link;
    }

    public boolean isThankYouActionConfigured(String email, int score, String comment) {
        return isLocalThankYouConfigured(score) || isAdminPanelThankYouConfigured(score);
    }

    private boolean isLocalThankYouConfigured(int score) {
        return localCustomThankYou != null &&
                localCustomThankYou.getLinkTextForScore(score, surveyType, surveyTypeScale) != null &&
                localCustomThankYou.getLinkUri(score, surveyType, surveyTypeScale) != null;
    }

    private boolean isAdminPanelThankYouConfigured(int score) {
        return adminPanelCustomThankYou != null &&
                adminPanelCustomThankYou.getLinkTextForScore(score, surveyType, surveyTypeScale) != null &&
                adminPanelCustomThankYou.getLinkUri(score, surveyType, surveyTypeScale) != null;
    }

    public String getSocialShareQuestion() {
        return localizedTexts.getSocialShareQuestion();
    }

    public String getSocialShareDecline() {
        return localizedTexts.getSocialShareDecline();
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

    public void setLocalCustomThankYou(WootricCustomThankYou localCustomThankYou) {
        this.localCustomThankYou = localCustomThankYou;
    }

    public WootricCustomThankYou getLocalCustomThankYou() { return localCustomThankYou; }

    public void setAdminPanelCustomThankYou(WootricCustomThankYou adminPanelCustomThankYou) {
        this.adminPanelCustomThankYou = adminPanelCustomThankYou;
    }

    public WootricCustomThankYou getAdminPanelCustomThankYou() {
        return adminPanelCustomThankYou;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
        String facebookPageId;

        facebookPageId = localSocial.getFacebookPageId();

        if (isBlank(facebookPageId) && adminPanelSocial != null) {
            facebookPageId = adminPanelSocial.getFacebookPageId();
        }

        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) { this.localSocial.setFacebookPageId(facebookPageId); }

    public String getTwitterPage() {
        String twitterPage;

        twitterPage = localSocial.getTwitterPage();

        if (isBlank(twitterPage) && adminPanelSocial != null) {
            twitterPage = adminPanelSocial.getTwitterPage();
        }

        return twitterPage;
    }

    public boolean isTwitterEnabled() {
        boolean twitterEnabled = false;

        if (adminPanelSocial != null && adminPanelSocial.isTwitterEnabled() != null) {
            twitterEnabled = adminPanelSocial.isTwitterEnabled();
        }

        if (localSocial != null && localSocial.isTwitterEnabled() != null) {
            twitterEnabled = localSocial.isTwitterEnabled();
        }

        return twitterEnabled;
    }

    public boolean isFacebookEnabled() {
        boolean facebookEnabled = false;

        if (adminPanelSocial != null && adminPanelSocial.isFacebookEnabled() != null) {
            facebookEnabled = adminPanelSocial.isFacebookEnabled();
        }

        if (localSocial != null && localSocial.isFacebookEnabled() != null) {
            facebookEnabled = localSocial.isFacebookEnabled();
        }

        return facebookEnabled;
    }

    public void setTwitterPage(String twitterPage) { this.localSocial.setTwitterPage(twitterPage); }

    public String getSurveyType() { return surveyType; }

    public int getSurveyTypeScale() { return surveyTypeScale; }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public void setCustomThankYou(WootricCustomThankYou customThankYou) {
        this.localCustomThankYou = customThankYou;
    }

    public void setCustomSurveyTypeScale(int customSurveyTypeScale) {
        this.surveyTypeScale = customSurveyTypeScale < 0 ? 0 : customSurveyTypeScale;

    }

    public int getSurveyColor() {
        if (surveyColor != Constants.NOT_SET){
            return surveyColor;
        }
        return R.color.wootric_survey_layout_header_background;
    }

    public void setSurveyColor(int surveyColor) {
        this.surveyColor = surveyColor;
    }

    public int getScoreColor() {
        if (scoreColor != Constants.NOT_SET){
            return scoreColor;
        }
        return R.color.wootric_score_color;
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
    }

    public int getThankYouButtonBackgroundColor () {
        if (thankYouButtonBackgroundColor != Constants.NOT_SET) {
            return thankYouButtonBackgroundColor;
        }
        return R.color.wootric_score_color;
    }

    public void setThankYouButtonBackgroundColor (int thankYouButtonBackgroundColor) {
        this.thankYouButtonBackgroundColor = thankYouButtonBackgroundColor;
    }

    public int getSocialSharingColor () {
        if (socialSharingColor != Constants.NOT_SET) {
            return socialSharingColor;
        }
        return R.color.wootric_social;
    }

    public void setSocialSharingColor (int socialSharingColor) {
        this.socialSharingColor = socialSharingColor;
    }

    public String getAccountToken() {
        return this.accountToken;
    }

    public String getClientId() {
        return this.clientID;
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
        dest.writeByte(surveyedDefault ? (byte) 1 : (byte) 0);
        dest.writeValue(this.dailyResponseCap);
        dest.writeValue(this.registeredPercent);
        dest.writeValue(this.visitorPercent);
        dest.writeValue(this.resurveyThrottle);
        dest.writeValue(this.declineResurveyThrottle);
        dest.writeString(this.languageCode);
        dest.writeString(this.productName);
        dest.writeString(this.recommendTarget);
        dest.writeString(this.surveyType);
        dest.writeParcelable(this.localCustomThankYou, 0);
        dest.writeParcelable(this.adminPanelCustomThankYou, 0);
        dest.writeParcelable(this.localSocial, 0);
        dest.writeParcelable(this.adminPanelSocial, 0);
        dest.writeString(this.accountToken);
        dest.writeString(this.clientID);
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
        this.surveyedDefault = in.readByte() != 0;
        this.dailyResponseCap = (Integer) in.readValue(Integer.class.getClassLoader());
        this.registeredPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visitorPercent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resurveyThrottle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.declineResurveyThrottle = (Integer) in.readValue(Integer.class.getClassLoader());
        this.languageCode = in.readString();
        this.productName = in.readString();
        this.recommendTarget = in.readString();
        this.surveyType = in.readString();
        this.localCustomThankYou = in.readParcelable(WootricCustomThankYou.class.getClassLoader());
        this.adminPanelCustomThankYou = in.readParcelable(WootricCustomThankYou.class.getClassLoader());
        this.localSocial = in.readParcelable(WootricSocial.class.getClassLoader());
        this.adminPanelSocial = in.readParcelable(WootricSocial.class.getClassLoader());
        this.accountToken = in.readString();
        this.clientID = in.readString();
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
        settings.adminPanelTimeDelay = settingsObject.optInt("time_delay");

        if (settingsObject.has("account_token")) {
            settings.accountToken = settingsObject.optString("account_token");
        }

        if (settingsObject.has("client_id")) {
            settings.clientID = settingsObject.optString("client_id");
        }

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

        JSONObject customThankYouJson = settingsObject.optJSONObject("custom_thank_you");
        settings.adminPanelCustomThankYou = WootricCustomThankYou.fromJson(customThankYouJson);

        JSONObject socialJson = settingsObject.optJSONObject("social");
        settings.adminPanelSocial = WootricSocial.fromJson(socialJson);

        return settings;
    }
}