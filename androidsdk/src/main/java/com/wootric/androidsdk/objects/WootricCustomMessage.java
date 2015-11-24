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

    public WootricCustomMessage() {
        this.followupQuestionsList = new HashMap<>();
        this.placeholderTextsList = new HashMap<>();
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

    public String getFollowupQuestionForScore(int scoreValue) {
        final Score score = new Score(scoreValue);

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

    public String getPlaceholderForScore(int scoreValue) {
        final Score score = new Score(scoreValue);

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
        public WootricCustomMessage createFromParcel(Parcel source) {
            return new WootricCustomMessage(source);
        }

        public WootricCustomMessage[] newArray(int size) {
            return new WootricCustomMessage[size];
        }
    };

    public static WootricCustomMessage fromJson(JSONObject customMessagesJson) throws JSONException {
        if(customMessagesJson == null) return null;

        WootricCustomMessage wootricCustomMessage = new WootricCustomMessage();
        wootricCustomMessage.followupQuestion = customMessagesJson.getString("followup_question");
        wootricCustomMessage.placeholderText = customMessagesJson.getString("placeholder_text");

        JSONObject followupQuestionListJson = customMessagesJson.getJSONObject("followup_questions_list");
        wootricCustomMessage.followupQuestionsList = new HashMap<>();
        wootricCustomMessage.followupQuestionsList.put(DETRACTOR_QUESTION_KEY, followupQuestionListJson.getString(DETRACTOR_QUESTION_KEY));
        wootricCustomMessage.followupQuestionsList.put(DETRACTOR_QUESTION_KEY, followupQuestionListJson.getString(DETRACTOR_QUESTION_KEY));
        wootricCustomMessage.followupQuestionsList.put(PASSIVE_QUESTION_KEY, followupQuestionListJson.getString(PASSIVE_QUESTION_KEY));
        wootricCustomMessage.followupQuestionsList.put(PROMOTER_QUESTION_KEY, followupQuestionListJson.getString(PROMOTER_QUESTION_KEY));

        JSONObject placeholderTextsListJson = customMessagesJson.getJSONObject("placeholder_texts_list");
        wootricCustomMessage.placeholderTextsList = new HashMap<>();
        wootricCustomMessage.placeholderTextsList.put(DETRACTOR_TEXT_KEY, placeholderTextsListJson.getString(DETRACTOR_TEXT_KEY));
        wootricCustomMessage.placeholderTextsList.put(PASSIVE_TEXT_KEY, placeholderTextsListJson.getString(PASSIVE_TEXT_KEY));
        wootricCustomMessage.placeholderTextsList.put(PROMOTER_TEXT_KEY, placeholderTextsListJson.getString(PROMOTER_TEXT_KEY));

        return wootricCustomMessage;
    }
}
