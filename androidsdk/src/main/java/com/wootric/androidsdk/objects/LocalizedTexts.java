package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class LocalizedTexts implements Parcelable {

    private final String npsQuestion;
    private final String anchorLikely;
    private final String anchorNotLikely;
    private final String followupQuestion;
    private final String followupPlaceholder;
    private final String finalThankYou;
    private final String send;
    private final String dismiss;
    private final String socialShareQuestion;
    private final String socialShareDecline;

    public LocalizedTexts(String npsQuestion, String anchorLikely, String anchorNotLikely,
                          String followupQuestion, String followupPlaceholder, String finalThankYou,
                          String send, String dismiss, String socialShareQuestion, String socialShareDecline) {
        this.npsQuestion = npsQuestion;
        this.anchorLikely = anchorLikely;
        this.anchorNotLikely = anchorNotLikely;
        this.followupQuestion = followupQuestion;
        this.followupPlaceholder = followupPlaceholder;
        this.finalThankYou = finalThankYou;
        this.send = send;
        this.dismiss = dismiss;
        this.socialShareQuestion = socialShareQuestion;
        this.socialShareDecline = socialShareDecline;
    }


    public static LocalizedTexts fromJson(JSONObject jsonObject) {
        if(jsonObject == null) return null;

        try {
            return new LocalizedTexts(
                    jsonObject.getString("nps_question"),
                    jsonObject.getJSONObject("anchors").getString("likely"),
                    jsonObject.getJSONObject("anchors").getString("not_likely"),
                    jsonObject.getString("followup_question"),
                    jsonObject.getString("followup_placeholder"),
                    jsonObject.getString("final_thank_you"),
                    jsonObject.getString("send"),
                    jsonObject.getString("dismiss"),
                    jsonObject.getJSONObject("social_share").getString("question"),
                    jsonObject.getJSONObject("social_share").getString("decline")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNpsQuestion() {
        return TextUtils.decode(npsQuestion);
    }

    public String getAnchorLikely() {
        return TextUtils.decode(anchorLikely);
    }

    public String getAnchorNotLikely() {
        return TextUtils.decode(anchorNotLikely);
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
        return TextUtils.decode(socialShareQuestion);
    }

    public String getSocialShareDecline() {
        return TextUtils.decode(socialShareDecline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.npsQuestion);
        dest.writeString(this.anchorLikely);
        dest.writeString(this.anchorNotLikely);
        dest.writeString(this.followupQuestion);
        dest.writeString(this.followupPlaceholder);
        dest.writeString(this.finalThankYou);
        dest.writeString(this.send);
        dest.writeString(this.dismiss);
        dest.writeString(this.socialShareQuestion);
        dest.writeString(this.socialShareDecline);
    }

    private LocalizedTexts(Parcel in) {
        this.npsQuestion = in.readString();
        this.anchorLikely = in.readString();
        this.anchorNotLikely = in.readString();
        this.followupQuestion = in.readString();
        this.followupPlaceholder = in.readString();
        this.finalThankYou = in.readString();
        this.send = in.readString();
        this.dismiss = in.readString();
        this.socialShareQuestion = in.readString();
        this.socialShareDecline = in.readString();
    }

    public static final Parcelable.Creator<LocalizedTexts> CREATOR = new Parcelable.Creator<LocalizedTexts>() {
        public LocalizedTexts createFromParcel(Parcel source) {
            return new LocalizedTexts(source);
        }

        public LocalizedTexts[] newArray(int size) {
            return new LocalizedTexts[size];
        }
    };
}
