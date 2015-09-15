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

    private String npsQuestion;
    private String anchorLikely;
    private String anchorNotLikely;
    private String followupQuestion;
    private String followupPlaceholder;
    private String finalThankYou;
    private String send;
    private String dismiss;
    private String socialShareQuestion;
    private String socialShareDecline;

    public LocalizedTexts() {}

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
