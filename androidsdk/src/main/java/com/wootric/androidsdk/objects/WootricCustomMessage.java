package com.wootric.androidsdk.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciejwitowski on 4/17/15.
 */
public class WootricCustomMessage implements Parcelable {

    private String recommendTo;
    private String placeholderText;

    public static WootricCustomMessage create() {
        return new WootricCustomMessage();
    }

    public WootricCustomMessage recommendTo(String value) {
        recommendTo = value;
        return this;
    }

    public WootricCustomMessage placeholderText(String value) {
        placeholderText = value;
        return this;
    }

    public String getRecommendTo() {
        return recommendTo;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recommendTo);
        dest.writeString(this.placeholderText);
    }

    private WootricCustomMessage() {}

    private WootricCustomMessage(Parcel in) {
        this.recommendTo = in.readString();
        this.placeholderText = in.readString();
    }

    public static final Creator<WootricCustomMessage> CREATOR = new Creator<WootricCustomMessage>() {
        public WootricCustomMessage createFromParcel(Parcel source) {
            return new WootricCustomMessage(source);
        }

        public WootricCustomMessage[] newArray(int size) {
            return new WootricCustomMessage[size];
        }
    };
}
