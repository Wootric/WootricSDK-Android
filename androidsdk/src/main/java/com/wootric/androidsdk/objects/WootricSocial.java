package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.wootric.androidsdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class WootricSocial implements Parcelable {
    private String facebookPageId;
    private String twitterPage;

    private Boolean facebookEnabled;
    private Boolean twitterEnabled;

    public WootricSocial() {}

    public WootricSocial(WootricSocial wootricSocial) {
        if (wootricSocial == null) return;
        this.facebookPageId = wootricSocial.facebookPageId;
        this.twitterPage = wootricSocial.twitterPage;
        this.facebookEnabled = wootricSocial.facebookEnabled;
        this.twitterEnabled = wootricSocial.twitterEnabled;
    }

    public void setFacebookPageId(String facebookPageId) { this.facebookPageId = facebookPageId; }

    public String getFacebookPageId() { return this.facebookPageId; }

    public void setTwitterPage(String twitterPage) { this.twitterPage = twitterPage; }

    public String getTwitterPage() { return twitterPage; }

    public Boolean isTwitterEnabled() {
        return twitterEnabled;
    }

    public Boolean isFacebookEnabled() {
        return facebookEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.facebookPageId);
        dest.writeString(this.twitterPage);
        dest.writeByte(Utils.getByteValue(facebookEnabled));
        dest.writeByte(Utils.getByteValue(twitterEnabled));
    }

    private WootricSocial(Parcel in) {
        this.facebookPageId = in.readString();
        this.twitterPage = in.readString();
        this.facebookEnabled = in.readByte() != 0;
        this.twitterEnabled = in.readByte() != 0;
    }

    public static final Creator<WootricSocial> CREATOR = new Creator<WootricSocial>() {
        public WootricSocial createFromParcel(Parcel source) {
            return new WootricSocial(source);
        }
        public WootricSocial[] newArray(int size) {
            return new WootricSocial[size];
        }
    };

    public static WootricSocial fromJson(JSONObject socialJson) throws JSONException {
        if(socialJson == null) return null;

        WootricSocial wootricSocial = new WootricSocial();

        wootricSocial.facebookEnabled = socialJson.optBoolean("facebook_enabled");
        wootricSocial.twitterEnabled = socialJson.optBoolean("twitter_enabled");

        JSONObject accountsJson = socialJson.optJSONObject("accounts");
        if (accountsJson != null) {
            wootricSocial.facebookPageId = accountsJson.optString("facebook_page");
            wootricSocial.twitterPage = accountsJson.optString("twitter_account");
        }

        return wootricSocial;
    }
}
