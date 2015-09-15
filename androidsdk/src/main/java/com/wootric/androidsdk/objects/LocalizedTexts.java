package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wootric.androidsdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class LocalizedTexts implements Parcelable {

    @SerializedName("nps_question")
    private String npsQuestion;

    private HashMap<String, String> anchors;

    @SerializedName("followup_question")
    private String followupQuestion;

    @SerializedName("followup_placeholder")
    private String followupPlaceholder;

    @SerializedName("final_thank_you")
    private String finalThankYou;

    @SerializedName("send")
    private String send;

    @SerializedName("dismiss")
    private String dismiss;

    @SerializedName("social_share")
    private HashMap<String, String> socialShare;

    public LocalizedTexts() {}

    public String getNpsQuestion() {
        return TextUtils.decode(npsQuestion);
    }

    public String getAnchorLikely() {
        return TextUtils.decode(anchors.get("likely"));
    }

    public String getAnchorNotLikely() {
        return TextUtils.decode(anchors.get("not_likely"));
    }

    public String getFollowupQuestion() {
        return TextUtils.decode(followupQuestion);
    }

    public String getFollowupPlaceholder() {
        return TextUtils.decode(followupPlaceholder);
    }

    public String getFinalThankYou() {
        return TextUtils.decode(finalThankYou);
    }

    public String getSend() {
        return TextUtils.decode(send);
    }

    public String getDismiss() {
        return TextUtils.decode(dismiss);
    }

    public String getSocialShareQuestion() {
        return TextUtils.decode(socialShare.get("question"));
    }

    public String getSocialShareDecline() {
        return TextUtils.decode(socialShare.get("decline"));
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
}
