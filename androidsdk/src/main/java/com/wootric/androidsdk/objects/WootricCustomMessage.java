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

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/17/15.
 */
public class WootricCustomMessage implements Parcelable {
    private static final String DETRACTOR_QUESTION_KEY = "detractor_question";
    private static final String PASSIVE_QUESTION_KEY = "passive_question";
    private static final String PROMOTER_QUESTION_KEY = "promoter_question";
    private static final String DETRACTOR_TEXT_KEY = "detractor_text";
    private static final String PASSIVE_TEXT_KEY = "passive_text";
    private static final String PROMOTER_TEXT_KEY = "promoter_text";

    private String followupQuestion;
    private HashMap<String, String> followupQuestionsList;
    private String placeholderText;
    private HashMap<String, String> placeholderTextsList;

    private JSONObject driverPicklist;
    private JSONObject driverPicklistList;

    public WootricCustomMessage() {
        this.followupQuestionsList = new HashMap<>();
        this.placeholderTextsList = new HashMap<>();
    }

    public WootricCustomMessage(WootricCustomMessage wootricCustomMessage) {
        this.followupQuestionsList = new HashMap<>();
        this.placeholderTextsList = new HashMap<>();

        if (wootricCustomMessage == null) return;

        this.followupQuestion = wootricCustomMessage.followupQuestion;
        this.followupQuestionsList = wootricCustomMessage.followupQuestionsList;
        this.placeholderText = wootricCustomMessage.placeholderText;
        this.placeholderTextsList = wootricCustomMessage.placeholderTextsList;
    }

    public String getFollowupQuestion() {
        return followupQuestion;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    String getDetractorQuestion() {
        return followupQuestionsList.get(DETRACTOR_QUESTION_KEY);
    }

    String getPassiveQuestion() {
        return followupQuestionsList.get(PASSIVE_QUESTION_KEY);
    }

    String getPromoterQuestion() {
        return followupQuestionsList.get(PROMOTER_QUESTION_KEY);
    }

    String getDetractorPlaceholder() {
        return placeholderTextsList.get(DETRACTOR_TEXT_KEY);
    }

    String getPassivePlaceholder() {
        return placeholderTextsList.get(PASSIVE_TEXT_KEY);
    }

    String getPromoterPlaceholder() {
        return placeholderTextsList.get(PROMOTER_TEXT_KEY);
    }

    JSONObject getDetractorPicklist() throws JSONException {
        return driverPicklistList.getJSONObject("detractor_picklist");
    }

    JSONObject getPassivePicklist() throws JSONException {
        return driverPicklistList.getJSONObject("passive_picklist");
    }

    JSONObject getPromoterPicklist() throws JSONException {
        return driverPicklistList.getJSONObject("promoter_picklist");
    }

    public void setFollowupQuestion(String followupQuestion) {
        this.followupQuestion = followupQuestion;
    }

    public void setDetractorFollowupQuestion(String detractorFollowupQuestion) {
        this.followupQuestionsList.put(DETRACTOR_QUESTION_KEY, detractorFollowupQuestion);
    }

    public void setPassiveFollowupQuestion(String passiveFollowupQuestion) {
        this.followupQuestionsList.put(PASSIVE_QUESTION_KEY, passiveFollowupQuestion);
    }

    public void setPromoterFollowupQuestion(String promoterFollowupQuestion) {
        this.followupQuestionsList.put(PROMOTER_QUESTION_KEY, promoterFollowupQuestion);
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }

    public void setDetractorPlaceholderText(String detractorPlaceholderText) {
        this.placeholderTextsList.put(DETRACTOR_TEXT_KEY, detractorPlaceholderText);
    }

    public void setPassivePlaceholderText(String passivePlaceholderText) {
        this.placeholderTextsList.put(PASSIVE_TEXT_KEY, passivePlaceholderText);
    }

    public void setPromoterPlaceholderText(String promoterPlaceholderText) {
        this.placeholderTextsList.put(PROMOTER_TEXT_KEY, promoterPlaceholderText);
    }

    public String getFollowupQuestionForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        final Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && getDetractorQuestion() != null) {
            return getDetractorQuestion();
        } else if(score.isPassive() && getPassiveQuestion()  != null) {
            return getPassiveQuestion() ;
        } else if(score.isPromoter() && getPromoterQuestion() != null) {
            return getPromoterQuestion();
        } else {
            return followupQuestion;
        }
    }

    public String getPlaceholderForScore(int scoreValue, String surveyType, int surveyTypeScale) {
        final Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && getDetractorPlaceholder() != null) {
            return getDetractorPlaceholder();
        } else if(score.isPassive() && getPassivePlaceholder() != null) {
            return getPassivePlaceholder();
        } else if(score.isPromoter() && getPromoterPlaceholder() != null) {
            return getPromoterPlaceholder();
        } else {
            return placeholderText;
        }
    }

    public JSONObject getDriverPicklistForScore(int scoreValue, String surveyType, int surveyTypeScale) throws JSONException{
        final Score score = new Score(scoreValue, surveyType, surveyTypeScale);

        if(score.isDetractor() && getDetractorPicklist() != null) {
            return getDetractorPicklist();
        } else if(score.isPassive() && getPassivePicklist() != null) {
            return getPassivePicklist();
        } else if(score.isPromoter() && getPromoterPicklist() != null) {
            return getPromoterPicklist();
        } else {
            return driverPicklist;
        }
    }

    public void setFollowupQuestionsList(HashMap<String, String> followupQuestionsList) {
        this.followupQuestionsList = followupQuestionsList;
    }

    public void setPlaceholderTextsList(HashMap<String, String> placeholderTextsList) {
        this.placeholderTextsList = placeholderTextsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.followupQuestion);
        dest.writeSerializable(this.followupQuestionsList);
        dest.writeString(this.placeholderText);
        dest.writeSerializable(this.placeholderTextsList);
    }

    private WootricCustomMessage(Parcel in) {
        this.followupQuestion = in.readString();
        this.followupQuestionsList = (HashMap<String, String>) in.readSerializable();
        this.placeholderText = in.readString();
        this.placeholderTextsList = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<WootricCustomMessage> CREATOR = new Creator<WootricCustomMessage>() {
        public WootricCustomMessage createFromParcel(Parcel source) { return new WootricCustomMessage(source); }
        public WootricCustomMessage[] newArray(int size) {
            return new WootricCustomMessage[size];
        }
    };

    public static WootricCustomMessage fromJson(JSONObject customMessagesJson) throws JSONException {
        if(customMessagesJson == null) return null;

        WootricCustomMessage wootricCustomMessage = new WootricCustomMessage();

        wootricCustomMessage.followupQuestion = customMessagesJson.optString("followup_question");
        wootricCustomMessage.placeholderText = customMessagesJson.optString("placeholder_text");
        wootricCustomMessage.driverPicklist = customMessagesJson.optJSONObject("picklist");

        wootricCustomMessage.followupQuestionsList = new HashMap<>();
        JSONObject followupQuestionListJson = customMessagesJson.optJSONObject("followup_questions_list");
        if(followupQuestionListJson != null) {
            wootricCustomMessage.followupQuestionsList.put(DETRACTOR_QUESTION_KEY, followupQuestionListJson.optString(DETRACTOR_QUESTION_KEY));
            wootricCustomMessage.followupQuestionsList.put(PASSIVE_QUESTION_KEY, followupQuestionListJson.optString(PASSIVE_QUESTION_KEY));
            wootricCustomMessage.followupQuestionsList.put(PROMOTER_QUESTION_KEY, followupQuestionListJson.optString(PROMOTER_QUESTION_KEY));
        }

        wootricCustomMessage.placeholderTextsList = new HashMap<>();
        JSONObject placeholderTextsListJson = customMessagesJson.optJSONObject("placeholder_texts_list");
        if(placeholderTextsListJson != null) {
            wootricCustomMessage.placeholderTextsList.put(DETRACTOR_TEXT_KEY, placeholderTextsListJson.optString(DETRACTOR_TEXT_KEY));
            wootricCustomMessage.placeholderTextsList.put(PASSIVE_TEXT_KEY, placeholderTextsListJson.optString(PASSIVE_TEXT_KEY));
            wootricCustomMessage.placeholderTextsList.put(PROMOTER_TEXT_KEY, placeholderTextsListJson.optString(PROMOTER_TEXT_KEY));
        }

        wootricCustomMessage.driverPicklistList = customMessagesJson.optJSONObject("driver_picklist");
        return wootricCustomMessage;
    }
}
