package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class LocalizedTexts implements Parcelable {

    private static final String ANCHOR_LIKELY_KEY = "likely";
    private static final String ANCHOR_NOT_LIKELY_KEY = "not_likely";
    private static final String SOCIAL_SHARE_QUESTION_KEY = "question";
    private static final String SOCIAL_SHARE_DECLINE_KEY = "decline";

    private String npsQuestion;
    private HashMap<String, String> anchors;
    private String followupQuestion;
    private String followupPlaceholder;
    private String finalThankYou;
    private String send;
    private String dismiss;
    private HashMap<String, String> socialShare;

    public LocalizedTexts() {}

    public String getNpsQuestion() {
        return npsQuestion;
    }

    public String getAnchorLikely() {
        return anchors.get(ANCHOR_LIKELY_KEY);
    }

    public String getAnchorNotLikely() {
        return anchors.get(ANCHOR_NOT_LIKELY_KEY);
    }

    public String getFollowupQuestion() {
        return followupQuestion;
    }

    public String getFollowupPlaceholder() {
        return followupPlaceholder;
    }

    public String getFinalThankYou() {
        return finalThankYou;
    }

    public String getSubmit() {
        return send;
    }

    public String getDismiss() {
        return dismiss;
    }

    public String getSocialShareQuestion() {
        return socialShare.get(SOCIAL_SHARE_QUESTION_KEY);
    }

    public String getSocialShareDecline() {
        return socialShare.get(SOCIAL_SHARE_DECLINE_KEY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.npsQuestion);
        dest.writeSerializable(this.anchors);
        dest.writeString(this.followupQuestion);
        dest.writeString(this.followupPlaceholder);
        dest.writeString(this.finalThankYou);
        dest.writeString(this.send);
        dest.writeString(this.dismiss);
        dest.writeSerializable(this.socialShare);
    }

    private LocalizedTexts(Parcel in) {
        this.npsQuestion = in.readString();
        this.anchors = (HashMap<String, String>) in.readSerializable();
        this.followupQuestion = in.readString();
        this.followupPlaceholder = in.readString();
        this.finalThankYou = in.readString();
        this.send = in.readString();
        this.dismiss = in.readString();
        this.socialShare = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<LocalizedTexts> CREATOR = new Creator<LocalizedTexts>() {
        public LocalizedTexts createFromParcel(Parcel source) {
            return new LocalizedTexts(source);
        }

        public LocalizedTexts[] newArray(int size) {
            return new LocalizedTexts[size];
        }
    };

    public static LocalizedTexts fromJson(JSONObject localizedTextsJson) throws JSONException {
        LocalizedTexts localizedTexts = new LocalizedTexts();
        localizedTexts.npsQuestion = localizedTextsJson.getString("nps_question");

        JSONObject anchorsJson = localizedTextsJson.getJSONObject("anchors");
        localizedTexts.anchors = new HashMap<>();
        localizedTexts.anchors.put(ANCHOR_LIKELY_KEY, anchorsJson.getString(ANCHOR_LIKELY_KEY));
        localizedTexts.anchors.put(ANCHOR_NOT_LIKELY_KEY, anchorsJson.getString(ANCHOR_NOT_LIKELY_KEY));

        localizedTexts.followupQuestion = localizedTextsJson.getString("followup_question");
        localizedTexts.followupPlaceholder = localizedTextsJson.getString("followup_placeholder");
        localizedTexts.finalThankYou = localizedTextsJson.getString("final_thank_you");
        localizedTexts.send = localizedTextsJson.getString("send");
        localizedTexts.dismiss = localizedTextsJson.getString("dismiss");

        JSONObject socialShareJson = localizedTextsJson.getJSONObject("social_share");
        localizedTexts.socialShare = new HashMap<>();
        localizedTexts.socialShare.put(SOCIAL_SHARE_QUESTION_KEY, socialShareJson.getString(SOCIAL_SHARE_QUESTION_KEY));
        localizedTexts.socialShare.put(SOCIAL_SHARE_DECLINE_KEY, socialShareJson.getString(SOCIAL_SHARE_DECLINE_KEY));

        return localizedTexts;
    }
}
