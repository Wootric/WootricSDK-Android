package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 4/17/15.
 */
public class CustomMessage implements Parcelable {

    private String recommendTarget;

    private String placeholder;
    private String detractorPlaceholder;
    private String passivePlaceholder;
    private String promoterPlaceholder;

    private String followupQuestion;
    private String detractorFollowupQuestion;
    private String passiveFollowupQuestion;
    private String promoterFollowupQuestion;

    public static CustomMessage create() {
        return new CustomMessage();
    }

    public CustomMessage recommendTarget(String value) {
        recommendTarget = value;
        return this;
    }

    public CustomMessage placeholder(String value) {
        placeholder = value;
        return this;
    }

    public CustomMessage detractorPlaceholder(String value) {
        detractorPlaceholder = value;
        return this;
    }

    public CustomMessage passivePlaceholder(String value) {
        passivePlaceholder = value;
        return this;
    }

    public CustomMessage promoterPlaceholder(String value) {
        promoterPlaceholder = value;
        return this;
    }

    public CustomMessage followupQuestion(String value) {
        followupQuestion = value;
        return this;
    }

    public CustomMessage detractorFollowupQuestion(String value) {
        detractorFollowupQuestion = value;
        return this;
    }

    public CustomMessage passiveFollowupQuestion(String value) {
        passiveFollowupQuestion = value;
        return this;
    }

    public CustomMessage promoterFollowupQuestion(String value) {
        promoterFollowupQuestion = value;
        return this;
    }

    public String getRecommendTarget() {
        return recommendTarget;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getDetractorPlaceholder() {
        return detractorPlaceholder;
    }

    public String getPassivePlaceholder() {
        return passivePlaceholder;
    }

    public String getPromoterPlaceholder() {
        return promoterPlaceholder;
    }

    public String getFollowupQuestion() {
        return followupQuestion;
    }

    public String getDetractorFollowupQuestion() {
        return detractorFollowupQuestion;
    }

    public String getPassiveFollowupQuestion() {
        return passiveFollowupQuestion;
    }

    public String getPromoterFollowupQuestion() {
        return promoterFollowupQuestion;
    }

    private CustomMessage() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recommendTarget);
        dest.writeString(this.placeholder);
        dest.writeString(this.detractorPlaceholder);
        dest.writeString(this.passivePlaceholder);
        dest.writeString(this.promoterPlaceholder);
        dest.writeString(this.followupQuestion);
        dest.writeString(this.detractorFollowupQuestion);
        dest.writeString(this.passiveFollowupQuestion);
        dest.writeString(this.promoterFollowupQuestion);
    }

    private CustomMessage(Parcel in) {
        this.recommendTarget = in.readString();
        this.placeholder = in.readString();
        this.detractorPlaceholder = in.readString();
        this.passivePlaceholder = in.readString();
        this.promoterPlaceholder = in.readString();
        this.followupQuestion = in.readString();
        this.detractorFollowupQuestion = in.readString();
        this.passiveFollowupQuestion = in.readString();
        this.promoterFollowupQuestion = in.readString();
    }

    public static final Creator<CustomMessage> CREATOR = new Creator<CustomMessage>() {
        public CustomMessage createFromParcel(Parcel source) {
            return new CustomMessage(source);
        }

        public CustomMessage[] newArray(int size) {
            return new CustomMessage[size];
        }
    };

    public String getFollowupQuestionForScore(int score) {
        if(score <= 6 && detractorFollowupQuestion != null) {
            return detractorFollowupQuestion;
        } else if(score >= 7 && score <= 8 && passiveFollowupQuestion != null) {
            return passiveFollowupQuestion;
        } else if(score >= 9 && score <= 10 && promoterFollowupQuestion != null) {
            return promoterFollowupQuestion;
        } else {
            return followupQuestion;
        }
    }

    public String getPlaceholderForScore(int score) {
        if(score <= 6 && detractorPlaceholder != null) {
            return detractorPlaceholder;
        } else if(score >= 7 && score <= 8 && passivePlaceholder != null) {
            return passivePlaceholder;
        } else if(score >= 9 && score <= 10 && promoterPlaceholder != null) {
            return promoterPlaceholder;
        } else {
            return placeholder;
        }
    }

    public static CustomMessage fromJson(JSONObject json) throws JSONException {
        CustomMessage message = new CustomMessage();

        if(json.has("followup_question")) {
            message.followupQuestion = json.getString("followup_question");
        }

        if(json.has("followup_questions_list")) {
            JSONObject followupQuestions = json.getJSONObject("followup_questions_list");

            if(followupQuestions != null) {
                if (followupQuestions.has("detractor_question")) {
                    message.detractorFollowupQuestion = followupQuestions.getString("detractor_question");
                }

                if (followupQuestions.has("passive_question")) {
                    message.passiveFollowupQuestion = followupQuestions.getString("passive_question");
                }

                if (followupQuestions.has("promoter_question")) {
                    message.promoterFollowupQuestion = followupQuestions.getString("promoter_question");
                }
            }
        }

        if(json.has("placeholder_text")) {
            message.placeholder = json.getString("placeholder_text");
        }

        if(json.has("placeholder_texts_list")) {
            JSONObject placeholderQuestions = json.getJSONObject("placeholder_texts_list");

            if(placeholderQuestions != null) {
                if (placeholderQuestions.has("detractor_text")) {
                    message.detractorPlaceholder = placeholderQuestions.getString("detractor_text");
                }

                if (placeholderQuestions.has("passive_text")) {
                    message.passivePlaceholder = placeholderQuestions.getString("passive_text");
                }

                if (placeholderQuestions.has("promoter_text")) {
                    message.promoterPlaceholder = placeholderQuestions.getString("promoter_text");
                }
            }
        }

        if(json.has("wootric_recommend_target")) {
            message.recommendTarget = json.getString("wootric_recommend_target");
        }

        return message;
    }
}
